import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateCommentDTO } from '../dto/create-comment.dto';
import { WebSocketSubject, webSocket } from 'rxjs/webSocket';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { LocalStorageService } from './local-storage.service';
import { API_ENDPOINTS } from '../constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class CodeRunService {
    private socket$?: WebSocketSubject<any>;
    private connectionStatus$ = new BehaviorSubject<'connected' | 'disconnected' | 'error'>('disconnected');
  
    constructor(private localStorageService: LocalStorageService) {
      this.initializeWebSocket();
    }
  
    private initializeWebSocket(): void {
      if (this.localStorageService.getProjectId() && this.localStorageService.getToken()) {
        this.socket$ = webSocket({
          url: `ws://${API_ENDPOINTS.DOMAIN}/code-run?token=${this.localStorageService.getToken()}&projectId=${this.localStorageService.getProjectId()}`,
          openObserver: {
            next: () => {
              console.log('Code Run WebSocket connection opened');
              this.connectionStatus$.next('connected');
            }
          },
          closeObserver: {
            next: () => {
              console.log('Code Run WebSocket connection closed');
              this.connectionStatus$.next('disconnected');
            }
          }
        });
  
        this.socket$.subscribe({
          error: (error) => {
            console.error('Code Run WebSocket error:', error);
            this.connectionStatus$.next('error');
          }
        });
      }
    }
  
    public sendMessage(action: string, data: any): void {
      const message = {
        action: action,
        timestamp: new Date().toISOString(),
        data: data
      };
      this.socket$?.next(message);
    }
  
    public onMessage(): Observable<any> {
      return this.socket$?.asObservable() || of();
    }
  
    public closeConnection(): void {
      this.socket$?.complete();
      this.connectionStatus$.next('disconnected');
    }
  
    public getConnectionStatus(): Observable<'connected' | 'disconnected' | 'error'> {
      return this.connectionStatus$.asObservable();
    }
  
    public reconnect(): void {
      this.closeConnection();
      this.initializeWebSocket();
    }
  
 
    public runCode(): void {
      this.sendMessage('run', {});
    }
  
     
    public inputConsoleText(text:string): void {
        this.sendMessage('input-console-text', text);
      }
    

}