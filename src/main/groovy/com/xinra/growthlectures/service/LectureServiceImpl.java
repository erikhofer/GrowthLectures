package com.xinra.growthlectures.service;

import com.google.common.collect.Streams;
import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.CategoryRepository;
import com.xinra.growthlectures.entity.Lecture;
import com.xinra.growthlectures.entity.LectureRepository;
import com.xinra.growthlectures.entity.LectureUserData;
import com.xinra.growthlectures.entity.LectureUserDataRepository;
import com.xinra.growthlectures.entity.LecturerRepository;
import com.xinra.growthlectures.entity.Media;
import com.xinra.growthlectures.entity.MediaRepository;
import com.xinra.growthlectures.entity.UserRepository;
import com.xinra.growthlectures.entity.YoutubeMedia;
import com.xinra.nucleus.entity.EntityFactory;
import com.xinra.nucleus.service.DtoFactory;
import groovy.transform.CompileStatic;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@CompileStatic
@Service
public class LectureServiceImpl extends GrowthlecturesServiceImpl implements LectureService {

  @Autowired
  private DtoFactory dtoFactory;

  @Autowired
  private LectureRepository lectureRepo;
  
  @Autowired
  private LecturerRepository lecturerRepo;
  
  @Autowired
  private CategoryRepository categoryRepo;
  
  @Autowired
  private CategoryService categoryService;
  
  @Autowired
  private LecturerService lecturerService;
  
  @Autowired
  private MediaRepository mediaRepo;

  @Autowired
  private LectureUserDataRepository lectureUserDataRepo;
  
  @Autowired
  private UserRepository userRepo;
  
  @Autowired
  private EntityFactory entityFactory;
  
  public List<LectureSummaryDto> getAllLectures() {
    
    return Streams.stream(lectureRepo.findAll())
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
       
  }
   
  private void convertToSummaryDto(Lecture lecture, LectureSummaryDto dto) {

    dto.setName(lecture.getName());
    dto.setSlug(lecture.getSlug());
    dto.setDescription(lecture.getDescription());
    dto.setRating(lecture.getRatingAverage());
    dto.setAdded(lecture.getAdded());
    
    NamedDto cat = dtoFactory.createDto(NamedDto.class);
    cat.setName(lecture.getCategory().getName());
    cat.setSlug(lecture.getCategory().getSlug());
    dto.setCategory(cat);
    
    NamedDto lecturer = dtoFactory.createDto(NamedDto.class);
    lecturer.setName(lecture.getLecturer().getName());
    lecturer.setSlug(lecture.getLecturer().getSlug());
    dto.setLecturer(lecturer);
    
    MediaDto mediaDto = null;
    if (lecture.getMedia() instanceof YoutubeMedia) {
      YoutubeMediaDto youtubeMediaDto = dtoFactory.createDto(YoutubeMediaDto.class);
      youtubeMediaDto.setType(MediaType.YOUTUBE);
      YoutubeMedia youtubeMedia = (YoutubeMedia)lecture.getMedia();
      youtubeMediaDto.setYoutubeId(youtubeMedia.getYoutubeId());
      mediaDto = youtubeMediaDto;
    }
    mediaDto.setThumbnail(lecture.getMedia().getThumbnail());
    mediaDto.setStart(lecture.getMedia().getStart());
    mediaDto.setEnd(lecture.getMedia().getStart() + lecture.getMedia().getDuration());
    mediaDto.setUrl(lecture.getMedia().getUrl());
    dto.setMedia(mediaDto);
  }
  
  private LectureSummaryDto convertToSummaryDto(Lecture lecture) {
    LectureSummaryDto dto = dtoFactory.createDto(LectureSummaryDto.class);
    convertToSummaryDto(lecture, dto);
    return dto;
  }

  public List<LectureSummaryDto> getRecentLectures() {
    return getAllLectures();
  }
  
  public List<LectureSummaryDto> getPopularLectures() {
    return getAllLectures();
  }

  public NamedDto getLecture(String slug) throws SlugNotFoundException {
    
    String name = lectureRepo.getNameBySlug(slug);
    if (name == null) {
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
    String thumbnail = Util.normalize(newLectureDto.getThumbnail());
    
    Media newMedia = null;
    if (platform.equals(MediaType.YOUTUBE.name())) {
      YoutubeMedia youtubeMedia = entityFactory.createEntity(YoutubeMedia.class);
      youtubeMedia.setStart(Util.parseTime(start));
      youtubeMedia.setDuration(Util.parseTime(end) - Util.parseTime(start));
      youtubeMedia.setYoutubeId(videoId);
      youtubeMedia.setUrl(url);
      youtubeMedia.setThumbnail(thumbnail);
      newMedia = youtubeMedia;
    }
    
    mediaRepo.save(newMedia);
    
    Lecture newLecture = new Lecture();
    newLecture.setName(title);
    newLecture.setDescription(description);
    newLecture.setAdded(LocalDate.now());
    newLecture.setCategory(categoryRepo.findBySlug(category));
    newLecture.setLecturer(lecturerRepo.findBySlug(lecturer));
    newLecture.setRatingAmount(0);
    newLecture.setRatingAverage(0d);
    newLecture.setSlug(slug);
    newLecture.setMedia(newMedia);
    lectureRepo.save(newLecture);
   
    
    
    LectureSummaryDto newLectureSummaryDto = dtoFactory.createDto(LectureSummaryDto.class);
    newLectureSummaryDto.setSlug(newLecture.getSlug());
      NamedDto categoryDto = dtoFactory.createDto(NamedDto.class);
      categoryDto.setName(newLecture.getCategory().getName());
      categoryDto.setSlug(newLecture.getCategory().getSlug());
    newLectureSummaryDto.setCategory(categoryDto);
    
    return newLectureSummaryDto;
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
  
  public boolean doesSlugExists(String slug) {
    Lecture l = lectureRepo.findBySlug(slug);
    if (l == null) return false;
    return true;
  }
  
}
