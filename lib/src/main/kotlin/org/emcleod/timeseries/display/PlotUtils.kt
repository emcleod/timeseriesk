package org.emcleod.timeseries.display

import org.emcleod.timeseries.LocalDateDoubleTimeSeries
import org.knowm.xchart.CategoryChart
import org.knowm.xchart.CategorySeries
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYSeries
import java.awt.Color
import java.awt.Font
import java.time.temporal.ChronoUnit

class ChartStyler {
    companion object {
        fun applyLinePlotStyling(chart: XYChart) {
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
        }

        fun applyBarChartStyling(chart: CategoryChart) {
            chart.styler.apply {
                isChartTitleVisible = true
                isLegendVisible = false
                markerSize = 8
                chartTitleFont = Font("Arial", Font.BOLD, 20)
                axisTitleFont = Font("Arial", Font.PLAIN, 16)
                axisTickLabelsFont = Font("Arial", Font.PLAIN, 12)
                setPlotGridHorizontalLinesVisible(true)
                setPlotGridVerticalLinesVisible(false)
                setPlotBorderVisible(false)
                plotBackgroundColor = Color.WHITE
                chartBackgroundColor = Color.WHITE
                axisTickMarkLength = 10
                axisTickPadding = 10
                isOverlapped = true
                availableSpaceFill = 0.1
            }
        }
    }
}

fun XYChart.addSeries(seriesName: String, timeSeries: LocalDateDoubleTimeSeries): XYSeries {
    val (xData, yData) = timeSeries.getEntries().map { (date, value) ->
        Pair(date.until(timeSeries.getKeyAtIndex(0), ChronoUnit.DAYS).toDouble(), value)
    }.unzip()
    return addSeries(seriesName, xData, yData)
}

fun XYChart.addSeries(seriesName: String, data: List<Pair<Int, Double>>): XYSeries {
    val (xData, yData) = data.unzip()
    return addSeries(seriesName, xData, yData)
}

fun CategoryChart.addSeries(seriesName: String, data: List<Pair<Int, Double>>): CategorySeries {
    val (xData, yData) = data.unzip()
    return addSeries(seriesName, xData, yData)
}
