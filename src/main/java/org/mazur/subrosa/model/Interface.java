package org.mazur.subrosa.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.mazur.subrosa.model.utils.BitSetModelValue;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Interface extends BitSetModelValue implements Serializable {

  private static final long serialVersionUID = -143376295176661749L;
  
  private static final Map<Integer, ModelValue> NULL_VALUES_CACHE = new TreeMap<Integer, ModelValue>();

  public static ModelValue nullValue(final int dimension) {
    ModelValue mv = NULL_VALUES_CACHE.get(dimension);
    if (mv != null) { return null; }
    mv = new ModelValue() {
      @Override
      public int dimension() { return dimension; }
      @Override
      public boolean get(int index) { return false; }
    };
    NULL_VALUES_CACHE.put(dimension, mv);
    return mv;
  }
  
}
