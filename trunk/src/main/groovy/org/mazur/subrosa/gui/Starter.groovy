package org.mazur.subrosa.gui

import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WCimport java.awt.BorderLayout as BLimport org.apache.log4j.Loggerimport javax.swing.JToolBarimport javax.swing.JSplitPaneimport java.awt.Fontimport java.awt.Colorimport org.mazur.subrosa.model.ModelControllerimport org.mazur.subrosa.model.ElementsFactory
import java.awt.event.MouseAdapterimport org.mazur.subrosa.gui.listeners.SimpleMouseListener
import org.mazur.subrosa.gui.listeners.SimpleChangeListener
import javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport org.codehaus.groovy.control.CompilerConfigurationimport java.io.StringWriterimport java.io.PrintWriter/**
 * Starter script.
 * @author Roman Mazur (mailto:mazur.roman@gmail.com)
 */

def log = Logger.getLogger(Starter.class)

/** Some colors. */
Color INFO_COLOR = new Color(40, 165, 90), ERROR_COLOR = Color.RED

/** Handlers for GUI actions. */
def actionsHandlers = [:]

/** Main state. */
MainFrameState state

/** Elements factory. */
ElementsFactory eFactory = new ElementsFactory()

/** Configuration object. */
Config configuration = new Config()

/** Main frame. */
def mainFrame

/** Compiler configuration. */
def compilerConfiguration = new CompilerConfiguration()
compilerConfiguration.debug = true
compilerConfiguration.recompileGroovySource = true


