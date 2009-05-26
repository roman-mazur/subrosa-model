package org.mazur.subrosa.model;

import groovy.lang.Binding;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.mazur.subrosa.gui.graph.ElementView;
import org.mazur.subrosa.gui.graph.GraphComponent;
import org.mazur.subrosa.model.elements.CompilationElement;
import org.mazur.subrosa.model.elements.ConstantElement;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ModelController {
  
  /** Logger. */
  private static final Logger LOG = Logger.getLogger(ModelController.class);
  
  /** Current format version. */
  private static final int CURRENT_VERSION = 1;
  
  /** Elements map. */
  private HashMap<ElementView, AbstractModelElement> elementsMap = new HashMap<ElementView, AbstractModelElement>(),
          inputsMap = new HashMap<ElementView, AbstractModelElement>();
  
  /** Elements to compile. */
  private Map<String, CompilationElement> compileElements = new HashMap<String, CompilationElement>();
  /** Compile bindings. */
  private Map<String, Binding> compileBindings = new HashMap<String, Binding>();
  
  /** Generator code. */
  private String generatorCode;
  
  /**
   * @return the elementsMap
   */
  public HashMap<ElementView, AbstractModelElement> getElementsMap() {
    return elementsMap;
  }

  public void addElement(final AbstractModelElement e) {
    elementsMap.put(e.getView(), e);
    if (e instanceof ConstantElement) { inputsMap.put(e.getView(), e); }
    if (e instanceof CompilationElement) { 
      compileElements.put(((CompilationElement)e).getName(), (CompilationElement)e); 
    }
  }
  
  public void saveModel(final OutputStream out) throws IOException {
    ObjectOutputStream output = new ObjectOutputStream(out);
    output.writeInt(CURRENT_VERSION);
    byte[] bytes = null;
    int bytesLen = 0;
    if (generatorCode != null) {
      bytes = generatorCode.getBytes();
      bytesLen = bytes.length;
    }
    output.writeInt(bytesLen);
    if (bytesLen > 0) { output.write(bytes); }
    for (Entry<ElementView, AbstractModelElement> e : elementsMap.entrySet()) {
      output.writeObject(e.getValue());
      output.flush();
    }
    output.close();
  }
  
  public void loadModel(final InputStream in) throws IOException {
    ObjectInputStream input = new ObjectInputStream(in);
    int v = input.readInt();
    LOG.info("Version: " + v);
    int bytesLen = input.readInt();
    if (bytesLen > 0) {
      byte[] bytes = new byte[bytesLen];
      input.read(bytes);
      generatorCode = new String(bytes);
    }
    while (true) {
      try {
        AbstractModelElement e = (AbstractModelElement)input.readObject();
        addElement(e);
      } catch (Exception e) {
        LOG.debug("Exception in read", e);
        break;
      }
    }
    input.close();
  }
  
  public List<AbstractModelElement> getDebugElements() {
    ArrayList<AbstractModelElement> result = new ArrayList<AbstractModelElement>(elementsMap.values());
    for (Entry<ElementView, AbstractModelElement> e : inputsMap.entrySet()) {
      result.remove(e.getValue());
    }
    return result;
  }
  
  public void display(final GraphComponent component) {
    for (Entry<ElementView, AbstractModelElement> e : elementsMap.entrySet()) {
      component.getGraphLayoutCache().insert(e.getValue().getView());
    }
    for (Entry<ElementView, AbstractModelElement> e : elementsMap.entrySet()) {
      for (Interface i : e.getValue().getInputs()) {
        component.getGraphLayoutCache().insert(i.getView());
      }
    }
  }
  
  public Collection<AbstractModelElement> getInputs() {
    return inputsMap.values();
  }
  
  public String getGeneratorCode() { return generatorCode; }
  public void setGeneratorCode(final String generatorCode) { this.generatorCode = generatorCode; }
  
  public Map<String, CompilationElement> getCompileElements() { return compileElements; }
  public Map<String, Binding> getCompileBindings() { return compileBindings; }
}
