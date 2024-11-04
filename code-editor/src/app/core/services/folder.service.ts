import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateFolderDTO } from '../dto/create-folder.dto';
import { CreateFolderPrimaryDTO } from '../dto/create-folder-primary.dto';


@Injectable({
  providedIn: 'root'
})
export class FolderService {
    constructor(private webSocketService: EditorOperationsWebsocketService) { }

    createFolder(createFolderDTO: CreateFolderDTO): void {
      this.webSocketService.sendMessage('create-folder', createFolderDTO);
    }
    createFolderPrimary(createFolderPrimaryDTO: CreateFolderPrimaryDTO): void {
      this.webSocketService.sendMessage('create-primary-folder', createFolderPrimaryDTO);
    }
  
    deleteFolder(folderId: number): void {
      this.webSocketService.sendMessage('delete-folder', folderId);
    }
}