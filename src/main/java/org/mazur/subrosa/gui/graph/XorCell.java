package org.mazur.subrosa.gui.graph;

import java.awt.Color;
import java.awt.Dimension;

import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;
import org.mazur.subrosa.gui.graph.views.EllipseView;
import org.mazur.subrosa.model.AbstractModelElement;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class XorCell extends ElementCell {
  
  private static final long serialVersionUID = -193326977245601334L;

  private static final Dimension SIZE = new Dimension(30, 30);
  
  public XorCell(final AbstractModelElement element) {
    super(element);
    GraphConstants.setOpaque(getAttributes(), true);
    GraphConstants.setValue(getAttributes(), element.getLabel());
    GraphConstants.setBorderColor(getAttributes(), Color.BLACK);
    GraphConstants.setGradientColor(getAttributes(), Color.GRAY);
  }
  
  @Override
  public VertexView createJGrpahView() { return new EllipseView(); }
  
  @Override
  protected Dimension getSize() { return SIZE; }
}
