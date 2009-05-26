package org.mazur.subrosa.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mazur.subrosa.gui.graph.ElementView;


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
  
  /** Logger. */
  private static final Logger LOG = Logger.getLogger(AbstractModelElement.class);
  
  /** Inputs and outputs. */
  private List<Interface> inputs = new LinkedList<Interface>(), outputs = new LinkedList<Interface>();
  
  /** Current value. */
  private ModelValue currentValue = nullValue();
  
  /** Element notes. */
  private String notes = "";
  
  public String getNotes() { return notes; }
  public void setNotes(final String notes) { this.notes = notes; }
  
  public void setCurrentValue(final ModelValue currentValue) {
    this.currentValue = currentValue;
  }
  
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
    LOG.debug("Get current value " + currentValue);
    return currentValue;
  }
  
  public List<Interface> getInputs() { return inputs; }
  public List<Interface> getOutputs() { return outputs; }
  
  @Override
  public ModelValue nullValue() {
    int d = 0;
    if (outputs != null) {
      for (Interface i : outputs) { d += i.dimension(); }
    }
    LOG.debug("Get null value " + d);
    return Interface.nullValue(d);
  }
  
  /**
   * @return a view to represent it in the jGraph component
   */
  public abstract ElementView getView();
  
  public abstract String getLabel();
 
  protected abstract boolean validateConnection(final AbstractModelElement source);
  
  public Interface connect(final AbstractModelElement source) {
    if (!validateConnection(source)) { return null; }
    Interface i = new Interface(source, this);
    inputs.add(i);
    source.outputs.add(i);
    return i;
  }
  
  public boolean hasConnections() {
    return !inputs.isEmpty() || !outputs.isEmpty();
  }
  
  @Override
  public String toString() {
    return "Model element[" + getLabel() + "(" + getNotes() + "), hasConnections:" + hasConnections() + "]";
  }
}
