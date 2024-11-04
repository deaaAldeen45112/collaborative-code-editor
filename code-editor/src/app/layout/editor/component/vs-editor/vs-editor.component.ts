
import { Component, OnInit, ViewChild, ElementRef, OnDestroy, NgZone } from '@angular/core';
import loader from '@monaco-editor/loader';

import * as monaco from 'monaco-editor';
import {
  ContextMenuAction,
  MonacoTreeElement,
  NgxMonacoTreeComponent,
  DragAndDropEvent,
} from 'ngx-monaco-tree';
import { ProjectService } from '../../../../core/services/project.service';
import { FileService } from '../../../../core/services/file.service';
import { FolderService } from '../../../../core/services/folder.service';
import { CommentService } from '../../../../core/services/comment.service';
import { ProjectWebsocketService } from '../../../../core/services/project-websocket.service';
import { EditorOperationsWebsocketService } from '../../../../core/services/editor-operations-websocket.service';
import { FileWebsocketService } from '../../../../core/services/file-websocket-service';
import { FolderTreeDTO } from '../../../../core/dto/folder-tree.dto';
import { NgxTreeService } from '../../../../core/services/ngx-tree.service';
import { TreeUtils } from '../../../../core/utils/tree-utils';
import { ReadFileDTO } from '../../../../core/dto/read-file.dto';
import { FileDTO } from '../../../../core/dto/file.dto';
import { LocalStorageService } from '../../../../core/services/local-storage.service';
import { CodeRunService } from '../../../../core/services/code-run.service';
import { ProjectDTO } from '../../../../core/dto/project.dto';
import { FileVersionDTO } from '../../../../core/dto/file-version.dto';
import { DiscussionWebsocketService } from '../../../../core/services/discussion-websocket.service';
@Component({
  selector: 'app-vs-editor',
  templateUrl: './vs-editor.component.html',
  styleUrl: './vs-editor.component.css'
})
export class VsEditorComponent {
  @ViewChild('editorContainer', { static: true }) editorContainer!: ElementRef;
  editor!: monaco.editor.IStandaloneCodeEditor;
  @ViewChild(NgxMonacoTreeComponent) treeComponent!: NgxMonacoTreeComponent;
  currentFileName: string = '';
  fileContents: { [key: string]: string } = {
    'settings.json': '{ "editor.fontSize": 14 }',
    'app.component.html': '<h1>Hello World</h1>',
    'app.component.css': 'h1 { color: red; }',
    'app.component.spec.ts': 'describe("AppComponent", () => { it("should create", () => {}); });',
    'app.component.ts': 'export class AppComponent {}',
    'app.module.ts': 'import { NgModule } from "@angular/core";',
    'environment.prod.ts': 'export const environment = { production: true };',
    'environment.ts': 'export const environment = { production: false };',
    'favicon.ico': 'Binary content',
    'index.html': '<!DOCTYPE html><html><head></head><body></body></html>',
    'main.ts': 'platformBrowserDynamic().bootstrapModule(AppModule);',
    'polyfill.ts': 'import "core-js";',
    'styles.css': 'body { margin: 0; }',
    'angular.json': '{ "projects": {} }',
    'package-lock.json': '{ "name": "project" }',
    'package.json': '{ "name": "project" }',
    'tsconfig.json': '{ "compilerOptions": {} }'
  };
  dark = true;
  currentPath = '';
  tree: MonacoTreeElement[] = []
  folderTreeDTO: FolderTreeDTO = {
    folderId: 1,
    folderName: 'RootFolder',
    subFolders: [
      {
        folderId: 2,
        folderName: 'Subfolder1',
        subFolders: [

          {
            folderId: 2,
            folderName: 'Subfolder3',
            subFolders: [],
            files: [{ fileId: 1, fileName: 'File1.txt' }]
          },
        ],
        files: [{ fileId: 1566, fileName: 'File1.txt' }]
      },
      {
        folderId: 3,
        folderName: 'Subfolder2',
        subFolders: [],
        files: [{ fileId: 2, fileName: 'File2.txt' }]
      }
    ],
    files: [{ fileId: 3, fileName: 'RootFile.txt' }]
  };
  constructor(private ngZone: NgZone,
    private projectService: ProjectService,
    private fileService: FileService,
    private fileWebsocketService: FileWebsocketService,
    private folderService: FolderService,
    private commentService: CommentService,
    private webSocketService: EditorOperationsWebsocketService,
    private discussionWebsocketService: DiscussionWebsocketService,
    private localStorageService: LocalStorageService,
    ) {}

