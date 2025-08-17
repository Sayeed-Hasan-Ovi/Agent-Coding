import os
import json
import subprocess
from datetime import datetime
from pathlib import Path
from typing import Dict, List, Optional

class GitAgentLogger:
    def __init__(self, log_file: str = "agent_interactions.json", repo_path: str = "."):
        self.log_file = log_file
        self.repo_path = Path(repo_path)
        self.session_id = self._generate_session_id()
        self.current_step = 0
        self.interactions = []
        self._load_existing_log()
    
    def _generate_session_id(self) -> str:
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        return f"session_{timestamp}"
    
    def _load_existing_log(self):
        """Load existing log file if it exists"""
        if Path(self.log_file).exists():
            try:
                with open(self.log_file, 'r') as f:
                    data = json.load(f)
                    self.interactions = data.get('interactions', [])
            except json.JSONDecodeError:
                self.interactions = []
    
    def _save_log(self):
        """Save current interactions to log file"""
        log_data = {
            'session_id': self.session_id,
            'created': datetime.now().isoformat(),
            'total_steps': len(self.interactions),
            'interactions': self.interactions
        }
        with open(self.log_file, 'w') as f:
            json.dump(log_data, f, indent=2)
    
    def _run_git_command(self, command: List[str]) -> str:
        """Run a git command and return output"""
        try:
            result = subprocess.run(
                ['git'] + command, 
                cwd=self.repo_path,
                capture_output=True, 
                text=True, 
                check=True
            )
            return result.stdout.strip()
        except subprocess.CalledProcessError as e:
            return f"Git error: {e.stderr.strip()}"
    
    def get_current_git_status(self) -> Dict:
        """Get current git status and staged changes"""
        status = self._run_git_command(['status', '--porcelain'])
        staged_diff = self._run_git_command(['diff', '--cached'])
        unstaged_diff = self._run_git_command(['diff'])
        
        return {
            'status': status,
            'staged_diff': staged_diff,
            'unstaged_diff': unstaged_diff,
            'has_changes': bool(status.strip())
        }
    
    def get_commit_diff(self, commit_hash: str = None) -> str:
        """Get diff for a specific commit or last commit"""
        if commit_hash:
            return self._run_git_command(['show', commit_hash, '--format='])
        else:
            return self._run_git_command(['show', '--format='])
    
    def create_checkpoint_commit(self, message: str = None) -> str:
        """Create a checkpoint commit with current changes"""
        if not message:
            message = f"Agent checkpoint - Step {self.current_step + 1}"
        
        # Stage all changes
        self._run_git_command(['add', '.'])
        
        # Commit changes
        commit_result = self._run_git_command(['commit', '-m', message])
        
        # Get the commit hash
        commit_hash = self._run_git_command(['rev-parse', 'HEAD'])
        
        return commit_hash
    
    def log_interaction(self, prompt: str, insights: str = "", create_commit: bool = True):
        """Log a complete interaction: prompt -> agent changes -> insights"""
        self.current_step += 1
        
        interaction = {
            'step': self.current_step,
            'timestamp': datetime.now().isoformat(),
            'prompt': prompt,
            'pre_change_status': self.get_current_git_status(),
            'insights': insights,
            'commit_hash': None,
            'diff': None
        }
        
        print(f"\n📝 Step {self.current_step}: Logged prompt")
        print(f"Prompt: {prompt[:100]}{'...' if len(prompt) > 100 else ''}")
        
        # Wait for user to indicate agent has made changes
        input("\n⏸️  Press Enter after the agent has made changes...")
        
        # Check for changes after agent action
        post_change_status = self.get_current_git_status()
        
        if post_change_status['has_changes']:
            if create_commit:
                # Create checkpoint commit
                commit_message = f"Step {self.current_step}: {prompt[:50]}..."
                commit_hash = self.create_checkpoint_commit(commit_message)
                interaction['commit_hash'] = commit_hash
                
                # Get the diff from this commit
                interaction['diff'] = self.get_commit_diff(commit_hash)
                
                print(f"✅ Created commit: {commit_hash[:8]}")
            else:
                # Just capture the current diff without committing
                interaction['diff'] = post_change_status['unstaged_diff']
                print("📋 Captured diffs without committing")
        else:
            print("ℹ️  No changes detected after agent action")
        
        interaction['post_change_status'] = post_change_status
        
        # Add insights if not provided initially
        if not insights:
            insights = input("\n💭 Add insights about why these changes were made (optional): ").strip()
            interaction['insights'] = insights
        
        self.interactions.append(interaction)
        self._save_log()
        
        print(f"✅ Logged interaction for Step {self.current_step}")
        return interaction
    
    def add_insights_to_step(self, step: int, insights: str):
        """Add or update insights for a specific step"""
        for interaction in self.interactions:
            if interaction['step'] == step:
                interaction['insights'] = insights
                self._save_log()
                print(f"✅ Updated insights for Step {step}")
                return
        print(f"❌ Step {step} not found")
    
    def show_interaction_summary(self, step: int = None):
        """Show summary of a specific interaction or all interactions"""
        if step:
            interactions_to_show = [i for i in self.interactions if i['step'] == step]
        else:
            interactions_to_show = self.interactions
        
        for interaction in interactions_to_show:
            print(f"\n{'='*60}")
            print(f"STEP {interaction['step']} - {interaction['timestamp'][:19]}")
            print(f"{'='*60}")
            
            print(f"\n📝 PROMPT:")
            print(f"{interaction['prompt']}")
            
            if interaction['insights']:
                print(f"\n💭 INSIGHTS:")
                print(f"{interaction['insights']}")
            
            if interaction['commit_hash']:
                print(f"\n📌 COMMIT: {interaction['commit_hash'][:8]}")
            
            if interaction['diff']:
                print(f"\n📊 CHANGES:")
                # Show condensed diff info
                diff_lines = interaction['diff'].split('\n')
                file_changes = {}
                current_file = None
                
                for line in diff_lines:
                    if line.startswith('+++') and not line.endswith('/dev/null'):
                        current_file = line.split('/')[-1]
                        file_changes[current_file] = {'added': 0, 'removed': 0}
                    elif line.startswith('+') and current_file and not line.startswith('+++'):
                        file_changes[current_file]['added'] += 1
                    elif line.startswith('-') and current_file and not line.startswith('---'):
                        file_changes[current_file]['removed'] += 1
                
                for file, changes in file_changes.items():
                    print(f"  📄 {file}: +{changes['added']} -{changes['removed']}")
                
                # Option to show full diff
                if input("\n🔍 Show full diff? (y/n): ").lower() == 'y':
                    print(f"\n{interaction['diff']}")
    
    def export_markdown_report(self, output_file: str = None):
        """Export interactions as a markdown report"""
        if not output_file:
            output_file = f"agent_session_{self.session_id}.md"
        
        with open(output_file, 'w') as f:
            f.write(f"# Agent Interaction Log - {self.session_id}\n\n")
            f.write(f"**Session Created:** {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}  \n")
            f.write(f"**Total Interactions:** {len(self.interactions)}\n\n")
            
            for interaction in self.interactions:
                f.write(f"## Step {interaction['step']}\n\n")
                f.write(f"**Timestamp:** {interaction['timestamp'][:19]}  \n")
                
                if interaction['commit_hash']:
                    f.write(f"**Commit:** `{interaction['commit_hash'][:8]}`\n\n")
                
                f.write(f"### 📝 Prompt\n")
                f.write(f"```\n{interaction['prompt']}\n```\n\n")
                
                if interaction['insights']:
                    f.write(f"### 💭 Insights\n")
                    f.write(f"{interaction['insights']}\n\n")
                
                if interaction['diff']:
                    f.write(f"### 📊 Changes Made\n")
                    f.write(f"```diff\n{interaction['diff']}\n```\n\n")
                
                f.write("---\n\n")
        
        print(f"📋 Exported markdown report: {output_file}")

# Example usage
if __name__ == "__main__":
    logger = GitAgentLogger()
    
    print("🚀 Git Agent Logger initialized")
    print("Use logger.log_interaction('your prompt here') to start logging")
    
    # Example usage:
    # logger.log_interaction("Create a simple web server using Flask")
    # logger.log_interaction("Add error handling to the web server", "Added try-catch blocks for better error handling")
    # logger.show_interaction_summary()
    # logger.export_markdown_report()