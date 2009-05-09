package org.mazur.subrosa.gui.listeners

import javax.swing.event.ChangeListenerimport javax.swing.event.ChangeEvent

/**
 * Change listener for tabed pane.
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class SimpleChangeListener implements ChangeListener {

  /** Handler. */
  def closure
  
  void stateChanged(ChangeEvent e) { closure(e) }
  
}
