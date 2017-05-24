package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name="\"User\"")
@CompileStatic
class User extends BaseEntity {

	@OneToMany(mappedBy = "user")
	def Set<Login> logins;
  
  @ElementCollection
  @Enumerated(EnumType.STRING)
  def Set<Role> roles;
	
}
