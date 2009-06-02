package org.mazur.subrosa.gui

import org.mazur.subrosa.model.ModelControllerimport org.apache.log4j.Loggerimport groovy.swing.SwingBuilderimport javax.xml.ws.BindingTypeimport java.awt.BorderLayout as BLimport groovy.lang.Bindingimport groovy.lang.GroovyShellimport org.codehaus.groovy.control.CompilerConfigurationimport org.mazur.subrosa.Interpreterimport groovy.lang.Scriptimport javax.swing.JOptionPaneimport org.jfree.chart.JFreeChartimport org.mazur.subrosa.model.utils.ConstantModelValueimport org.jfree.data.xy.XYSeriesCollectionimport org.jfree.data.xy.XYSeriesimport org.jfree.chart.ChartPanelimport org.jfree.chart.ChartFactoryimport org.jfree.chart.plot.PlotOrientation

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
  
  void prepare() {
    outputs = controller.ouputs.findAll() { it.includeInStats }
    outputs.sort() { it.number }
    if (log.debugEnabled) { log.debug "List of outputs: $outputs" }
    inputs = new ArrayList(controller.inputs)
    inputs.sort() { it.number }
    if (log.debugEnabled) { log.debug "List of inputs: $inputs" }
    
    def expBinding = new Binding()
    expBinding['f'] = { x ->
      log.debug "Calculate the function value"
      interpreter.init()
      interpreter.calculate(compilerConfiguration)
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

  private void incValues(def inValues) {
    int index = inValues.size() - 1
    while (index >= 0) {
      def v = ++inValues[index]
      if (v >= (1 << inputs[index].dimension)) {
        inValues[index--] = 0
      } else {
        break
      }
    }
  }
  
  private void draw() {
    try {
      mainChartData.removeAllSeries()
      Script calcScript = shell.parse(funcExp)
      def series = { new XYSeries() }
      def inValues = [0] * inputs.size(), outValues = [0] * outputs.size(),
          outSeries = [series()] * outputs.size()
      int maxValue = 1 << inputs[0].dimension
      int counter = 0
      while (inValues[0] < maxValue) {
        if (log.debugEnabled) { log.info "inValues: $inValues" }
        inputs.eachWithIndex() { def item, int index -> item.value = inValues[index] }
        calcScript.run()
        incValues(inValues)
        outputs.eachWithIndex() { def item, int index -> 
          outValues[index] = new ConstantModelValue(item.currentValue).value
          outSeries[index].add(counter, outValues[index])
        }
        if (log.debugEnabled) { log.info "outValues: $outValues" }
        counter++
      }
      outSeries.each() { mainChartData.addSeries(it) }
    } catch (Exception e) {
      errorDlg('Error happened. ${e.message}')
      log.error 'Incorect expression', e
      return
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
              draw()
            }
          ))
        }
        panel(constraints : BL.CENTER) {
          borderLayout()
          widget(mcp)
        }
      }
    }
  }
  
}
