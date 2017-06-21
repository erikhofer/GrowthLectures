package com.xinra.growthlectures.frontend;

import com.google.common.collect.ImmutableMap;
import com.xinra.growthlectures.service.SlugNotFoundException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the frontend (is applied to all controllers).
 */
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidDataException.class)
  public final Object handleInvalidDataException(InvalidDataException ide,
      HandlerMethod controllerMethod) {
    return getResponse(ide.getMessages(), controllerMethod, HttpStatus.UNPROCESSABLE_ENTITY);
  }
  
  @ExceptionHandler(SlugNotFoundException.class)
  public final Object handleNotFoundException(Throwable throwable, HandlerMethod controllerMethod) {
    return getResponse(throwable.getMessage(), controllerMethod, HttpStatus.NOT_FOUND);
  }
  
  @ExceptionHandler(IllegalArgumentException.class)
  public final Object handleIllegalArgumentException(IllegalArgumentException iae,
      HandlerMethod controllerMethod) {
    return getResponse(iae.getMessage(), controllerMethod, HttpStatus.BAD_REQUEST);
  }
  
  /**
   * Generates a response depending on the current controller. For normal controllers the "error" 
   * template is returned and supplied with the usual model, for REST controllers the
   * {@code message} is returned as response body.
   */
  protected Object getResponse(Object message, HandlerMethod controllerMethod, HttpStatus status) {
    
    boolean isRestController = controllerMethod.hasMethodAnnotation(ResponseBody.class)
        || controllerMethod.getBeanType().isAnnotationPresent(ResponseBody.class)
        || controllerMethod.getBeanType().isAnnotationPresent(RestController.class);
    
    if (isRestController) {
      return new ResponseEntity<>(message, status);
    } else {
      Map<String, Object> model = ImmutableMap.of(
          "message", message != null ? message : "No message available",
          "status", status.value(),
          "error", status.getReasonPhrase()
        );
      return new ModelAndView("error", model, status);
    }
  }
}
