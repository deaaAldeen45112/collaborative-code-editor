import { Injectable } from '@angular/core';
import { EditorOperationsWebsocketService } from './editor-operations-websocket.service';
import { CreateFolderDTO } from '../dto/create-folder.dto';
import { CreateFolderPrimaryDTO } from '../dto/create-folder-primary.dto';
import { API_ENDPOINTS } from '../constants/api.constants';
import { CreateInvitationDTO } from '../dto/create-invitation.dto';
import { ApiResponse } from '../utils/api-response';
import { GenericApiHandlerService } from './api.service';
import { InvitationDTO } from '../dto/invitaion.dto';
import { UpdateInvitationStatusDTO } from '../dto/update-invitaion-status.dto';


@Injectable({
  providedIn: 'root'
})
export class InvitationService {
    constructor(private apiHandler: GenericApiHandlerService) { }

    createInvitation(createInvitationDTO: CreateInvitationDTO) {
      return this.apiHandler.post<ApiResponse<InvitationDTO>>(API_ENDPOINTS.INVITATION.CREATE, createInvitationDTO);
    }

   updateInvitationStatus(updateInvitationStatus: UpdateInvitationStatusDTO) {
        return this.apiHandler.patch<ApiResponse<InvitationDTO>>(API_ENDPOINTS.INVITATION.UPDATE_INVITATION_STATUS, updateInvitationStatus);
    }
    getInvitationsByUserId(userId: number) {
        return this.apiHandler.get<ApiResponse<InvitationDTO[]>>(API_ENDPOINTS.INVITATION.GET_INVITATIONS_BY_USER_ID(userId));
    }
    getSentInvitaions(userId: number) {
      return this.apiHandler.get<ApiResponse<InvitationDTO[]>>(API_ENDPOINTS.INVITATION.GET_SENT_INVITATIONS_BY_SENDER_ID(userId));
  }
 
}