package com.record.myplace.infra.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender mailSender;
	
	public void sendText(String to, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);
		
		mailSender.send(msg);
	}
}
