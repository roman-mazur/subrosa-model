package org.mazur.subrosa;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class InterpreterException extends RuntimeException {
  private static final long serialVersionUID = -8961197893870750881L;

  /**
   * @param msg message
   */
  public InterpreterException(final String msg) {
    super(msg);
  }
  
  /**
   * @param msg message
   */
  public InterpreterException(final String msg, final Throwable cause) {
    super(msg, cause);
  }
  
}
