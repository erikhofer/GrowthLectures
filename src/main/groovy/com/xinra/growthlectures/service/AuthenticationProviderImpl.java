package com.xinra.growthlectures.service;

import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.EmailLogin;
import com.xinra.growthlectures.entity.EmailLoginRepository;
import com.xinra.nucleus.service.DtoFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Custom authentication provider. Currently supports e-mail login only.
 */
public class AuthenticationProviderImpl implements AuthenticationProvider {
  
  private static final String ERROR_MESSAGE = "Username or password is invalid!";
  
  private DtoFactory dtoFactory;
  private EmailLoginRepository emailLoginRepo;

  public AuthenticationProviderImpl(DtoFactory dtoFactory, EmailLoginRepository emailLoginRepo) {
    this.dtoFactory = dtoFactory;
    this.emailLoginRepo = emailLoginRepo;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
    String email = Util.normalize(authentication.getName());
    String password = authentication.getCredentials().toString();
    
    if (email == null || password == null) {
      throw new BadCredentialsException(ERROR_MESSAGE);
    }
    
    EmailLogin login = emailLoginRepo.findByEmailIgnoreCase(email);
    
    if (login == null || !BCrypt.checkpw(password, login.getPassword())) {
      throw new BadCredentialsException(ERROR_MESSAGE);
    }
    
    UserDto user = dtoFactory.createDto(UserDto.class);
    user.setPk(login.getUser().getPk());
    user.setName(login.getEmail());
    user.setRole(login.getUser().getRole());
    
    return new UsernamePasswordAuthenticationToken(user, password,
        UserDetailsServiceImpl.getAuthorities(login.getUser()));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

}
