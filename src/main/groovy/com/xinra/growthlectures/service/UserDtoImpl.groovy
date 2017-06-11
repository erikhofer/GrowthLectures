package com.xinra.growthlectures.service

import com.xinra.growthlectures.entity.Role
import com.xinra.nucleus.entity.EntityPk
import groovy.transform.CompileStatic

@CompileStatic
class UserDtoImpl extends GrowthlecturesDtoImpl implements UserDto {

  def EntityPk pk;
  def String name;
  def Set<Role> roles;
  
  @Override
  public String toString() {
    return name;
  }
  
}
