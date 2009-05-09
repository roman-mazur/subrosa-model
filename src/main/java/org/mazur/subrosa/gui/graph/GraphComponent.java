package org.mazur.subrosa.gui.graph;

import java.awt.Point;

import org.jgraph.JGraph;
import org.mazur.subrosa.model.AbstractModelElement;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class GraphComponent extends JGraph {
  private static final long serialVersionUID = 7695315624674279265L;

  public GraphComponent() {
    getGraphLayoutCache().setFactory(new CellViewsFactory());
  }
  
  public void addElementView(final AbstractModelElement element, final Point point) {
    ElementView view = element.getView();
    view.setPosition(point);
    getGraphLayoutCache().insert(view);
  }
}
