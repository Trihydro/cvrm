// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { Router }	from '@angular/router';
import { Response } from '@angular/http';
import { UserService } from '../../services/user.service';
import { RoleService } from '../../services/role.service';
import { User } from '../../classes/user';
import { Role } from '../../classes/role';
import { UserTicket } from '../../classes/user-ticket';
import { environment } from '../../../environments/environment';
import { EmailService } from '../../services/email.service';
import { Email } from '../../classes/email';

@Component({
	selector: 'pt-admin',
	templateUrl: './admin.component.html',
	providers: [UserService, RoleService, EmailService]
})
export class AdminComponent implements OnInit{

	users: User[] = [];
	roles: Role[] = [];
  	errorMessage: string = '';
  	isLoading: boolean = true;
  	selectedUser: User;
	rowsOnPage = 10;
	editUser: boolean;
	inviteUser: boolean;
	manageUsers: boolean;
	roleSelections: Role[] = [];
	saveSuccess: boolean;
	newUser: User;
	rolesMessage: boolean;
	private webUrl: string;
    userTicket: UserTicket;
    emailSent: boolean;

   	constructor(private userService : UserService, private roleService : RoleService, private router: Router, private emailService: EmailService){ }
 
	ngOnInit(){		
		this.editUser = false;
		this.inviteUser = false;
		this.manageUsers = true;
		this.saveSuccess = false;
		this.emailSent = false;
		this.roleSelections = [];
		this.users = [];
		this.webUrl = environment.webUrl;
		this.userService.getAll().subscribe(
		 /* happy path */ u => this.users = u,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => { this.isLoading = false; this.getRoles(); }
		);
	}

	onUpdate(u){
		this.roleSelections = [];
		this.selectedUser = u;
		this.editUser = true;		
		this.manageUsers = false;
		this.buildEditRoles();
	}

	goBack(){
		this.inviteUser = false;
		this.editUser = false;
		this.manageUsers = true; 
	}

	inviteNewUser(){
		this.roleSelections = [];
		this.inviteUser = true;
		this.manageUsers = false; 
		this.newUser = new User();
		this.buildInviteRoles();
	}

	getRoles(){
		this.roleService.getAll().subscribe(
		 /* happy path */ r => this.roles = r,
         /* error path */ e => this.errorMessage = e,
         /* onComplete */ () => { this.isLoading = false; this.getUserRolesList(); }
		);	
	}

	getUserRolesList(){
		for(let u of this.users){
			u.rolesList = "";			
			for(let r of u.roles){
				for(let role of this.roles){
					if(role.role == r){
						u.rolesList = u.rolesList + role.roleDescription + ', ';
					}
				}				
			}
			u.rolesList = u.rolesList.substring(0, u.rolesList.length - 2);
		}	
	}

	block(){			
		this.selectedUser.blocked = true;
		this.userService
	      	.update(this.selectedUser)
	      	.subscribe((r: Response) => { this.saveSuccess = true; })
	}

	unblock(){			
		this.selectedUser.blocked = false;
		this.userService
	      	.update(this.selectedUser)
	      	.subscribe((r: Response) => { this.saveSuccess = true;  })
	}

	onSelect(u){
		this.selectedUser = u;		
	}

	buildInviteRoles(){
		var newRole;
		for(let r of this.roles){		
			newRole = new Role();
			newRole.role = r.role;
			newRole.roleDescription = r.roleDescription;
			newRole.isSelected = false;			
			this.roleSelections.push(newRole);
		}
	}	

	buildEditRoles(){
		var newRole;
		for(let r of this.roles){		
			newRole = new Role();
			newRole.role = r.role;
			newRole.roleDescription = r.roleDescription;
			if(this.selectedUser.roles.indexOf(r.role) > -1)
				newRole.isSelected = true;			
			else
				newRole.isSelected = false;
			
			this.roleSelections.push(newRole);
		}
	}	

	checkChanged(e){
		for(let r of this.roleSelections){  			
			if(r.role == e.target.name){ 
				if(e.target.checked)
					r.isSelected = true;			
				else
					r.isSelected = false;
			}
		}
	}

	createUser(){
		let newRoles: string[] = [];
		for(let r of this.roleSelections){  			
			if(r.isSelected)
				newRoles.push(r.role);  			
		}
		this.newUser.roles = newRoles;
		console.log("roles: " + this.newUser.roles);
		if(newRoles.length > 0){
			let createdUser: User;
			this.rolesMessage = false;
			this.userService
      		.create(this.newUser)
      		.subscribe(r => createdUser = r, e => this.errorMessage = e,  () => { this.isLoading = false; console.log("created user: " + createdUser); this.getTicket(createdUser); })  			
		}
		else{
			this.rolesMessage = true;
		}
	}

	getTicket(createdUser){
		console.log("new jwt: " + createdUser.token);
		let userTicket = new UserTicket();
		userTicket.userId = createdUser.userId;
		userTicket.resultUrl = `${this.webUrl}/#/setpassword?userToken=${createdUser.token}`;
		console.log(userTicket.resultUrl);
		this.userService
      	.getUserTicket(userTicket)
      	.subscribe(r => this.userTicket = r, e => this.errorMessage = e,  () => { this.isLoading = false; this.sendEmail(); })
	}

	sendEmail(){
	    let expireHours = this.userTicket.ticketLife / 60 / 60;

	    let email = new Email();
	    email.to = this.newUser.email;
	    email.from = "support_cvrm@trihydro.com";
	    email.subject = "Invitation to join the WYDOT CV Resource Manager";
	    email.body = `
	    <div>
			Hello!
		</div>
		<div style="padding-top: 20px;">
			You have been invited to join the WYDOT CV Resource Manager. Please click <a href="${this.userTicket.ticketUrl}">here</a> to reset your password.
			This link will expire in ${expireHours} hours. 
		</div>
		<div style="padding-top: 20px;">
			Please do not reply to this email, and contact <a href="mailto:szumpf@trihydro.com">Shane Zumpf</a> for assistance. 
		</div>
		<div style="padding-top: 20px;">
			Thank you!
		</div>`;

	    this.emailService
	        .sendEmail(email)
	        .subscribe((r: Response) => {console.log('success'); this.emailSent = true; this.goBack(); this.ngOnInit(); });
    }

    submitEdits(): void {   		
		let newRoles: string[] = [];
  		for(let r of this.roleSelections){  			
  			if(r.isSelected)
  				newRoles.push(r.role);  			
  		}  		
		this.selectedUser.roles = newRoles;
		if(newRoles.length > 0){	
			this.userService
	      	.update(this.selectedUser)
	      	.subscribe((r: Response) => { }, e => this.errorMessage, () => { this.saveSuccess = true; this.updateRoles(newRoles); this.goBack(); })
        }
        else{
    		this.rolesMessage = true;
    	}
  	}

  	updateRoles(newRoles): void{		
		for(let u of this.users){
			if(u.userId == this.selectedUser.userId){
				u.rolesList = "";			
				for(let r of newRoles){
					for(let role of this.roles){
						if(role.role == r){
							u.rolesList = u.rolesList + role.roleDescription + ', ';
						}
					}
					
				}	
				u.rolesList = u.rolesList.substring(0, u.rolesList.length - 2);				
			}				
		}	
  	}

}