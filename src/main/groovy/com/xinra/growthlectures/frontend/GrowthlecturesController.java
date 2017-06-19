package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.UserDto;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

/**
 * Superclass for controllers (not mandatory).
 */
public abstract class GrowthlecturesController {

  @Autowired
  private HttpServletRequest context;
  
  @Autowired 
  private LectureService lectureService;
  
  /**
   * @return The DTO of the current user or {@code null} if the user is not authenticated.
   */
  public Optional<UserDto> getUserDto() {
    Principal principal = context.getUserPrincipal();
    return Optional.ofNullable((UserDto) ((Authentication) principal).getPrincipal());
  }
  
  /**
   * @return The ID of the current user or {@code null} if the user is not authenticated.
   */
  public Optional<String> getUserId() {
    return getUserDto().map(u -> u.getPk().getId());
  }
  
  /**
   * Processes a list of lecture summaries. Adds user ratings if a user is authenticated.
   */
  public <T extends Iterable<LectureSummaryDto>> T process(T lectures) {
    getUserId().ifPresent(userId -> lectureService.supplyRatings(lectures, userId));
    return lectures;
  }
  
}
