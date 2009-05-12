package org.mazur.subrosa.gui.graph;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class EditException extends RuntimeException {
  private static final long serialVersionUID = -3536137831422776781L;

  public EditException() { super(); }
  public EditException(final String message) { super(message); }
  public EditException(final String message, final Throwable cause) { super(message, cause); }
  public EditException(final Throwable cause) { super(cause); }
}