  ngOnInit() {
    const project = <ProjectDTO>this.localStorageService.getItem(this.localStorageService.PROJECT_KEY);
    loader.config({ paths: { vs: 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.33.0/min/vs' } });
    loader.init().then((monaco) => {
      this.ngZone.runOutsideAngular(() => {
        this.editor = monaco.editor.create(this.editorContainer.nativeElement, {
          value: '',
          language: this.getLanguage(project.templateName),
          theme: 'vs-dark',
          automaticLayout: true,
          minimap: { enabled: false },
          scrollBeyondLastLine: true,
        });
      });
  
      this.editor.onDidChangeModelContent((event) => {
        if (this.isSettingValue) {
          return; 
        }
        console.log('Editor content changed:',this.editor.getValue(),event);
        const obj = TreeUtils.findObjectByPath(this.folderTreeDTO, this.currentFilePath);
        if(obj.found && TreeUtils.isFileDTO(obj.found)){
          const editorContent = this.editor.getValue();
          this.fileWebsocketService.editFile({fileId:obj.found.fileId,content:editorContent});
          console.log('Editor content changed:', editorContent);
        }
      });

      this.editor.addAction({
        id: 'create-discussion',
        label: 'Create Discussion',
        contextMenuGroupId: 'navigation',
        contextMenuOrder: 1,
        run: (editor) => {
          const selection = editor.getSelection();
          if (!selection || selection.isEmpty()) {
            console.log('No code selected');
            return;
          }
  
          const startLineNumber = selection?.startLineNumber;
          const selectedCode = editor.getModel()?.getValueInRange(selection);
          console.log('Selected Code:', selectedCode);
          console.log('Start Line Number:', startLineNumber);
          const file = TreeUtils.findObjectByPath(this.folderTreeDTO, this.currentPath);
          console.log(file);
       
          if(TreeUtils.isFileDTO(file.found)){
          this.discussionWebsocketService.createDiscussion({
            fileId:file.found.fileId,
            topic:selectedCode!,
            startLineNum:startLineNumber
          });
          }
        }
      });

    }).catch(error => {
      console.error('Monaco initialization error: ', error);
    });


    this.webSocketService.onMessage().subscribe(message => {
      console.log('Received WebSocket message:', message);
      switch (message.action) {
        case "get-project-structure":
          this.handleProjectStructure(message.data);
          break;

        case "get-file-content":
          this.handleFileContent(message.data);
          break;
      }
    });

    this.webSocketService.getConnectionStatus().subscribe(
      status => {
        if (status === 'connected') {
          console.log('WebSocket is now open and connected');
          this.projectService.getTreeById(Number(this.localStorageService.getProjectId())).subscribe(res => {
            if(res.data){
              console.log(res.data);
              this.handleProjectStructure(res.data);
            }
          });
        }
      }
    );
  }



  ngOnDestroy() {
    if (this.editor) {
      this.editor.dispose();
    }
    
  }
  handleContextMenu(action: ContextMenuAction) {
    const obj = TreeUtils.findObjectByPath(this.folderTreeDTO, action[1]);
    console.log(obj);
    if (action[0] === 'new_directory') {
      const folderName = window.prompt('name');
      if (folderName && TreeUtils.isFolderTreeDTO(obj.found)) {
        if (obj.parent) {
          console.log(obj.found);
          this.folderService.createFolder({ folderName: folderName, parentFolderId: obj.found.folderId });
        }
        else if (this.localStorageService.getProjectId()) {
          this.folderService.createFolderPrimary({ folderName: folderName, projectId: Number(this.localStorageService.getProjectId()) });
        }
      }
    } else if (action[0] === 'new_file') {
      const filename = window.prompt('name');
      if (filename && TreeUtils.isFolderTreeDTO(obj.found)) {
        console.log(obj);
        this.fileWebsocketService.createFile({ fileName: filename, folderId: obj.found.folderId });
      }
    } else if (action[0] === 'delete_file') {
      if (action[1].includes('.') && TreeUtils.isFileDTO(obj.found)) {
        this.fileWebsocketService.deleteFile(obj.found.fileId);
      }
      else if (TreeUtils.isFolderTreeDTO(obj.found)) {
        this.folderService.deleteFolder(obj.found.folderId);
      }
    }
  }

  selectedTab: string = 'console';

  selectTab(tab: string) {
    this.selectedTab = tab;
  }
  handleProjectStructure(data: any) {
    this.folderTreeDTO = data;
    const monacoTree: MonacoTreeElement = TreeUtils.mapFolderTreeToMonacoTree(data);
    this.tree = [monacoTree];
    console.log(data);
  }
  private isSettingValue: boolean = false;
  handleFileContent(data: ReadFileDTO) {
    const fileContent: ReadFileDTO = data;
    const file = TreeUtils.findObjectByPath(this.folderTreeDTO, this.currentFilePath);
    if (file.found) {
      if (TreeUtils.isFileDTO(file.found) && (file.found.fileId === fileContent.fileId)) {
        this.isSettingValue = true; 
        this.editor.setValue(fileContent.content);
        this.isSettingValue = false; 
      }
    }
    console.log(fileContent);
  }

  currentFilePath: string = '';
  handleFileClick(event: any) {
    console.log(event);
    this.currentPath = event;
    const file = TreeUtils.findObjectByPath(this.folderTreeDTO, this.currentPath);
    console.log(file);
 
    if(TreeUtils.isFileDTO(file.found)){
      this.currentFilePath = event;
    this.fileService.readById(file.found?.fileId).subscribe(res => {
      if(res.data!=undefined){
        this.isSettingValue = true; 
        this.editor.setValue(String(res.data));
        this.isSettingValue = false; 
      }
    });
    this.currentFileName = TreeUtils.extractFileName(this.currentPath);
  }

  }
 


  showRangeInput: boolean = false;
  progress: number = 0;

  toggleHistory() {
    this.showRangeInput = !this.showRangeInput;
    if(this.showRangeInput){
    const file = TreeUtils.findObjectByPath(this.folderTreeDTO, this.currentFilePath);
    if (file.found) {
      if (TreeUtils.isFileDTO(file.found)) {
        this.getFileVersion(file.found.fileId);
        }
      } 
    }
  }



  getLanguage(templateName:string){
    if(templateName.toLowerCase().includes('c++')){
      return 'cpp';
    }
    else if(templateName.toLowerCase().includes('python')){
      return 'python';
    }
  return 'javascript';
  }
   
  numVersions:number=0;
  fileVersions:FileVersionDTO[]=[];
  selectedVersion = 100;
  coderName:string='deaa';
  versionNumber:number=1;
  timestamp:string='';

  getFileVersion(fileId:number){
    this.fileService.getFileVersions(fileId,1000).subscribe(res => {
      this.numVersions=res.data?.length!;
      console.log(this.numVersions);
      this.fileVersions=res.data!;
      this.selectedVersion=this.numVersions-1;
      this.versionNumber=this.fileVersions[0].versionNumber;
      this.timestamp=this.fileVersions[0].createdAt.toLocaleString();
      console.log(res);
      console.log(res.data);
    });
  }
  
  valueChanged(event:any){
    console.log(event.target.value);
    const fileVersion=this.fileVersions[this.numVersions-event.target.value-1];
    this.coderName=fileVersion.userName;
    this.versionNumber=fileVersion.versionNumber;
    this.timestamp=fileVersion.createdAt.toLocaleString();
    console.log(fileVersion.diffContent);
    this.isSettingValue=true;
    this.editor.setValue(fileVersion.diffContent);
    }
  resetEditor(){
    this.isSettingValue=false;
    this.editor.setValue(this.fileVersions[this.numVersions-this.selectedVersion-1].diffContent);
    console.log(this.selectedVersion);
    this.showRangeInput=false;
  }

}

