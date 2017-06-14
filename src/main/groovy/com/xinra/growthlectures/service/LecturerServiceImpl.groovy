package com.xinra.growthlectures.service

import com.xinra.growthlectures.entity.Lecturer
import com.xinra.growthlectures.entity.LecturerRepository
import com.xinra.nucleus.service.DtoFactory
import groovy.transform.CompileStatic
import java.util.Collection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@CompileStatic
@Service
class LecturerServiceImpl extends GrowthlecturesServiceImpl implements LecturerService {
	
	@Autowired
	private DtoFactory dtoFactory;
	
	@Autowired
	private LecturerRepository lecturerRepo;
	
	public NamedDto getLecturer(String slug) throws SlugNotFoundException {
		
		String name = lecturerRepo.getNameBySlug(slug);
		if(name == null) {
			throw new SlugNotFoundException();
		}
		 
		NamedDto dto = dtoFactory.createDto(NamedDto.class);
		dto.setSlug(slug);
	  dto.setName(name);
	  
	  return dto;
	}
	
  public Collection<Lecturer> getAllLecturers() {
    return (Collection<Lecturer>)lecturerRepo.findAll();
  }
  
  public Lecturer findLecturer(String slug) {
    return lecturerRepo.findBySlug(slug);
  }
  
  public boolean doesExists(String slug) {
    Lecturer lecturer = lecturerRepo.findBySlug(slug);
    if(lecturer == null) return false;
    return true;
  }

  public Collection<NamedDto> getAllLecturersAsNamedDto() {
    Collection<NamedDto> dtos = new ArrayList<NamedDto>();
    for (lecturer in this.getAllLecturers()) {
      NamedDto newLecturer = dtoFactory.createDto(NamedDto.class);
      newLecturer.setName(lecturer.getName());
      newLecturer.setSlug(lecturer.getSlug());
      dtos.add(newLecturer);
    }
    return dtos;
  }
  
}
