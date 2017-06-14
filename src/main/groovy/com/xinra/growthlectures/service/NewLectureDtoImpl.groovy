package com.xinra.growthlectures.service

import com.xinra.growthlectures.entity.Category
import com.xinra.growthlectures.entity.Lecturer
import com.xinra.nucleus.interfacegenerator.GenerateInterface
import groovy.transform.CompileStatic

@CompileStatic
class NewLectureDtoImpl extends NamedDtoImpl {

  def String description;
  def String link;
  def String newcategory;
  def String newlecturer;
  def String start;
  def String end;
  def String videoId;
  def String platform;
  
}
