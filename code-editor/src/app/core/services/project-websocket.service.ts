import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../utils/api-response';
import { UserDTO } from '../dto/user.dto';
import { GenericApiHandlerService } from './api.service';
import { API_ENDPOINTS } from '../constants/api.constants';
import { CreateProjectDTO } from '../dto/create-project.dto';
import { ProjectDTO } from '../dto/project.dto'; 
import { ProjectWithTemplateDTO } from '../dto/project-with-template.dto'; // Add this line
import { FolderTreeDTO } from '../dto/folder-tree.dto';
import { ForkProjectDTO } from '../dto/fork-project.dto';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectWebsocketService {

  constructor(private webSocketService: EditorOperationsWebsocketService) { }

  getTreeById(id: number):void {
     this.webSocketService.sendMessage('get-project-tree-by-id', id);
  }
  
  getByUserId(id: number):void {
     this.webSocketService.sendMessage('get-project-by-user-id', id);
  }

  create(createProjectDTO: CreateProjectDTO): void {
     this.webSocketService.sendMessage('create-project', createProjectDTO);
  }

}
