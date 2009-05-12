package org.mazur.subrosa.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jfree.util.Log;
import org.mazur.subrosa.gui.graph.ElementView;
import org.mazur.subrosa.gui.graph.GraphComponent;

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
  
  public void saveModel(final OutputStream out) throws IOException {
    ObjectOutputStream output = new ObjectOutputStream(out);
    for (Entry<ElementView, AbstractModelElement> e : elementsMap.entrySet()) {
      output.writeObject(e.getValue());
      output.flush();
    }
    output.close();
  }
  
  public void loadModel(final InputStream in) throws IOException {
    ObjectInputStream input = new ObjectInputStream(in);
    while (true) {
      try {
        AbstractModelElement e = (AbstractModelElement)input.readObject();
        addElement(e);
      } catch (Exception e) {
        Log.debug("Exception in read", e);
        break;
      }
    }
    input.close();
  }
  
  public void display(final GraphComponent component) {
    for (Entry<ElementView, AbstractModelElement> e : elementsMap.entrySet()) {
      component.getGraphLayoutCache().insert(e.getValue().getView());
    }
    for (Entry<ElementView, AbstractModelElement> e : elementsMap.entrySet()) {
      for (Interface i : e.getValue().getInputs()) {
        component.getGraphLayoutCache().insert(i.getView());
      }
    }
  }
}
