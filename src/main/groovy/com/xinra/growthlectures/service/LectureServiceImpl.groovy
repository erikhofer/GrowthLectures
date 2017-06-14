package com.xinra.growthlectures.service

import com.github.slugify.Slugify
import com.xinra.growthlectures.Util
import com.xinra.growthlectures.entity.Lecture
import com.xinra.growthlectures.entity.LectureRepository
import com.xinra.growthlectures.entity.YoutubeMedia
import com.xinra.growthlectures.entity.YoutubeMediaRepository
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
  private LectureService lectureService;
  
  @Autowired
  private CategoryService categoryService;
  
  @Autowired
  private LecturerService lecturerService;
  
  @Autowired
  private YoutubeMediaRepository youtubeMediaRepo;
  
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
  
  public NamedDto getLecture(String slug) throws SlugNotFoundException {
    
    String name = lectureRepo.getNameBySlug(slug);
    if(name == null) {
      throw new SlugNotFoundException();
    }
     
    NamedDto dto = dtoFactory.createDto(NamedDto.class);
    dto.setSlug(slug);
    dto.setName(name);
    
    return dto;
  }
  
  public LectureSummaryDto createLecture(NewLectureDto newLectureDto) {
    
    String title = Util.normalize(newLectureDto.getName());
    String videoId = Util.normalize(newLectureDto.getVideoId());
    String slug = Util.normalize(newLectureDto.getSlug());
    String description = Util.normalize(newLectureDto.getDescription());
    String url = Util.normalize(newLectureDto.getLink());
    String category = Util.normalize(newLectureDto.getNewcategory());
    String lecturer = Util.normalize(newLectureDto.getNewlecturer());
    String start = Util.normalize(newLectureDto.getStart());
    String end = Util.normalize(newLectureDto.getEnd());
    String platform = Util.normalize(newLectureDto.getPlatform());
    
    Lecture newLecture = new Lecture();
    newLecture.setName(title);
    newLecture.setDescription(description);
    newLecture.setAdded(LocalDate.now());
    newLecture.setCategory(categoryService.findCategory(category));
    newLecture.setLecturer(lecturerService.findLecturer(lecturer));
    newLecture.setRatingAmount(0);
    newLecture.setRatingAverage(0d);
    newLecture.setSlug(slug);
    lectureRepo.save(newLecture);
   
    if(platform.equals("youtube")) {
      YoutubeMedia newMedia = new YoutubeMedia();
      newMedia.setStart(Util.parseTime(start));
      newMedia.setDuration(Util.parseTime(end) - Util.parseTime(start));
      newMedia.setYoutubeId(videoId);
      newMedia.setUrl(url);
      youtubeMediaRepo.save(newMedia);
    }
    
    LectureSummaryDto newLectureSummaryDto = dtoFactory.createDto(LectureSummaryDto.class);
    newLectureSummaryDto.setSlug(newLecture.getSlug());
      NamedDto categoryDto = dtoFactory.createDto(NamedDto.class);
      categoryDto.setName(newLecture.getCategory().getName());
      categoryDto.setSlug(newLecture.getCategory().getSlug());
    newLectureSummaryDto.setCategory(categoryDto);
    
    return newLectureSummaryDto;
  }
  
  public boolean doesSlugExists(String slug) {
    Lecture l = lectureRepo.findBySlug(slug);
    if(l == null) return false;
    return true;
  }
  
}
