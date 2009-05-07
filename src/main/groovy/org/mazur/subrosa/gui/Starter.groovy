package org.mazur.subrosa.gui

import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WCimport java.awt.BorderLayout as BLimport org.apache.log4j.Loggerimport javax.swing.JToolBarimport javax.swing.JSplitPaneimport java.awt.Fontimport java.awt.Colorimport org.mazur.subrosa.model.ModelControllerimport org.mazur.subrosa.model.ElementsFactory
/**
 * Starter script.
 * @author Roman Mazur (mailto:mazur.roman@gmail.com)
 */

def log = Logger.getLogger(Starter.class)

/** Some colors. */
Color INFO_COLOR = new Color(40, 165, 90), ERROR_COLOR = Color.RED

/** Handlers for GUI actions. */
def actionsHandlers = [:]

/** Main state. */
MainFrameState state = new MainFrameState()

/** Controller. */
ModelController controller = new ModelController()

/** Elements factory. */
ElementsFactory eFactory = new ElementsFactory()

Config configuration = new Config()

SwingBuilder.build() {
  
  // ================================== COMPONENTS ==================================
  
  /** Tabs for opened documents. */
  def documentTabs = tabbedPane()
  
  /** For logs. */
  def logTextArea = textArea()
  /** For errors. */
  def errorsTextArea = textArea()
  
  /** Label for status. */
  def statusLabel = label()
  
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
  
  /** Add a new element to the model. */
  def addNewElement = userAction(
    closure : {
      def e = state.lastElementToCreate
      if (!e) { return }
      infoStatus("Element $e was added.")
    }
  )
  
  // ===================================== MAIN =====================================
  
  /** Main frame of the program. */
  def f = frame(title : 'Subrosa model', pack : true, defaultCloseOperation : WC.EXIT_ON_CLOSE) {
    menuBar() {
      menu(text : 'File') {
        menuItem(action : newDocumentAction)
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
      configuration.elements().each() {
        button(text : it.key)
      }
    }
    widget(statusLabel, constraints : BL.SOUTH)
  }
  f.show()
}

 