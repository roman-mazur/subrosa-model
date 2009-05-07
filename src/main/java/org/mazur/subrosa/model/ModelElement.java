package org.mazur.subrosa.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model element.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public interface ModelElement extends Serializable {
  /**
   * @param input input bits
   * @return output bits
   */
  ModelValue calculate(final List<? extends ModelValue> input);
  
  /**
   * @return null value
   */
  ModelValue nullValue();
}
