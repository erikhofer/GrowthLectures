package com.xinra.growthlectures.service;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.Maps;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
  private MediaRepository mediaRepo;

  @Autowired
  private LectureUserDataRepository lectureUserDataRepo;
  
  @Autowired
  private UserRepository userRepo;
  
  @Autowired
  private EntityFactory entityFactory;
  
  public List<LectureSummaryDto> getLecturesByCategory(String categorySlug) {
    return Streams.stream(lectureRepo.findByCategorySlug(categorySlug))
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }

  public List<LectureSummaryDto> getLecturesByLecturer(String lecturerSlug) {
    return Streams.stream(lectureRepo.findByLecturerSlug(lecturerSlug))
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }

  public List<LectureSummaryDto> getAllLectures() {
    return Streams.stream(lectureRepo.findAll())
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }
  
  public List<LectureSummaryDto> getRecentLectures(Integer count) {
    return lectureRepo.findByOrderByAddedDesc(new PageRequest(0, count)).stream()
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }
  
  public List<LectureSummaryDto> getPopularLectures(Integer count) {
    return lectureRepo.findByOrderByRatingAverageDesc(new PageRequest(0, count)).stream()
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }
  
  public List<LectureSummaryDto> getRecentLecturesByCategory(String categorySlug) {
    return Streams.stream(lectureRepo.findByCategorySlugOrderByAddedDesc(categorySlug))
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());

  }
  
  public List<LectureSummaryDto> getRecentLecturesByLecturer(String lecturerSlug) {
    return Streams.stream(lectureRepo.findByLecturerSlugOrderByAddedDesc(lecturerSlug))
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }
  
  public List<LectureSummaryDto> getRecentLecturesByCategory(String categorySlug, int count) {
    return lectureRepo.findByCategorySlugOrderByAddedDesc(categorySlug, new PageRequest(0, count))
        .stream()
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }
  
  public List<LectureSummaryDto> getRecentLecturesByLecturer(String lecturerSlug, int count) {
    return lectureRepo.findByLecturerSlugOrderByAddedDesc(lecturerSlug, new PageRequest(0, count)).stream()
        .map(this::convertToSummaryDto)
        .collect(Collectors.toList());
  }

  public NamedDto getLecture(String slug) throws SlugNotFoundException {
    Lecture l = lectureRepo.findBySlug(slug);
    if (l == null) {
      throw new SlugNotFoundException(slug);
    }
    return convertLectureToNamedDto(l);
  }
  
  public LectureSummaryDto createLecture(EditLectureDto dto) throws SlugNotFoundException {
    
    Util.checkNotNull(dto, 
        EditLectureDto.Name,
        EditLectureDto.VideoId,
        EditLectureDto.Slug,
        EditLectureDto.Link,
        EditLectureDto.Newcategory,
        EditLectureDto.Newlecturer,
        EditLectureDto.Start,
        EditLectureDto.End,
        EditLectureDto.Platform,
        EditLectureDto.Thumbnail);
    
    if (doesSlugExists(dto.getSlug())) {
      throw new IllegalArgumentException("Slug already exists!");
    }
    
    Media newMedia = null;
    if (dto.getPlatform().equals(MediaType.YOUTUBE.name())) {
      YoutubeMedia youtubeMedia = entityFactory.createEntity(YoutubeMedia.class);
      youtubeMedia.setYoutubeId(dto.getVideoId());
      newMedia = youtubeMedia;
    }
    
    if (newMedia == null) {
      throw new IllegalArgumentException("Invalid video platform!");
    }
    
    newMedia.setStart(Util.parseDuration(dto.getStart()));
    newMedia.setDuration(Util.parseDuration(dto.getEnd()) - newMedia.getStart());
    newMedia.setUrl(dto.getLink());
    newMedia.setThumbnail(dto.getThumbnail());
    
    mediaRepo.save(newMedia);
    
    Lecture newLecture = new Lecture();
    newLecture.setName(dto.getName());
    newLecture.setDescription(dto.getDescription());
    newLecture.setAdded(LocalDate.now());
    newLecture.setRatingAmount(0);
    newLecture.setRatingAverage(0d);
    newLecture.setSlug(dto.getSlug());
    newLecture.setMedia(newMedia);
    newLecture.setCategory(categoryRepo.findBySlug(dto.getNewcategory()));
    newLecture.setLecturer(lecturerRepo.findBySlug(dto.getNewlecturer()));
    if (newLecture.getCategory() == null) {
      throw new SlugNotFoundException(dto.getNewcategory());
    } else if (newLecture.getLecturer() == null) {
      throw new SlugNotFoundException(dto.getNewlecturer());
    }
    
    lectureRepo.save(newLecture);
   
    return convertToSummaryDto(newLecture);
  }

  public LectureDto getBySlug(String lectureSlug, String categorySlug, Optional<String> userId)
      throws SlugNotFoundException {
    Objects.requireNonNull(lectureSlug);
    Objects.requireNonNull(categorySlug);
    
    Lecture lecture = lectureRepo.findBySlugAndCategorySlug(lectureSlug, categorySlug);
    
    if (lecture == null) {
      throw new SlugNotFoundException(categorySlug, lectureSlug);
    }
    
    LectureDto lectureDto = dtoFactory.createDto(LectureDto.class);
    convertToSummaryDto(lecture, lectureDto);
    
    userId.ifPresent(u -> {
      LectureUserData userData = lectureUserDataRepo
          .findByLectureIdAndUserId(lecture.getPk().getId(), u);
      
      if (userData != null) {
        lectureDto.setNote(userData.getNote());
        lectureDto.setUserRating(userData.getRating());
      }
    });
    
    return lectureDto;
  }
  
  public String getLectureId(String lectureSlug, String categorySlug) 
      throws SlugNotFoundException {
    Objects.requireNonNull(lectureSlug);
    Objects.requireNonNull(categorySlug);
    
    String[][] result = lectureRepo.getIdAndCatgorySlug(lectureSlug);
    
    if (result.length == 0) {
      throw new SlugNotFoundException(lectureSlug);
    } else if (!result[0][1].equals(categorySlug)) {
      throw new SlugNotFoundException(categorySlug);
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
    
    LectureUserData lectureUserData = getUserData(lectureId, userId);
    
    note = Util.normalize(note);
    if (note.length() > 4000) {
      note = note.substring(0, 4000);
    }
    lectureUserData.setNote(note);
    lectureUserDataRepo.save(lectureUserData);
    
    return note;
  }
    
  public void deleteNote(String lectureId, String userId) {
    
    getUserDataIfPresent(lectureId, userId).ifPresent(userData -> {
      userData.setNote(null);
      lectureUserDataRepo.save(userData);
    });
  }
  
  public boolean doesSlugExists(String slug) {
    Lecture l = lectureRepo.findBySlug(slug); 
    if (l == null) {
      return false;
    }
    return true;
  }   
  
  private NamedDto convertLectureToNamedDto(Lecture l) {
    NamedDto returnDto = dtoFactory.createDto(NamedDto.class);
    returnDto.setName(l.getName());
    returnDto.setSlug(l.getSlug());
    return returnDto;
  }
  
  void convertToSummaryDto(Lecture lecture, LectureSummaryDto dto) {

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
  
  public LectureSummaryDto convertToSummaryDto(Lecture lecture) {
    LectureSummaryDto dto = dtoFactory.createDto(LectureSummaryDto.class);
    convertToSummaryDto(lecture, dto);
    return dto;
  }
  
  public void supplyRatings(Iterable<LectureSummaryDto> lectures, String userId) {
    Map<String, LectureSummaryDto> map = Maps.uniqueIndex(lectures, LectureSummaryDto::getId);
    for (Object[] result : lectureUserDataRepo.getRatings(map.keySet(), userId)) {
      map.get(result[0]).setUserRating((Integer) result[1]);
    }
  }

  public void saveRating(String lectureSlug, String categorySlug, String userId, int rating)
      throws SlugNotFoundException {
    
    checkArgument(rating >= 1 && rating <= 5 , "Rating must be between 1 and 5.");
    
    LectureUserData userData = getUserData(getLectureId(lectureSlug, categorySlug), userId);
    userData.setRating(rating);
    lectureUserDataRepo.save(userData);
  }
  
  public void deleteRating(String lectureSlug, String categorySlug, String userId)
      throws SlugNotFoundException {
    
    getUserDataIfPresent(getLectureId(lectureSlug, categorySlug), userId).ifPresent(userData -> {
      userData.setRating(null);
      lectureUserDataRepo.save(userData);
    });
  }
  
  /**
   * Retrieves a {@link LectureUserData} or creates one if it does't exist.
   */
  private LectureUserData getUserData(String lectureId, String userId) {
    
    return getUserDataIfPresent(lectureId, userId).orElseGet(() -> {
      LectureUserData lectureUserData = entityFactory.createEntity(LectureUserData.class);
      lectureUserData.setUser(userRepo.findOne(userId));
      lectureUserData.setLecture(lectureRepo.findOne(lectureId));
      return lectureUserData;
    });
  }
  
  private Optional<LectureUserData> getUserDataIfPresent(String lectureId, String userId) {
    Objects.requireNonNull(lectureId);
    Objects.requireNonNull(userId);
    
    return Optional.ofNullable(lectureUserDataRepo.findByLectureIdAndUserId(lectureId, userId));
  }
  
}
