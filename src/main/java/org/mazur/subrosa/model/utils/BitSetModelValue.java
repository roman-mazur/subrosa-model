package org.mazur.subrosa.model.utils;

import java.util.BitSet;

import org.mazur.subrosa.model.ModelValue;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class BitSetModelValue extends ModelValue {
  private static final long serialVersionUID = 4084212021292376016L;

  /** Set. */
  private BitSet set = new BitSet();
  
  private int size;
  
  @Override
  public int dimension() { return size; }

  @Override
  public boolean get(int index) { return set.get(index); }

  public void add(final ModelValue v, final Range range) {
    size += range.size();
    range.each(new RangeItemProcessor() {
      @Override
      public void process(final int index) { set.set(index, v.get(index)); }
    });
  }
  
  @Override
  public String toString() {
    return "ModelValue[values=" + set + ", dim: " + dimension() + "]";
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof BitSetModelValue)) { return super.equals(obj); }
    BitSetModelValue o = (BitSetModelValue)obj;
    if (o.size != size) { return false; }
    return set.equals(o.set);
  }
}
