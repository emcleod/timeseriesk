package org.emcleod.timeseries.display

import org.emcleod.timeseries.LocalDateDoubleTimeSeries
import org.emcleod.timeseries.analysis.generateAutocorrelations
import org.emcleod.timeseries.sample.generateMASeries
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.CategoryChartBuilder
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import java.awt.Font
import java.time.LocalDate

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
    val size = 150
    val startDate = LocalDate.of(2024, 1, 1)
    val data = generateMASeries(size, listOf(0.5, -0.3, 0.2, 0.6))
    val ts = LocalDateDoubleTimeSeries.fromMap(
        data.associate { (key, value) -> startDate.plusDays(key.toLong()) to value })
    val autocorrelations = generateAutocorrelations(ts, 100)
    autocorrelationBarChartPlot(autocorrelations)
}
