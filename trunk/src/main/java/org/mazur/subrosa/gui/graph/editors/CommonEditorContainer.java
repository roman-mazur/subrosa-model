package org.mazur.subrosa.gui.graph.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.mazur.subrosa.gui.IconsFactory;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.Interface;
import org.mazur.subrosa.model.utils.Range;

/**
 * Common editor container.
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public abstract class CommonEditorContainer {

  /** Logger. */
  private static final Logger LOG = Logger.getLogger(CommonEditorContainer.class);
  
  private AbstractModelElement element;
  
  /** Main panel. */
  private JPanel mainPanel = new JPanel(),
                 connectionsPanel = new JPanel();
  
  /** Apply button. */
  private JButton applyButton = new JButton(IconsFactory.getIcon("yes-icon"));
  
  /** Connections button. */
  private JButton connectionsButton = new JButton();
  
  /** Last size. */
  private Dimension lastMainPanelSize = null;
  
  /** Notes. */
  private JTextArea notesArea; 

  private JScrollPane connectionsScroll;
  
  /** Spinners. */
  private List<SpinnersPair> spinners = new LinkedList<SpinnersPair>();
  
  protected CommonEditorContainer(final AbstractModelElement element) {
    LOG.debug("New editor container.");
    this.element = element;
    this.notesArea = new JTextArea(element.getNotes(), 5, 0);
    JPanel commonPanel = new JPanel();
    commonPanel.setLayout(new BorderLayout());
    JComponent topC = createTopComponent();
    commonPanel.add(BorderLayout.CENTER, topC);
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BorderLayout());
    bottomPanel.add(BorderLayout.NORTH, new JLabel(" Notes:"));
    JScrollPane scroll = new JScrollPane(notesArea);
    bottomPanel.add(BorderLayout.CENTER, scroll);
    bottomPanel.add(BorderLayout.SOUTH, connectionsButton);
    commonPanel.add(BorderLayout.SOUTH, bottomPanel);

    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(BorderLayout.CENTER, commonPanel);
    connectionsScroll = new JScrollPane(connectionsPanel);
    mainPanel.add(BorderLayout.EAST, connectionsScroll);

    connectionsPanel.setLayout(new BorderLayout());
    
    applyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) { 
        element.setNotes(notesArea.getText());
        for (SpinnersPair sp : spinners) {
          Range r = new Range((Integer)sp.fromSpinner.getValue(), (Integer)sp.toSpinner.getValue());
          if (sp.inputs) {
            sp.i.setTargetRange(r);
          } else {
            sp.i.setSourceRange(r);
          }
        }
      }
    });
    connectionsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        connectionsButton.setText(connectionsScroll.isVisible() ? "Connections <<" : "Connections >>");
        connectionsScroll.setVisible(!connectionsScroll.isVisible()); 
      }
    });
  }
  
  protected abstract JComponent createTopComponent();
  
  protected JButton getApplyButton() { return applyButton; }
  
  private void processInterfaces(final List<Interface> interfaces, final JPanel panel, final boolean inputs) {
    for (Interface i : interfaces) {
      LOG.debug("interface: " + i);
      SpinnersPair sp = new SpinnersPair();
      sp.fromSpinner = new JSpinner();
      sp.toSpinner = new JSpinner();
      sp.i = i;
      sp.inputs = inputs;
      sp.fromSpinner.setValue((inputs ? i.getTargetRange() : i.getSourceRange()).getMin());
      sp.toSpinner.setValue((inputs ? i.getTargetRange() : i.getSourceRange()).getMax());
      spinners.add(sp);
      JPanel captions = new JPanel();
      captions.setLayout(new GridLayout(1, 3));
      captions.add(new JLabel("From"));
      captions.add(new JLabel(" "));
      captions.add(new JLabel("To"));
      JPanel sPanel = new JPanel();
      sPanel.setLayout(new GridLayout(1, 3));
      sPanel.add(sp.fromSpinner);
      sPanel.add(new JLabel(" "));
      sPanel.add(sp.toSpinner);
      JPanel p = new JPanel();
      p.setLayout(new BorderLayout());
      p.add(BorderLayout.NORTH, captions);
      p.add(BorderLayout.CENTER, sPanel);
      JPanel wrapper = new JPanel();
      wrapper.setLayout(new BorderLayout());
      wrapper.add(BorderLayout.CENTER, p);
      JLabel l = new JLabel(i.getSource().getLabel() + " >> " + i.getTarget().getLabel());
      l.setFont(new Font(Font.MONOSPACED, 0, 12));
      wrapper.add(BorderLayout.NORTH, l);
      wrapper.setBorder(new LineBorder(Color.GRAY, 1, true));
      panel.add(wrapper);
    }
  }
  
  private void buildConnectionsPanel(final JPanel connectionsPanel) {
    spinners.clear();
    connectionsPanel.removeAll();
    if (!element.getInputs().isEmpty()) {
      JPanel wrapper = new JPanel();
      wrapper.setLayout(new BorderLayout());
      wrapper.add(BorderLayout.NORTH, new JLabel("Inputs"));
      JPanel iPanel = new JPanel();
      iPanel.setLayout(new GridLayout(element.getInputs().size(), 1));
      processInterfaces(element.getInputs(), iPanel, true);
      wrapper.add(BorderLayout.CENTER, iPanel);
      connectionsPanel.add(BorderLayout.NORTH, wrapper);
    }
    if (!element.getOutputs().isEmpty()) {
      JPanel wrapper = new JPanel();
      wrapper.setLayout(new BorderLayout());
      wrapper.add(BorderLayout.NORTH, new JLabel("Outputs"));
      JPanel iPanel = new JPanel();
      iPanel.setLayout(new GridLayout(element.getOutputs().size(), 1));
      processInterfaces(element.getOutputs(), iPanel, false);
      wrapper.add(BorderLayout.CENTER, iPanel);
      connectionsPanel.add(BorderLayout.SOUTH, wrapper);
    }
  }
  
  public JPanel getMainPanel() {
    LOG.debug("Redraw");
    connectionsButton.setVisible(element.hasConnections());
    connectionsButton.setText("Connections <<");
    buildConnectionsPanel(connectionsPanel);
    connectionsScroll.setVisible(false);
    LOG.debug("Size: " + mainPanel.getPreferredSize());
    if (connectionsButton.isVisible() && lastMainPanelSize == null) {
      lastMainPanelSize = mainPanel.getPreferredSize();
      mainPanel.setPreferredSize(new Dimension(lastMainPanelSize.width + 150, lastMainPanelSize.height + 50));
    }
    return mainPanel; 
  }

  protected AbstractModelElement getElement() { return element; }
  
  private static class SpinnersPair {
    private JSpinner fromSpinner, toSpinner;
    private Interface i;
    private boolean inputs;
  }
}
