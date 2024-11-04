import { FileDTO } from "./file.dto";

export interface FolderTreeDTO {
    folderId: number;
    folderName: string;
    subFolders: FolderTreeDTO[];
    files: FileDTO[];
}