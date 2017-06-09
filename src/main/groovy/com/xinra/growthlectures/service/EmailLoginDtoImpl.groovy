package com.xinra.growthlectures.service

import com.xinra.growthlectures.validation.ValidEmail
import groovy.transform.CompileStatic
import javax.validation.constraints.NotNull

@CompileStatic
class EmailLoginDtoImpl extends GrowthlecturesDtoImpl {

  def String email;
  def String password;
  
}
