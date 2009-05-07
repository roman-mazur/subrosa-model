package org.mazur.subrosa.gui

import groovy.swing.SwingBuilderimport org.jgraph.JGraph

/**
 * Editor builder.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class EditorBuilder {

  /**
   * @return new editor
   */
  def editor() {
    JGraph result = new JGraph()
    result.disconnectable = false
    result.gridEnabled = true
    result.gridVisible = true
    return result
  }
  
}
