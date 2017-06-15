package com.xinra.growthlectures.service;

import com.google.common.collect.ImmutableSet;
import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.EmailLogin;
import com.xinra.growthlectures.entity.EmailLoginRepository;
import com.xinra.growthlectures.entity.Role;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Integrates user entities with Spring Security.
 */
public class UserDetailsServiceImpl implements UserDetailsService {
  
  private EmailLoginRepository emailLoginRepo;

  public UserDetailsServiceImpl(EmailLoginRepository emailLoginRepo) {
    this.emailLoginRepo = emailLoginRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      username = Util.normalize(username);
      Objects.requireNonNull(username);
      
      EmailLogin login = emailLoginRepo.findByEmailIgnoreCase(username);
      Objects.requireNonNull(login);
      
      return new User(login.getEmail(), login.getPassword(), getAuthorities(login.getUser()));
    } catch (Exception ex) {
      throw new UsernameNotFoundException("User not found!");
    }
  }
  
  /**
   * Converts the roles of a user entity to Spring Security authorities.
   */
  public static Set<GrantedAuthority> getAuthorities(com.xinra.growthlectures.entity.User user) {
    return user.getRoles().stream()
        .map(Role::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(ImmutableSet.toImmutableSet());
  }
}
