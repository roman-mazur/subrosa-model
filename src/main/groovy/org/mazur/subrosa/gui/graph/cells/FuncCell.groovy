package org.mazur.subrosa.gui.graph.cells

import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import groovy.swing.SwingBuilderimport java.awt.BorderLayout as BL
import javax.swing.JComponent
import javax.swing.JTextArea

import org.mazur.subrosa.gui.graph.ElementCell
import org.mazur.subrosa.gui.graph.editors.CommonEditorContainer
import org.mazur.subrosa.gui.listeners.SimpleActionListener
import org.mazur.subrosa.model.AbstractModelElement
import org.mazur.subrosa.model.elements.FunctionElement

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class FuncCell extends ElementCell {
  private static final long serialVersionUID = -284985315549259958L

  /** Cell size. */
  private static final Dimension SIZE = new Dimension(30, 30)
  
  /** Editor. */
  private transient EditorContainer editor
  
  public FuncCell(final AbstractModelElement element) {
    super(element)
    editor = new EditorContainer(element as FunctionElement)
  }

  @Override
  public JComponent getEditorComponent() { return editor.mainPanel }

  @Override
  protected Dimension getSize() { return SIZE }

}

private class EditorContainer extends CommonEditorContainer {
  private def codeArea
  protected EditorContainer(final FunctionElement element) {
    super(element)
    getApplyButton().addActionListener(new SimpleActionListener(closure : {
      element.code = codeArea.text
    }))
  }

  @Override
  protected JComponent createTopComponent() {
    def result
    SwingBuilder.build() {
      result = panel() {
        panel() {
          borderLayout()
          label(text : ' Code:', constraints : BL.NORTH)
          scrollPane(constraints : BL.CENTER) {
            codeArea = textArea()
          }
        }
        widget(applyButton)
      }
    }
    return result
  }
}
