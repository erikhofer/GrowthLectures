package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@Repository
public interface EmailLoginRepository extends AbstractEmailLoginRepository<EmailLogin> {}

@NoRepositoryBean
public interface AbstractEmailLoginRepository<T extends EmailLogin>
    extends AbstractEntityRepository<T> {

  EmailLogin findByEmailIgnoreCase(String email);

}
