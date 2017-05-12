package com.xinra.growthlectures.entity;

/**
 * User roles that grant certain permissions.
 */
public enum Role {
  
  USER,
  MODERATOR,
  ADMIN;
  
  @Override
  public String toString() {
    return "ROLE_" + super.toString();
  }
  
}
