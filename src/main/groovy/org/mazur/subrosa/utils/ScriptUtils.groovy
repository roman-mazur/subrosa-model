/*
 * $Id$
 */
package org.mazur.subrosa.utils

import org.apache.log4j.Logger

/**
 * @author Roman Mazur (Stanfy - http://www.stanfy.com)
 *
 */
public class ScriptUtils {

  private static Logger log = Logger.getLogger(ScriptUtils.class)
  
  static boolean incValues(def inValues, def inputs) {
    int index = inValues.size() - 1
    while (index >= 0) {
      def v = ++inValues[index]
      if (v >= (1 << inputs[index].dimension)) {
        inValues[index--] = 0
      } else {
        return true
      }
    }
    return false
  }
  
  static def iterateAllInputVars = { inputs, handler ->
    def inValues = [0] * inputs.size()
    boolean continueFlag = true
    while (continueFlag) {
      if (log.debugEnabled) { log.debug "inValues: $inValues" }
      inputs.eachWithIndex() { def item, int index -> item.value = inValues[index] }
      handler(inValues)
      continueFlag = ScriptUtils.incValues(inValues, inputs)
    }
  }
  
}
