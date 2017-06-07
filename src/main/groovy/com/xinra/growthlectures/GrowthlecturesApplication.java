package com.xinra.growthlectures;

import com.xinra.growthlectures.entity.Lecturer;
import com.xinra.growthlectures.entity.LecturerRepository;
import com.xinra.growthlectures.service.LecturerService;
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

  @Override
  public void run(String... args) throws Exception {
    
    Lecturer l = new Lecturer();
    l.setSlug("peter-lustig");
    l.setName("Peter Lustig");
    lecturerRepo.save(l);
    
  }

}
