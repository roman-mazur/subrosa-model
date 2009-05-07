package org.mazur.subrosa.model;

/**
 * Model value.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public interface ModelValue {

  /**
   * @return cardinality (bits count)
   */
  int dimension();
  
  /**
   * @param index bit index 
   * @return bit value
   */
  boolean get(final int index);
  
}
