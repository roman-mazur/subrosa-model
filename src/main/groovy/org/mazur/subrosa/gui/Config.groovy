package org.mazur.subrosa.gui



/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Config {

  /** List of elements. */
  private final static def ELEMENTS_LIST = [
    'xor' : 'org.mazur.subrosa.model.elements.XorElement'                                            
  ]
  
  public def elements() { return ELEMENTS_LIST }
  
}
