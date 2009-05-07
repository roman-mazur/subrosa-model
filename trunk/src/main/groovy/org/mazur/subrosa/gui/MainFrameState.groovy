package org.mazur.subrosa.gui

import groovy.swing.SwingBuilderimport java.io.Fileimport java.io.FileWriter
/**
 * State of the main frame.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class MainFrameState {

  /** Map with documents and text areas. */
  private def documentAreaMap = [:]
  
  /** Last element to create. */
  def lastElementToCreate
  
  /** Active document. */
  Document activeDocument
  
  /** Last index for the pain. */
  private int lastIndex = -1
  
  /**
   * @return active code area
   */
  def getActiveComponent() {
    return documentAreaMap[activeDocument]
  }
  
  /**
   * @return new component
   */
  private def newEditor() {
    def b = new EditorBuilder()
    return b.editor()
  }
  
  /**
   * Add the new document.
   */
  void newDocument() {
    ++lastIndex
    def d = new Document(index : lastIndex)
    documentAreaMap[d] = newEditor()
    activeDocument = d
  }
  
  /**
   * Open a document.
   */
  void openDocument(final File file) {
    ++lastIndex
    def d = new Document(index : lastIndex, name : file.name, sourceFile : file)
    documentAreaMap[d] = newEditor()
    activeDocument = d
  }
  
  /**
   * 'Save as' a document
   */
  void saveDocument(final File file) {
    activeDocument.sourceFile = file
    activeDocument.name = file.name
    saveDocument()
  }
  
  /**
   * Save a document.
   */
  boolean saveDocument() {
    if (!activeDocument.sourceFile) { return false }
    def w = new FileWriter(activeDocument.sourceFile)
    //w << documentAreaMap[activeDocument].text
    w.close()
    return true
  }
}
