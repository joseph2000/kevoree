/*
 *  MinimalDynamicChart.java of project jchart2d, a demonstration 
 *  of the minimal code to set up a chart with dynamic data. 
 *  Copyright (C) 2007 - 2011 Achim Westermann, created on 10.12.2004, 13:48:55
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */
package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.io.ADataCollector;
import info.monitorenter.gui.chart.io.RandomDataCollectorOffset;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Demonstrates minimal effort to create a dynamic chart.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public final class MinimalDynamicChart {

  /**
   * Main entry.
   * <p>
   * 
   * @param args
   *          ignored
   */
  public static void main(final String[] args) {
    // Create a chart:
    Chart2D chart = new Chart2D();
    // Create an ITrace:
    // Note that dynamic charts need limited amount of values!!!
    ITrace2D trace = new Trace2DLtd(100);
    trace.setColor(Color.RED);

    // Add the trace to the chart:
    chart.addTrace(trace);
    IAxis axisX = chart.getAxisX();
    axisX.setStartMajorTick(false);
    axisX.setMajorTickSpacing(10);
    // Make it visible:
    // Create a frame.
    JFrame frame = new JFrame("MinimalDynamicChart");
    // add the chart to the frame:
    frame.getContentPane().add(chart);
    frame.setSize(400, 300);
    // Enable the termination button [cross on the upper right edge]:
    frame.addWindowListener(new WindowAdapter() {
      /**
       * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setVisible(true);
    // Every 20 milliseconds a new value is collected.
    ADataCollector collector = new RandomDataCollectorOffset(trace, 100);
    collector.start();
  }

  /** Defcon. */
  private MinimalDynamicChart() {
    // nop
  }
}
