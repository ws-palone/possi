package fr.istic.iodeman;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Value("${MAIL_HOST}")
	private String host;
	
	@Value("${MAIL_PORT}")
	private String port;
	
	@Value("${MAIL_USERNAME}")
	private String username;

	@Value("${MAIL_PASSWORD}")
	private String password;
	
	@Bean
	public JavaMailSender javaMailService() {
		
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(host);
		javaMailSender.setPort(Integer.parseInt(port));
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		
		javaMailSender.setJavaMailProperties(getMailProperties());
		
		return javaMailSender;
	}
	
	private Properties getMailProperties() {
		
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.debug", "true");
        return properties;
	}
	
}
