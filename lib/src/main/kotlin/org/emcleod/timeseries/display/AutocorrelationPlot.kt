package org.emcleod.timeseries.display

import org.emcleod.timeseries.LocalDateDoubleTimeSeries
import org.emcleod.timeseries.analysis.generateAutocorrelations
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.CategoryChartBuilder
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import java.awt.Font
import java.time.LocalDate
import kotlin.random.Random

fun autocorrelationLinePlot(data: List<Pair<Int, Double>>) {
    val chart = XYChartBuilder()
        .width(800)
        .height(600)
        .title("Autocorrelation")
        .xAxisTitle("Lag")
        .yAxisTitle("Autocorrelation")
        .build()
    ChartStyler.applyLinePlotStyling(chart)
    chart.addSeries("Data", data)
    SwingWrapper(chart).displayChart()
}

fun autocorrelationBarChartPlot(data: List<Pair<Int, Double>>) {
    val chart: CategoryChart = CategoryChartBuilder()
        .width(800)
        .height(600)
        .title("Autocorrelation")
        .xAxisTitle("Lag")
        .yAxisTitle("Autocorrelation")
        .build()
    ChartStyler.applyBarChartStyling(chart)
    if (data.size > 20) {
        chart.styler.xAxisLabelRotation = 90
        chart.styler.axisTickLabelsFont = Font("Arial", Font.PLAIN, 8)
    }
    chart.addSeries("Data", data)
    SwingWrapper(chart).displayChart()
}

fun main() {
    val seed = 123
    val random = Random(seed)
    val size = 150
    val ts = LocalDateDoubleTimeSeries.of(
        List(size) { index -> LocalDate.of(2023, 1, 1).plusDays(index.toLong()) },
        List(size) { _ -> random.nextDouble() }
    )
    val autocorrelations = generateAutocorrelations(ts, size / 10)
    autocorrelationBarChartPlot(autocorrelations)
}
