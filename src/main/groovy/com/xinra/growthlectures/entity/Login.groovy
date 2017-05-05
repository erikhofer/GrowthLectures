package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.ManyToOne

@Entity
@CompileStatic
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Login extends BaseEntity {
  
  @ManyToOne(optional = false)
  def User user;

}
