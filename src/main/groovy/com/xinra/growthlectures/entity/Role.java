package com.xinra.growthlectures.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * User roles that grant certain permissions.
 */
public enum Role {
  
  USER,
  MODERATOR(USER),
  ADMIN(USER, MODERATOR);
  
  private final Role[] include;
  private ImmutableSet<Role> transitiveRoles;
  
  private Role(Role... include) {
    this.include = include;
  }
  
  /**
   * Returns an enum set of this role as well of all roles that are inherited.
   * For example, a {@link #MODERATOR} is also a {@link #USER}.
   */
  public ImmutableSet<Role> getTransitiveRoles() {
    // EnumSet can not be used in the constructor. It is thus created
    // the first time the getter is called.
    if (transitiveRoles == null) {
      transitiveRoles = Sets.immutableEnumSet(this, include);
    }
    return transitiveRoles;
  }
  
  /**
   * Prefixes constant names with "ROLE_".
   */
  @Override
  public String toString() {
    return "ROLE_" + name();
  }
  
}
