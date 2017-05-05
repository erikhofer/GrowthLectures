package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
@CompileStatic
class LectureUserData extends BaseEntity {
  
  def String note;
  def Integer rating;
  
  @ManyToOne(optional = false)
  def User user;
  
  @ManyToOne(optional = false)
  def Lecture lecture;
  
}
