const BASE_URL = 'http://ec2-54-81-143-14.compute-1.amazonaws.com:8002/api';
const DOMAIN = 'ec2-54-81-143-14.compute-1.amazonaws.com:8002';
const AUTH_PATH = `/auth`;
const PROJECT_PATH = `/projects`;
const FILE_PATH = `/files`;
const PROJECT_TEMPLATE_PATH = `/project-templates`;
const ROLE_PATH = `/role`;
const USER_PATH = `/users`;
const INVITATION_PATH = `/invitations`;
const STATISTIC_PATH = `${BASE_URL}/statistic`;
const COMMENT_PATH = `/comments`;
const DISCUSSION_PATH = `/discussions`;
//
export const API_ENDPOINTS = {
  BASE_URL,
  DOMAIN,
  AUTH_PATH,
  PROJECT_PATH,
  PROJECT_TEMPLATE_PATH,
  ROLE_PATH,
  USER_PATH,
  STATISTIC_PATH,

  AUTH: {
    LOGIN: `${AUTH_PATH}/login`,
    REGISTER: `${AUTH_PATH}/register-coder`,
    LOGIN_BY_GMAIL: `${AUTH_PATH}/google`,      
    LOGIN_BY_GITHUB: `${AUTH_PATH}/github`,     
  },

  PROJECT: {
    GET_PROJECT_TREE_BY_ID: (id: number) => `${PROJECT_PATH}/project-tree/${id}`,
    GET_PROJECT_BY_USER_ID: (id: number) => `${PROJECT_PATH}/users/${id}`,
    CREATE: `${PROJECT_PATH}`,
    DELETE:(id: number)=> `${PROJECT_PATH}/${id}`,
    FORK:`${PROJECT_PATH}/fork`,
    CLONE:(id: number)=> `${BASE_URL}${PROJECT_PATH}/clone/${id}`,
  },
  INVITATION: { 
    CREATE: `${INVITATION_PATH}`,
    DELETE: (id: number) => `${INVITATION_PATH}/${id}`,
    GET_INVITATIONS_BY_USER_ID: (userId: number) => `${INVITATION_PATH}/users/${userId}`,
    GET_SENT_INVITATIONS_BY_SENDER_ID: (userId: number) => `${INVITATION_PATH}/sent?userId=${userId}`,
    UPDATE_INVITATION_STATUS:  `${INVITATION_PATH}/status`,
  },
  FILE: {
    GET_FILE_BY_ID: (id: number) => `${FILE_PATH}/${id}`,
    GET_FILE_VERSIONS: (fileId: number, limit: number) => `${FILE_PATH}/${fileId}/versions?limit=${limit}`,
  },
  COMMENT: {
    GET_COMMENTS_BY_DISCUSSION_ID: (discussionId: number) => `${COMMENT_PATH}/discussions/${discussionId}`,
  },
  DISCUSSION: {
    GET_DISCUSSIONS_BY_PROJECT_ID: (projectId: number) => `${DISCUSSION_PATH}/projects/${projectId}`,
  },
  PROJECT_TEMPLATE: {
    GET_ALL: `${PROJECT_TEMPLATE_PATH}`,
  },
  
  ROLE: {
    GET_ALL: `${ROLE_PATH}/getAll`,
    GET_BY_ID: (id: number) =>`${ROLE_PATH}/getById/${id}`,
    CREATE: `${ROLE_PATH}/create`,
    UPDATE: `${ROLE_PATH}/update`,
    DELETE: `${ROLE_PATH}/delete/`, 
  },

  STATISTIC: {
    GET_MAIN_STATISTICS: `${STATISTIC_PATH}/getMainStatistics`,
  },

  USER: {
    GET_ALL: `${USER_PATH}/getAll`,
    GET_BY_ID: (id: number) =>  `${USER_PATH}/getById/${id}`, 
    SEARCH: (searchTerm: string) => `${USER_PATH}/search?searchTerm=${searchTerm}`,
    CREATE: `${USER_PATH}/create`,
    UPDATE: `${USER_PATH}/update`,
    DELETE: `${USER_PATH}/delete/`, 
  },
};
