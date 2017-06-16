package com.xinra.growthlectures.service

import groovy.transform.CompileStatic

@CompileStatic
class MediaDtoImpl extends GrowthlecturesDtoImpl {
  
  def MediaType type
  def String url
  def Integer start
  def Integer end
  
}
