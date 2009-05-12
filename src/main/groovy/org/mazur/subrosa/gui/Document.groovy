package org.mazur.subrosa.gui

import org.mazur.subrosa.model.ModelController/**
 * Gromula document.
 * 
 * Version: $Id: Document.groovy 8 2009-04-27 20:15:17Z mazur.roman $
 * 
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Document {

  /** Document name. */
  String name = 'noname'
  
  /** Source file. */
  File sourceFile

  /** Index for the pane. */
  int index
  
  /** Model controller. */
  ModelController controller
}
