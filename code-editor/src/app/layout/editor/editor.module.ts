import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EditorRoutingModule } from './editor-routing.module';
import { EditorLayoutComponent } from './editor-layout/editor-layout.component';

import { SharedModule } from '../../shared/shared.module';
import { NgxMonacoTreeComponent } from 'ngx-monaco-tree';
import { VsEditorComponent } from './component/vs-editor/vs-editor.component';
import { ConsoleComponent } from './component/cosole/cosole.component';
import { ChatComponent } from './component/chat/chat.component';


@NgModule({
  declarations: [
    EditorLayoutComponent,
    VsEditorComponent,
    ConsoleComponent,
    ChatComponent
  ],
  imports: [
    CommonModule,
    EditorRoutingModule,
    SharedModule ,
    NgxMonacoTreeComponent 
  ]
})
export class EditorModule { }
