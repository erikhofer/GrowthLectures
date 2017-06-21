package com.xinra.growthlectures.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class LectureRepositoryImpl implements AbstractLectureRepositoryCustom<Lecture> {
  
  @Autowired
  EntityManager entityManager;


  @Override
  public List<Lecture> search(String term, OrderBy orderBy, boolean decending) {
    Map<String, Object> parameters = new HashMap<>();
    return searchInternal(parameters, "", term, orderBy, decending);
  }
  

  @Override
  public List<Lecture> searchForCategory(String categorySlug, String term, 
      OrderBy orderBy, boolean decending) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categorySlug", categorySlug);
    return searchInternal(parameters, " AND l.category.slug = :categorySlug", term, orderBy, decending);
  }
  

  @Override
  public List<Lecture> searchForLecturer(String lecturerSlug, String term, 
      OrderBy orderBy, boolean decending) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("lecturerSlug", lecturerSlug);
    return searchInternal(parameters, " AND l.lecturer.slug = :lecturerSlug", term, orderBy, decending);
  }

  
  @SuppressWarnings("unchecked")
  private List<Lecture> searchInternal(Map<String, Object> parameters, String where, String term, 
      OrderBy orderBy, boolean decending) {
    
    String queryString = "SELECT l FROM Lecture l WHERE 1=1" + where;
    
    if (!term.isEmpty()) { // throws NPE
      
      queryString += " AND (UPPER(l.name) LIKE UPPER(:term)"
          + " OR UPPER(l.lecturer.name) LIKE UPPER(:term)"
          + " OR UPPER(l.description) LIKE UPPER(:term)"
          + " OR UPPER(l.category.name) LIKE UPPER(:term))";
      
      parameters.put("term", "%" + term + "%");
    } 
    
    switch (orderBy) {
      case ADDED:       
        queryString += " ORDER BY l.added";     
        break;   
      case RATING:        
        queryString += " ORDER BY l.ratingAverage";       
        break;
      default:
        throw new IllegalArgumentException();
    }
    
    if (decending) {
      queryString += " DESC";
    }
    
    Query query = entityManager.createQuery(queryString);
    parameters.forEach((k, v) -> query.setParameter(k, v));
    return query.getResultList();
  }

}
