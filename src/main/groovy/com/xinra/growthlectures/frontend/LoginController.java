package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.EmailLoginDto;
import com.xinra.nucleus.service.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {
   
  @Autowired
  private DtoFactory dtoFactory;
  
  /**
   * Displays the login form.
   */
  @RequestMapping(Ui.URL_LOGIN)
  public String login(Model model, @RequestParam(name = "error", required = false) String error,
      @RequestParam(name = "logout", required = false) String logout) {
    
    model.addAttribute("emailLoginDto", dtoFactory.createDto(EmailLoginDto.class));
    
    if (error != null) {
      model.addAttribute("error", true);
    } else if (logout != null) {
      model.addAttribute("logout", true);
    }
    
    return "login";
  }

}
