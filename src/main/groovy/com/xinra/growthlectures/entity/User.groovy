package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@CompileStatic
class User extends BaseEntity {

	@OneToMany(mappedBy = "user")
	def Collection<Login> logins;
	
}
