package edu.nyu.stex.analyzer;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartGenerator extends JFrame {

  private static final long serialVersionUID = 1L;

  public ChartGenerator(String chartTitle, String filePath) {
    // super(applicationTitle);

    switch (chartTitle) {
    case "Probability Statistics of Topics":
      Double[] data = ProbabilityAnalyzer.ProbabilitySum(filePath);

      // based on the dataset we create the chart
      JFreeChart barChart = ChartFactory.createBarChart(chartTitle, "Topic",
          "documents", createDatasetForBasicBarChart(data),
          PlotOrientation.VERTICAL, true, true, false);

      // Adding chart into a chart panel
      ChartPanel chartPanel = new ChartPanel(barChart);

      // settind default size
      chartPanel.setPreferredSize(new java.awt.Dimension(1000, 540));

      // add to contentPane
      setContentPane(chartPanel);

      // save as PNG to a file
      try {
        ChartUtilities.saveChartAsPNG(new File("./basicBarchart.png"),
            barChart, 1000, 540);
      } catch (IOException e) {
        e.printStackTrace();
      }
      break;
    }

  }

  private CategoryDataset createDatasetForBasicBarChart(Double[] input) {

    // y-axis keys...
    final String probability = "Probability";

    // create the dataset...
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for (int i = 0; i < input.length; i++) {
      dataset.addValue(input[i], probability, Integer.toString(i));
    }

    return dataset;
  }
}
