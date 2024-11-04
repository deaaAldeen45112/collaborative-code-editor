import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateCommentDTO } from '../dto/create-comment.dto';
import { CreateDiscussionDTO } from '../dto/create-discussion.dto';


@Injectable({
  providedIn: 'root'
})
export class DiscussionWebsocketService {


     constructor(private webSocketService: EditorOperationsWebsocketService) { }
     createDiscussion(createDiscussionDTO: CreateDiscussionDTO) {
        this.webSocketService.sendMessage('create-discussion', createDiscussionDTO);
      }
     
      getDiscussions(fileId: number) {
        this.webSocketService.sendMessage('get-discussions', {fileId});
      }
      joinDiscussion(discussionId:number){
        this.webSocketService.sendMessage('join-discussion', {discussionId});
      }
      exitDiscussion(discussionId:number){
        this.webSocketService.sendMessage('exit-discussion', {discussionId});
      }
}