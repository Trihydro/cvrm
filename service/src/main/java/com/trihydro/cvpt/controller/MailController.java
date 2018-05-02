/********************************************************************
 *  Copyright 2016 Trihydro 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ********************************************************************/

 package com.trihydro.cvpt.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.mail.MessagingException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import com.trihydro.cvpt.model.Email;
import com.trihydro.cvpt.model.service.EmailService;

@CrossOrigin
@RestController
@RequestMapping("/cvpt")
public class MailController{

	@Autowired
	private EmailService emailService;

    @RequestMapping(method = RequestMethod.POST, value = "/sendEmail")
	public void sendMail(@RequestBody Email input) throws MessagingException {		
		emailService.send(input.getTo(), input.getFrom(), input.getSubject(), input.getBody());
	}
}