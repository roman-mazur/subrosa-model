package org.mazur.subrosa.gui.graph.editors;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.mazur.subrosa.gui.IconsFactory;

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
  
  /** Main panel. */
  private JPanel mainPanel = new JPanel();
  
  /** Apply button. */
  private JButton applyButton = new JButton(IconsFactory.getIcon("yes-icon"));
  
  /** Notes. */
  private JTextArea notesArea = new JTextArea(getNotesValue(), 5, 0);
  
  protected CommonEditorContainer() {
    LOG.debug("New editor container.");
    mainPanel.setLayout(new BorderLayout());
    JComponent topC = createTopComponent();
    mainPanel.add(BorderLayout.NORTH, topC);
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BorderLayout());
    bottomPanel.add(BorderLayout.NORTH, new JLabel(" Notes:"));
    JScrollPane scroll = new JScrollPane(notesArea);
    bottomPanel.add(BorderLayout.CENTER, scroll);
    mainPanel.add(BorderLayout.SOUTH, bottomPanel);
    
    applyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) { setNotesValue(notesArea.getText()); }
    });
  }
  
  /**
   * @return value for notes
   */
  protected abstract String getNotesValue();
  protected abstract void setNotesValue(final String value);

  protected abstract JComponent createTopComponent();
  
  protected JButton getApplyButton() { return applyButton; }
  
  public JPanel getMainPanel() { return mainPanel; }
  
}
