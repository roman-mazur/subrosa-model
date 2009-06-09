package org.mazur.subrosa.gui

import groovy.swing.SwingBuilderimport java.io.Fileimport java.io.FileWriterimport org.mazur.subrosa.model.ModelControllerimport java.io.FileInputStreamimport java.io.FileOutputStreamimport org.codehaus.groovy.control.CompilerConfigurationimport org.mazur.subrosa.InterpreterException
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
  
  /** Editor builder. */
  EditorBuilder editorBuilder
  
  /** Compiler configuration. */
  def CompilerConfiguration compilerConf
  
  /** Last index for the pain. */
  private int lastIndex = -1

  /** Closure. */
  def displayCompileErrors, infoLog
  
  /**
   * @return active code area
   */
  def getActiveComponent() {
    return documentAreaMap[activeDocument]
  }
  
  /**
   * Add the new document.
   */
  void newDocument() {
    ++lastIndex
    def d = new Document(index : lastIndex, controller : new ModelController())
    documentAreaMap[d] = editorBuilder.editor()
    activeDocument = d
  }

  /**
   * Add an element to the active graph.
   */
  void addElementToActiveGraph(def element, def point) {
    def component = documentAreaMap[activeDocument]
    component.addElementView(element, point)
    activeDocument.controller.addElement(element)
  }
  
  /**
   * Change active document.
   */
  void changeActiveDocument(int index) {
    activeDocument = documentAreaMap.find() {
      it.key.index == index
    }.key
  }
  
  /**
   * Open a document.
   */
  void openDocument(final File file) {
    try {
      ModelController ctl = new ModelController()
      ctl.loadModel(new FileInputStream(file))
      ++lastIndex
      def d = new Document(index : lastIndex, name : file.name, sourceFile : file, controller : ctl)
      def editor = editorBuilder.editor()
      documentAreaMap[d] = editor
      activeDocument = d
      ctl.display(editor)
    } catch (Exception e) {
      e.printStackTrace()
    }
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
    try {
      activeDocument.controller.saveModel(new FileOutputStream(activeDocument.sourceFile))
      return true
    } catch (Exception e) {
      e.printStackTrace()
      return false
    }
  }
  
  void logMessage(final def message) {
    if (infoLog) { infoLog(message?.toString()) }
  }
  
  /**
   * Start debugger.
   */
  void startDebugger() {
    def debugger = new Debugger(
      controller : activeDocument.controller,
      compilerConfiguration : compilerConf
    )
    try {
      debugger.prepare()
    } catch (InterpreterException e) {
      displayCompileErrors(e)
    }
    debugger.showFrame()
  }
  
  /**
   * Start generator editor.
   */
  void startGenEditor() {
    def editor = new Generator(controller : activeDocument.controller, cconf : compilerConf, state : this)
    editor.prepare()
    editor.showFrame()
  }

  /**
   * Start generator.
   */
  void startGenerator() {
    def editor = new Generator(controller : activeDocument.controller, cconf : compilerConf, state : this)
    try {
      editor.prepare()
      editor.runCode()
    } catch (InterpreterException e) {
      displayCompileErrors(e)
    }
  }
  
  /**
   * Show graphs.
   */
  void showGraphs() {
    def gc = new Graphs(controller : activeDocument.controller, compilerConfiguration : compilerConf)
    try {
      gc.prepare()
      gc.show()
    } catch (InterpreterException e) {
      displayCompileErrors(e)
    }
  }
  
  /**
   * Show console.
   */
  void showConsole() {
    def cc = new Console(controller : activeDocument.controller, cconf : compilerConf, state : this)
    try {
      cc.prepare()
      cc.showFrame()
    } catch (InterpreterException e) {
      displayCompileErrors(e)
    }
  }
  
}