SwingBuilder.build() {
  
  // ================================== COMPONENTS ==================================
  
  /** Tabs for opened documents. */
  def documentTabs = tabbedPane()
  documentTabs.addChangeListener(new SimpleChangeListener(closure : {
    state.changeActiveDocument(documentTabs.selectedIndex)
  }))
  
  /** For logs. */
  def logTextArea = textArea()
  /** For errors. */
  def errorsTextArea = textArea()
  
  /** Label for status. */
  def statusLabel = label()

  /** Files chooser. */
  JFileChooser filesChooser = new JFileChooser()
  filesChooser.fileFilter = new FileNameExtensionFilter("Subrosa files", "srm")
  
  // =================================== HELPERS ====================================

  /** Creates the scroll pane with the defined text area for the tabbed pane. */
  def infoTextArea = { String title, def area ->
    scrollPane(name : title) { widget(area) }  
  }
  /** Displays a short information within the status label. */
  def infoStatus = { String msg ->
    statusLabel.text = ' INFO: ' + msg
    statusLabel.foreground = INFO_COLOR
  }
  /** Displays a short error description within the status label. */
  def errorStatus = { String msg ->
    statusLabel.text = ' ERROR: ' + msg
    statusLabel.foreground = ERROR_COLOR
  }
  /** Write a message to the text area. */
  def textMessageToArea = { def area, String msg ->
    Date d = new Date()
    area.text += "\n[$d] -> $msg" 
  }
  /** Log an error. */
  def logError = { textMessageToArea(errorsTextArea, it) }
  /** Log an error. */
  def logInfo = { textMessageToArea(logTextArea, it) }
  /** Create a user action. */
  def userAction = { Map args ->
    def n = args['name']
    def c = args['closure']
    args['closure'] = {
      if (c) { c() }
      def h = actionsHandlers[n]
      log.debug "Handler for $n: $h"
      if (h) { h() }
    }
    action(args)
  }
  /** Add the active document to the tabbed pane. */
  def addDocToTab = {
    def panel = panel(border : raisedEtchedBorder()) {
      borderLayout()
      scrollPane(constraints : BL.CENTER) {
        widget(state.activeComponent)
      }
    }
    documentTabs.add(panel, state.activeDocument.name)
    documentTabs.selectedIndex = state.activeDocument.index
  }
  /** Log exception. */
  def logException = {
    StringWriter out = new StringWriter()
    it.printStackTrace(new PrintWriter(out))
    logError(out.toString())
    errorStatus('An exception was thrown!');
  }
  
  // =================================== ACTIONS ====================================

  /** Create new document. */
  def newDocumentAction = userAction(
    name : 'New', mnemonic : 'N',
    accelerator : 'ctrl N',
    keyStroke : 'ctrl N',
    closure : {
      state.newDocument()
      addDocToTab()
      infoStatus("New document was created.")
    }
  )
  
  /** Open document action. */
  def openDocumentAction = userAction(
    name : 'Open', mnemonic : 'O',
    accelerator : 'ctrl O',
    keyStroke : 'ctrl O',
    closure : {
      int res = filesChooser.showOpenDialog(mainFrame)
      if (res == JFileChooser.APPROVE_OPTION) {
        state.openDocument(filesChooser.selectedFile)
        addDocToTab()
        infoStatus("The document was opened.")
      }
    }
  )
  
  /** Save as document action. */
  def saveAsDocumentAction = userAction(
    name : 'Save as', mnemonic : 'A',
    accelerator : 'ctrl shift S',
    keyStroke : 'ctrl shift S',
    closure : {
      log.info "Execute 'save as' action"
      int res = filesChooser.showSaveDialog(mainFrame)
      if (res == JFileChooser.APPROVE_OPTION) {
        state.saveDocument(filesChooser.selectedFile)
        documentTabs.setTitleAt(state.activeDocument.index, state.activeDocument.name)
        infoStatus("The document was saved.")
        logInfo("The document was saved.")
      }
    }
  )

  /** Save a document document. */
  def saveDocumentAction = userAction(
    name : 'Save', mnemonic : 'S',
    accelerator : 'ctrl S',
    keyStroke : 'ctrl S',
    closure : {
      log.info "Execute 'save' action"
      if (!state.saveDocument()) {
        saveAsDocumentAction.actionPerformed(null)
      } else {
        infoStatus("The document was saved.")
        logInfo("The document was saved.")
      }
    }
  )
  
  /** Add a new element to the model. */
  def addNewElement = { def event ->
    def eKey = state.lastElementToCreate
    if (!eKey) { return }
    def className = configuration.elements()[eKey]
    if (!className) {
      def msg = "Element $eKey not found"
      errorStatus(msg)
      logError(msg)
      return 
    }
    def element = eFactory.createElement(className)
    state.addElementToActiveGraph(element, event.point)
    state.lastElementToCreate = null
    def msg = "Element '$eKey' was added."
    infoStatus(msg)
    logInfo(msg)
  }
  
  /** Start debugger. */
  def debugAction = userAction(
    name : 'Debug', mnemonic : 'D',
    accelerator : 'ctrl D',
    keyStroke : 'ctrl D',
    closure : {
      log.info "Execute 'debug' action"
      state.startDebugger()
    }
  )
  
  /** Start generator editor. */
  def genEditorAction = userAction(
    name : 'Generator', mnemonic : 'G',
    accelerator : 'ctrl G',
    keyStroke : 'ctrl G',
    closure : {
      log.info "Execute 'generator' action"
      state.startGenEditor()
    }
  )
  
  /** Start generator. */
  def startGeneratorAction = userAction(
    name : 'Start generator', mnemonic : 'S',
    accelerator : 'ctrl shift G',
    keyStroke : 'ctrl shift G',
    closure : {
      log.info "Execute 'start generator' action"
      state.startGenerator()
    }
  )
  
  /** Graph action. */
  def graphAction = userAction(
    name : 'Graphs', mnemonic : 'G',
    closure : {
      log.info "Execute 'graph' action"
      state.showGraphs()
    }
  )
  
  // ===================================== MAIN =====================================
  
  // actions are formed -> create the state instance
  state = new MainFrameState(
    editorBuilder : new EditorBuilder(
      editorMouseListener : new SimpleMouseListener(
        // add a new element when clicking on the graph
        clicked : addNewElement
      )
    ),
    compilerConf : compilerConfiguration,
    displayCompileErrors : logException
  )
  
  /** Main frame of the program. */
  mainFrame = frame(title : 'Subrosa model', pack : true, defaultCloseOperation : WC.EXIT_ON_CLOSE) {
    menuBar() {
      menu(text : 'File') {
        menuItem(action : newDocumentAction)
        menuItem(action : openDocumentAction)
        menuItem(action : saveDocumentAction)
        menuItem(action : saveAsDocumentAction)
      }
      menu(text : 'Edit') {
        menuItem(action : genEditorAction)
        menuItem(action : startGeneratorAction)
      }
      menu(text : 'Analyze') {
        menuItem(action : debugAction)
        menuItem(action : graphAction)
      }
      menu(text : 'Options')
      menu(text : 'Help')
    }
    borderLayout()
    panel(constraints : BL.CENTER) {
      borderLayout()
      splitPane(orientation : JSplitPane.VERTICAL_SPLIT, constraints : BL.CENTER) {
        widget(documentTabs, constraints : 'top')
        tabbedPane(constraints : 'bottom') {
          infoTextArea('Log', logTextArea)
          infoTextArea('Errors', errorsTextArea)
        }
      }
    }
    toolBar(constraints : BL.EAST, orientation : JToolBar.VERTICAL) {
      button(action : action(
        name : 'cursor',
        closure : { state.lastElementToCreate = null }
      ))
      configuration.elements().each() {
        final def eName = it.key
        button(action : action(
          name : eName,
          closure : { state.lastElementToCreate = eName }
        ))
      }
    }
    widget(statusLabel, constraints : BL.SOUTH)
  }
  mainFrame.show()
}

 