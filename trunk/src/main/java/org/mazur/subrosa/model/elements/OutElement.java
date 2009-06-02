package org.mazur.subrosa.model.elements;

import java.util.List;

import org.mazur.subrosa.gui.graph.ElementView;
import org.mazur.subrosa.gui.graph.cells.OutCell;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.MarginElement;
import org.mazur.subrosa.model.ModelValue;

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class OutElement extends MarginElement {
  private static final long serialVersionUID = 8269594413939589371L;
  
  /** Include in stats. */
  private boolean includeInStats = true;
  
  /** View. */
  private OutCell view;
  
  public boolean isIncludeInStats() { return includeInStats; }
  public void setIncludeInStats(final boolean includeInStats) { this.includeInStats = includeInStats; }
  
  @Override
  public String getLabel() { return "out"; }

  @Override
  protected boolean validateConnection(final AbstractModelElement source) {
    return getInputs().isEmpty();
  }

  @Override
  public ModelValue calculate(final List<? extends ModelValue> input) { 
    return input.get(0); 
  }

  @Override
  public ElementView getView() {
    if (view == null) { view = new OutCell(this); }
    return view;
  }

}
