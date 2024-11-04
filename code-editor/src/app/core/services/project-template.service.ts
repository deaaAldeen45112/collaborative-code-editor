import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../utils/api-response';
import { UserDTO } from '../dto/user.dto';
import { GenericApiHandlerService } from './api.service';
import { API_ENDPOINTS } from '../constants/api.constants';
import { ProjectTemplateDTO } from '../dto/project-template.dto';

@Injectable({
  providedIn: 'root'
})
export class ProjectTemplateService {

  constructor(private apiHandler: GenericApiHandlerService) { }

  getAll(): Observable<ApiResponse<ProjectTemplateDTO[]>> {
    return this.apiHandler.get<ApiResponse<ProjectTemplateDTO[]>>(API_ENDPOINTS.PROJECT_TEMPLATE.GET_ALL);
  }
}
