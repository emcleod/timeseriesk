package org.emcleod.timeseries.analysis

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.emcleod.timeseries.LocalDateDoubleTimeSeries
import org.emcleod.timeseries.intersect
import org.emcleod.timeseries.lag

private fun LocalDateDoubleTimeSeries.valuesAsDoubleArray() = getEntries().values.toDoubleArray()

val pearsonCorrelation: (LocalDateDoubleTimeSeries, LocalDateDoubleTimeSeries) -> Double = { x, y ->
    require(x.getSize() >= 2) { "Time series must contain at least two values for Pearson correlation calculation." }
    require(y.getSize() >= 2) { "Time series must contain at least two values for Pearson correlation calculation." }
    PearsonsCorrelation().correlation(x.valuesAsDoubleArray(), y.valuesAsDoubleArray())
}

val autocorrelation: (LocalDateDoubleTimeSeries, Int) -> Double = { ts, lag ->
    require(ts.getSize() >= 2) { "Time series must contain at least two values for autocorrelation calculation." }
    val laggedSeries = ts.lag(lag)
    pearsonCorrelation(ts.intersect(laggedSeries), laggedSeries)
}

fun generateAutocorrelations(ts: LocalDateDoubleTimeSeries, maxLag: Int): List<Pair<Int, Double>> {
    return (0..maxLag).map { lag -> Pair(lag, autocorrelation(ts, lag)) }
}