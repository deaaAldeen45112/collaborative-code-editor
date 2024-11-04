export interface InvitationDTO {
    invitationId: number;
    userName: string;
    projectName: string;
    statusName: string;
    invitationSentAt: Date;
    expiresAt: Date;
    statusId: number;
    acceptedAt: Date;
}   