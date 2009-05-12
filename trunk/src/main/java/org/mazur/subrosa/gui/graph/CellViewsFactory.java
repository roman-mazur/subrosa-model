package org.mazur.subrosa.gui.graph;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

/**
 * Factory for jGraph views.
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class CellViewsFactory extends DefaultCellViewFactory {
  private static final long serialVersionUID = 6358958210266902682L;

  @Override
  protected VertexView createVertexView(final Object vertex) {
    if (vertex instanceof ElementCell) {
      VertexView v = ((ElementCell)vertex).createJGrpahView();
      v.setCell(vertex);
      return v;
    }
    return super.createVertexView(vertex);
  }
  
}
