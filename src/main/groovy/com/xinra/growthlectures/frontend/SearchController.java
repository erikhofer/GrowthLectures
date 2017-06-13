package com.xinra.growthlectures.frontend;

import com.google.common.collect.ImmutableMap;
import com.xinra.growthlectures.service.OrderBy;
import com.xinra.nucleus.service.DtoFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController {
  
  public static final OrderBy DEFAULT_ORDER = OrderBy.ADDED;
  
//  @Autowired
//  SearchService searchService;
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @Autowired
  private HttpServletRequest context;
  
  @RequestMapping(Ui.URL_SEARCH)
  public String search(Model model) {
    
    addSearchModel(model, Ui.URL_SEARCH);

    model.addAttribute("results", Collections.emptyList());
    
    return "search";
  }
  
  public void addSearchModel(Model model, String path) {
  
    OrderBy currentOrder;
    
    String orderParam = context.getParameter("orderby");    
    if (orderParam == null) {
      currentOrder = DEFAULT_ORDER;
    } else {
      try {
        currentOrder = OrderBy.valueOf(orderParam.toUpperCase());
      } catch (IllegalArgumentException iae) {
        currentOrder = DEFAULT_ORDER;
      }
    }
    
    String queryParam = context.getParameter("q");
    if (queryParam == null) {
      queryParam = "";
    }
    
    List<OrderByDto> orders = new ArrayList<>();
    
    Map<OrderBy, String> names = ImmutableMap.of(
          OrderBy.ADDED, "Added",
          OrderBy.RATING, "Rating"
        );
    
    for (OrderBy order : names.keySet()) {
      
      if (order == currentOrder) {
        continue;
      }
      
      OrderByDto orderDto = dtoFactory.createDto(OrderByDto.class);
      orderDto.setValue(order.name().toLowerCase());
      orderDto.setName(names.get(order));
      orders.add(orderDto);
    }
    
    model.addAttribute("searchQuery", queryParam);
    model.addAttribute("searchPath", path);
    model.addAttribute("searchOrders", orders);
    model.addAttribute("searchCurrentOrder", names.get(currentOrder));
  }
  
}
