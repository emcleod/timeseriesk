package org.emcleod.timeseries

import java.time.LocalDate

private inline fun <reified T : Number, reified R : LocalDateTimeSeries<T>> LocalDateTimeSeries<T>.lag(lag: Int): R {
    val builder = when (R::class) {
        LocalDateDoubleTimeSeries::class -> LocalDateDoubleTimeSeries.Builder()
        LocalDateIntTimeSeries::class -> LocalDateIntTimeSeries.Builder()
        LocalDateLongTimeSeries::class -> LocalDateLongTimeSeries.Builder()
        else -> throw IllegalArgumentException("Unsupported time series type")
    }
    for (i in 0 until getSize()) {
        val shiftedIndex = i + lag
        if (shiftedIndex in 0 until getSize()) {
            when (builder) {
                is LocalDateDoubleTimeSeries.Builder -> builder.put(
                    getKeyAtIndex(i),
                    getValueAtIndex(shiftedIndex) as Double
                )

                is LocalDateIntTimeSeries.Builder -> builder.put(getKeyAtIndex(i), getValueAtIndex(shiftedIndex) as Int)
                is LocalDateLongTimeSeries.Builder -> builder.put(
                    getKeyAtIndex(i),
                    getValueAtIndex(shiftedIndex) as Long
                )
            }
        }
    }
    return builder.build() as R
}

private inline fun <reified T : Number, reified R : LocalDateTimeSeries<T>> LocalDateTimeSeries<T>.intersect(other: LocalDateTimeSeries<T>): R {
    val builder = when (R::class) {
        LocalDateDoubleTimeSeries::class -> LocalDateDoubleTimeSeries.Builder()
        LocalDateIntTimeSeries::class -> LocalDateIntTimeSeries.Builder()
        LocalDateLongTimeSeries::class -> LocalDateLongTimeSeries.Builder()
        else -> throw IllegalArgumentException("Unsupported time series type")
    }

    other.forEach { date, _ ->
        val value = this[date]
        if (value != null) {
            when (builder) {
                is LocalDateDoubleTimeSeries.Builder -> builder.put(date, value as Double)
                is LocalDateIntTimeSeries.Builder -> builder.put(date, value as Int)
                is LocalDateLongTimeSeries.Builder -> builder.put(date, value as Long)
            }
        }
    }
    return builder.build() as R
}

fun LocalDateDoubleTimeSeries.intersect(other: LocalDateDoubleTimeSeries): LocalDateDoubleTimeSeries {
    return intersect<Double, LocalDateDoubleTimeSeries>(other)
}

fun LocalDateIntTimeSeries.intersect(other: LocalDateIntTimeSeries): LocalDateIntTimeSeries {
    return intersect<Int, LocalDateIntTimeSeries>(other)
}

fun LocalDateLongTimeSeries.intersect(other: LocalDateLongTimeSeries): LocalDateLongTimeSeries {
    return intersect<Long, LocalDateLongTimeSeries>(other)
}

fun LocalDateDoubleTimeSeries.lag(lag: Int): LocalDateDoubleTimeSeries {
    return lag<Double, LocalDateDoubleTimeSeries>(lag)
}

fun LocalDateIntTimeSeries.lag(lag: Int): LocalDateIntTimeSeries {
    return lag<Int, LocalDateIntTimeSeries>(lag)
}

fun LocalDateLongTimeSeries.lag(lag: Int): LocalDateLongTimeSeries {
    return lag<Long, LocalDateLongTimeSeries>(lag)
}

fun <T : Number> LocalDateTimeSeries<T>.unzip(): Pair<List<LocalDate>, List<T?>> {
    return Pair(getEntries().keys.toList(), getEntries().values.toList())
}