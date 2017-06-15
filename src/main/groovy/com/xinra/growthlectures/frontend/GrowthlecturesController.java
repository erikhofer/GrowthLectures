package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.UserDto;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

/**
 * Superclass for controllers (not mandatory).
 */
public abstract class GrowthlecturesController {

  @Autowired
  private HttpServletRequest context;
  
  /**
   * @return The DTO of the current user or {@code null} if the user is not authenticated.
   */
  public UserDto getUserDto() {
    Principal principal = context.getUserPrincipal();
    return principal == null ? null : (UserDto) ((Authentication) principal).getPrincipal();
  }
  
  /**
   * @return The ID of the current user or {@code null} if the user is not authenticated.
   */
  public String getUserId() {
    UserDto dto = getUserDto();
    return dto == null ? null : dto.getPk().getId();
  }
  
}
