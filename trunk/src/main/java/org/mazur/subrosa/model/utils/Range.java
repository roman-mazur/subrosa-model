package org.mazur.subrosa.model.utils;

import java.io.Serializable;

/**
 * Range.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Range implements Serializable {
  private static final long serialVersionUID = 8481416034517812505L;
  
  /** Two values. */
  private int min, max;
  
  /**
   * Constructor.
   * @param min min value
   * @param max max value
   */
  public Range(final int min, final int max) {
    this.min = min;
    this.max = max;
  }
  
  /**
   * Constructor.
   * @param min min value
   * @param max max value
   * @param minInc flag to include the min value
   * @param maxInc flag to include the max value
   */
  public Range(final int min, final boolean minInc, final int max, final boolean maxInc) {
    this.min = minInc ? min : min + 1;
    this.max = maxInc ? max : max - 1;
  }

  /**
   * @return min value
   */
  public int getMin() { return min; }
  
  /**
   * @return max value
   */
  public int getMax() { return max; }
  
  /**
   * @param value value to check
   * @return true if this range contains the defined value
   */
  boolean contains(final int value) {
    return min <= value && max >= value;
  }
  
  /**
   * @param p items processor
   */
  public void each(final RangeItemProcessor p) {
    for (int value = min; value <= max; value++) { p.process(value); }
  }
  
  public int size() {
    return max - min + 1;
  }
}
