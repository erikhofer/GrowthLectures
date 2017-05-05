package com.xinra.growthlectures.entity

import groovy.transform.CompileStatic
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@CompileStatic
class Category extends NamedEntity {
  
  @OneToMany(mappedBy = "category")
  def Collection<Lecture> lectures;

}
