package edu.concordia.comp354.gui.EVA;

import edu.concordia.comp354.model.EVA.EarnedValuePoint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by joao on 15.08.10.
 */
public class Charts {
    final JFreeChart chart;

    public Charts(String title, String xLabel, String yLabel, List<EarnedValuePoint> points, LocalDate start_date) {

        final XYSeriesCollection dataset = getXYSeriesCollection(points, start_date);

        chart = ChartFactory.createTimeSeriesChart(
                title,      // chart title
                xLabel,                      // x axis label
                yLabel,                      // y axis label
                dataset,                  // data
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);


        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.

        rangeAxis.setAutoRangeIncludesZero(true);

        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd MMM"));
    }

    private XYSeriesCollection getXYSeriesCollection(List<EarnedValuePoint> points, LocalDate start_date) {

        LocalDateTime zeroDate = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime projectStart = start_date.atStartOfDay();

        final XYSeries pvSeries = new XYSeries(EVATab.PV);
        final XYSeries evSeries = new XYSeries(EVATab.EV);
        final XYSeries acSeries = new XYSeries(EVATab.AC);

        for (EarnedValuePoint point : points) {
            long date = Duration.between(zeroDate, projectStart.plusDays(point.getDate())).toMillis();
            pvSeries.add(date, point.getPlannedValue());
            evSeries.add(date, point.getEarnedValue());
            acSeries.add(date, point.getActualCost());
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(pvSeries);
        dataset.addSeries(evSeries);
        dataset.addSeries(acSeries);

        return dataset;
    }
}
