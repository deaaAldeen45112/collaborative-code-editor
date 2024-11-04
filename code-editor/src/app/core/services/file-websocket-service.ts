import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateFileDTO } from '../dto/create-file.dto';
import { UpdateFileDTO } from '../dto/edit-file-dto';
import { GenericApiHandlerService } from './api.service';
import { API_ENDPOINTS } from '../constants/api.constants';
import { ApiResponse } from '../utils/api-response';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class FileWebsocketService {

  constructor(private webSocketService: EditorOperationsWebsocketService) { }

  createFile(createFileDTO: CreateFileDTO): void {
    this.webSocketService.sendMessage('create-file', createFileDTO);
  }
 
  deleteFile(fileId: number): void {
    this.webSocketService.sendMessage('delete-file', fileId);
  }
  editFile(updateFileDTO: UpdateFileDTO): void {
    this.webSocketService.sendMessage('edit-file', updateFileDTO);
  }
  
  readById(id: number): void {
    this.webSocketService.sendMessage('read-file', id);
  } 
}