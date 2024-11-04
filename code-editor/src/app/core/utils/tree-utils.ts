import { MonacoTreeElement } from "ngx-monaco-tree";
import { FolderTreeDTO } from "../dto/folder-tree.dto";
import { FileDTO } from "../dto/file.dto";

export class TreeUtils {

  static isFileDTO(obj: any): obj is FileDTO {
    return obj && typeof obj === 'object' && 'fileId' in obj && 'fileName' in obj;
  }
  static isFolderTreeDTO(obj: any): obj is FolderTreeDTO {
    return obj && typeof obj === 'object' && 'folderId' in obj;
  }


  static mapFolderTreeToMonacoTree(folderTreeDTO: FolderTreeDTO): MonacoTreeElement {
    const folderElement: MonacoTreeElement = {
      name: folderTreeDTO.folderName,
      color: 'gray',
      content: []
    };
    if (folderTreeDTO.subFolders && folderTreeDTO.subFolders.length > 0) {
      const subFolderElements = folderTreeDTO.subFolders.map(subFolder =>
        this.mapFolderTreeToMonacoTree(subFolder)
      );
      folderElement.content!.push(...subFolderElements);
    }
    if (folderTreeDTO.files && folderTreeDTO.files.length > 0) {
      const fileElements = folderTreeDTO.files.map(file => ({
        name: file.fileName,
        color: 'green'
      }));
      folderElement.content!.push(...fileElements);
    }

    return folderElement;
  }
  static extractFileName(path: string): string {
    const segments = path.split('/');
    return segments[segments.length - 1];

  }

  static searchFolderTree(tree: FolderTreeDTO, searchTerm: string): (FolderTreeDTO | FileDTO)[] {
    let results: (FolderTreeDTO | FileDTO)[] = [];

    if (tree.folderName.includes(searchTerm)) {
      results.push(tree);
    }

    for (const file of tree.files) {
      if (file.fileName.includes(searchTerm)) {
        results.push(file);
      }
    }

    for (const subFolder of tree.subFolders) {
      results = results.concat(this.searchFolderTree(subFolder, searchTerm));
    }

    return results;
  }



  static findByPath(tree: FolderTreeDTO, path: string): FolderTreeDTO | FileDTO | null {
    const pathParts = path.split('/').filter(part => part !== '');

    if (pathParts.length === 0) {
      return tree;
    }

    const currentPart = pathParts[0];

    if (tree.folderName === currentPart) {
      if (pathParts.length === 1) {
        return tree;
      }

      // Check for file in the current folder
      if (pathParts.length === 2) {
        const file = tree.files.find(f => f.fileName === pathParts[1]);
        if (file) {
          return file;
        }
      }

      // Search in subfolders
      for (const subFolder of tree.subFolders) {
        const result = TreeUtils.findByPath(subFolder, pathParts.slice(1).join('/'));
        if (result) {
          return result;
        }
      }
    }

    return null;
  }


  //  static findObjectByPath(tree: FolderTreeDTO, path: string, parent: FolderTreeDTO | null = null): {found: FolderTreeDTO | FileDTO | null, parent: FolderTreeDTO | null} {
  //     const pathParts = path.split('/').filter(part => part !== '');

  //     if (pathParts.length === 0) {
  //       return { found: tree, parent };
  //     }

  //     const currentPart = pathParts[0];

  //     if (tree.folderName === currentPart) {
  //       if (pathParts.length === 1) {
  //         return { found: tree, parent };
  //       }

  //       if (pathParts.length === 2) {
  //         const file = tree.files.find(f => f.fileName === pathParts[1]);
  //         if (file) {
  //           return { found: file, parent: tree };
  //         }
  //       }

  //     for (const subFolder of tree.subFolders) {
  //         const result = this.findObjectByPath(subFolder, pathParts.slice(1).join('/'), tree);
  //         if (result.found) {
  //           return result;
  //         }
  //       }
  //     }

  //     return { found: null, parent: null };
  //   }


  static findObjectByPath(tree: FolderTreeDTO, path: string, parent: FolderTreeDTO | null = null): { found: FolderTreeDTO | FileDTO | null, parent: FolderTreeDTO | null } {
    const pathParts = path.split('/').filter(part => part !== '');
    if (pathParts.length === 0) {
      return { found: tree, parent };
    }
    const currentPart = pathParts[0];
    if (tree.folderName === currentPart) {
      if (pathParts.length === 1) {
        return { found: tree, parent };
      }
      const nextPart = pathParts[1];
      const matchingSubFolder = tree.subFolders.find(subFolder => subFolder.folderName === nextPart);
      if (matchingSubFolder) {
        return this.findObjectByPath(matchingSubFolder, pathParts.slice(1).join('/'), tree);
      }
      if (pathParts.length === 2) {
        const file = tree.files.find(f => f.fileName === pathParts[1]);
        if (file) {
          return { found: file, parent: tree };
        }
      }
    }
    return { found: null, parent: null };
  }

}