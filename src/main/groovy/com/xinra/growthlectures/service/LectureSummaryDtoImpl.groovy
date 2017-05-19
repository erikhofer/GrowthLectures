package com.xinra.growthlectures.service

import groovy.transform.CompileStatic

@CompileStatic
class LectureSummaryDtoImpl extends NamedDtoImpl {

  //def NamedDto category;
  def Boolean played;
  def String description;
  def Double rating;
  def Integer userRating;
  def Integer duration;
  
  private NamedDto category;
  
  public NamedDto getCategory() {
    return category;
  }
  
  public void setCategory(NamedDto category) {
    this.category = category;
  }
}
