package org.mazur.subrosa.gui

import java.awt.Font

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Config {

  static Config instance = new Config()
  
  /** List of elements. */
  private final static def ELEMENTS_LIST = [
    'xor' : 'org.mazur.subrosa.model.elements.XorElement',                                            
    'const' : 'org.mazur.subrosa.model.elements.ConstantElement',                                            
    'func' : 'org.mazur.subrosa.model.elements.FunctionElement',                                            
    'out' : 'org.mazur.subrosa.model.elements.OutElement'                                            
  ]

  def codeFont = new Font(Font.MONOSPACED, 0, 12)
  
  public def elements() { return ELEMENTS_LIST }
  
}
