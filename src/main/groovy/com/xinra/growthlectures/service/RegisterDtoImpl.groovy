package com.xinra.growthlectures.service

import groovy.transform.CompileStatic
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@CompileStatic
class RegisterDtoImpl extends GrowthlecturesDtoImpl {
  
  def String email;
  def String password;
  def String passwordConfirm;
  
}
