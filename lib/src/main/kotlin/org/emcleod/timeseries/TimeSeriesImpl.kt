package org.emcleod.timeseries

import java.time.LocalDate

interface LocalDateTimeSeriesOperators<T : Number> {
    operator fun plus(a: Number): LocalDateTimeSeries<T>
    operator fun <V : Number> plus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>
    operator fun minus(a: Number): LocalDateTimeSeries<T>
    operator fun <V : Number> minus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>
    operator fun times(a: Number): LocalDateTimeSeries<T>
    operator fun <V : Number> times(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>
    operator fun div(a: Number): LocalDateTimeSeries<T>
    operator fun <V : Number> div(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>
}

open class LocalDateTimeSeries<V : Number> protected constructor(times: List<LocalDate>, values: List<V>) :
    AbstractTimeSeries<LocalDate, V, LocalDateTimeSeries<V>>(times, values) {

    companion object {
        fun <V : Number> empty(): LocalDateTimeSeries<V> {
            return LocalDateTimeSeries(emptyList(), emptyList())
        }

        fun <V : Number> fromMap(entries: Map<LocalDate, V>): LocalDateTimeSeries<V> {
            return LocalDateTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun <V : Number> of(keys: List<LocalDate>, values: List<V>): LocalDateTimeSeries<V> {
            return LocalDateTimeSeries(keys, values)
        }
    }

    override fun fromMap(entries: Map<LocalDate, V>): LocalDateTimeSeries<V> {
        return LocalDateTimeSeries.fromMap(entries)
    }

    fun <R : Number> performOperation(
        other: LocalDateTimeSeries<R>,
        transform: (Number?) -> V,
        operation: (V, V) -> V
    ): LocalDateTimeSeries<V> {
        return fromMap(getEntries().keys
            .intersect(other.getEntries().keys)
            .filter { key -> this[key] != null && other[key] != null }
            .associateWith { key ->
                operation(transform(getEntries()[key]), transform(other[key]))
            }
            .toSortedMap())
    }

}

class LocalDateDoubleTimeSeries private constructor(times: List<LocalDate>, values: List<Double>) :
    LocalDateTimeSeries<Double>(times, values),
    LocalDateTimeSeriesOperators<Double> {

    companion object {
        fun empty(): LocalDateDoubleTimeSeries {
            return LocalDateDoubleTimeSeries(emptyList(), emptyList())
        }

        fun fromMap(entries: Map<LocalDate, Double>): LocalDateDoubleTimeSeries {
            return LocalDateDoubleTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun of(keys: List<LocalDate>, values: List<Double>): LocalDateDoubleTimeSeries {
            return LocalDateDoubleTimeSeries(keys, values)
        }
    }

    override fun fromMap(entries: Map<LocalDate, Double>): LocalDateDoubleTimeSeries {
        return LocalDateDoubleTimeSeries.fromMap(entries)
    }

    private val _transform: (Number?) -> Double = { it?.toDouble() ?: 0.0 }

    override operator fun plus(a: Number): LocalDateTimeSeries<Double> = map { (_, value) -> value + a.toDouble() }

    override operator fun <V : Number> plus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Double> {
        return performOperation(other, _transform) { a, b -> a + b }
    }

    override operator fun minus(a: Number): LocalDateTimeSeries<Double> = map { (_, value) -> value - a.toDouble() }

    override operator fun <V : Number> minus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Double> {
        return performOperation(other, _transform) { a, b -> a - b }
    }

    override operator fun times(a: Number): LocalDateTimeSeries<Double> = map { (_, value) -> value * a.toDouble() }

    override operator fun <V : Number> times(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Double> {
        return performOperation(other, _transform) { a, b -> a * b }
    }

    override operator fun div(a: Number): LocalDateTimeSeries<Double> = map { (_, value) -> value / a.toDouble() }

    override operator fun <V : Number> div(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Double> {
        return performOperation(other, _transform) { a, b -> a / b }
    }
}

class LocalDateIntTimeSeries private constructor(times: List<LocalDate>, values: List<Int>) :
    LocalDateTimeSeries<Int>(times, values),
    LocalDateTimeSeriesOperators<Int> {

    companion object {
        fun empty(): LocalDateIntTimeSeries {
            return LocalDateIntTimeSeries(emptyList(), emptyList())
        }

        fun fromMap(entries: Map<LocalDate, Int>): LocalDateIntTimeSeries {
            return LocalDateIntTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun of(keys: List<LocalDate>, values: List<Int>): LocalDateIntTimeSeries {
            return LocalDateIntTimeSeries(keys, values)
        }
    }

    override fun fromMap(entries: Map<LocalDate, Int>): LocalDateIntTimeSeries {
        return LocalDateIntTimeSeries.fromMap(entries)
    }

    private val _transform: (Number?) -> Int = { it?.toInt() ?: 0 }

    override operator fun plus(a: Number): LocalDateTimeSeries<Int> = map { (_, value) -> value + a.toInt() }

    override operator fun <V : Number> plus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Int> {
        return performOperation(other, _transform) { a, b -> a + b }
    }

    override operator fun minus(a: Number): LocalDateTimeSeries<Int> = map { (_, value) -> value - a.toInt() }

    override operator fun <V : Number> minus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Int> {
        return performOperation(other, _transform) { a, b -> a - b }
    }

    override operator fun times(a: Number): LocalDateTimeSeries<Int> = map { (_, value) -> value * a.toInt() }

    override operator fun <V : Number> times(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Int> {
        return performOperation(other, _transform) { a, b -> a * b }
    }

    override operator fun div(a: Number): LocalDateTimeSeries<Int> = map { (_, value) -> value / a.toInt() }

    override operator fun <V : Number> div(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Int> {
        return performOperation(other, _transform) { a, b -> a / b }
    }
}

class LocalDateLongTimeSeries private constructor(times: List<LocalDate>, values: List<Long>) :
    LocalDateTimeSeries<Long>(times, values),
    LocalDateTimeSeriesOperators<Long> {

    companion object {
        fun empty(): LocalDateLongTimeSeries {
            return LocalDateLongTimeSeries(emptyList(), emptyList())
        }

        fun fromMap(entries: Map<LocalDate, Long>): LocalDateLongTimeSeries {
            return LocalDateLongTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun of(keys: List<LocalDate>, values: List<Long>): LocalDateLongTimeSeries {
            return LocalDateLongTimeSeries(keys, values)
        }
    }

    override fun fromMap(entries: Map<LocalDate, Long>): LocalDateLongTimeSeries {
        return LocalDateLongTimeSeries.fromMap(entries)
    }

    private val _transform: (Number?) -> Long = { it?.toLong() ?: 0L }

    override operator fun plus(a: Number): LocalDateTimeSeries<Long> = map { (_, value) -> value + a.toLong() }

    override operator fun <V : Number> plus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Long> {
        return performOperation(other, _transform) { a, b -> a + b }
    }

    override operator fun minus(a: Number): LocalDateTimeSeries<Long> = map { (_, value) -> value - a.toLong() }

    override operator fun <V : Number> minus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Long> {
        return performOperation(other, _transform) { a, b -> a - b }
    }

    override operator fun times(a: Number): LocalDateTimeSeries<Long> = map { (_, value) -> value * a.toLong() }

    override operator fun <V : Number> times(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Long> {
        return performOperation(other, _transform) { a, b -> a * b }
    }

    override operator fun div(a: Number): LocalDateTimeSeries<Long> = map { (_, value) -> value / a.toLong() }

    override operator fun <V : Number> div(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<Long> {
        return performOperation(other, _transform) { a, b -> a / b }
    }

}
