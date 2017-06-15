package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.MappedSuperclass

@Entity
@CompileStatic
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Media extends BaseEntity {

	@Column(nullable = false)
  def String url;
  
  def Integer start;
  
  @Column(nullable = false)
  def Integer duration;
  
  def String thumbnail;
  
}
