package com.app.authentication.services;

import com.app.authentication.dtos.request.EmailValuesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService{

  private final static Logger _log = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private TemplateEngine templateEngine;

  @Value("${mail.url.front}")
  private String urlFront;


  @Override
  public void sendEmail(EmailValuesDto emailValuesDto) {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      Context context = new Context();
      Map<String, Object> model = new HashMap<>();
      model.put("userName", emailValuesDto.getUserName());
      model.put("url", urlFront + emailValuesDto.getTokenPassword());
      context.setVariables(model);
      String htmlText = templateEngine.process("email-template", context);
      helper.setFrom(emailValuesDto.getMailFrom());
      helper.setTo(emailValuesDto.getMailTo());
      helper.setSubject(emailValuesDto.getSubject());
      helper.setText(htmlText, true);
      javaMailSender.send(message);

    }catch (MessagingException e){
      _log.error("El envio del email fallo");
      _log.error(e.getMessage());
    }

  }
}
