package com.xinra.growthlectures.service

import com.xinra.nucleus.service.DtoFactory
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@CompileStatic
@Service
class LectureServiceImpl extends GrowthlecturesServiceImpl {
	
	@Autowired
	private DtoFactory dtoFactory;
	
	//TODO: remove
	private LectureSummaryDto getSampleLecture() {
		LectureSummaryDto dto = dtoFactory.createDto(LectureSummaryDto.class);
		NamedDto cat = new NamedDtoImpl() as NamedDto;
		cat.setName("Peter");
		cat.setSlug("peter");
		dto.setCategory(cat);
		dto.description = "Das ist die Beschreibung";
		dto.duration = 783;
		dto.rating = 3.5D;
		dto.name = "Hallo i bims"
		dto.slug = "hallo-i-bims"
		return dto as LectureSummaryDto;
	}

	public List<LectureSummaryDto> getRecentLectures() {
		List<LectureSummaryDto> dtos = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			dtos.add(getSampleLecture());
		}
		return dtos;
	}
	
	public List<LectureSummaryDto> getPopularLectures() {
		List<LectureSummaryDto> dtos = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
		  dtos.add(getSampleLecture());
		}
		return dtos;
  }
	
}
