package org.emcleod.timeseries.display

import org.emcleod.timeseries.LocalDateDoubleTimeSeries
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import java.awt.Color
import java.awt.Font
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

    // Customize chart styling
    chart.styler.apply {
        defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line
        isChartTitleVisible = true
        isLegendVisible = false
        markerSize = 8
        chartTitleFont = Font("Arial", Font.BOLD, 20)
        axisTitleFont = Font("Arial", Font.PLAIN, 16)
        axisTickLabelsFont = Font("Arial", Font.PLAIN, 12)
        setPlotGridHorizontalLinesVisible(false)
        setPlotGridVerticalLinesVisible(false)
        setPlotBorderVisible(false)
        setPlotBackgroundColor(Color.WHITE)
        setChartBackgroundColor(Color.WHITE)
    }

//    val xData = timeSeries.map { (date, _) -> timeSeries.getKeyAtIndex(0).until(date, ChronoUnit.DAYS) }.toList()
//    val yData = timeSeries.map { (_, value) -> value }.toList()
//    val series = chart.addSeries("Data", xData, yData)
//    series.marker = SeriesMarkers.NONE

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
