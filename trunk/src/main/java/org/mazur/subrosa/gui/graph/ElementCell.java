package org.mazur.subrosa.gui.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;
import org.mazur.subrosa.gui.IconsFactory;
import org.mazur.subrosa.gui.graph.views.SimpleJGraphVertexView;
import org.mazur.subrosa.model.AbstractModelElement;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public abstract class ElementCell extends DefaultGraphCell implements ElementView {
  private static final long serialVersionUID = -4689271084577338034L;
  
  /** Logger. */
  private static final Logger LOG = Logger.getLogger(ElementCell.class);
  
  /** Element. */
  private AbstractModelElement element;
  
  public ElementCell(final AbstractModelElement element) {
    this.element = element;
    GraphConstants.setOpaque(getAttributes(), true);
    GraphConstants.setValue(getAttributes(), element.getNotes());
    GraphConstants.setBorderColor(getAttributes(), Color.BLACK);
    GraphConstants.setValue(getAttributes(), new Object() {
      @Override
      public String toString() { return element.getNotes(); }
    });
    DefaultPort p = new DefaultPort();
    add(p);
  }
  
  @Override
  public Icon getIcon() { return IconsFactory.getIcon("e-" + element.getLabel() + "-icon"); }

  @Override
  public void setPosition(final Point point) {
    Dimension d = getSize();
    GraphConstants.setBounds(getAttributes(), 
        new Rectangle2D.Double(point.x, point.y, d.width, d.height));
  }
  
  protected abstract Dimension getSize();
  
  @Override
  public AbstractModelElement getElement() { return element; }
  
  /**
   * @return new vertex view
   */
  public VertexView createJGrpahView() { 
    LOG.debug("new vertex view");
    return new SimpleJGraphVertexView(this);
  }
  
  
  public abstract JComponent getEditorComponent();
  
  public boolean isEditorInsideCell() { return false; } 
  
  public Object getValue() { return element.getNotes(); }
  
}
