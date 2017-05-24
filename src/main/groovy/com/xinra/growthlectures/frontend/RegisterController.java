package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.EmailLoginDto;
import com.xinra.growthlectures.service.RegisterDto;
import com.xinra.nucleus.service.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {
   
  @Autowired
  private DtoFactory dtoFactory;
  
  @RequestMapping(Ui.URL_REGISTER)
  public String registerForm(Model model) {
    
    model.addAttribute("registerDto", dtoFactory.createDto(RegisterDto.class));
    
    return "register";
  }
  
  @RequestMapping(value = Ui.URL_REGISTER, method = RequestMethod.POST)
  public String register(Model model, EmailLoginDto user) {
       
    model.addAttribute("registerDto", dtoFactory.createDto(RegisterDto.class));
    
    return "register";
  }

}

