import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateCommentDTO } from '../dto/create-comment.dto';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class CommentWebsocketService {


     constructor(private webSocketService: EditorOperationsWebsocketService) { 
     }
     createComment(createCommentDTO: CreateCommentDTO): void {
        this.webSocketService.sendMessage('create-comment', createCommentDTO);
      }
     
      getComments(discussionId: number): void {
        this.webSocketService.sendMessage('get-comments', {discussionId});
      }


}