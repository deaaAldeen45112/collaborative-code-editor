import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateCommentDTO } from '../dto/create-comment.dto';
import { CommentDTO } from '../dto/comment.dto';
import { ApiResponse } from '../utils/api-response';
import { API_ENDPOINTS } from '../constants/api.constants';
import { GenericApiHandlerService } from './api.service';
import { Observable } from 'rxjs';
import { DiscussionDTO } from '../dto/discussion.dto';


@Injectable({
  providedIn: 'root'
})
export class DiscussionService {


  constructor(private apiHandler: GenericApiHandlerService) { }
  getDiscussionsByProjectId(projectId: number): Observable<ApiResponse<DiscussionDTO[]>> {
    return this.apiHandler.get<ApiResponse<DiscussionDTO[]>>(API_ENDPOINTS.DISCUSSION.GET_DISCUSSIONS_BY_PROJECT_ID(projectId));
  }
}