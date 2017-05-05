package com.xinra.growthlectures.entity

import groovy.transform.CompileStatic
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@CompileStatic
class EmailLogin extends Login {
  
	@Column(nullable = false)
  def String email;
  
  @Column(nullable = false)
  def String password;

}
