package org.mazur.subrosa.gui.graph.editors;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCellEditor;
import org.mazur.subrosa.gui.IconsFactory;
import org.mazur.subrosa.gui.graph.InterfaceView;
import org.mazur.subrosa.model.Interface;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class InterfaceEditor extends DefaultGraphCellEditor {
  private static final long serialVersionUID = -354262263618228546L;
  
  /** Logger. */
  private static final Logger LOG = Logger.getLogger(InterfaceEditor.class);
  
  private Interface element;
  
  private JSpinner sourceFrom = new JSpinner(), sourceTo = new JSpinner(), 
                   targetFrom = new JSpinner(), targetTo = new JSpinner();
  private JButton btn = new JButton(IconsFactory.getIcon("yes-icon"));
  
  public InterfaceEditor(final InterfaceView view) {
    element = view.getModelElement();
  }
  
  @Override
  public Component getGraphCellEditorComponent(JGraph arg0, Object arg1,
      boolean arg2) {
    LOG.debug("call editor component");
    JPanel main = new JPanel();
    main.setLayout(new BorderLayout());
    JPanel top = new JPanel();
    top.add(new JLabel("Source:"));
    top.add(sourceFrom);
    top.add(new JLabel("-"));
    top.add(sourceTo);
    JPanel left = new JPanel();
    left.setLayout(new BorderLayout());
    left.add(BorderLayout.NORTH, top);
    JPanel bottom = new JPanel();
    bottom.add(new JLabel("Target:"));
    bottom.add(targetFrom);
    bottom.add(new JLabel("-"));
    bottom.add(targetTo);
    left.add(BorderLayout.SOUTH, bottom);
    main.add(BorderLayout.WEST, left);
    main.add(BorderLayout.EAST, btn);
    return main;
  }
}
