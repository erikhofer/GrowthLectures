package com.xinra.growthlectures;

import com.xinra.growthlectures.entity.Category;
import com.xinra.growthlectures.entity.CategoryRepository;
import com.xinra.growthlectures.entity.Lecture;
import com.xinra.growthlectures.entity.LectureRepository;
import com.xinra.growthlectures.entity.Lecturer;
import com.xinra.growthlectures.entity.LecturerRepository;
import com.xinra.growthlectures.entity.MediaRepository;
import com.xinra.growthlectures.entity.YoutubeMedia;
import com.xinra.growthlectures.frontend.Formatter;
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
    media.setUrl("https://www.youtube.com/watch?v=S0Vzykygwvk");
    media.setYoutubeId("S0Vzykygwvk");
    media.setStart(0);
    media.setDuration(1519);
    media.setThumbnail("https://i.ytimg.com/vi/S0Vzykygwvk/maxresdefault.jpg");
    mediaRepo.save(media);
    
    YoutubeMedia media2 = new YoutubeMedia();
    media2.setUrl("https://www.youtube.com/watch?v=4gSOMba1UdM");
    media2.setYoutubeId("4gSOMba1UdM");
    media2.setStart(140);
    media2.setDuration(25);
    media2.setThumbnail("https://i.ytimg.com/vi/4gSOMba1UdM/mqdefault.jpg");
    mediaRepo.save(media2);
    
    Lecture lecture = new Lecture();
    lecture.setAdded(LocalDate.now());
    lecture.setDescription("Dies ist ein Test-Video");
    lecture.setCategory(c);
    lecture.setName("Löwenzahn - Folge 3");
    lecture.setLecturer(l);
    lecture.setRatingAmount(4);
    lecture.setRatingAverage(1.6d);
    lecture.setSlug("lz3");
    lecture.setMedia(media);
    lectureRepo.save(lecture);
    
    Lecture lecture2 = new Lecture();
    lecture2.setAdded(LocalDate.now());
    lecture2.setDescription("How it is (wap bap) AVAILABLE EVERYWHERE NOW: http://wmg.click/BibiH_HowItIsAY * STORES Germany: - Amazon - Media Markt - Saturn - Müller - Expert - Medimax - Weltbild - jpc.de Austria: - Media Markt - Müller DOWNLOAD: - ITunes - Amazon - GooglePlay STREAM: - Spotify - AppleMusic - Deezer - Napster - Google Music by: Sam Sommer Lyrics by: Sam Sommer | Dave Knight Produced by: Chinzilla Films Special thanks to Warner Music Germany -- | LYRICS | -- Verse 1 It’s late at night I go to bed But I can’t get no rest My boyfriend quit I’m almost dead I’ll have to do my best! I’m up and down I feel so fat I ain’t got no more fizz Don’t even get to keep the cat But that’s just how it is! Chorus 1 I Sing… Wap bap wah da de da dah Dap bap bah da de da dah Dap bap bah da de da dah dah! Wap bap wah da de da dah Dap bap wah da de da dah Dap bap Bah da de da dah dah! Verse 2 I get up late I stub my toe I’ve got a few missed calls I lost my job I didn’t know And that’s not good at all! I get my bag I go outside Just minding my own biz I go to pay but my card has been denied But that’s just how it is! Chorus 2 I Sing… Wap bap wah da de da dah Dap bap bah da de da dah Dap bap bah da de da dah dah! And everybody sing… Wap bap wah da de da dah Dap bap wah da de da dah Dap bap Bah da de da dah dah Verse 3 Another week the same old shit My troubles getting worse And then I get a bigger hit Cos someone stole my purse! I met a guy he knows my name He also told me his And if we want to have some fun Well that’s just how it is Chorus 3 * Product Placement - Special thanks to Warner Music Germany");
    lecture2.setCategory(c);
    lecture2.setName("Bibi H - How it is ( wap bap ... ) [Official Video]");
    lecture2.setLecturer(l);
    lecture2.setRatingAmount(3);
    lecture2.setRatingAverage(1.5);
    lecture2.setSlug("bibi-h-how-it-is-wap-bap-official-video");
    lecture2.setMedia(media2);
    lectureRepo.save(lecture2);    
  }

}
