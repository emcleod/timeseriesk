package org.emcleod.timeseries.display

import org.emcleod.timeseries.sample.generateMA1Series
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.markers.SeriesMarkers

fun xyLinePlot(data: List<Pair<Int, Double>>, xTitle: String = "x", yTitle: String = "y") {
    val chart = XYChartBuilder()
        .width(800)
        .height(600)
        .title("Data")
        .xAxisTitle(xTitle)
        .yAxisTitle(yTitle)
        .build()
    ChartStyler.applyLinePlotStyling(chart)
    val series = chart.addSeries("Data", data)
    series.marker = SeriesMarkers.NONE
    SwingWrapper(chart).displayChart()
}

fun main() {
    val data = accumulatePairs(100.0, generateMA1Series(1000, 0.4, 0.0, 1.0))
    xyLinePlot(data)
}
