package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.MappedSuperclass

@MappedSuperclass
@CompileStatic
abstract class Media extends BaseEntity {

	@Column(nullable = false)
  def String url;
  
  def Integer start;
  
  @Column(nullable = false)
  def Integer duration;
  
}
