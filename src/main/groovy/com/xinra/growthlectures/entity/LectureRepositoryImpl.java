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
    String queryString = "SELECT l FROM Lecture l WHERE l.name = :term";
    
    Query query = entityManager.createQuery(queryString);
    query.setParameter("term", term);
    return query.getResultList();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Lecture> searchForCategory(String categorySlug, String term, OrderBy orderBy, boolean decending) {
    String queryString = "Select l From Lecture l Where l.category.slug = :categorySlug AND l.name = :term";
    
    Query query = entityManager.createQuery(queryString);
    query.setParameter("term", term);
    query.setParameter("categorySlug", categorySlug);
    return query.getResultList(); 
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Lecture> searchForLecturer(String lecturerSlug, String term, OrderBy orderBy, boolean decending) {
    String queryString = "Select l From Lecture l Where l.lecturer.slug = :lecturerSlug AND l.name = :term";
    
    Query query = entityManager.createQuery(queryString);
    query.setParameter("term", term);
    query.setParameter("lecturerSlug", lecturerSlug);
    return query.getResultList(); 
  }

}
