package com.app.authentication.services;

import com.app.authentication.dtos.request.EmailValuesDto;

public interface EmailService {

  void sendEmail(EmailValuesDto emailValuesDto);

}
