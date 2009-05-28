package org.mazur.subrosa.gui

import org.apache.log4j.Loggerimport org.mazur.subrosa.model.ModelControllerimport groovy.swing.SwingBuilderimport java.awt.BorderLayout as BLimport org.mazur.subrosa.model.elements.CompilationElementimport org.codehaus.groovy.control.CompilerConfigurationimport org.codehaus.groovy.control.CompilationFailedExceptionimport org.mazur.subrosa.InterpreterExceptionimport org.mazur.subrosa.model.utils.ConstantModelValue

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class Generator {

  /** Logger. */
  private Logger log = Logger.getLogger(Generator.class)
  
  /** Controller. */
  ModelController controller
  
  /** Compiler conf. */
  CompilerConfiguration cconf
  
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
  /** 'Run' action. */
  private def runAction = swing.action(
    name : 'Run',
    closure : {
      log.info "Execute 'run' action"
      controller.generatorCode = codeArea.text
      grun()
    }
  )
  
  private def constValueClouse = { val, dim ->
    return new ConstantModelValue(val, dim)
  }
  
  private void extendFuncElement(final String name, String funcName, def func) {
    log.info "Extending $name by $funcName"
    Binding b = controller.compileBindings[name]
    if (!b) {
      b = new Binding() 
      controller.compileBindings[name] = b
    }
    b[funcName] = func
  }
  
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
        button(action : runAction)
      }
    }
  }
  
  public void grun() {
    controller.compileBindings.clear()
    log.info 'Generator is working'
    if (controller.generatorCode) {
      Binding gb = new Binding()
      gb['extend'] = this.&extendFuncElement
      GroovyShell shell = new GroovyShell(gb, cconf);
      try {
        shell.evaluate(controller.generatorCode)
      } catch (CompilationFailedException e) {
        throw new InterpreterException('Error in generator code.', e)
      }
    }
    controller.compileElements.each() {
      String name = it.key
      CompilationElement el = it.value
      extendFuncElement(name, 'constValue', constValueClouse)
    }
    log.info 'Generator has finished'
  }
}
