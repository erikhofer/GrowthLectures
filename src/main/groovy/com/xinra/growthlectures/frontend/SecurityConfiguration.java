package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.entity.EmailLoginRepository;
import com.xinra.growthlectures.service.AuthenticationProviderImpl;
import com.xinra.growthlectures.service.EmailLoginDto;
import com.xinra.growthlectures.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private EmailLoginRepository emailLoginRepo;
  
  @Override
  public UserDetailsService userDetailsServiceBean() throws Exception {
    return new UserDetailsServiceImpl(emailLoginRepo);
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsServiceBean());
    auth.authenticationProvider(new AuthenticationProviderImpl(emailLoginRepo));
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests().antMatchers("/").permitAll()
        .and()
            .formLogin()
            .loginPage(Ui.URL_LOGIN)
            .usernameParameter(EmailLoginDto.Email)
        .and()
            .logout()
            .logoutSuccessUrl(Ui.URL_INDEX);
  }
  
}
