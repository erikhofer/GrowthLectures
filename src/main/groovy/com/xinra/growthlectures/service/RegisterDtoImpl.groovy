package com.xinra.growthlectures.service

import groovy.transform.CompileStatic

@CompileStatic
class RegisterDtoImpl extends GrowthlecturesDtoImpl {
  
  def String email;
  def String password;
  def String passwordConfirm;
  
}
