package org.mazur.subrosa.model;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultPort;
import org.mazur.subrosa.gui.graph.ElementCell;
import org.mazur.subrosa.gui.graph.InterfaceView;
import org.mazur.subrosa.model.utils.ConstantModelValue;
import org.mazur.subrosa.model.utils.Range;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Interface extends ModelValue {
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
  public AbstractModelElement getSource() { return source; }

  /**
   * @return the target
   */
  public AbstractModelElement getTarget() { return target; }

  /**
   * @return the sourceRange
   */
  public Range getSourceRange() { return sourceRange; }

  /**
   * @return the targetRange
   */
  public Range getTargetRange() { return targetRange; }

  /**
   * @param source the source to set
   */
  public void setSource(final AbstractModelElement source) { this.source = source; }

  /**
   * @param target the target to set
   */
  public void setTarget(final AbstractModelElement target) { this.target = target; }

  /**
   * @param sourceRange the sourceRange to set
   */
  public void setSourceRange(final Range sourceRange) { this.sourceRange = sourceRange; }

  /**
   * @param targetRange the targetRange to set
   */
  public void setTargetRange(final Range targetRange) { this.targetRange = targetRange; }

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
    if (mv != null) { return mv; }
    mv = new ModelValue() {
      private static final long serialVersionUID = -2300679701642884695L;
      @Override
      public int dimension() { return dimension; }
      @Override
      public boolean get(int index) { return false; }
      @Override
      public String toString() {
        return "ModelValue[dimension: " + dimension + ", always false]";
      }
    };
    NULL_VALUES_CACHE.put(dimension, mv);
    return mv;
  }

  @Override
  public int dimension() {
    return source.getCurrentValue().dimension();
  }

  @Override
  public boolean get(final int index) {
    return source.getCurrentValue().get(index);
  }
  
  public ConstantModelValue constValue() { return new ConstantModelValue(source.getCurrentValue()); }
  
  public Object getView() { return view; }
  
  @Override
  public String toString() {
    return "Interface, value: " + source.getCurrentValue() + "(source: " + source + ")";
  }
}
