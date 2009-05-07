package org.mazur.subrosa.model



/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ElementsFactory {

  /**
   * @param className class name
   * @return instance 
   */
  public AbstractModelElement createElement(final String className) {
    Class clazz = Class.forName(className)
    return clazz.newInstance()
  }
  
}
