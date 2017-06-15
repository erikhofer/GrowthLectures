package com.xinra.growthlectures.service

import com.xinra.growthlectures.Util
import com.xinra.growthlectures.entity.AbstractLectureUserDataRepository
import com.xinra.growthlectures.entity.Lecture
import com.xinra.growthlectures.entity.LectureRepository
import com.xinra.growthlectures.entity.LectureUserData
import com.xinra.growthlectures.entity.LectureUserDataRepository
import com.xinra.growthlectures.entity.UserRepository
import com.xinra.nucleus.entity.EntityFactory
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
  private LectureRepository lectureRepo;
  
  @Autowired
  private LectureUserDataRepository lectureUserDataRepo;
  
  @Autowired
  private UserRepository userRepo;
  
  @Autowired
  private EntityFactory entityFactory;
  
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
  
  private void convertToSummaryDto(Lecture lecture, LectureSummaryDto dto) {
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
  }
  
  private LectureSummaryDto convertToSummaryDto(Lecture lecture) {
	  LectureSummaryDto dto = dtoFactory.createDto(LectureSummaryDto.class);
	  convertToSummaryDto(lecture, dto);
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
  
  public LectureDto getBySlug(String lectureSlug, String categorySlug, String userId)
      throws SlugNotFoundException {
		Objects.requireNonNull(lectureSlug);
		Objects.requireNonNull(categorySlug);
		
		Lecture lecture = lectureRepo.findBySlugAndCategorySlug(lectureSlug, categorySlug);
		
		if (lecture == null) {
			throw new SlugNotFoundException();
		}
		
		LectureDto lectureDto = dtoFactory.createDto(LectureDto.class);
		convertToSummaryDto(lecture, lectureDto);
		
		if (userId != null) {
  		LectureUserData userData = lectureUserDataRepo
  		    .findByLectureIdAndUserId(lecture.getPk().getId(), userId);
  			
  		if (userData != null) {
  			lectureDto.setNote(userData.getNote());
  			lectureDto.setUserRating(userData.getRating());
  		}
		}
		
		return lectureDto;
	}
  
  public String getLectureId(String lectureSlug, String categorySlug) 
      throws SlugNotFoundException {
    Objects.requireNonNull(lectureSlug);
    Objects.requireNonNull(categorySlug);
    
    String[][] result = lectureRepo.getIdAndCatgorySlug(lectureSlug);
    
    if (result.length == 0 || !result[0][1].equals(categorySlug)) {
      throw new SlugNotFoundException();
    }
    
    return result[0][0];
  }
  
  public String getNote(String lectureId, String userId) {
	  Objects.requireNonNull(lectureId);
	  Objects.requireNonNull(userId);
	  
    String[][] result = lectureUserDataRepo.getNote(lectureId, userId);
	  return result.length == 0 ? null : result[0][0];
  }
  
  public String saveNote(String lectureId, String userId, String note) {
	  Objects.requireNonNull(lectureId);
	  Objects.requireNonNull(userId);
	  
	  LectureUserData lectureUserData = lectureUserDataRepo
	      .findByLectureIdAndUserId(lectureId, userId);
	  
	  if (lectureUserData == null) {
		  lectureUserData = entityFactory.createEntity(LectureUserData.class);
		  lectureUserData.setUser(userRepo.findOne(userId));
		  lectureUserData.setLecture(lectureRepo.findOne(lectureId));
	  }
	  
	  note = Util.normalize(note);
	  if (note.length() > 4000) {
		  note = note.substring(0, 4000);
	  }
	  lectureUserData.setNote(note);
	  lectureUserDataRepo.save(lectureUserData);
	  
	  return note;
  }
  
  public void deleteNote(String lectureId, String userId) {
	  Objects.requireNonNull(lectureId);
	  Objects.requireNonNull(userId);
	  
	  LectureUserData lectureUserData = lectureUserDataRepo
	      .findByLectureIdAndUserId(lectureId, userId);
		
		if (lectureUserData != null) {
			lectureUserData.setNote(null);
			lectureUserDataRepo.save(lectureUserData);
		}
  }
}
