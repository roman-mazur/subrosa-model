package org.mazur.subrosa.gui.listeners

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class SimpleActionListener implements ActionListener {

  /** Closure. */
  def closure
  
  @Override
  public void actionPerformed(final ActionEvent e) { closure() }

}
