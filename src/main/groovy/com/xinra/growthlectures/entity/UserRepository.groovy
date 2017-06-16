package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository

public interface UserRepository extends AbstractUserRepository<User> {}

public interface AbstractUserRepository<T extends User> extends AbstractEntityRepository<T> {

}
