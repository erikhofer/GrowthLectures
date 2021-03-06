package com.xinra.growthlectures.frontend;

import com.google.common.collect.ImmutableMap;
import com.xinra.growthlectures.entity.OrderBy;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.SearchService;
import com.xinra.growthlectures.service.UserDto;
import com.xinra.nucleus.service.DtoFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

/**
 * Superclass for controllers (not mandatory).
 */
public abstract class GrowthlecturesController {
  
  protected static interface SearchHandler {
    List<LectureSummaryDto> search(String query, OrderBy orderBy, boolean decending);
  }

  @Autowired
  private HttpServletRequest context;
  
  @Autowired 
  private LectureService lectureService;

  @Autowired
  protected DtoFactory dtoFactory;
  
  @Autowired
  protected SearchService searchService;
  
  /**
   * @return The DTO of the current user or {@code null} if the user is not authenticated.
   */
  public Optional<UserDto> getUserDto() {
    return Optional.ofNullable(context.getUserPrincipal())
        .map(p -> (UserDto) ((Authentication) p).getPrincipal());
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
  public <T extends Collection<LectureSummaryDto>> T process(T lectures) {
    getUserId().ifPresent(userId -> lectureService.supplyRatings(lectures, userId));
    return lectures;
  }
  
  protected void addSearchModel(Model model, String path, SearchHandler searchHandler) {
    boolean searchFieldOnly = searchHandler == null;
    
    model.addAttribute("searchFieldOnly", searchFieldOnly);
    model.addAttribute("searchPath", path);
        
    if (searchFieldOnly) {
      return;
    }

    OrderBy currentOrder; 
    String orderParam = context.getParameter("orderby");    
    if (orderParam == null) {
      currentOrder = OrderBy.DEFAULT;
    } else {
      try {
        currentOrder = OrderBy.valueOf(orderParam.toUpperCase());
      } catch (IllegalArgumentException iae) {
        currentOrder = OrderBy.DEFAULT;
      }
    }
    
    boolean decending = !"false".equals(context.getParameter("decending"));
    
    String queryParam = context.getParameter("q");
    if (queryParam == null) {
      queryParam = "";
    }
    
    List<OrderByDto> orders = new ArrayList<>();
    
    Map<OrderBy, String> names = ImmutableMap.of(
          OrderBy.ADDED, "Added",
          OrderBy.RATING, "Rating"
        );
    
    Map<OrderBy, String> icons = ImmutableMap.of(
        OrderBy.ADDED, "glyphicon glyphicon-calendar",
        OrderBy.RATING, "glyphicon glyphicon-star-empty"
      );
    
    for (OrderBy order : names.keySet()) {
      
      if (order == currentOrder) {
        continue;
      }
      
      OrderByDto orderDto = dtoFactory.createDto(OrderByDto.class);
      orderDto.setValue(order.name().toLowerCase());
      orderDto.setName(names.get(order));
      orderDto.setIcon(icons.get(order));
      orders.add(orderDto);
    }
    
    OrderByDto currentOrderDto = dtoFactory.createDto(OrderByDto.class);
    currentOrderDto.setValue(currentOrder.name().toLowerCase());
    currentOrderDto.setName(names.get(currentOrder));
    currentOrderDto.setIcon(icons.get(currentOrder));
    
    List<LectureSummaryDto> results = 
        process(searchHandler.search(queryParam, currentOrder, decending));
    
    model.addAttribute("searchQuery", queryParam);    
    model.addAttribute("searchOrders", orders);
    model.addAttribute("searchCurrentOrder", currentOrderDto);
    model.addAttribute("searchDecending", decending);
    model.addAttribute("searchResults", results);
  }
  
}
