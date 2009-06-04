package org.mazur.subrosa.model;

import java.io.Serializable;

/**
 * Model value.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public abstract class ModelValue implements Serializable {
  private static final long serialVersionUID = -7672388865293579377L;

  /**
   * @return cardinality (bits count)
   */
  public abstract int dimension();
  
  /**
   * @param index bit index 
   * @return bit value
   */
  public abstract boolean get(final int index);
 
  @Override
  public boolean equals(final Object obj) {
    ModelValue o = (ModelValue)obj;
    int d = o.dimension(); 
    if (d != dimension()) { return false; }
    for (int i = 0; i < d; i++) {
      if (get(i) != o.get(i)) { return false; }
    }
    return true;
  }
}
