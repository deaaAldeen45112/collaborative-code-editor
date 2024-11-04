import { Injectable } from '@angular/core';
import { GenericApiHandlerService } from './api.service';
import { API_ENDPOINTS } from '../constants/api.constants';
import { ApiResponse } from '../utils/api-response';
import { Observable } from 'rxjs';
import { FileVersionDTO } from '../dto/file-version.dto';


@Injectable({
  providedIn: 'root'
})
export class FileService {
    constructor(private apiHandler: GenericApiHandlerService) { }

    readById(id: number): Observable<ApiResponse<String>> {
      return this.apiHandler.get<ApiResponse<String>>(API_ENDPOINTS.FILE.GET_FILE_BY_ID(id));
    } 
    getFileVersions(fileId: number, limit: number) {
      return this.apiHandler.get<ApiResponse<FileVersionDTO[]>>(API_ENDPOINTS.FILE.GET_FILE_VERSIONS(fileId, limit));
    }

}