package com.xinra.growthlectures;

import com.xinra.growthlectures.entity.Category;
import com.xinra.growthlectures.entity.CategoryRepository;
import com.xinra.growthlectures.entity.Lecture;
import com.xinra.growthlectures.entity.LectureRepository;
import com.xinra.growthlectures.entity.Lecturer;
import com.xinra.growthlectures.entity.LecturerRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.xinra.growthlectures", "asset.pipeline.springboot", "com.xinra.nucleus"})
public class GrowthlecturesApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(GrowthlecturesApplication.class, args);
  }
  
  @Autowired
  private LecturerRepository lecturerRepo;
  
  @Autowired
  private CategoryRepository categoryRepo;
  
  @Autowired
  private LectureRepository lectureRepo;

  @Override
  public void run(String... args) throws Exception {
    
    Lecturer l = new Lecturer();
    l.setSlug("peter-lustig");
    l.setName("Peter Lustig");
    lecturerRepo.save(l);
    
    Lecturer l2 = new Lecturer();
    l2.setName("Lorenz Kock");
    l2.setSlug("lorenz-kock");
    lecturerRepo.save(l2);
    
    Category c = new Category();
    c.setName("Fernsehsendung");
    c.setSlug("fernsehsendung");
    categoryRepo.save(c);
    
    Category c2 = new Category();
    c2.setName("Java-Entwicklung");
    c2.setSlug("java-entwicklung");
    categoryRepo.save(c2);
    
    Lecture lecture = new Lecture();
    lecture.setAdded(LocalDate.now());
    lecture.setDescription("Dies ist ein Test-Video");
    lecture.setCategory(c);
    lecture.setName("Löwenzahn - Folge 3");
    lecture.setLecturer(l);
    lecture.setRatingAmount(3);
    lecture.setRatingAverage(1.5);
    lecture.setSlug("loewenzahn_-_folge_3");
    lectureRepo.save(lecture);
    
    
  }

}
