import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { InvitationDTO } from '../../../../core/dto/invitaion.dto';
import { InvitationService } from '../../../../core/services/invitation.service';
import { LocalStorageService } from '../../../../core/services/local-storage.service';
import { CreateInvitationDTO } from '../../../../core/dto/create-invitation.dto';
import { UserDTO } from '../../../../core/dto/user.dto';
import { SortType } from '@swimlane/ngx-datatable';
import { CommonUtils } from '../../../../core/utils/common-uils';
import { ProjectService } from '../../../../core/services/project.service';
import { ProjectWithTemplateDTO } from '../../../../core/dto/project-with-template.dto';
import { UserService } from '../../../../core/services/user.service';
import { Subject } from 'rxjs';
import { debounceTime, switchMap } from 'rxjs/operators';
import { InvitationStatus } from '../../../../core/constants/invitation-status';

@Component({
  selector: 'app-invitation',
  templateUrl: './invitation.component.html',
  styleUrl: './invitation.component.css'
})
export class InvitationComponent {
  createInvitationForm: FormGroup;
  users: UserDTO[] = [];
  invitations: InvitationDTO[] = [];
  invitationsFilter: InvitationDTO[] = [];
  projects: ProjectWithTemplateDTO[] = [];
  invitationIdNow:number=1;
  userId:number=0;
   selectedTab: 'received' | 'sent' = 'received';
  SortType=SortType;
  constructor(
    private invitationService: InvitationService, 
    private fb: FormBuilder,
    private localStorageService: LocalStorageService,
    private router:Router,
    private projectService: ProjectService,
    private userService: UserService
  ) {
    this.createInvitationForm = this.fb.group({
      projectId: ['', Validators.required],
      userId: ['', Validators.required],
    });
  
  }

  createInvitation(): void {
    if (this.createInvitationForm.valid) {
      const invitationData: CreateInvitationDTO = this.createInvitationForm.value;
      this.invitationService.createInvitation(invitationData).subscribe(
        response => {
          this.createInvitationForm.reset();
          //this.getInvitationsByUserId(this.userId);
          this.showSentInvitations();
          console.log('Project created successfully:', response);
          
        },
        error => {
          console.error('Error creating project:', error);
        }
      );
    } else {
      console.log('Form is invalid');
    }
  }
  searchUser$ = new Subject<string>();
  ngOnInit(): void {
    const user = <UserDTO>this.localStorageService.getItem(this.localStorageService.USER_SESSION_KEY);
    this.userId=user.userId;
    this.getInvitationsByUserId(user.userId);
    this.getProjectsByUserId(user.userId);
   

    this.searchUser$
    .pipe(
      debounceTime(300), 
      switchMap(searchTerm => this.userService.searchUsers(searchTerm))
    )
    .subscribe(response => {
      console.log(response.data)
      this.users = response.data!;
    });


  }

    getInvitationsByUserId(userId: number): void {
    this.invitationService.getInvitationsByUserId(userId).subscribe(
      response => {
        this.invitations=response.data!;
        this.invitationsFilter=this.invitations;
        console.log('All Projects:', response);
      },
      error => {
        console.error('Error fetching all projects:', error);
      }
    );
  }
  getProjectsByUserId(userId: number): void {
    this.projectService.getByUserId(userId).subscribe(
      response => {
        this.projects=response.data!;
        console.log('All Projects:', response);
      },
      error => {
        console.error('Error fetching all projects:', error);
      }
    );      
  }
  getUsers(): void {
    this.userService.searchUsers(this.createInvitationForm.value.userId).subscribe(
      response => {
        this.users=response.data!;
        console.log('All Users:', response);
      },
      error => {
        console.error('Error fetching all users:', error);
      }
    );      
  }

  changeInvitationIdNow(invitationId: number) {
    console.log(invitationId);
    this.invitationIdNow=invitationId;

  }

  resetCreateInvitationForm() {
    this.createInvitationForm.reset();
  }

  updateInvitationFilter(event: any) {
    let val = event.target.value.toLowerCase();
    this.invitations= CommonUtils.filterData(this.invitationsFilter,val)
  }
  acceptInvitation(invitationId: number) {
    this.invitationService.updateInvitationStatus({invitationId:invitationId,invitationStatusId: InvitationStatus.ACCEPTED}).subscribe(
      response => {
        this.getInvitationsByUserId(this.userId);
      }
    );
  }
  rejectInvitation(invitationId: number) {
    this.invitationService.updateInvitationStatus({invitationId:invitationId,invitationStatusId: InvitationStatus.REJECTED}).subscribe(
      response => {
        this.getInvitationsByUserId(this.userId);
      }
    );
  }
  showReceivedInvitations()
  {  this.selectedTab = 'received';
    this.getInvitationsByUserId(this.userId);
  }

  showSentInvitations(){
    this.selectedTab = 'sent';
    this.invitationService.getSentInvitaions(this.userId).subscribe(
      response => {
        this.invitations=response.data!;
      }
    );
  }
} 
