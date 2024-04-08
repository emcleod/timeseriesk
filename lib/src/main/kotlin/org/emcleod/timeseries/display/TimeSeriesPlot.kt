package org.emcleod.timeseries.display

import org.emcleod.timeseries.ImmutableLocalDateDoubleTimeSeries
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

fun timeSeriesLinePlot(timeSeries: ImmutableLocalDateDoubleTimeSeries) {
    val chart = XYChartBuilder()
        .width(800)
        .height(600)
        .title("Time Series")
        .xAxisTitle("Days")
        .yAxisTitle("Value")
        .build()

    // Customize chart styling
    chart.styler.apply {
        defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line
        isChartTitleVisible = true
        isLegendVisible = false
        markerSize = 8
    }

    val xData = timeSeries.entries.map { (date, _) -> timeSeries.getKeyAtIndex(0).until(date, ChronoUnit.DAYS) }.toList()
    val yData = timeSeries.entries.map { (_, value) -> value }.toList()
    // Add data to the chart
    chart.addSeries("Data", xData, yData)

    // Display the chart in a Swing frame
    SwingWrapper(chart).displayChart()
}

fun main() {
    val seed = 123
    val random = Random(seed)
    val size = 150
    val ts = ImmutableLocalDateDoubleTimeSeries.of(
        List(size) { index -> LocalDate.of(2023, 1, 1).plusDays(index.toLong()) },
        List(size) { _ -> random.nextDouble() }
    )
    timeSeriesLinePlot(ts)
}
