package com.xinra.growthlectures.entity

import groovy.transform.CompileStatic
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@CompileStatic
class Lecturer extends NamedEntity {
  
  @OneToMany(mappedBy = "lecturer")
  def Set<Lecture> lectures;

}
