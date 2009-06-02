package org.mazur.subrosa.model;


/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public abstract class MarginElement extends AbstractModelElement {
  private static final long serialVersionUID = -5059311026315690642L;

  /** Number. */
  private Number number;
  
  public Number getNumber() { return number; }
  public void setNumber(final Number number) { this.number = number; }

}
