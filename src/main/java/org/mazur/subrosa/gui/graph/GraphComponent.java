package org.mazur.subrosa.gui.graph;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.apache.log4j.Logger;
import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.PortView;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.Interface;

/**
 * Facilitates dealing with the jGraph component.
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class GraphComponent extends JGraph {
  private static final long serialVersionUID = 7695315624674279265L;

  /** Logger. */
  private static final Logger LOG = Logger.getLogger(GraphComponent.class);
  
  public GraphComponent() {
    super(new DefaultGraphModel());
    getGraphLayoutCache().setFactory(new CellViewsFactory());
    setMarqueeHandler(new MarqeeHandler());
    setPortsVisible(true);
  }
  
  public void addElementView(final AbstractModelElement element, final Point point) {
    ElementView view = element.getView();
    view.setPosition(point);
    getGraphLayoutCache().insert(view);
  }
  
  private JPopupMenu createPopupMenu(final Point2D point, final Object cell) {
    JPopupMenu menu = new JPopupMenu();
    return menu;
  }
  
  private void connect(final AbstractModelElement source, final AbstractModelElement target) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Connect cells " + source.getClass() + " -> " + target.getClass());
    }
    try {
      Interface i = target.connect(source); 
      if (i == null) { 
        repaint(); 
      } else {
        LOG.debug("Connected. Draw it.");
        getGraphLayoutCache().insert(i.getView());
      }
    } catch (EditException e) {
      LOG.error("Edit exception.", e);
      JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      repaint();
    }
  }
  
  /**
   * For D&D.
   * @author Roman Mazur (mailto: mazur.roman@gmail.com)
   */
  private class MarqeeHandler extends BasicMarqueeHandler {
    /** Highlight. */
    private JComponent highlight;
    /** Ports. */
    private PortView port, startPort;
    /** Points. */
    private Point2D current, startPoint;
    
    MarqeeHandler() {
      highlight = new JPanel();
      highlight.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
      highlight.setVisible(false);
      highlight.setOpaque(false);
    }
    
    private PortView getSourcePortAt(final Point2D point) {
      // Disable jumping
      GraphComponent.this.setJumpToDefaultPort(false);
      PortView result;
      try {
        // Find a Port View in Model Coordinates and Remember
        result = GraphComponent.this.getPortViewAt(point.getX(), point.getY());
      } finally {
        GraphComponent.this.setJumpToDefaultPort(true);
      }
      return result;
    }
    private PortView getTargetPortAt(final Point2D point) {
      // Find a Port View in Model Coordinates and Remember
      return GraphComponent.this.getPortViewAt(point.getX(), point.getY());
    }
    
    /**
     * Draw A Line From Start to Current Point.
     */
    private void drawConnectorLine(final Graphics g) {
      if (startPort != null && startPoint != null && current != null) {
        g.drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) current.getX(), (int) current.getY());
      }
    }

    /**
     * Use the Preview Flag to Draw a Highlighted Port.
     */
    private void paintPort(final Graphics g) {
      if (port != null) {
        boolean o = (GraphConstants.getOffset(port.getAllAttributes()) != null);
        // use parent bounds for float port
        Rectangle2D r = (o) ? port.getBounds() : port.getParentView().getBounds();
        // Scale from Model to Screen
        r = GraphComponent.this.toScreen((Rectangle2D) r.clone());
        // Add Space For the Highlight Border
        r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r.getHeight() + 6);
        // Paint Port in Preview (=Highlight) Mode
        GraphComponent.this.getUI().paintCell(g, port, r, true);
      }
    }

    /**
     * Highlights the given cell view or removes the highlight if
     * no cell view is specified.
     */
    private void highlight(final CellView cellView) {
      if (cellView != null) {
        highlight.setBounds(getHighlightBounds(cellView));
        if (highlight.getParent() == null) {
          GraphComponent.this.add(highlight);
          highlight.setVisible(true);
        }
      } else {
        if (highlight.getParent() != null) {
          highlight.setVisible(false);
          highlight.getParent().remove(highlight);
        }
      }
    }

    /**
     * @return the bounds to be used to highlight the given cell view
     */
    private Rectangle getHighlightBounds(final CellView cellView) {
      boolean offset = (GraphConstants.getOffset(cellView.getAllAttributes()) != null);
      Rectangle2D r = (offset) ? cellView.getBounds() : cellView.getParentView().getBounds();
      r = GraphComponent.this.toScreen((Rectangle2D) r.clone());
      int s = 3;
      return new Rectangle((int)(r.getX() - s), (int)(r.getY() - s), (int)(r.getWidth() + 2 * s), (int)(r.getHeight() + 2 * s));
    }

    /**
     * Use Xor-Mode on Graphics to Paint Connector
     * @param fg foreground color
     * @param bg background color
     * @param g graphics instance
     */
    private void paintConnector(Color fg, Color bg, Graphics g) {
      if (GraphComponent.this.isXorEnabled()) {
        g.setColor(fg);
        g.setXORMode(bg);
        paintPort(GraphComponent.this.getGraphics());
        drawConnectorLine(g);
      } else {
        Rectangle dirty = new Rectangle((int)startPoint.getX(), (int)startPoint.getY(), 1, 1);
        if (current != null) { dirty.add(current); }
        dirty.grow(1, 1);
        GraphComponent.this.repaint(dirty);
        highlight(port);
      }
    }

    @Override
    public boolean isForceMarqueeEvent(final MouseEvent e) {
      if (e.isShiftDown()) { return false; }
      // If Right Mouse Button we want to Display the PopupMenu
      if (SwingUtilities.isRightMouseButton(e)) { return true; }
      // Find and Remember Port
      port = getSourcePortAt(e.getPoint());
      // If Port Found and in ConnectMode (=Ports Visible)
      if (port != null && GraphComponent.this.isPortsVisible()) { return true; }
      return super.isForceMarqueeEvent(e);
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
      // If Right Mouse Button
      if (SwingUtilities.isRightMouseButton(e)) {
        // Find Cell in Model Coordinates
        Object cell = GraphComponent.this.getFirstCellForLocation(e.getX(), e.getY());
        // Create PopupMenu for the Cell
        JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
        // Display PopupMenu
        menu.show(GraphComponent.this, e.getX(), e.getY());
        // Else if in ConnectMode and Remembered Port is Valid
      } else if (port != null && GraphComponent.this.isPortsVisible()) {
        // Remember Start Location
        startPoint = GraphComponent.this.toScreen(port.getLocation());
        // Remember First Port
        startPort = port;
      } else {
        super.mousePressed(e);
      }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
      // If remembered Start Point is Valid
      if (startPoint != null) {
        // Fetch Graphics from Graph
        Graphics g = GraphComponent.this.getGraphics();
        // Reset Remembered Port
        PortView newPort = getTargetPortAt(e.getPoint());
        // Do not flicker (repaint only on real changes)
        if (newPort == null || newPort != port) {
          // Xor-Paint the old Connector (Hide old Connector)
          paintConnector(Color.black, GraphComponent.this.getBackground(), g);
          // If Port was found then Point to Port Location
          port = newPort;
          if (port != null) {
            current = GraphComponent.this.toScreen(port.getLocation());
          } else {
            current = GraphComponent.this.snap(e.getPoint());
          }
          // Xor-Paint the new Connector
          paintConnector(GraphComponent.this.getBackground(), Color.black, g);
        }
      }
      super.mouseDragged(e);
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
      highlight(null);
      if (e != null && port != null && startPort != null && startPort != port) {
        DefaultGraphCell sourcePort = (DefaultGraphCell)startPort.getCell(),
                         targetPort = (DefaultGraphCell)port.getCell();
        boolean canConnect = sourcePort.getParent() instanceof ElementView && targetPort.getParent() instanceof ElementView;
        if (canConnect) {
          GraphComponent.this.connect(
            ((ElementView)sourcePort.getParent()).getElement(), 
            ((ElementView)targetPort.getParent()).getElement()
          );
          e.consume();
        } else {
          GraphComponent.this.repaint();
        }
      } else {
        GraphComponent.this.repaint();
      }
      startPort = port = null;
      startPoint = current = null;
      super.mouseReleased(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
      if (e != null && getSourcePortAt(e.getPoint()) != null && GraphComponent.this.isPortsVisible()) {
        GraphComponent.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        e.consume();
      } else {
        super.mouseMoved(e);
      }
    }

    @Override
    public void paint(final JGraph graph, final Graphics g) {
      super.paint(graph, g);
      if (!graph.isXorEnabled()) {
        g.setColor(Color.black);
        drawConnectorLine(g);
      }
    }
  }
  
}
