package org.emcleod.timeseries.display

import org.emcleod.timeseries.LocalDateDoubleTimeSeries
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.markers.SeriesMarkers
import java.time.LocalDate
import kotlin.random.Random

fun timeSeriesLinePlot(timeSeries: LocalDateDoubleTimeSeries) {
    val chart = XYChartBuilder()
        .width(800)
        .height(600)
        .title("Time Series")
        .xAxisTitle("Days")
        .yAxisTitle("Value")
        .build()
    ChartStyler.applyLinePlotStyling(chart)
    val series = chart.addSeries("Data", timeSeries)
    series.marker = SeriesMarkers.NONE
    SwingWrapper(chart).displayChart()
}

fun main() {
    val seed = 123
    val random = Random(seed)
    val size = 150
    val ts = LocalDateDoubleTimeSeries.of(
        List(size) { index -> LocalDate.of(2023, 1, 1).plusDays(index.toLong()) },
        List(size) { _ -> random.nextDouble() - 0.5 }
    )
    timeSeriesLinePlot(ts)
}
