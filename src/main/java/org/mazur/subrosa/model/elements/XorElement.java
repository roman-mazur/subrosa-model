package org.mazur.subrosa.model.elements;

import java.util.List;

import org.mazur.subrosa.gui.graph.EditException;
import org.mazur.subrosa.gui.graph.ElementCell;
import org.mazur.subrosa.gui.graph.cells.XorCell;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.ModelValue;
import org.mazur.subrosa.model.utils.ArrayModelValue;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class XorElement extends AbstractModelElement {

  private static final long serialVersionUID = -5862703245886927046L;

  /** Max input count. */
  private int maxInputCount = 5;
  
  /** View. */
  private XorCell view = new XorCell(this);
  
  @Override
  public ElementCell getView() { return view; }

  @Override
  public ModelValue calculate(final List<? extends ModelValue> input) {
    if (input == null) { return null; }
    int d = input.get(0).dimension();
    ArrayModelValue mv = new ArrayModelValue(d);
    for (int i = 0; i < d; i++) {
      boolean temp = false;
      for (ModelValue in : input) { temp |= in.get(i); }
      mv.set(i, temp);
    }
    return mv;
  }

  @Override
  public String getLabel() { return "xor"; }

  public int getMaxInputCount() { return maxInputCount; }
  public void setMaxInputCount(final int maxInputCount) { this.maxInputCount = maxInputCount; }

  @Override
  protected boolean validateConnection(final AbstractModelElement source) {
    if (getInputs().size() >= maxInputCount) {
      throw new EditException("Maimum inputs count is " + maxInputCount);
    }
    return true;
  }
}
