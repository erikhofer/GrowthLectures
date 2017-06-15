package com.xinra.growthlectures;

import com.xinra.growthlectures.entity.Category;
import com.xinra.growthlectures.entity.CategoryRepository;
import com.xinra.growthlectures.entity.Lecture;
import com.xinra.growthlectures.entity.LectureRepository;
import com.xinra.growthlectures.entity.Lecturer;
import com.xinra.growthlectures.entity.LecturerRepository;
import com.xinra.growthlectures.entity.MediaRepository;
import com.xinra.growthlectures.entity.YoutubeMedia;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.UserService;
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
  private UserService userService;
  
  @Autowired
  private LectureService lectureService;
  
  @Autowired
  private LecturerRepository lecturerRepo;
  
  @Autowired
  private CategoryRepository categoryRepo;
  
  @Autowired
  private LectureRepository lectureRepo;
  
  @Autowired
  private MediaRepository mediaRepo;

  @Override
  public void run(String... args) throws Exception {
    
    userService.register("herbert@yahoo.de", "123");
    
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
        
    YoutubeMedia media = new YoutubeMedia();
    media.setUrl("https://www.youtube.com/embed/S0Vzykygwvk?rel=0&amp;showinfo=0");
    media.setYoutubeId("S0Vzykygwvk");
    media.setStart(0);
    media.setDuration(1519);
    mediaRepo.save(media);
    
    Lecture lecture = new Lecture();
    lecture.setAdded(LocalDate.now());
    lecture.setDescription("Dies ist ein Test-Video");
    lecture.setCategory(c);
    lecture.setName("Löwenzahn - Folge 3");
    lecture.setLecturer(l);
    lecture.setRatingAmount(3);
    lecture.setRatingAverage(1.5);
    lecture.setSlug("lz3");
    lecture.setMedia(media);
    lectureRepo.save(lecture);
    
  }

}
