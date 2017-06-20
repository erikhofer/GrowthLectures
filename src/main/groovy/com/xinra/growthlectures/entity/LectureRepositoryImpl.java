package com.xinra.growthlectures.entity;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class LectureRepositoryImpl implements AbstractLectureRepositoryCustom<Lecture> {
  
  @Autowired
  EntityManager entityManager;

  @SuppressWarnings("unchecked")
  @Override
  public List<Lecture> search(String term, OrderBy orderBy, boolean decending) {
    String queryString = "SELECT l FROM Lecture l";
    
    if (term.isEmpty()) {
      Query query = entityManager.createQuery(queryString + orderBy(orderBy) 
          + decending(decending));
      return query.getResultList(); 
      
    } else {  
      Query query = entityManager.createQuery(queryString + " WHERE l.name = :term" 
          + orderBy(orderBy) + decending(decending));
      query.setParameter("term", term);
      return query.getResultList();
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Lecture> searchForCategory(String categorySlug, String term, OrderBy orderBy, boolean decending) {
    String queryString = "Select l From Lecture l Where l.category.slug = :categorySlug";
    
    if (term.isEmpty()) {
      Query query = entityManager.createQuery(queryString + orderBy(orderBy) 
          + decending(decending));
      query.setParameter("categorySlug", categorySlug);
      return query.getResultList(); 
      
    } else {    
      Query query = entityManager.createQuery(queryString + " AND l.name = :term" 
          + orderBy(orderBy) + decending(decending));
      query.setParameter("term", term);
      query.setParameter("categorySlug", categorySlug);
      return query.getResultList(); 
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Lecture> searchForLecturer(String lecturerSlug, String term, OrderBy orderBy, boolean decending) {
    String queryString = "Select l From Lecture l Where l.lecturer.slug = :lecturerSlug";
    
    if (term.isEmpty()) {
      Query query = entityManager.createQuery(queryString + orderBy(orderBy) 
          + decending(decending));
      query.setParameter("lecturerSlug", lecturerSlug);
      return query.getResultList(); 
      
    } else {      
      Query query = entityManager.createQuery(queryString + " AND l.name = :term" 
          + orderBy(orderBy) + decending(decending));
      query.setParameter("term", term);
      query.setParameter("lecturerSlug", lecturerSlug);
      return query.getResultList(); 
    }
  }
  
  public String orderBy(OrderBy orderBy) {
    
    String addedString;
    
    switch (orderBy.toString()) {
      case "ADDED":
        
        addedString = " ORDER BY l.added";
        
        break;
      
      case "RATING":
        
        addedString = " ORDER BY l.ratingAverage";
        
        break;

      default:
        addedString = "";
        break;
    }
    
    return addedString;
  }
  
  public String decending(boolean decending) {
    
    String addedString;
    
    if (decending) {    
      addedString = " DESC";
      
    } else {      
      addedString = " ASC";
    }
    return addedString;
  }

}
