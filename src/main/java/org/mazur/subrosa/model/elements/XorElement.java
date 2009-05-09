package org.mazur.subrosa.model.elements;

import java.util.List;

import org.mazur.subrosa.gui.graph.ElementCell;
import org.mazur.subrosa.gui.graph.XorCell;
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

}