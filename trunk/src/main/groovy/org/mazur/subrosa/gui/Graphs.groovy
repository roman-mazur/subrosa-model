package org.mazur.subrosa.gui

import org.mazur.subrosa.model.ModelControllerimport org.apache.log4j.Loggerimport groovy.swing.SwingBuilderimport javax.xml.ws.BindingTypeimport java.awt.BorderLayout as BLimport groovy.lang.Bindingimport groovy.lang.GroovyShellimport org.codehaus.groovy.control.CompilerConfigurationimport org.mazur.subrosa.Interpreterimport groovy.lang.Scriptimport javax.swing.JOptionPaneimport org.jfree.chart.JFreeChartimport org.mazur.subrosa.model.utils.ConstantModelValueimport org.jfree.data.xy.XYSeriesCollectionimport org.jfree.data.xy.XYSeriesimport org.jfree.chart.ChartPanelimport org.jfree.chart.ChartFactoryimport org.jfree.chart.plot.PlotOrientationimport java.math.BigInteger

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public class Graphs {
  /** Logger. */
  private Logger log = Logger.getLogger(Graphs.class)
  
  /** Controller. */
  ModelController controller
  /** X, Y. */
  private def outputs, inputs
  
  /** Compiler configuration. */
  CompilerConfiguration compilerConfiguration
  /** Expression. */
  private String funcExp
  /** Shell. */
  private GroovyShell shell
  private Interpreter interpreter
  
  /** Dataset. */
  private XYSeriesCollection mainChartData = new XYSeriesCollection()
  /** Charts. */
  private JFreeChart mainChart
  
  /** Separate graphs flag. */
  private boolean separateGraphs = true
  
  /** Status label. */
  private def statusLabel
 
  private void setStatus(final String msg) {
    statusLabel.text = msg
    statusLabel.visible = !!msg
  }
  
  private BigInteger mergeOutputs() {
    BigInteger result = new BigInteger('0')
    outputs.each() {
      def v = new ConstantModelValue(it.currentValue)
      result += v.value
      result *= 2 ** v.dimension()
    }
    return result
  }
  
  void prepare() {
    outputs = controller.ouputs.findAll() { it.includeInStats }
    outputs = (outputs.sort() { it.number }).reverse()
    if (log.debugEnabled) { log.debug "List of outputs: $outputs" }
    inputs = new ArrayList(controller.inputs)
    inputs = inputs.sort() { it.number }
    if (log.debugEnabled) { log.debug "List of inputs: $inputs" }
    
    def expBinding = new Binding()
    expBinding['f'] = { x ->
      log.debug "Calculate the function value"
      interpreter.init()
      interpreter.calculate(compilerConfiguration)
      if (!separateGraphs) {
        log.debug 'Calculating the single output'
        return mergeOutputs() 
      }
    }
    expBinding['x'] = 3
    shell = new GroovyShell(expBinding)
    interpreter = new Interpreter(controller : this.controller)
    interpreter.compile(compilerConfiguration)

    mainChart = ChartFactory.createXYLineChart("", "", "", mainChartData, PlotOrientation.VERTICAL, true, true, false);
  }

  private void errorDlg(msg) {
    def d
    SwingBuilder.build() {
      d = optionPane(message : msg, messageType : JOptionPane.ERROR_MESSAGE)      
    }
    d.show()
  }

  private boolean incValues(def inValues) {
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
  
  private void draw() {
    setStatus('Start calculations...')
    this.separateGraphs = funcExp.trim() == 'f(x)'
    try {
      mainChartData.removeAllSeries()
      Script calcScript = shell.parse(funcExp)
      def inValues = [0] * inputs.size(), outValues = [0] * outputs.size();
      def outSeries = []
      (separateGraphs ? outputs.size() : 1).times() {
        outSeries += new XYSeries(outputs[it].label + outputs[it].number)
      }
      int maxValue = 1 << inputs[0].dimension
      int counter = 0
      boolean continueFlag = true
      while (continueFlag) {
        //if (log.debugEnabled) { log.debug "inValues: $inValues" }
        log.info "inValues: $inValues"
        inputs.eachWithIndex() { def item, int index -> item.value = inValues[index] }
        def sResult = calcScript.run()
        continueFlag = incValues(inValues)
        if (separateGraphs) {
          outputs.eachWithIndex() { def item, int index -> 
            outValues[index] = new ConstantModelValue(item.currentValue).value
            outSeries[index].add(counter, outValues[index])
          }
        } else {
          outSeries[0].add(counter, sResult)
        }
        if (log.debugEnabled) { log.info "outValues: $outValues" }
        counter++
      }
      outSeries.each() { mainChartData.addSeries(it) }
    } catch (Exception e) {
      errorDlg('Error happened. ${e.message}')
      log.error 'Incorect expression', e
      return
    } finally {
      setStatus('')
    }
  }
  
  /**
   * Show the frame.
   */
  void show() {
    log.debug "Show graphs..."
    def mcp = new ChartPanel(mainChart)
    SwingBuilder.build() {
      frame(title : 'Graphs', pack : true, show : true) {
        borderLayout()
        panel(constraints : BL.NORTH, border : raisedEtchedBorder()) {
          borderLayout()
          label(text : 'Expression: ', constraints : BL.WEST)
          def expField = textField(constraints : BL.CENTER, text : 'f(x)')
          button(constraints : BL.EAST, action : action(
            name : 'Draw',
            closure : { 
              funcExp = expField.text
              new Thread(new ClosureRunnable() {
                draw()
              }).start()
            }
          ))
        }
        panel(constraints : BL.CENTER) {
          borderLayout()
          statusLabel = label(constraints : BL.NORTH)
          widget(mcp)
        }
      }
    }
  }
  
}
