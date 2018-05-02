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

 package com.trihydro.cvpt.model.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;

@Component
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;	
	
	public void send(String to, String from, String subject, String body) throws MessagingException {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		
		helper = new MimeMessageHelper(message, true, "UTF-8"); 
		helper.setSubject(subject);
		helper.setTo(to);
		helper.setFrom(from);
		helper.setText("<html><body><img src='cid:cvLogo' style='width:350px;'>" + body + "</body></html>", true); // true indicates html
		                        
		ClassPathResource logo = new ClassPathResource("static/images/WYDOTCVP_logo_transparent.png");
		helper.addInline("cvLogo", logo);

		javaMailSender.send(message);
		
		
	}

}
