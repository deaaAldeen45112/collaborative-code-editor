import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../utils/api-response';
import { UserDTO } from '../dto/user.dto';
import { GenericApiHandlerService } from './api.service';
import { API_ENDPOINTS } from '../constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private apiHandler: GenericApiHandlerService) { }

  getAll(): Observable<ApiResponse<UserDTO[]>> {
    return this.apiHandler.get<ApiResponse<UserDTO[]>>(API_ENDPOINTS.USER.GET_ALL);
  }

  getById(id: number): Observable<ApiResponse<UserDTO>> {
    return this.apiHandler.get<ApiResponse<UserDTO>>(API_ENDPOINTS.USER.GET_BY_ID(id)).pipe(map(response => response));
  }

 searchUsers(searchTerm: string): Observable<ApiResponse<UserDTO[]>> {
    return this.apiHandler.get<ApiResponse<UserDTO[]>>(API_ENDPOINTS.USER.SEARCH(searchTerm));
  }

  // create(viewModel: CreateUserDTO): Observable<ApiResponse<UserDTO>> {
  //   return this.apiHandler.post<ApiResponse<UserDTO>>(API_ENDPOINTS.USER.CREATE, viewModel);
  // }

  // update(viewModel: UpdateUserDTO): Observable<ApiResponse<UserDTO>> {
  //   return this.apiHandler.put<ApiResponse<UserDTO>>(API_ENDPOINTS.USER.UPDATE, viewModel);
  // }

  // delete(id: number): Observable<boolean> {
  //   return this.apiHandler.delete<boolean>(API_ENDPOINTS.USER.DELETE(id));
  // }
}
