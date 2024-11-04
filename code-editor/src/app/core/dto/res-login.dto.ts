import { UserDTO } from "./user.dto";

export interface ResLoginDTO {
    user:UserDTO;
    token: string;
}