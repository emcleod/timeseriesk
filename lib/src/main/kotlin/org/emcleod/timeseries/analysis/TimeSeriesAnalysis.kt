package org.emcleod.timeseries.analysis

import org.emcleod.timeseries.ImmutableLocalDateDoubleTimeSeries
import org.emcleod.timeseries.lag
import org.emcleod.timeseries.intersect
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import java.time.LocalDate

private fun ImmutableLocalDateDoubleTimeSeries.valuesAsDoubleArray() = entries.values.toDoubleArray()

val pearsonCorrelation: (ImmutableLocalDateDoubleTimeSeries, ImmutableLocalDateDoubleTimeSeries) -> Double = { x, y ->
    PearsonsCorrelation().correlation(x.valuesAsDoubleArray(), y.valuesAsDoubleArray())
}

val autocorrelation: (ImmutableLocalDateDoubleTimeSeries, Int) -> Double = { ts, lag ->
    val laggedSeries = ts.lag(lag)
    pearsonCorrelation(ts.intersect(laggedSeries), laggedSeries)
}
