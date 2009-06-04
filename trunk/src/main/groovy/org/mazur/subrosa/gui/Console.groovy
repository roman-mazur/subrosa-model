/*
 * $Id$
 */
package org.mazur.subrosa.gui

import org.codehaus.groovy.control.CompilationFailedExceptionimport org.mazur.subrosa.InterpreterExceptionimport org.mazur.subrosa.Interpreter
/**
 * @author Roman Mazur
 *
 */
public class Console extends Generator {

  /** Interpreter. */
  private Interpreter interpreter
  private def inputs, outputs
  
  protected String getCode() { return this.controller.experimentsCode }
  protected String setCode(final String code) { return controller.experimentsCode = code }
  protected String getTitle() { return 'Console' }

  protected void prepareBindings() {
    super.prepareBindings()
    baseBinding['startGenerator'] = { state.startGenerator() }
    baseBinding['calculate'] = {
      if (!interpreter) { 
        interpreter = new Interpreter(controller : this.controller)
        interpreter.compile(cconf)
      }
      interpreter.calculate(cconf) 
    }
    def elements = { name ->
      if (!this."$name") { this."$name" = controller."$name".sort() { it.number } }
      this."$name"
    }
    ['inputs', 'outputs'].each() { bName -> baseBinding[bName] = { elements(bName) } }
    ['extend'].each() {
      def c = baseBinding[it]
      baseBinding[it] = { 
        c() 
        interpreter = null 
      }
    }
  }
  
  void runCode() {
    log.info 'Console code start'
    if (getCode()) {
      prepareBindings()
      GroovyShell shell = new GroovyShell(baseBinding, cconf);
      try {
        shell.evaluate(getCode())
      } catch (CompilationFailedException e) {
        throw new InterpreterException('Error in generator code.', e)
      }
    }
  }
}
