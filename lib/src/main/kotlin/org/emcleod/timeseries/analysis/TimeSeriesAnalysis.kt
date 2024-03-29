package org.emcleod.timeseries.analysis

import org.emcleod.timeseries.ImmutableLocalDateDoubleTimeSeries
import org.emcleod.timeseries.lag
import org.emcleod.timeseries.intersect
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import java.time.LocalDate

private fun ImmutableLocalDateDoubleTimeSeries.valuesAsDoubleArray() = entries.values.toDoubleArray()

val pearsonCorrelation: (ImmutableLocalDateDoubleTimeSeries, ImmutableLocalDateDoubleTimeSeries) -> Double = { x, y ->
    require(x.size >= 2) { "Time series must contain at least two values for Pearson correlation calculation." }
    require(y.size >= 2) { "Time series must contain at least two values for Pearson correlation calculation." }
    PearsonsCorrelation().correlation(x.valuesAsDoubleArray(), y.valuesAsDoubleArray())
}

val autocorrelation: (ImmutableLocalDateDoubleTimeSeries, Int) -> Double = { ts, lag ->
    require(ts.size >= 2) { "Time series must contain at least two values for autocorrelation calculation." }
    val laggedSeries = ts.lag(lag)
    pearsonCorrelation(ts.intersect(laggedSeries), laggedSeries)
}

fun generateAutocorrelations(ts: ImmutableLocalDateDoubleTimeSeries, maxLag: Int): List<Pair<Int, Double>> {
    return (0..maxLag).map { lag -> Pair(lag, autocorrelation(ts, lag)) }
}