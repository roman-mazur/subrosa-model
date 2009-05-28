package org.mazur.subrosa.model.elements;

import java.util.List;

import org.mazur.subrosa.gui.graph.ElementView;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.ModelValue;

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class OutElement extends AbstractModelElement {
  private static final long serialVersionUID = 8269594413939589371L;
  
  /** Include in stats. */
  private boolean includeInStats = true;
  
  public boolean isIncludeInStats() { return includeInStats; }
  public void setIncludeInStats(boolean includeInStats) { this.includeInStats = includeInStats; }
  
  @Override
  public String getLabel() { return "out"; }

  @Override
  public ElementView getView() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected boolean validateConnection(AbstractModelElement source) {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.mazur.subrosa.model.ModelElement#calculate(java.util.List)
   */
  @Override
  public ModelValue calculate(List<? extends ModelValue> input) {
    // TODO Auto-generated method stub
    return null;
  }

}
