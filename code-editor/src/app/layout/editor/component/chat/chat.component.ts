import { Component } from '@angular/core';
import { DiscussionWebsocketService } from '../../../../core/services/discussion-websocket.service';
import { CommentWebsocketService } from '../../../../core/services/comment-websocket-service';
import { LocalStorageService } from '../../../../core/services/local-storage.service';
import { CommentService } from '../../../../core/services/comment.service';
import { DiscussionService } from '../../../../core/services/discussion.service';
import { EditorOperationsWebsocketService } from '../../../../core/services/editor-operations-websocket.service';
import { Subscription } from 'rxjs';
import { DiscussionDTO } from '../../../../core/dto/discussion.dto';
import { UserDTO } from '../../../../core/dto/user.dto';
import { CreateCommentDTO } from '../../../../core/dto/create-comment.dto';
import { CommentDTO } from '../../../../core/dto/comment.dto';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent {
  discussions: DiscussionDTO[] = [];
  comments: CommentDTO[] = []; // Array to store comments for the selected discussion
  selectedDiscussionId: number | null = null; // Tracks the currently selected discussion
  private messageSubscription!: Subscription;

  constructor(
    private discussionWebsocketService: DiscussionWebsocketService,
    private commentWebsocketService: CommentWebsocketService,
    private localStorageService: LocalStorageService,
    private commentService: CommentService,
    private webSocketService: EditorOperationsWebsocketService,
    private discussionService: DiscussionService
  ) { }

  userId:number=0;
  ngOnInit(): void {
    this.messageSubscription = this.webSocketService.onMessage().subscribe(message => {
      switch (message.action) {
        case "get-discussion":
          this.handleGetDiscussions(message.data);
          break;
        case "get-comment":
          this.handleGetComments(message.data);
          break;
        case "join-discussion-done":
          this.handleJoinDiscussionDone(message.data);
          break; 
        case "exit-discussion-done":
          this.handleExitDiscussionDone(message.data);
          break; 
      }
      console.log('Received WebSocket message:', message);
    });

    const user = <UserDTO>this.localStorageService.getItem(this.localStorageService.USER_SESSION_KEY);
    this.userId=user.userId;
    const projectId=<number>this.localStorageService.getItem(this.localStorageService.PROJECT_ID);
    this.discussionService.getDiscussionsByProjectId(projectId).subscribe(discussions => {
      this.discussions = discussions.data!;
      console.log(discussions);
    });
  
  }

  ngOnDestroy(): void {
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
  }

  handleGetDiscussions(data: any) {
    this.discussions.unshift(data);
    console.log(data);
  }

  handleGetComments(data: any) {
    this.comments.push(data);
    console.log(data);
  }

  selectDiscussion(discussionId: number) {
    this.selectedDiscussionId = discussionId;
    this.commentService.getCommentsByDiscussionId(discussionId).subscribe(comments => {
      this.comments = comments.data!; 
      console.log(comments);
      this.discussionWebsocketService.joinDiscussion(discussionId);
    });
  }

  clearSelectedDiscussion() {
    this.discussionWebsocketService.exitDiscussion(this.selectedDiscussionId!);
    this.selectedDiscussionId = null; 
    this.comments = [];
   
  }

  handleJoinDiscussionDone(data: any) {
    console.log(data);
  }

  handleExitDiscussionDone(data: any) {
    console.log(data);
  }
  currentInput:string='';
  inputComment(){

    this.commentWebsocketService.createComment({
      userId: this.userId,
      content: this.currentInput,
      discussionId: this.selectedDiscussionId!
    });
    this.currentInput='';
  }

}
