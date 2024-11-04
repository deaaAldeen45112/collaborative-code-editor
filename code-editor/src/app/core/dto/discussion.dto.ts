export interface DiscussionDTO {
    discussionId: number;
    topic: string;
    startLineNum: number;
    fileId: number;
    fileName: string;
    filePath: string;
    createdAt: Date;
}
