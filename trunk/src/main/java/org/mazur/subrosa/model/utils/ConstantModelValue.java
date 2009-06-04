package org.mazur.subrosa.model.utils;

import org.mazur.subrosa.model.ModelValue;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ConstantModelValue extends ModelValue {
  private static final long serialVersionUID = 3065096792986847600L;
  /** Value and dimension. */
  private int value, dimension;
  
  public ConstantModelValue(final int value, final int dimension) {
    this.value = value;
    this.dimension = dimension;
  }
  
  public ConstantModelValue(final ModelValue v) {
    this.dimension = v.dimension();
    if (v instanceof ConstantModelValue) {
      this.value = ((ConstantModelValue)v).value;
    } else {
      this.value = 0;
      for (int i = 0; i < dimension; i++) {
        if (v.get(i)) { value |= (1 << i); }
      }
    }
  }
  
  @Override
  public int dimension() { return dimension; }

  @Override
  public boolean get(final int index) { return (value & (1 << index)) != 0; }
  
  @Override
  public String toString() {
    return "ModelValue[value=" + value + ", dim: " + dimension() + "]";
  }
  
  public int getValue() { return value; }
  
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof ConstantModelValue)) { return super.equals(obj); }
    ConstantModelValue o = (ConstantModelValue)obj;
    return dimension == o.dimension && value == o.value;
  }
}
