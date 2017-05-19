package com.xinra.growthlectures.service

import com.xinra.nucleus.interfacegenerator.GenerateInterface
import com.xinra.nucleus.interfacegenerator.InterfaceNamingStrategy
import groovy.transform.CompileStatic

@CompileStatic
@GenerateInterface(
      namingStrategy = InterfaceNamingStrategy.EXCEPT_LAST_FOUR_CHARS,
      propertyConstants = true
    )
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
