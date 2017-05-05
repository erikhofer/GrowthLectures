package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.BaseEntity
import groovy.transform.CompileStatic
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import org.springframework.data.repository.util.NullableWrapper


/**
 * Super type for everything that has a name and is a frontend resource (identified by slug).
 */
@MappedSuperclass
@CompileStatic
abstract class NamedEntity extends BaseEntity {
  
	@Column(nullable = false)
  def String name;
  
  @Column(unique = true, nullable = false)
  def String slug;
  
}
