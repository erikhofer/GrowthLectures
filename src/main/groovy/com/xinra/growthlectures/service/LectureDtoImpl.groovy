package com.xinra.growthlectures.service

import com.xinra.nucleus.interfacegenerator.GenerateInterface
import com.xinra.nucleus.interfacegenerator.InterfaceNamingStrategy
import com.xinra.nucleus.service.DtoImpl
import org.codehaus.groovy.transform.trait.Traits.Implemented

class LectureDtoImpl extends LectureSummaryDtoImpl {
	
	def String note;
  def Integer userRating;
	
}
