import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { LocationStrategy } from '@angular/common';
import { LocalStorageService } from './local-storage.service';
import {API_ENDPOINTS} from '../constants/api.constants'
@Injectable({
  providedIn: 'root'
})
export class EditorOperationsWebsocketService {
  private socket$?: WebSocketSubject<any>;
  private connectionStatus$ = new BehaviorSubject<'connected' | 'disconnected'>('disconnected');

  constructor(private localStorageService:LocalStorageService) {
    if(this.localStorageService.getProjectId() && this.localStorageService.getToken()){
      this.socket$ = webSocket({
        url: `ws://${API_ENDPOINTS.DOMAIN}/editor-operations?token=${this.localStorageService.getToken()}&projectId=${this.localStorageService.getProjectId()}`,
        openObserver: {
          next: () => {
            console.log('WebSocket connection opened');
            this.connectionStatus$.next('connected');
          }
        },
        closeObserver: {
          next: () => {
            console.log('WebSocket connection closed');
            this.connectionStatus$.next('disconnected');
          }
        }
      }
      );
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

  public getConnectionStatus(): Observable<'connected' | 'disconnected'> {
    return this.connectionStatus$.asObservable();
  }
  public onMessage(): Observable<any> {
    return this.socket$?.asObservable() || of();
  }
  public closeConnection(): void {
    this.socket$?.complete();
  }
  public disconnect(): void {
    if (this.socket$) {
      this.socket$.complete(); 
      this.socket$ = undefined;
      this.connectionStatus$.next('disconnected'); 
      console.log('WebSocket has been disconnected and instance removed');
    }
  }
}
