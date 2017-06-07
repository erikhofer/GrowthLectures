package com.xinra.growthlectures.service

import com.xinra.growthlectures.entity.LecturerRepository
import com.xinra.nucleus.service.DtoFactory
import groovy.transform.CompileStatic
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
	

}
