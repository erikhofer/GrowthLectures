package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.service.RegisterDto;
import com.xinra.growthlectures.service.UserService;
import com.xinra.nucleus.service.DtoFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {
  
  //source: http://howtodoinjava.com/regex/java-regex-validate-email-address/
  private static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+"
      + "(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
   
  @Autowired
  private DtoFactory dtoFactory;
  
  @Autowired
  private UserService userService;
  
  /**
   * Displays the register form.
   */
  @RequestMapping(Ui.URL_REGISTER)
  public String registerForm(Model model) {
    
    model.addAttribute("registerDto", dtoFactory.createDto(RegisterDto.class));
    
    return "register";
  }
  
  /**
   * Handles register form input for email registration.
   */
  @RequestMapping(value = Ui.URL_REGISTER, method = RequestMethod.POST)
  public String registerEmail(Model model, RegisterDto registerDto) {
    
    List<String> errors = new ArrayList<>();
    
    String email = Util.normalize(registerDto.getEmail());
    
    if (email == null || !Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
      errors.add("Invalid e-mail!");
    } else if (userService.doesEmailExist(email)) {
      errors.add("E-mail is already registered!");
    }
    
    if (registerDto.getPassword() == null || registerDto.getPassword().isEmpty()) {
      errors.add("Password is required!");
    } else if (registerDto.getPasswordConfirm() == null 
        || !registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
      errors.add("Passwords don't match!");
    }
    
    if (errors.isEmpty()) {
      userService.register(registerDto.getEmail(), registerDto.getPassword());
      
      model.addAttribute("success", true);
      model.addAttribute("registerDto", dtoFactory.createDto(RegisterDto.class));
    } else {
      model.addAttribute("errors", errors);
      registerDto.setPassword(null);
      registerDto.setPasswordConfirm(null);
      model.addAttribute("registerDto", registerDto);
    }
    
    return "register";
  }

}

