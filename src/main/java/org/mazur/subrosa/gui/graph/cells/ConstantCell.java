package org.mazur.subrosa.gui.graph.cells;

import java.awt.BorderLayout;
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
import org.mazur.subrosa.model.elements.ConstantElement;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ConstantCell extends ElementCell {
  private static final long serialVersionUID = 4792032463501292359L;
  /** Cell size. */
  private static final Dimension SIZE = new Dimension(45, 25);
  
  private transient EditorContainer editor = new EditorContainer();
  
  @Override
  protected Dimension getSize() { return SIZE; }

  public ConstantCell(final AbstractModelElement element) {
    super(element);
    GraphConstants.setIcon(getAttributes(), getIcon());
    GraphConstants.setGradientColor(getAttributes(), Color.GRAY);
  }

  @Override
  public JComponent getEditorComponent() {
    if (editor == null) { editor = new EditorContainer(); }
    return editor.getMainPanel(); 
  }

  private final class EditorContainer extends CommonEditorContainer {
    private JSpinner valueSpinner, dimensionSpinner;
    EditorContainer() {
      super(getElement());
      final ConstantElement ce = (ConstantElement)getElement();
      valueSpinner.setValue(ce.getValue());
      dimensionSpinner.setValue(ce.getDimension());
      getApplyButton().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ce.setValue((Integer)valueSpinner.getValue());
          ce.setDimension((Integer)dimensionSpinner.getValue());
        }
      });
    }

    @Override
    protected JComponent createTopComponent() { 
      valueSpinner = new JSpinner();
      dimensionSpinner = new JSpinner();
      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BorderLayout());
      JPanel tmp = new JPanel();
      tmp.add(new JLabel("Value:"));
      tmp.add(valueSpinner);
      JPanel leftP = new JPanel();
      leftP.setLayout(new BorderLayout());
      leftP.add(BorderLayout.NORTH, tmp);
      tmp = new JPanel();
      tmp.add(new JLabel("Dimension:"));
      tmp.add(dimensionSpinner);
      leftP.add(BorderLayout.SOUTH, tmp);
      topPanel.add(BorderLayout.CENTER, leftP);
      topPanel.add(BorderLayout.EAST, getApplyButton());
      return topPanel;
    }
    
  }
}
