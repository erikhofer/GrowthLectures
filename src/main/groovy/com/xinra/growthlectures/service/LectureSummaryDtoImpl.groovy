package com.xinra.growthlectures.service

import groovy.transform.CompileStatic
import org.apache.catalina.UserDatabase

@CompileStatic
class LectureSummaryDtoImpl extends NamedDtoImpl {

  def NamedDto category;
  def Boolean played;
  def String description;
  def Double rating;
  def Integer userRating;
  def Integer duration;
  
}
