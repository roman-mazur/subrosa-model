package org.mazur.subrosa.model.elements

import org.mazur.subrosa.gui.graph.ElementView
import org.mazur.subrosa.model.AbstractModelElement
import org.mazur.subrosa.model.ModelValueimport org.apache.log4j.Logger
import org.mazur.subrosa.gui.graph.cells.FuncCell

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class FunctionElement extends CompilationElement {
  private static final long serialVersionUID = -6145375989893458994L;
  
  /** Logger. */
  private static Logger log = Logger.getLogger(FunctionElement.class)
  
  /** Code. */
  transient String code
  
  private FuncCell view = new FuncCell(this)
  
  public ElementView getView() { return view }
  
  public String getLabel() { return "Func" }
  
  public boolean validateConnection(AbstractModelElement source) { return true }
  
  public ModelValue calculate(final List values) {
    if (log.debugEnabled) { log.debug "F inputs: $values" }
    script.binding['inputs'] = values
    def res = script.run() as ModelValue
    if (log.debugEnabled) { log.debug "F result: $res" }
    return res
  }
}
