package org.mazur.subrosa.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.mazur.subrosa.model.utils.BitSetModelValue;
import org.mazur.subrosa.model.utils.Range;

/**
 * Link between model elements.
 * 
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class ModelLink implements Serializable {

  /** serialVersionUID. */
  private static final long serialVersionUID = -4309026854059154068L;
  
  /** Main collection. */
  private LinksCollection collection = new LinksCollection();
  
  /**
   * @param receiver model element
   * @return input value
   */
  public ModelValue getInputValue(final AbstractModelElement receiver) {
    List<LinksRecord> records = collection.getInputLinks(receiver); 
    BitSetModelValue result = new BitSetModelValue();
    for (LinksRecord r : records) {
      result.add(r.sender.getCurrentValue(), r.senderRange);
    }
    return result;
  }
  
  /**
   * Links record.
   */
  private static final class LinksRecord implements Serializable {
    private static final long serialVersionUID = 1470132964970791369L;
    /** Sender and receiver. */
    private AbstractModelElement sender, receiver;
    /** Ranges. */
    private Range senderRange, receiverRange;
  }
  
  /**
   * Links collection.
   */
  private static final class LinksCollection implements Serializable {
    private static final long serialVersionUID = 994503209888647227L;
    /** Receivers and senders maps. */
    private Map<AbstractModelElement, List<LinksRecord>> receiversMap, sendersMap;
    /** Constructor. */
    public LinksCollection() {
      receiversMap = new TreeMap<AbstractModelElement, List<LinksRecord>>();
      sendersMap = new TreeMap<AbstractModelElement, List<LinksRecord>>();
    }
    private static List<LinksRecord> getFromMap(final Map<AbstractModelElement, List<LinksRecord>> map, final AbstractModelElement e) {
      List<LinksRecord> res = map.get(e);
      if (res == null) {
        res = Collections.emptyList();
        map.put(e, res); 
      }
      return res;
    }
    /**
     * @param element element
     * @return input links
     */
    public List<LinksRecord> getInputLinks(final AbstractModelElement element) {
      return getFromMap(receiversMap, element);
    }
    /**
     * @param element element
     * @return output links
     */
    public List<LinksRecord> getOutputLinks(final AbstractModelElement element) {
      return getFromMap(sendersMap, element);
    }
  }
}
