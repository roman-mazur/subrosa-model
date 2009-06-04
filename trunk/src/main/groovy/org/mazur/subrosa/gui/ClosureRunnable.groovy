/*
 * $Id$
 */
package org.mazur.subrosa.gui

/**
 * @author Roman Mazur
 */
public class ClosureRunnable implements Runnable{

  /** Closure. */
  def closure
  
  public ClosureRunnable(final def closure) {
    this.closure = closure
  }
  
  @Override
  public void run() { closure() }
  
}
