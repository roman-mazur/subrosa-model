package org.mazur.subrosa.gui.graph.cells;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import org.jgraph.graph.GraphConstants;
import org.mazur.subrosa.gui.graph.ElementCell;
import org.mazur.subrosa.gui.graph.editors.CommonEditorContainer;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.elements.XorElement;

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class XorCell extends ElementCell {
  private static final long serialVersionUID = -193326977245601334L;

  /** Cell size. */
  private static final Dimension SIZE = new Dimension(30, 30);
  
  /** Editor. */
  private EditorContainer editor = new EditorContainer();
  
  public XorCell(final AbstractModelElement element) {
    super(element);
    GraphConstants.setIcon(getAttributes(), getIcon());
    GraphConstants.setGradientColor(getAttributes(), Color.GRAY);
  }
  
  @Override
  protected Dimension getSize() { return SIZE; }
  
  @Override
  public JComponent getEditorComponent() { return editor.getMainPanel(); }
  
  /**
   * Cell editor components.
   * @author Roman Mazur (mailto: mazur.roman@gmail.com)
   */
  private final class EditorContainer extends CommonEditorContainer {
    /** Inputs count. */
    private JSpinner inputsCountSpinner;
    
    /** Constructor. */
    EditorContainer() {
      super();
      final XorElement xe = (XorElement)getElement();
      inputsCountSpinner.setValue(xe.getMaxInputCount());
      getApplyButton().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          xe.setMaxInputCount((Integer)inputsCountSpinner.getValue());
        }
      });
    }

    @Override
    protected String getNotesValue() { return getElement().getNotes(); }
    @Override
    protected void setNotesValue(final String value) {
      getElement().setNotes(value);
    }
    @Override
    protected JComponent createTopComponent() {
      inputsCountSpinner = new JSpinner();
      JPanel topPanel = new JPanel();
      topPanel.add(new JLabel("Max inputs count:"));
      topPanel.add(inputsCountSpinner);
      topPanel.add(getApplyButton());
      return topPanel;
    }
  }
}
