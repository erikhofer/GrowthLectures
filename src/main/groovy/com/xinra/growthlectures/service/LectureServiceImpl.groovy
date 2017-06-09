package com.xinra.growthlectures.service

import com.xinra.growthlectures.entity.Lecture
import com.xinra.growthlectures.entity.LectureRepository
import com.xinra.nucleus.service.DtoFactory
import groovy.transform.CompileStatic
import java.time.LocalDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@CompileStatic
@Service
class LectureServiceImpl extends GrowthlecturesServiceImpl implements LectureService {
	
	@Autowired
	private DtoFactory dtoFactory;
	
  @Autowired
  private LectureRepository lecureRepo;
  
	//TODO: remove
	private LectureSummaryDto getSampleLecture() {
		LectureSummaryDto dto = dtoFactory.createDto(LectureSummaryDto.class);
		NamedDto cat = new NamedDtoImpl() as NamedDto;
		cat.setName("Fernsehsendung");
		cat.setSlug("fernsehsendung");
		dto.setCategory(cat);
		dto.description = "Das ist die Beschreibung";
		dto.duration = 783;
		dto.rating = 3.5D;
		dto.name = "Hallo i bims"
		dto.slug = "hallo-i-bims"
    NamedDto lecturer = dtoFactory.createDto(NamedDto.class);
    lecturer.setName("Peter Lustig");
    lecturer.setSlug("peter-lustig");
    dto.setLecturer(lecturer);
    dto.setAdded(LocalDate.of(2017, 12, 01));
    
		return dto as LectureSummaryDto;
	}
  
  private LectureSummaryDto convertToDto(Lecture lecture) {
    LectureSummaryDto dto = dtoFactory.createDto(LectureSummaryDto.class);
    dto.description = lecture.getDescription();
    dto.rating = lecture.getRatingAverage()
    NamedDto cat = new NamedDtoImpl() as NamedDto;
    cat.setName(lecture.getCategory().getName());
    cat.setSlug(lecture.getCategory().getSlug());
    dto.setCategory(cat);
    dto.name = lecture.getName();
    dto.slug = lecture.getSlug();
    NamedDto lecturer = dtoFactory.createDto(NamedDto.class);
    lecturer.setName(lecture.getLecturer().getName());
    lecturer.setSlug(lecture.getLecturer().getSlug());
    dto.setLecturer(lecturer);
    dto.setAdded(lecture.getAdded());
    
    return dto;
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
