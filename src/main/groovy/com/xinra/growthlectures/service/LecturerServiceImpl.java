package com.xinra.growthlectures.service;

import com.google.common.collect.Streams;
import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.Lecturer;
import com.xinra.growthlectures.entity.LecturerRepository;
import com.xinra.nucleus.service.DtoFactory;
import groovy.transform.CompileStatic;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@CompileStatic
@Service
class LecturerServiceImpl extends GrowthlecturesServiceImpl implements LecturerService {

  @Autowired
  private DtoFactory dtoFactory;

  @Autowired
  private LecturerRepository lecturerRepo;

  public ContainerDto getRandomLecturer() {

    Long lecturersCount = lecturerRepo.count();
    int randomIndex = (int) (Math.random() * lecturersCount);
    Page<Lecturer> lecturerList = lecturerRepo.findAll(new PageRequest(randomIndex, 1));
    Lecturer randomLecturer = null;
    for (Lecturer l : lecturerList) {
      randomLecturer = l;
    }
    return convertLecturer(randomLecturer);
  }
  
  public List<ContainerDto> getPopularLecturers(int limit) {
    List<Lecturer> popularLecturers = lecturerRepo.findByOrderByName(new PageRequest(0, limit));
    return popularLecturers.stream().map(this::convertLecturer).collect(Collectors.toList());
  }
  
  public ContainerDto getLecturerBySlug(String slug) throws SlugNotFoundException {
  
    Lecturer lecture = lecturerRepo.findOneBySlug(slug);
    if (lecture == null) {
      throw new SlugNotFoundException(slug);
    }
    return convertLecturer(lecture);
  }

  public List<ContainerDto> getAllLecturers() {
    return Streams.stream(lecturerRepo.findAll())
            .map(this::convertLecturer)
            .collect(Collectors.toList());
  }
  
  public boolean doesExists(String slug) {
    Lecturer lecturer = lecturerRepo.findBySlug(slug);
    if (lecturer == null) {
      return false;
    }
    return true;
  }

  public NamedDto createLecturer(NamedDto newLecurerDto) {
    
    Lecturer newLecturer = new Lecturer();

    String name = Util.normalize(newLecurerDto.getName());
    String slug = Util.normalize(newLecurerDto.getSlug());
    newLecturer.setName(name);
    newLecturer.setSlug(slug);
    lecturerRepo.save(newLecturer);
    
    return newLecurerDto;
  }
  
  private ContainerDto convertLecturer(Lecturer l) {
    
    if (l == null) {
      return null;
    }
    
    ContainerDto returnDto = dtoFactory.createDto(ContainerDto.class);
    returnDto.setName(l.getName());
    returnDto.setSlug(l.getSlug());
    returnDto.setAmount(l.getLectures().size());
    
    return returnDto;
    
  }
}
