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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.mazur.subrosa.gui.graph.ElementView;
import org.mazur.subrosa.gui.graph.GraphComponent;
import org.mazur.subrosa.model.elements.CompilationElement;
import org.mazur.subrosa.model.elements.ConstantElement;
import org.mazur.subrosa.model.elements.OutElement;

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
  private static final int CURRENT_VERSION = 3;
  
  /** Elements map. */
  private HashMap<ElementView, AbstractModelElement> elementsMap = new HashMap<ElementView, AbstractModelElement>(),
          inputsMap = new HashMap<ElementView, AbstractModelElement>();
  private HashMap<ElementView, OutElement> outputsMap = new HashMap<ElementView, OutElement>();
  
  /** Elements to compile. */
  private Map<String, CompilationElement> compileElements = new HashMap<String, CompilationElement>();
  /** Compile bindings. */
  private Map<String, Binding> compileBindings = new HashMap<String, Binding>();
  
  /** Generator code. */
  private String generatorCode, experimentsCode;
  
  /** Counter for outs. */
  private int outsCounter = 0, inputsCounter = 0;
  
  /**
   * @return the elementsMap
   */
  public HashMap<ElementView, AbstractModelElement> getElementsMap() {
    return elementsMap;
  }

  public void addElement(final AbstractModelElement e) {
    elementsMap.put(e.getView(), e);
    if (e instanceof ConstantElement) {
      ConstantElement el = (ConstantElement)e;
      inputsMap.put(el.getView(), el); 
      if (el.getNumber() == null || el.getNumber().equals(0)) { el.setNumber(++inputsCounter); }
    }
    if (e instanceof CompilationElement) { 
      compileElements.put(((CompilationElement)e).getName(), (CompilationElement)e); 
    }
    if (e instanceof OutElement) {
      OutElement el = (OutElement)e;
      outputsMap.put(e.getView(), el);
      if (el.getNumber() == null)  { el.setNumber(++outsCounter); }
    }
  }
  
  private void writeString(final ObjectOutputStream out, final String v) throws IOException {
    LOG.info("write: " + v);
//    byte[] bytes = null;
//    int bytesLen = 0;
//    if (v != null) {
//      bytes = v.getBytes();
//      bytesLen = bytes.length;
//    }
//    out.writeInt(bytesLen);
//    LOG.info("Write " + bytesLen + " bytes");
//    if (bytesLen > 0) { out.write(bytes, 0, bytesLen); }
    out.writeObject(v);
  }
  
  private String readString(final ObjectInputStream input) throws IOException, ClassNotFoundException {
    String res = (String)input.readObject();
//    int bytesLen = input.readInt();
//    if (bytesLen > 0) {
//      LOG.info("Count of bytes: " + bytesLen);
//      byte[] bytes = new byte[bytesLen];
//      input.read(bytes, 0, bytesLen);
//      res = new String(bytes);
//    }
    LOG.info("Read " + res + "\n" + (res != null ? res.length() : "0"));
    return res;
  }
  
  public void saveModel(final OutputStream out) throws IOException {
    ObjectOutputStream output = new ObjectOutputStream(out);
    output.writeInt(CURRENT_VERSION);
    writeString(output, generatorCode);
    writeString(output, experimentsCode);
    for (Entry<ElementView, AbstractModelElement> e : elementsMap.entrySet()) {
      AbstractModelElement ame = e.getValue();
      output.writeObject(ame);
      if (ame instanceof CompilationElement) {
        writeString(output, ((CompilationElement)ame).getCode());
      }
      output.flush();
    }
    output.close();
  }
  
  public void loadModel(final InputStream in) throws IOException, ClassNotFoundException {
    ObjectInputStream input = new ObjectInputStream(in);
    int v = input.readInt();
    LOG.info("Version: " + v);
    generatorCode = readString(input);
    if (v == CURRENT_VERSION) {
//      for (int i = 0; i < 30; i++) {
//        byte b = input.readByte();
//        LOG.info("Next: " + b + " " + (char)b); 
//      }
      experimentsCode = readString(input); 
    }
    while (true) {
      try {
        AbstractModelElement e = (AbstractModelElement)input.readObject();
        addElement(e);
        if (e instanceof CompilationElement) {
          ((CompilationElement)e).setCode(readString(input));
        }
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
    for (Entry<ElementView, OutElement> e : outputsMap.entrySet()) {
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
  public Collection<OutElement> getOutputs() {
    return outputsMap.values();
  }
  
  public String getGeneratorCode() { return generatorCode; }
  public void setGeneratorCode(final String generatorCode) { this.generatorCode = generatorCode; }
  public void setExperimentsCode(final String experimentsCode) { this.experimentsCode = experimentsCode; }
  public String getExperimentsCode() { return experimentsCode; }
  
  public Map<String, CompilationElement> getCompileElements() { return compileElements; }
  public Map<String, Binding> getCompileBindings() { return compileBindings; }
}
