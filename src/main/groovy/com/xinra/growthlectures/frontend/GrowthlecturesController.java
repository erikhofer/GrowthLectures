package com.xinra.growthlectures.frontend;

import com.google.common.collect.ImmutableMap;
import com.xinra.growthlectures.entity.OrderBy;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.SearchService;
import com.xinra.growthlectures.service.UserDto;
import com.xinra.nucleus.service.DtoFactory;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
  protected DtoFactory dtoFactory;
  
  @Autowired
  protected SearchService searchService;
  
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
    
    List<LectureSummaryDto> results = searchHandler.search(queryParam, currentOrder, decending);
    
    model.addAttribute("searchQuery", queryParam);    
    model.addAttribute("searchOrders", orders);
    model.addAttribute("searchCurrentOrder", currentOrderDto);
    model.addAttribute("searchDecending", decending);
    model.addAttribute("searchResults", results);
  }
  
}
