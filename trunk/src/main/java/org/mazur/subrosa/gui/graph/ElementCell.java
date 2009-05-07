package org.mazur.subrosa.gui.graph;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jgraph.graph.DefaultGraphCell;
import org.mazur.subrosa.model.AbstractModelElement;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public abstract class ElementCell extends DefaultGraphCell implements ElementView {
  private static final long serialVersionUID = -4689271084577338034L;
  /** Element. */
  private AbstractModelElement element;
  
  public ElementCell(final AbstractModelElement element) {
    this.element = element;
  }
  
  @Override
  public Icon getIcon() {
    String name = "/e-" + element.getLabel() + "-icon.gif";
    return new ImageIcon(ElementCell.class.getResource(name), element.getLabel());
  }

}
