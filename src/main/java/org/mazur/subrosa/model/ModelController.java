package org.mazur.subrosa.model;

import java.util.HashMap;

import org.mazur.subrosa.gui.graph.ElementView;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ModelController {

  /** Elements map. */
  private HashMap<ElementView, AbstractModelElement> elementsMap = new HashMap<ElementView, AbstractModelElement>();
  
  public void addElement(final AbstractModelElement e) {
    elementsMap.put(e.getView(), e);
  }
  
}
