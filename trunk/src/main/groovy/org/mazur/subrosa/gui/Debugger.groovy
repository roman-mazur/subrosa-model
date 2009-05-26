package org.mazur.subrosa.gui

import org.mazur.subrosa.model.ModelController
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Debugger {

  /** Logger. */
  private Logger log = Logger.getLogger(Debugger.class)
  
  /** Controller. */
  ModelController controller
  
  /** Interpreter. */
  private Interpreter interpreter
  
  /** Elements to debug. */
  private def debugElements
  
  /** Debugger frame. */
  private def dFrame

  /** Info field. */
  private def infoField
  
  void prepare() {
    this.interpreter = new Interpreter(controller : this.controller)
    this.interpreter.init()
    def de = controller.debugElements
    debugElements = new HashMap(de.size())
    int s = de.size()
    int r = (int)Math.floor(Math.sqrt(s))
    log.debug "rows=$r"
    int c = s.div(r)
    if (!(s % r)) { c++ }
    log.debug "columns=$s"
    SwingBuilder.build() {
      def stepAction = action(closure : {
        log.debug "Next step start"
        interpreter.next()
        debugElements.each() { it.value.valueLabel.text = it.key.currentValue?.toString() }
        infoField.text = interpreter.nextStepDescription
        log.debug "Next step finish"
      })
      dFrame = frame(title : 'Debugger', pack : true) {
        borderLayout()
        toolBar(constraints : BL.NORTH) {
          button(action : stepAction, icon : IconsFactory.getIcon('debug-icon'))
        }
        panel(constraints : BL.CENTER) {
          gridLayout(rows : r, columns : c)
          de.each() { def el ->
            ElementContainer ec = new ElementContainer(valueLabel : label(text : el.currentValue?.toString()))
            debugElements[el] = ec
            vbox() {
              lineBorder(color : Color.GREEN, thickness : 3, roundedCorners : true);
              label(text : el.label)
              label(text : el.notes)
              widget(ec.valueLabel)
            }
          }
        }
        panel(constraints : BL.SOUTH) {
          infoField = label(text : "${interpreter.nextStepDescription}")
        }
      }
    }
  }
  
  void showFrame() { dFrame.visible = true }
  
}

class ElementContainer {
  def valueLabel
}