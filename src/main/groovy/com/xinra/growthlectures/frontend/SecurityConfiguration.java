package com.xinra.growthlectures.frontend;

import static org.springframework.http.HttpMethod.POST;

import com.xinra.growthlectures.entity.EmailLoginRepository;
import com.xinra.growthlectures.entity.Role;
import com.xinra.growthlectures.service.AuthenticationProviderImpl;
import com.xinra.growthlectures.service.EmailLoginDto;
import com.xinra.growthlectures.service.UserDetailsServiceImpl;
import com.xinra.nucleus.service.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @Autowired
  private EmailLoginRepository emailLoginRepo;
  
  @Override
  public UserDetailsService userDetailsServiceBean() throws Exception {
    return new UserDetailsServiceImpl(emailLoginRepo);
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsServiceBean());
    auth.authenticationProvider(new AuthenticationProviderImpl(dtoFactory, emailLoginRepo));
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/assets/partials/**").denyAll()
            .antMatchers(Ui.URL_CATEGORIES + "/*/*/note").authenticated()
            .antMatchers(Ui.URL_CATEGORIES + "/*/*/rating").authenticated()
        .and()
            .formLogin()
            .loginPage(Ui.URL_LOGIN)
            .usernameParameter(EmailLoginDto.Email)
        .and()
            .logout()
            // Enable logout via GET. WARNING: This disables CSRF prevention!
            .logoutRequestMatcher(new AntPathRequestMatcher(Ui.URL_LOGOUT));
    
    authorizeForRole(http, Ui.URL_CATEGORIES, Role.MODERATOR, POST);
    authorizeForRole(http, Ui.URL_LECTURERS, Role.MODERATOR, POST);
    authorizeForRole(http, Ui.URL_CATEGORIES + "/*", Role.MODERATOR, POST);
  }
  
  private void authorizeForRole(HttpSecurity http, String antPattern, Role role,
      HttpMethod... methods) throws Exception {
    
    for (HttpMethod method : methods) {
      http.authorizeRequests().antMatchers(method, antPattern).hasAuthority(role.toString());
    }
  }
  
}
