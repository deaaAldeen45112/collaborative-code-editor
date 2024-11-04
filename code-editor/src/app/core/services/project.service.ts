import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../utils/api-response';
import { UserDTO } from '../dto/user.dto';
import { GenericApiHandlerService } from './api.service';
import { API_ENDPOINTS } from '../constants/api.constants';
import { CreateProjectDTO } from '../dto/create-project.dto';
import { ProjectDTO } from '../dto/project.dto'; 
import { ProjectWithTemplateDTO } from '../dto/project-with-template.dto'; // Add this line
import { FolderTreeDTO } from '../dto/folder-tree.dto';
import { ForkProjectDTO } from '../dto/fork-project.dto';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private apiHandler: GenericApiHandlerService,private http: HttpClient) { }

  getTreeById(id: number): Observable<ApiResponse<FolderTreeDTO>> {
    return this.apiHandler.get<ApiResponse<FolderTreeDTO>>(API_ENDPOINTS.PROJECT.GET_PROJECT_TREE_BY_ID(id));
  }
  
  getByUserId(id: number): Observable<ApiResponse<ProjectWithTemplateDTO[]>> {
    return this.apiHandler.get<ApiResponse<ProjectWithTemplateDTO[]>>(API_ENDPOINTS.PROJECT.GET_PROJECT_BY_USER_ID(id));
  }

  create(createProjectDTO: CreateProjectDTO): Observable<ApiResponse<ProjectDTO>> {
    return this.apiHandler.post<ApiResponse<ProjectDTO>>(API_ENDPOINTS.PROJECT.CREATE, createProjectDTO);
  }

  delete(id: number): Observable<any> {
    return this.apiHandler.delete<any>(API_ENDPOINTS.PROJECT.DELETE(id));
  }
  fork(forkProjectDTO: ForkProjectDTO): Observable<ApiResponse<ProjectDTO>> {
    return this.apiHandler.post<ApiResponse<ProjectDTO>>(API_ENDPOINTS.PROJECT.FORK, forkProjectDTO);
  }
  clone(id:number){
    return this.http.get(API_ENDPOINTS.PROJECT.CLONE(id), {
     responseType: 'blob',
     headers: new HttpHeaders({
      'Accept': 'application/octet-stream' 
    })
    });
   
  }
}
