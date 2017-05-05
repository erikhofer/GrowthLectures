package com.xinra.growthlectures.entity;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractLectureRepository<T extends Lecture> 
    extends AbstractNamedEntityRepository<T> {

}
