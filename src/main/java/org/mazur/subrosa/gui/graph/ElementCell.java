package org.mazur.subrosa.gui.graph;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;
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

  protected abstract Dimension getSize();
  
  @Override
  public void setPosition(final Point point) {
    Dimension d = getSize();
    GraphConstants.setBounds(getAttributes(), 
        new Rectangle2D.Double(point.x, point.y, d.width, d.height));
  }
  
  /**
   * @return vertex view
   */
  public abstract VertexView createJGrpahView();
}
