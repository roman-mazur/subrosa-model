package org.mazur.subrosa.gui.graph;

import java.awt.Point;

import javax.swing.Icon;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public interface ElementView {

  /**
   * @return icon
   */
  Icon getIcon();
  
  /**
   * @param point the point
   */
  void setPosition(final Point point);
  
}
