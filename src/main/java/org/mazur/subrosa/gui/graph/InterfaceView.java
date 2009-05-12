package org.mazur.subrosa.gui.graph;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.mazur.subrosa.model.Interface;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class InterfaceView extends DefaultEdge {
  private static final long serialVersionUID = 1475885208448879092L;

  private Interface modelElement;
  
  public InterfaceView(final DefaultPort source, final DefaultPort target) {
    this.source = source;
    this.target = target;
    source.addEdge(this);
    target.addEdge(this);
    GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_CLASSIC);
    GraphConstants.setEndFill(getAttributes(), true);
  }
  
  public Interface getModelElement() { return modelElement; }
  public void setModelElement(final Interface modelElement) {
    this.modelElement = modelElement;
  }
  
}
