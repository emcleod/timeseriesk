package org.emcleod.timeseries

import java.time.LocalDate

fun ImmutableLocalDateDoubleTimeSeries.lag(lag: Int): ImmutableLocalDateDoubleTimeSeries {
    val shiftedData = sortedMapOf<LocalDate, Double>()
    for (i in 0 until size) {
        val shiftedIndex = i + lag
        if (shiftedIndex in 0 until size) {
            shiftedData[getKeyAtIndex(i)] = getValueAtIndex(shiftedIndex)
        }
    }
    return ImmutableLocalDateDoubleTimeSeries.fromMap(shiftedData)
}

fun ImmutableLocalDateDoubleTimeSeries.intersect(other: ImmutableLocalDateDoubleTimeSeries): ImmutableLocalDateDoubleTimeSeries {
    val times = mutableListOf<LocalDate>()
    val values = mutableListOf<Double>()
    other.forEach { date, _ -> 
        times.add(date)
        values.add(this.get(date) ?: 0.0) // TODO what happens when there's no value
    }
    return ImmutableLocalDateDoubleTimeSeries.of(times, values)
}