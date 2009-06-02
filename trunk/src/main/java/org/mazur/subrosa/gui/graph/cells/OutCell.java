package org.mazur.subrosa.gui.graph.cells;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import org.jgraph.graph.GraphConstants;
import org.mazur.subrosa.gui.graph.ElementCell;
import org.mazur.subrosa.gui.graph.editors.CommonEditorContainer;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.elements.OutElement;

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class OutCell extends ElementCell {
  private static final long serialVersionUID = 5953634921139253971L;
  
  /** Cell size. */
  private static final Dimension SIZE = new Dimension(30, 30);
  
  /** Editor. */
  private transient EditorContainer editor;
  
  public OutCell(final AbstractModelElement element) {
    super(element);
    GraphConstants.setIcon(getAttributes(), getIcon());
    GraphConstants.setGradientColor(getAttributes(), Color.GREEN);
  }

  @Override
  public JComponent getEditorComponent() {
    if (editor == null) { editor = new EditorContainer(getElement()); }
    return editor.getMainPanel();
  }

  @Override
  protected Dimension getSize() { return SIZE; }

  private static class EditorContainer extends CommonEditorContainer {
    private JCheckBox mainCb;
    private JSpinner numberSpinner;
    
    protected EditorContainer(final AbstractModelElement el) {
      super(el);
      final OutElement oe = (OutElement)el;
      numberSpinner.setValue(oe.getNumber());
      getApplyButton().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          oe.setIncludeInStats(mainCb.isSelected());
          oe.setNumber((Number)numberSpinner.getValue());
        }
      });
    }

    @Override
    protected JComponent createTopComponent() {
      JPanel result = new JPanel();
      result.setLayout(new BorderLayout());
      mainCb = new JCheckBox("include to stats");
      mainCb.setSelected(((OutElement)getElement()).isIncludeInStats());
      numberSpinner = new JSpinner();
      JPanel p = new JPanel();
      p.add(new JLabel("number:"));
      p.add(numberSpinner);
      Box c = Box.createVerticalBox();
      c.add(mainCb);
      c.add(p);
      result.add(BorderLayout.CENTER, c);
      result.add(BorderLayout.EAST, getApplyButton());
      return result;
    }
    
  }
  
}
