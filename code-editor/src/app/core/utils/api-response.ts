export interface ApiResponse<T = any> {
    message: string;            
    status: string;              
    data?: T;                   
    validationErrors?: {         
      [key: string]: string[];  
    };
  }