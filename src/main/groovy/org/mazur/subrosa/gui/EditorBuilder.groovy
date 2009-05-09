package org.mazur.subrosa.gui

import org.mazur.subrosa.gui.graph.GraphComponentimport java.awt.event.MouseListener
/**
 * Editor builder.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class EditorBuilder {

  /** Editor mouse listener. */
  MouseListener editorMouseListener
  
  /**
   * @return new editor
   */
  def editor() {
    GraphComponent result = new GraphComponent()
    result.disconnectable = false
    result.gridEnabled = true
    result.gridVisible = true
    result.addMouseListener(editorMouseListener)
    return result
  }
  
}
