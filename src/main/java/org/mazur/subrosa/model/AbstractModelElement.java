package org.mazur.subrosa.model;

import java.util.List;

import org.mazur.subrosa.gui.graph.ElementCell;


/**
 * Abstract element of the model.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public abstract class AbstractModelElement implements ModelElement {
  private static final long serialVersionUID = -2276833922428686502L;

  /** Inputs and outputs. */
  private List<Interface> inputs, outputs;
  
  /** Current value. */
  private ModelValue currentValue;
  
  /**
   * @return output value
   */
  public ModelValue calculate() {
    return inputs != null && !inputs.isEmpty() ? calculate(inputs) : nullValue();
  }
  
  /**
   * @return the current value
   */
  public ModelValue getCurrentValue() {
    return currentValue;
  }
  
  @Override
  public ModelValue nullValue() {
    int d = 0;
    if (outputs != null) {
      for (Interface i : outputs) { d += i.dimension(); }
    }
    return Interface.nullValue(d);
  }
  
  /**
   * @return a view to represent it in the jGraph component
   */
  public abstract ElementCell getView();
  
  public abstract String getLabel();
 
}
