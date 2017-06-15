package com.xinra.growthlectures;

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
  
  public static Integer parseTime(String time) {
    
    if (time != null) {
      try {
        String[] timeParts = time.split(":");
        if (timeParts.length == 1) {
          Integer seconds = Integer.parseInt(timeParts[0]);
          return seconds;
        } else if (timeParts.length == 2) {
          Integer minutes = Integer.parseInt(timeParts[0]);
          Integer seconds = Integer.parseInt(timeParts[1]);
          if (seconds < 60) {
            return minutes * 60 + seconds;
          }
        } else if (timeParts.length == 3) {
          Integer hours = Integer.parseInt(timeParts[0]);
          Integer minutes = Integer.parseInt(timeParts[1]);
          Integer seconds = Integer.parseInt(timeParts[2]);
          if (minutes < 60 && seconds < 60) {
            return hours * 60 * 60 + minutes * 60 + seconds;
          }
        }
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
    return null;
  }

}
