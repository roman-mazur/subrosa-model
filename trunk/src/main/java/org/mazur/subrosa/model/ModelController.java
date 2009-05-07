package org.mazur.subrosa.model;

import java.util.HashMap;

import org.jgraph.graph.GraphCell;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ModelController {

  /** Elements map. */
  private HashMap<GraphCell, AbstractModelElement> elementsMap = new HashMap<GraphCell, AbstractModelElement>();
  
  public void addElement(final AbstractModelElement e) {
    elementsMap.put(e.getView(), e);
  }
  
}
