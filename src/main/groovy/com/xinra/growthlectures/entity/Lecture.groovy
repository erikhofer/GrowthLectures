package com.xinra.growthlectures.entity

import groovy.transform.CompileStatic
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToOne


@Entity
@CompileStatic
class Lecture extends NamedEntity {

  @Column(length = 4000)
  def String description;
  
  @ManyToOne(optional = false)
  def Lecturer lecturer;
  
  @ManyToOne(optional = false)
  def Category category;
  
  def LocalDate added;
  
  @OneToOne(optional = false)
  def Media media;
  
  //cache
  def Double ratingAverage;
  def Integer ratingAmount;
  
  
}
