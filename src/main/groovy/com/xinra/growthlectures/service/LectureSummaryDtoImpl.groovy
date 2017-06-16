package com.xinra.growthlectures.service

import com.xinra.nucleus.interfacegenerator.GenerateInterface
import com.xinra.nucleus.interfacegenerator.InterfaceNamingStrategy
import groovy.transform.CompileStatic
import java.time.LocalDate

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
  def LocalDate added;
  
  private MediaDto media;
  private NamedDto lecturer;
  private NamedDto category;
  
  public MediaDto getMedia() {
    return media;
  }
  public void setMedia(MediaDto media) {
    this.media = media;
  }
  public NamedDto getLecturer() {
    return lecturer;
  }
  public void setLecturer(NamedDto lecturer) {
    this.lecturer = lecturer;
  }
  public NamedDto getCategory() {
    return category;
  }
  public void setCategory(NamedDto category) {
    this.category = category;
  }
  
  
  
}
