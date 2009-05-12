package org.mazur.subrosa.gui.graph.views;

import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.VertexView;
import org.mazur.subrosa.gui.graph.ElementCell;
import org.mazur.subrosa.gui.graph.editors.BusinessElementEditor;

/**
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class SimpleJGraphVertexView extends VertexView {
  private static final long serialVersionUID = 946036506784726453L;

  /** Cell. */
  private ElementCell cell;

  public SimpleJGraphVertexView(final ElementCell cell) {
    super(cell);
    this.cell = cell;
  }
  
  @Override
  public GraphCellEditor getEditor() { return new BusinessElementEditor(cell); }
}
