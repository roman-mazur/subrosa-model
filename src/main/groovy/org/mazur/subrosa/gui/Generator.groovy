package org.mazur.subrosa.gui

import org.apache.log4j.Loggerimport org.mazur.subrosa.model.ModelControllerimport groovy.swing.SwingBuilderimport java.awt.BorderLayout as BLimport org.mazur.subrosa.model.elements.CompilationElementimport org.codehaus.groovy.control.CompilerConfigurationimport org.codehaus.groovy.control.CompilationFailedExceptionimport org.mazur.subrosa.InterpreterExceptionimport org.mazur.subrosa.model.utils.ConstantModelValue

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class Generator {

  /** Logger. */
  protected Logger log = Logger.getLogger(getClass())
  
  /** Controller. */
  ModelController controller
  
  /** Compiler conf. */
  CompilerConfiguration cconf
  
  /** Base binding. */
  Binding baseBinding
  
  /** Main frame state. */
  MainFrameState state
  
  /** Generator frame. */
  protected def gframe

  /** Code area. */
  protected def codeArea
  
  /** Builder. */
  protected SwingBuilder swing = new SwingBuilder()
  
  /** 'Ok' action. */
  protected def okAction = swing.action(
    name : 'OK',
    closure : {
      log.info "Execute 'ok' action"
      setCode(codeArea.text)
      gframe.visible = false
    }
  )
  /** 'Cancel' action. */
  protected def cancelAction = swing.action(
    name : 'Cancel',
    closure : {
      log.info "Execute 'cancel' action"
      gframe.visible = false
    }
  )
  /** 'Run' action. */
  protected def runAction = swing.action(
    name : 'Run',
    closure : {
      log.info "Execute 'run' action"
      setCode(codeArea.text)
      runCode()
    }
  )
  
  protected def constValueClouse = { val, dim ->
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
  
  protected String getCode() { return controller.generatorCode }
  protected String setCode(final String code) { return controller.generatorCode = code }
  protected String getTitle() { return 'Generator code' }
  
  public void prepare() {
    gframe = swing.frame(title : getTitle(), pack : true) {
      borderLayout()
      label(text : 'Enter the code here', constraints : BL.NORTH)
      scrollPane(constraints : BL.CENTER, preferredSize : [400, 250]) {
        codeArea = textArea(text : getCode(), font : Config.instance.codeFont)
      }
      panel(constraints : BL.SOUTH) {
        button(action : okAction)
        button(action : cancelAction)
        button(action : runAction)
      }
    }
  }
  
  protected void prepareBindings() {
    if (!baseBinding) { baseBinding = new Binding() }
    baseBinding['extend'] = this.&extendFuncElement
    baseBinding['log'] = {msg -> state.logMessage(msg)}
  }
  
  protected extendAllElements(def extMap) {
    extMap.put('constValue', constValueClouse)
    extMap.put('log', {msg -> state.logMessage(msg)})
    extMap.each() { extEntry ->
      controller.compileElements.each() {
        String name = it.key
        CompilationElement el = it.value
        extendFuncElement(name, extEntry.key, extEntry.value)
      }
    }
  }
  
  public void runCode() {
    controller.compileBindings.clear()
    log.info 'Generator is working'
    if (controller.generatorCode) {
      prepareBindings()
      GroovyShell shell = new GroovyShell(baseBinding, cconf);
      try {
        shell.evaluate(controller.generatorCode)
      } catch (CompilationFailedException e) {
        throw new InterpreterException('Error in generator code.', e)
      }
    }
    extendAllElements([:])
    log.info 'Generator has finished'
  }
}
