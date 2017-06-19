package com.xinra.growthlectures.frontend;

import com.google.common.collect.ImmutableList;

/**
 * Thrown by REST controllers if the input data is not valid. Holds multiple
 * error messages that are returned with a 422 response.
 */
public class InvalidDataException extends Exception {

  private static final long serialVersionUID = 1L;
  
  private final ImmutableList<String> messages;
  
  public InvalidDataException(Iterable<String> messages) {
    super(messages.toString());
    this.messages = ImmutableList.copyOf(messages);
  }
  
  public InvalidDataException(String... messages) {
    super(messages.toString());
    this.messages = ImmutableList.copyOf(messages);
  }
  
  public InvalidDataException(String message) {
    super(message);
    this.messages = ImmutableList.of(message);
  }
  
  public ImmutableList<String> getMessages() {
    return messages;
  }
}
