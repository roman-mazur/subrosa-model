package org.mazur.subrosa.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultPort;
import org.mazur.subrosa.gui.graph.ElementCell;
import org.mazur.subrosa.gui.graph.InterfaceView;
import org.mazur.subrosa.model.utils.Range;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Interface implements Serializable, ModelValue {

  private static final long serialVersionUID = -143376295176661749L;
  
  /** Logger. */
  private static final Logger LOG = Logger.getLogger(Interface.class);
  
  private static final Map<Integer, ModelValue> NULL_VALUES_CACHE = new TreeMap<Integer, ModelValue>();

  private AbstractModelElement source, target;
  private InterfaceView view;
  private Range sourceRange, targetRange;
  
  /**
   * @return the source
   */
  public AbstractModelElement getSource() {
    return source;
  }

  /**
   * @return the target
   */
  public AbstractModelElement getTarget() {
    return target;
  }

  /**
   * @return the sourceRange
   */
  public Range getSourceRange() {
    return sourceRange;
  }

  /**
   * @return the targetRange
   */
  public Range getTargetRange() {
    return targetRange;
  }

  /**
   * @param source the source to set
   */
  public void setSource(AbstractModelElement source) {
    this.source = source;
  }

  /**
   * @param target the target to set
   */
  public void setTarget(AbstractModelElement target) {
    this.target = target;
  }

  /**
   * @param sourceRange the sourceRange to set
   */
  public void setSourceRange(Range sourceRange) {
    this.sourceRange = sourceRange;
  }

  /**
   * @param targetRange the targetRange to set
   */
  public void setTargetRange(Range targetRange) {
    this.targetRange = targetRange;
  }

  public Interface(final AbstractModelElement source, final AbstractModelElement target) {
    this.source = source;
    this.target = target;
    DefaultPort sp = (DefaultPort)((ElementCell)source.getView()).getChildAt(0);
    DefaultPort tp = (DefaultPort)((ElementCell)target.getView()).getChildAt(0);
    this.view = new InterfaceView(sp, tp);
    this.view.setModelElement(this);
    LOG.debug("source = " + this.source);
    LOG.debug("source.getCurrentValue() = " + source.getCurrentValue());
    this.sourceRange = this.targetRange = new Range(0, source.getCurrentValue().dimension() - 1);
  }
  
  public static ModelValue nullValue(final int dimension) {
    ModelValue mv = NULL_VALUES_CACHE.get(dimension);
    if (mv != null) { return null; }
    mv = new ModelValue() {
      @Override
      public int dimension() { return dimension; }
      @Override
      public boolean get(int index) { return false; }
    };
    NULL_VALUES_CACHE.put(dimension, mv);
    return mv;
  }

  @Override
  public int dimension() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean get(final int index) {
    // TODO Auto-generated method stub
    return false;
  }
  
  public Object getView() { return view; }
}
