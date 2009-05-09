package org.mazur.subrosa.gui.listeners

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class SimpleMouseListener extends MouseAdapter {
  /** Closures. */
  def clicked
  
  void mouseClicked(final MouseEvent e) { clicked(e) }
}
