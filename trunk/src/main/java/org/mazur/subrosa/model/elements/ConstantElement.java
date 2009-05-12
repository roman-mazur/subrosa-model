package org.mazur.subrosa.model.elements;

import java.util.List;

import org.mazur.subrosa.gui.graph.ElementView;
import org.mazur.subrosa.gui.graph.cells.ConstantCell;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.ModelValue;
import org.mazur.subrosa.model.utils.ConstantModelValue;

/**
 * Constant.
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class ConstantElement extends AbstractModelElement {
  private static final long serialVersionUID = 7121956549721419752L;

  /** Value. */
  private int value = 0, dimension = 5;
  
  /** View. */
  private ConstantCell view = new ConstantCell(this);
  
  /**
   * @return the value
   */
  public int getValue() { return value; }

  /**
   * @return the dimension
   */
  public int getDimension() { return dimension; }

  /**
   * @param value the value to set
   */
  public void setValue(final int value) { this.value = value; }

  /**
   * @param dimension the dimension to set
   */
  public void setDimension(final int dimension) { this.dimension = dimension; }

  @Override
  public String getLabel() { return "const"; }

  @Override
  public ElementView getView() { return view; }

  @Override
  protected boolean validateConnection(final AbstractModelElement source) { return false; }

  @Override
  public ModelValue calculate(final List<? extends ModelValue> input) {
    return new ConstantModelValue(value, dimension);
  }

  @Override
  public ModelValue getCurrentValue() { return calculate(null); }
}
