import { Component } from '@angular/core';
import { CodeRunService } from '../../../../core/services/code-run.service';

@Component({
  selector: 'app-console',
  template: `
    <div class="console-container">
      <div class="console-header">
        <div class="welcome-message">Welcome to the console</div>
        <button class="run-button" (click)="runCode()">Run</button>
      </div>
      <div class="console-output">
        <pre *ngFor="let line of consoleOutput">{{ line }}</pre>
      </div>
      <div class="console-input-container">
        <input 
          class="console-input"
          [(ngModel)]="currentInput"
          (keyup.enter)="inputText()"
          placeholder="Enter command..."
        >
      </div>
    </div>
  `,
  styleUrls: ['./cosole.component.css']
})
export class ConsoleComponent {
  consoleOutput: string[] = [];
  currentInput: string = '';

  constructor(private codeRunService: CodeRunService) {
    this.codeRunService.onMessage().subscribe(message => {
    
      console.log('Received Code Run message:', message);
      
      switch(message.action){
        case 'output-console-text':
          this.consoleOutput.push(message.data);
          break;
        case 'error-console-text':
          this.consoleOutput.push(message.data);
          break;
      }
    
    });

    

  }

  inputText() {
    if (this.currentInput.trim()) {

      this.codeRunService.inputConsoleText(this.currentInput);
       this.consoleOutput.push(`${this.currentInput}`);
      this.currentInput = '';
    }
  }

 runCode(){
  this.consoleOutput = [];
  this.codeRunService.runCode();
 }

}