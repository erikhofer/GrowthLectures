package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository;

@Repository
public interface LectureUserDataRepository extends AbstractLectureUserDataRepository<LectureUserData> {}

@NoRepositoryBean
public interface AbstractLectureUserDataRepository<T extends LectureUserData>
    extends AbstractEntityRepository<T> {
  
  @Query("SELECT d.note FROM LectureUserData d WHERE d.user.id = :userId AND d.lecture.id = :lectureId")
  String[][] getNote(@Param("lectureId") String lectureId, @Param("userId") String userId);
  
  LectureUserData findByLectureIdAndUserId(String lectureId, String userId);
  
  @Query("SELECT d.lecture.id, d.rating FROM LectureUserData d WHERE d.user.id = :userId AND d.lecture.id IN :lectureIds")
  Object[][] getRatings(@Param("lectureIds") Set<String> lectureIds, @Param("userId") String userId);
  
}