package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name="\"User\"")
@CompileStatic
class User extends BaseEntity {

	@OneToMany(mappedBy = "user")
	def Set<Login> logins;
  
  @Enumerated(EnumType.STRING)
  def Role role;
	
}
