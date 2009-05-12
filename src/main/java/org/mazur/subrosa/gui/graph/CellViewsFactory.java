package org.mazur.subrosa.gui.graph;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.VertexView;
import org.mazur.subrosa.gui.graph.editors.InterfaceEditor;

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
  
  @Override
  protected EdgeView createEdgeView(final Object edge) {
    if (edge instanceof InterfaceView) {
      EdgeView v = new EdgeView() {
        private static final long serialVersionUID = -4738461269143586907L;
        @Override
        public GraphCellEditor getEditor() { 
          return new InterfaceEditor((InterfaceView)edge); 
        }
      };
      v.setCell(edge);
      return v;
    }
    return super.createEdgeView(edge);
  }
}
