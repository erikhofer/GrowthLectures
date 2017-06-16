package com.xinra.growthlectures.service

import com.google.common.collect.ImmutableSet
import com.xinra.growthlectures.Util
import com.xinra.growthlectures.entity.EmailLogin
import com.xinra.growthlectures.entity.EmailLoginRepository
import com.xinra.growthlectures.entity.Login
import com.xinra.growthlectures.entity.Role
import com.xinra.growthlectures.entity.User
import com.xinra.growthlectures.entity.UserRepository
import com.xinra.nucleus.entity.EntityFactory
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
@CompileStatic
class UserServiceImpl extends GrowthlecturesServiceImpl implements UserService {
  
  @Autowired
  private EmailLoginRepository emailLoginRepo;
  
  @Autowired
  private UserRepository userRepo;
  
  @Autowired
  private EntityFactory entityFactory;
  
  public boolean doesEmailExist(String email) {
    return emailLoginRepo.findByEmailIgnoreCase(Util.normalize(email)) != null;
  }
  
  public void register(String email, String password) {
    email = Util.normalize(email);
    Objects.requireNonNull(email);
    Objects.requireNonNull(password);
    
    Login login = entityFactory.createEntity(EmailLogin.class);
    login.setEmail(email);
    login.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
	    
    User user = entityFactory.createEntity(User.class);
    user.setLogins(ImmutableSet.of((Login) login));
	  user.setRole(Role.USER);
    login.setUser(user);
	  
	  userRepo.save(user);
    emailLoginRepo.save(login);
  }
  
}