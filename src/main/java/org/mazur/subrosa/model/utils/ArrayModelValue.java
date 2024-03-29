package org.mazur.subrosa.model.utils;

import java.util.Arrays;

import org.mazur.subrosa.model.ModelValue;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ArrayModelValue extends ModelValue {
  private static final long serialVersionUID = 5538198965199323043L;
  /** Values. */
  private boolean[] v;

  public ArrayModelValue(final int d) {
    v = new boolean[d];
  }
  
  @Override
  public int dimension() { return v.length; }

  @Override
  public boolean get(final int index) { return v[index]; };
  
  public void set(final int index, final boolean value) { v[index] = value; }
  
  @Override
  public String toString() {
    return "ModelValue[values=" + Arrays.toString(v) + ", dim: " + dimension() + "]";
  }
}
