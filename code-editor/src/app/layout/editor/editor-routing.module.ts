import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VsEditorComponent } from './component/vs-editor/vs-editor.component';
import { EditorLayoutComponent } from './editor-layout/editor-layout.component';

const routes: Routes = [
  {
    path: '',
    component: EditorLayoutComponent,
    children: [
      { path: 'vs-editor', component: VsEditorComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EditorRoutingModule { }
