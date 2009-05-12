package org.mazur.subrosa.gui.graph.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCellEditor;
import org.jgraph.graph.GraphCellEditor;
import org.mazur.subrosa.gui.graph.ElementCell;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class BusinessElementEditor extends DefaultGraphCellEditor {
  private static final long serialVersionUID = 3097475659966407090L;

  /** Logger. */
  private static final Logger LOG = Logger.getLogger(BusinessElementEditor.class);
  
  /** Cell instance. */
  private ElementCell cellInstance;
  
  public BusinessElementEditor(final ElementCell cellInstance) {
    LOG.debug("new BusinessElementEditor");
    this.cellInstance = cellInstance;
  }
  
  @Override
  protected GraphCellEditor createGraphCellEditor() {
    LOG.debug("new RealElementEditor");
    return new RealElementEditor(); 
  }
  
  /**
   * @author Roman Mazur (mailto: mazur.roman@gmail.com)
   */
  private class RealElementEditor extends AbstractCellEditor implements GraphCellEditor {
    private static final long serialVersionUID = -1213038581894592378L;

    @Override
    public Component getGraphCellEditorComponent(final JGraph graph, final Object value,
        final boolean selected) {
      JComponent component = cellInstance.getEditorComponent();
      if (cellInstance.isEditorInsideCell()) {
        Rectangle2D tmp = graph.getGraphLayoutCache().getMapping(value, false).getBounds();
        component.setPreferredSize(new Dimension((int)tmp.getWidth(), (int)tmp.getHeight()));
      }
      Border b = UIManager.getBorder("Tree.editorBorder");
      component.setBorder(b);
      return component;
    }

    @Override
    public Object getCellEditorValue() { return cellInstance.getValue(); }
  }
}
