package org.mazur.subrosa;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.mazur.subrosa.gui.graph.ElementView;
import org.mazur.subrosa.model.AbstractModelElement;
import org.mazur.subrosa.model.Interface;
import org.mazur.subrosa.model.ModelController;
import org.mazur.subrosa.model.ModelValue;
import org.mazur.subrosa.model.elements.CompilationElement;

/**
 * Version: $Id$
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 */
public class Interpreter {
  /** Logger. */
  private static final Logger LOG = Logger.getLogger(Interpreter.class);
  
  /** Controller. */
  private ModelController controller;
  
  private LinkedList<HashMap<AbstractModelElement, ModelValue>> results = new LinkedList<HashMap<AbstractModelElement,ModelValue>>();
  private HashMap<AbstractModelElement, ModelValue> lastInputs = new HashMap<AbstractModelElement, ModelValue>();
  
  private boolean compiled = false, initedState = false;
  
  /** Elements to recalculate. */
  private Set<AbstractModelElement> elementsToChange = new HashSet<AbstractModelElement>();
  
  public void setController(final ModelController controller) { this.controller = controller; }

  public String getNextStepDescription() {
    StringBuilder result = new StringBuilder("Next elements to calculate: [");
    if (!elementsToChange.isEmpty()) { 
      for (AbstractModelElement e : elementsToChange) { result.append(e.getLabel()).append(", "); }
      result.delete(result.length() - 2, result.length()).append("]"); 
    } else {
      result.append("]");
    }
    return result.append(".").toString();
  }
  
  public void init() {
    LOG.debug("Init the interpreter");
    elementsToChange.clear();
    HashMap<AbstractModelElement, ModelValue> outs = new HashMap<AbstractModelElement, ModelValue>(controller.getElementsMap().size());
    for (AbstractModelElement e : controller.getDebugElements()) {
      ModelValue v = initedState ? e.getCurrentValue() : e.nullValue();
      e.setCurrentValue(v);
      LOG.debug("Init " + e + " by " + v);
      outs.put(e, v);
    }
    results.add(outs);
    LOG.debug("Form the list of elements to change.");
    for (AbstractModelElement e : controller.getInputs()) {
      if (!initedState || !lastInputs.get(e).equals(e.getCurrentValue())) {
        for (Interface i : e.getOutputs()) { elementsToChange.add(i.getTarget()); }
      }
      lastInputs.put(e, e.getCurrentValue());
    }
    initedState = true;
  }
  
  public void next() {
    LOG.info("Start the next step");
    if (elementsToChange == null || elementsToChange.isEmpty()) { return; }
    HashMap<AbstractModelElement, ModelValue> outs = new HashMap<AbstractModelElement, ModelValue>(controller.getElementsMap().size());
    for (Entry<ElementView, AbstractModelElement> e : controller.getElementsMap().entrySet()) {
      AbstractModelElement el = e.getValue();
      outs.put(el, el.getCurrentValue());
    }
    HashSet<AbstractModelElement> newCahnges = new HashSet<AbstractModelElement>(elementsToChange.size());
    if (LOG.isDebugEnabled()) {
      LOG.debug("Inputs: " + controller.getInputs());
    }
    for (AbstractModelElement e : elementsToChange) {
      ModelValue v = e.calculate();
      outs.put(e, v);
      if (!v.equals(e.getCurrentValue())) {
        for (Interface i : e.getOutputs()) { newCahnges.add(i.getTarget()); }
      }
      e.setCurrentValue(v);
    }
    elementsToChange = newCahnges;
    results.add(outs);
  }
  
  public void compile(final CompilerConfiguration conf) {
    LOG.info("Compile the scheme.");
    for (Entry<String, CompilationElement> e : controller.getCompileElements().entrySet()) {
      String code = e.getValue().getCode();
      if (code == null || code.trim().length() == 0) {
        code = "constValue(0, 1)";
      }
      Binding binding = controller.getCompileBindings().get(e.getKey());
      if (binding == null) { 
        throw new InterpreterException("No bindings found for '" + e.getKey() + "'. Try to start generator at first."); 
      }
      GroovyShell shell = new GroovyShell(binding, conf);
      try {
        e.getValue().setScript(shell.parse(code));
      } catch (CompilationFailedException ex) {
        throw new InterpreterException("Error in compiling '" + e.getKey() + "' element. "+ ex.getMessage(), ex);
      }
    }
    compiled = true;
  }

  public void calculate(final CompilerConfiguration conf) {
    if (!compiled) { compile(conf); }
    LOG.info("Compilation finished. Start calculating.");
    do {
      next();
    } while (elementsToChange != null && !elementsToChange.isEmpty());
    LOG.info("Calculation is finished");
  }
}
