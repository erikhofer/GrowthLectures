package com.xinra.growthlectures.service;

import com.google.common.collect.ImmutableList;
import com.xinra.nucleus.service.ServiceException;

/**
 * Thrown by services if no entity with the given slug exists.
 * If multiple slugs are returned by {@link #getSlugs()}, at least one of them does not exist.
 */
public class SlugNotFoundException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  private final ImmutableList<String> slugs;
  
  public SlugNotFoundException(String slug) {
    super("Slug does not exist: " + slug);
    this.slugs = ImmutableList.of(slug);
  }
  
  /**
   * Call this if (at least) one of the given slugs does not exist.
   */
  public SlugNotFoundException(String slug, String... slugs) {
    super("At least one of these slugs does not exist: " + slug + ", " 
      + String.join(", ", slugs));    
    this.slugs = ImmutableList.copyOf(slugs);
  }

  public ImmutableList<String> getSlugs() {
    return slugs;
  }
}
