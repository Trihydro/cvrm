// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Injectable } from '@angular/core';
import { Http, Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { User } from '../classes/user';
import { UserTicket } from '../classes/user-ticket';
import { UserPassword } from '../classes/user-password';
import { AuthorizeToken } from '../classes/authorize-token';
import { AuthHttp } from 'angular2-jwt';
import { environment } from '../../environments/environment';

@Injectable()
export class UserService{
  
  private baseUrl: string;

  constructor(private authHttp: AuthHttp, private http: Http){ 
    this.baseUrl = environment.baseUrl + "/cvpt";
  }

  getAll(): Observable<User[]>{
    let users$ = this.authHttp
    .get(this.baseUrl + '/users', {headers: this.getHeaders()})
    .map(mapUsers)
    .catch(handleError);
    return users$;
  }

  update(user: User) : Observable<Response>{   
    return this.authHttp
    .patch(this.baseUrl + "/users/" + user.userId, JSON.stringify(user), {headers: this.getHeaders()});
  }

  updatePassword(userPassword: UserPassword) : Observable<Response>{   
    return this.authHttp
    .patch(this.baseUrl + "/users/password", JSON.stringify(userPassword), {headers: this.getHeaders()});
  }

  create(user: User) : Observable<User>{   
    return this.authHttp
    .post(this.baseUrl + "/users/", JSON.stringify(user), {headers: this.getHeaders()}).map((res:Response) => res.json()).catch(handleError);;
  }

  getUserTicket(userTicket: UserTicket) : Observable<UserTicket>{   
    return this.authHttp
    .post(this.baseUrl + "/users/emailVerification", JSON.stringify(userTicket), {headers: this.getHeaders()}).map(mapUserTicket);
  }

  getPasswordTicket(userTicket: UserTicket) : Observable<UserTicket>{   
    return this.authHttp
    .post(this.baseUrl + "/users/passwordTicket", JSON.stringify(userTicket), {headers: this.getHeaders()}).map(mapUserTicket);
  }

  getNewUserTicket(userTicket: UserTicket) : Observable<UserTicket>{   
    return this.http
    .post(this.baseUrl + "/users/newUserpasswordTicket", JSON.stringify(userTicket), {headers: this.getHeaders()}).map(mapUserTicket);
  }

  verifyUser(user: User): Observable<User>{
    return this.http
    .post(this.baseUrl + "/users/verify", JSON.stringify(user), {headers: this.getHeaders()}).map((res:Response) => res.json()).catch(handleError);
  }
  
  authorizeUser(user: User):  Observable<AuthorizeToken>{
     return this.http
    .post(this.baseUrl + "/users/authorize", JSON.stringify(user), {headers: this.getHeaders()}).catch(handleError).map((res:Response) => res.json());
  }

  private getHeaders(){
    let headers = new Headers();
    headers.append('content-type', 'application/json');
    return headers;
  }   
}

function handleError (error: any) {
	// log error
	// could be something more sofisticated
	let errorMsg = error.message || `An error has occured.`
	console.error(errorMsg);

	// throw an application level error
	return Observable.throw(errorMsg);
}

function mapUsers(response:Response): User[]{
  console.log(response);
  return response.json().map(toUser);
}

function mapUser(response:Response): User{
  console.log(response.json());
  return response.json().map(toUser);
}

function mapUserTicket(response:Response): UserTicket{
  return JSON.parse(response.text());
}

function mapUserBody(response:Response): User{
  return JSON.parse(response.text());
}

function toUser(u:any): User{
  let user = <User>({
     email: u.email,
     userId: u.userId,
     givenName: u.givenName,
     familyName: u.familyName,
     organization: u.organization,
     blocked: u.blocked,
     roles: u.roles,
     emailVerified: u.emailVerified,
     token: u.token
  });

  return user;
}
