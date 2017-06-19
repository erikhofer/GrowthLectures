package com.xinra.growthlectures;

import com.xinra.nucleus.service.Dto;
import java.beans.PropertyDescriptor;
import java.util.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class Util {
  
  /**
   * Normalizes user input. Leading and trailing whitespace is trimmed.
   * Is string is empty (or only whitespace), {@code null} is returned.
   */
  public static String normalize(String str) {
    if (str == null) {
      return null;
    }
    str = str.trim();
    return str.isEmpty() ? null : str;
  }
  
  /**
   * Normalizes all String properties (as determined by the given type) of the given DTO.
   * Property scan is shallow, i.e. String properties of nested DTOs are not normalized.
   * @see #normalize(String)
   */
  public static <T extends Dto> void normalize(T dto, Class<T> type) {
    BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(dto);
    for (PropertyDescriptor prop : BeanUtils.getPropertyDescriptors(type)) {
      if (prop.getPropertyType() == String.class) {
        wrapper.setPropertyValue(prop.getName(),
            normalize((String) wrapper.getPropertyValue(prop.getName())));
      }
    }
  }
  
  /**
   * Checks that a property of the given DTO is not null.
   * @return the property value
   * @throws IllegalArgumentException if the property value is null. Note that this is
   *     inconsistent with {@link Objects#requireNonNull(Object)} which throws a
   *     {@link NullPointerException}.
   */
  @SuppressWarnings("unchecked")
  public static <T> T checkNotNull(Dto dto, String propertyId) {
    BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(dto);
    return (T) checkNotNull(wrapper, propertyId);
  }
  
  /**
   * Checks all given properties of the given DTO are not null.
   * @throws IllegalArgumentException if any property value is null. Note that this is
   *     inconsistent with {@link Objects#requireNonNull(Object)} which throws a
   *     {@link NullPointerException}.
   */
  public static void checkNotNull(Dto dto, String... propertyIds) {
    BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(dto);
    for (String propertyId : propertyIds) {
      checkNotNull(wrapper, propertyId);
    }
  }
  
  private static Object checkNotNull(BeanWrapper wrapper, String propertyId) {
    Object value = wrapper.getPropertyValue(propertyId);
    if (value == null) {
      throw new IllegalArgumentException(propertyId + " must not be null!");
    }
    return value;
  }
  
  /**
   * Parses a duration of the format {@code hh:mm:ss}.
   * @return the duration in seconds
   * @throws IllegalArgumentException if the format is not valid
   */
  public static int parseDuration(String durationString) {
    Objects.requireNonNull(durationString);
    
    String[] parts = durationString.split(":");
    
    if (parts.length < 1 || parts.length > 3) {
      throw new IllegalArgumentException(
          "Duration must consist of 1 to 3 parts separated by colons.");
    }
    
    try {
      int duration = Integer.parseInt(parts[parts.length - 1]);
      if (duration < 0 || duration > 60) {
        throw new IllegalArgumentException("Seconds must between 0 and 60.");
      }
      if (parts.length > 1) {
        int minutes = Integer.parseInt(parts[parts.length - 2]);
        if (minutes < 0 || minutes > 60) {
          throw new IllegalArgumentException("Minutes must between 0 and 60.");
        }
        duration += minutes * 60;
      }
      if (parts.length == 3) {
        int hours = Integer.parseInt(parts[0]);
        if (hours < 0) {
          throw new IllegalArgumentException("Hours must be positive.");
        }
        duration += hours * 60 * 60;
      }
      return duration;
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException(nfe.getMessage());
    }
  }
}
