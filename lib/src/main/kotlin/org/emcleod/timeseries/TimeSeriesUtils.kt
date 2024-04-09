package org.emcleod.timeseries

import java.time.LocalDate

fun LocalDateDoubleTimeSeries.lag(lag: Int): LocalDateDoubleTimeSeries {
    val shiftedData = sortedMapOf<LocalDate, Double>()
    for (i in 0 until getSize()) {
        val shiftedIndex = i + lag
        if (shiftedIndex in 0 until getSize()) {
            shiftedData[getKeyAtIndex(i)] = getValueAtIndex(shiftedIndex)
        }
    }
    return LocalDateDoubleTimeSeries.fromMap(shiftedData)
}

fun LocalDateDoubleTimeSeries.intersect(other: LocalDateDoubleTimeSeries): LocalDateDoubleTimeSeries {
    val times = mutableListOf<LocalDate>()
    val values = mutableListOf<Double>()
    other.forEach { date, _ -> 
        if (this.contains(date)) {
            times.add(date)
            values.add(this.get(date)!!) 
        }
    }
    return LocalDateDoubleTimeSeries.of(times, values)
}