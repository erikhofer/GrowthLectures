package com.xinra.growthlectures.frontend;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CsrfController {

  /**
   * REST controller that supplies a CSRF token for API requests.
   */
  @ResponseBody
  @RequestMapping(path = "/csrf-token", method = RequestMethod.GET)
  public String getCsrfToken(HttpServletRequest request) {
    return ((CsrfToken) request.getAttribute(CsrfToken.class.getName())).getToken();
  }
  
}
