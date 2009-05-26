package org.mazur.subrosa.model.elements;

import groovy.lang.Script;

import org.mazur.subrosa.model.AbstractModelElement;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public abstract class CompilationElement extends AbstractModelElement {
  private static final long serialVersionUID = -8145375989226539994L;
  
  /** Name. */
  private String name;
  
  /** Script. */
  private transient Script script;
  
  public String getName() { return name; }
  public void setName(final String name) { this.name = name;  }
  
  public void setScript(final Script script) { this.script = script; }
  protected Script getScript() { return script; }
  
  public abstract String getCode();
}
