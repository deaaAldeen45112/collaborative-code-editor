import { Component, ViewEncapsulation } from '@angular/core';
import { ProjectWithTemplateDTO } from '../../../../core/dto/project-with-template.dto';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../../../../core/services/project.service';
import { CreateProjectDTO } from '../../../../core/dto/create-project.dto';
import { CommonUtils } from '../../../../core/utils/common-uils';
import { LocalStorageService } from '../../../../core/services/local-storage.service';
import { UserDTO } from '../../../../core/dto/user.dto';
import { SortType } from '@swimlane/ngx-datatable';
import { ProjectTemplateDTO } from '../../../../core/dto/project-template.dto';
import { ProjectTemplateService } from '../../../../core/services/project-template.service';
import { ForkProjectDTO } from '../../../../core/dto/fork-project.dto';
import { Router } from '@angular/router';
import { saveAs } from 'file-saver'; 
@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrl: './project.component.css',
})
export class ProjectComponent {
  createProjectForm: FormGroup;
  projects: ProjectWithTemplateDTO[] = [];
  projectsFilter: ProjectWithTemplateDTO[] = [];
  projectIdNow:number=1;
  forkProjectForm: FormGroup;
  userId:number=0;
  templates: ProjectTemplateDTO[] = [];
  columnsProject = [
    { name: 'Project Name', prop: 'projectName' },
    { name: 'Project Description', prop: 'description' },
    { name: 'Template Name', prop: 'templateName'},
    { name: 'Created At', prop: 'createdAt' },
  ];
  constructor(
    private projectService: ProjectService, 
    private fb: FormBuilder,
    private localStorageService: LocalStorageService,
    private projectTemplateService: ProjectTemplateService,
    private router:Router
  ) {
    this.createProjectForm = this.fb.group({
      projectName: ['', Validators.required],
      description: ['', Validators.required],
      templateId: ['', Validators.required],

    });
    this.forkProjectForm = this.fb.group({
      projectName: ['', Validators.required],
      description: ['', Validators.required],

    });
  }

  createProject(): void {
    if (this.createProjectForm.valid) {
      const projectData: CreateProjectDTO = this.createProjectForm.value;
      this.projectService.create(projectData).subscribe(
        response => {
          this.createProjectForm.reset();
          this.getByUserId(this.userId);
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
 
  ngOnInit(): void {
    const user = <UserDTO>this.localStorageService.getItem(this.localStorageService.USER_SESSION_KEY);
    this.userId=user.userId;
    this.getByUserId(user.userId);
    this.getTemplates();
  }

  getByUserId(userId: number): void {
    this.projectService.getByUserId(userId).subscribe(
      response => {
        this.projects=response.data!;
        this.projectsFilter=this.projects;
        console.log('All Projects:', response);
      },
      error => {
        console.error('Error fetching all projects:', error);
      }
    );
  }
  getTemplates(): void {
    this.projectTemplateService.getAll().subscribe(
      response => {
        this.templates = response.data || [];
      },
      error => {
        console.error('Error fetching templates:', error);
      } 
    );
  }
  deleteProject() {
    this.projectService.delete(this.projectIdNow).subscribe(
      response => {
        console.log(`Project with ID ${this.projectIdNow} deleted successfully.`);
        this.getByUserId(this.userId);
      },
      error => {
        console.error(`Error deleting project with ID ${this.projectIdNow}:`, error);
      }
    );

  }

  forkProject() {
    if (this.forkProjectForm.valid) {
      const projectData: ForkProjectDTO = this.forkProjectForm.value;
      projectData.projectId=this.projectIdNow;
      this.projectService.fork(projectData).subscribe(
        response => {
        console.log(`Project with ID ${this.projectIdNow} forked successfully.`);
        this.getByUserId(this.userId);
        this.resetForkProjectForm();
      },
      error => {
        console.error(`Error forking project with ID ${this.projectIdNow}:`, error);
        }
      );
    }
  }


  onActivate(event: any) {
    if (event.type === 'click') {
      const projectId = event.row?.projectId;
      if (projectId) {
        console.log('Project ID:', event.row);
      }
      this.localStorageService.setItem(this.localStorageService.PROJECT_ID, projectId.toString());
      this.localStorageService.setItem(this.localStorageService.PROJECT_KEY, event.row);
      //this.router.navigate(['editor/vs-editor']);
    window.location.href = `editor/vs-editor`;
    }
  }


  protected readonly SortType = SortType;

  changeProjectIdNow(projectId: number) {
    console.log(projectId);
    this.projectIdNow=projectId;

  }

 
  resetCreateProjectForm() {
    this.createProjectForm.reset();
  }

  updateProjectFilter(event: any) {
    let val = event.target.value.toLowerCase();
    this.projects= CommonUtils.filterData(this.projectsFilter,val)
  }

  resetForkProjectForm() {
    this.forkProjectForm.reset();
  }

  cloneProject(id:number){
    this.projectService.clone(id).subscribe({
      next: (blob) => {
     
        if (blob.size === 0) {
          console.error('Received an empty file');
          return;
        }

        const fileName = `project.zip`; 
        saveAs(blob, fileName); 
      },
      error: (error) => {
        console.error('Error downloading file', error);
      }
    });
  }

}

