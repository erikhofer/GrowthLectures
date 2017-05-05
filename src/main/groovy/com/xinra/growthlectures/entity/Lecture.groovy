package com.xinra.growthlectures.entity

import groovy.transform.CompileStatic
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
@CompileStatic
class Lecture extends NamedEntity {

  def String description;
  
  @ManyToOne(optional = false)
  def Lecturer lecturer;
  
  @ManyToOne(optional = false)
  def Category category;
  
  //cache
  def Double ratingAverage;
  def Integer ratingAmount;
  
}
