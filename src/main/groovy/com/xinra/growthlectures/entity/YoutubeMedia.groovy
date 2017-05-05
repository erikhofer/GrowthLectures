package com.xinra.growthlectures.entity

import groovy.transform.CompileStatic
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@CompileStatic
class YoutubeMedia extends Media {

	@Column(nullable = false)
	def String youtubeId;
	
}
