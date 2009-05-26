package org.mazur.subrosa.gui

import org.apache.log4j.Loggerimport org.mazur.subrosa.model.ModelControllerimport groovy.swing.SwingBuilderimport java.awt.BorderLayout as BL

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class Generator {

  /** Logger. */
  private Logger log = Logger.getLogger(Generator.class)
  
  /** Controller. */
  ModelController controller
  
  /** Generator frame. */
  private def gframe

  /** Code area. */
  private def codeArea
  
  /** Builder. */
  private SwingBuilder swing = new SwingBuilder()
  
  /** 'Ok' action. */
  private def okAction = swing.action(
    name : 'OK',
    closure : {
      log.info "Execute 'ok' action"
      controller.generatorCode = codeArea.text
      gframe.visible = false
    }
  )
  /** 'Cancel' action. */
  private def cancelAction = swing.action(
    name : 'Cancel',
    closure : {
      log.info "Execute 'cancel' action"
      gframe.visible = false
    }
  )
  
  public void showFrame() { gframe.visible = true }
  
  public void prepare() {
    gframe = swing.frame(title : 'Generator code', pack : true) {
      borderLayout()
      label(text : 'Enter the generator code here', constraints : BL.NORTH)
      scrollPane(constraints : BL.CENTER, preferredSize : [300, 200]) {
        codeArea = textArea(text : controller.generatorCode)
      }
      panel(constraints : BL.SOUTH) {
        button(action : okAction)
        button(action : cancelAction)
      }
    }
  }
  
}
