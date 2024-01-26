package com.productiontracking.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl _vMailSender = new JavaMailSenderImpl();
        _vMailSender.setHost(host);
        _vMailSender.setPort(port);
        _vMailSender.setUsername(username);
        _vMailSender.setPassword(password);

        Properties _vProperties = _vMailSender.getJavaMailProperties();
        _vProperties.put("mail.transport.protocol", "smtp");
        _vProperties.put("mail.smtp.auth", "true");
        _vProperties.put("mail.smtp.starttls.enable", "true");
        _vProperties.put("mail.debug", "true");
        return _vMailSender;
    }
}
