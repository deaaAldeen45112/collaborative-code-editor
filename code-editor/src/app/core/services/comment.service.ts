import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateCommentDTO } from '../dto/create-comment.dto';
import { CommentDTO } from '../dto/comment.dto';
import { ApiResponse } from '../utils/api-response';
import { API_ENDPOINTS } from '../constants/api.constants';
import { GenericApiHandlerService } from './api.service';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class CommentService {


  constructor(private apiHandler: GenericApiHandlerService) { }
  getCommentsByDiscussionId(discussionId: number): Observable<ApiResponse<CommentDTO[]>> {
    return this.apiHandler.get<ApiResponse<CommentDTO[]>>(API_ENDPOINTS.COMMENT.GET_COMMENTS_BY_DISCUSSION_ID(discussionId));
  }
}