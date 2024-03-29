package org.emcleod.timeseries

import java.time.LocalDate
import java.util.SortedMap

open class ImmutableLocalDateTimeSeries<out V> protected constructor(times: List<LocalDate>, values: List<V>) :
    AbstractDateTimeSeries<LocalDate, V>(times, values) {

    companion object {
        fun <V> empty(): ImmutableLocalDateTimeSeries<V> {
            return ImmutableLocalDateTimeSeries(emptyList(), emptyList())
        }

        fun <V> fromMap(entries: Map<LocalDate, V>): ImmutableLocalDateTimeSeries<V> {
            return ImmutableLocalDateTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun <V> of(keys: List<LocalDate>, values: List<V>): ImmutableLocalDateTimeSeries<V> {
            return ImmutableLocalDateTimeSeries(keys, values)
        }
    }
    
    override fun subSeries(startTimeInclusive: LocalDate, endTimeExclusive: LocalDate): ImmutableLocalDateTimeSeries<V> {
        return ImmutableLocalDateTimeSeries.fromMap(subMap(startTimeInclusive, endTimeExclusive))
    }

    override fun subSeries(startTime: LocalDate, includeStart: Boolean, endTime: LocalDate, includeEnd: Boolean): ImmutableLocalDateTimeSeries<V> {
        val subMap = entries.toList()
            .dropWhile { (key, _) ->
                if (includeStart) key.isBefore(startTime) else !key.isAfter(startTime) }
            .takeWhile { (key, _) ->
                if (includeEnd) !key.isAfter(endTime) else key.isBefore(endTime) }
            .toMap()
        return ImmutableLocalDateTimeSeries.fromMap(subMap)
    }

    override fun tail(numItems: Int): ImmutableLocalDateTimeSeries<V> {
        return ImmutableLocalDateTimeSeries.fromMap(tailMap(numItems))
    }

    override fun tail(startTimeInclusive: LocalDate): ImmutableLocalDateTimeSeries<V> {
        val subMap = entries.toList().dropWhile { it.first.isBefore(startTimeInclusive) }.toMap()
        return ImmutableLocalDateTimeSeries.fromMap(subMap)
    }

    override fun head(numItems: Int): ImmutableLocalDateTimeSeries<V> {
        return ImmutableLocalDateTimeSeries.fromMap(headMap(numItems))
    }
    
    override fun head(endTimeExclusive: LocalDate): ImmutableLocalDateTimeSeries<V> {
        val subMap = entries.toList().takeWhile { it.first.isBefore(endTimeExclusive) }.toMap()
        return ImmutableLocalDateTimeSeries.fromMap(subMap)
    }
    
    inline fun filter(predicate: (Map.Entry<LocalDate, V>) -> Boolean): ImmutableLocalDateTimeSeries<V> {
        val filtered = entries.filter { entry -> predicate(entry) }.toMap()
        return ImmutableLocalDateTimeSeries.fromMap(filtered)
    }

    inline fun <R> map(transform: (Map.Entry<LocalDate, V>) -> R): ImmutableLocalDateTimeSeries<R> {
        val mapped = entries.map { entry -> entry.key to transform(entry) }.toMap().toSortedMap()
        return ImmutableLocalDateTimeSeries.fromMap(mapped)
    }

    inline fun <R> fold(initial: R, operation: (acc: R, value: V) -> R): R {
        return entries.values.fold(initial) { acc, value -> operation(acc, value) }
    }

    inline fun <R> reduce(initial: R, operation: (acc: R, value: V) -> R): R {
        return entries.values.fold(initial) { acc, value -> operation(acc, value) }
    }

    inline fun forEach(action: (LocalDate, V) -> Unit) {
        entries.forEach { (date, value) -> action(date, value) }
    }

    inline fun any(predicate: (Map.Entry<LocalDate, V>) -> Boolean): Boolean {
        return entries.any { entry -> predicate(entry) }
    }

    inline fun all(predicate: (Map.Entry<LocalDate, V>) -> Boolean): Boolean {
        return entries.all { entry -> predicate(entry) }
    }    
}

public class ImmutableLocalDateDoubleTimeSeries private constructor(times: List<LocalDate>, values: List<Double>) :
    ImmutableLocalDateTimeSeries<Double>(times, values) {

    companion object {
        fun empty(): ImmutableLocalDateDoubleTimeSeries {
            return ImmutableLocalDateDoubleTimeSeries(emptyList(), emptyList())
        }

        fun fromMap(entries: Map<LocalDate, Double>): ImmutableLocalDateDoubleTimeSeries {
            return ImmutableLocalDateDoubleTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun of(keys: List<LocalDate>, values: List<Double>): ImmutableLocalDateDoubleTimeSeries {
            return ImmutableLocalDateDoubleTimeSeries(keys, values)
        }
    }

    operator fun plus(a: Number) = 
        ImmutableLocalDateDoubleTimeSeries.fromMap(entries.map { (times, value) -> times to value + a.toDouble() }.toMap())
    operator fun <S : Number> plus(other: ImmutableLocalDateTimeSeries<S>): ImmutableLocalDateTimeSeries<Double> {
        val dates = (entries.keys + other.entries.keys).sorted()
        val values = dates.map { date ->
            (entries[date]?.toDouble() ?: 0.0) + (other.entries[date]?.toDouble() ?: 0.0)
        }
        return ImmutableLocalDateDoubleTimeSeries.fromMap(dates.zip(values).toMap())
    }
    operator fun minus(a: Number) = 
        ImmutableLocalDateDoubleTimeSeries.fromMap(entries.map { (times, value) -> times to value - a.toDouble() }.toMap())
    operator fun <S : Number> minus(other: ImmutableLocalDateTimeSeries<S>): ImmutableLocalDateTimeSeries<Double> {
        val dates = (entries.keys + other.entries.keys).sorted()
        val values = dates.map { date ->
            (entries[date]?.toDouble() ?: 0.0) - (other.entries[date]?.toDouble() ?: 0.0)
        }
        return ImmutableLocalDateDoubleTimeSeries.fromMap(dates.zip(values).toMap())
    }
    operator fun times(a: Number) = 
        ImmutableLocalDateDoubleTimeSeries.fromMap(entries.map { (times, value) -> times to value * a.toDouble() }.toMap())
    operator fun div(a: Number): ImmutableLocalDateTimeSeries<Double> {
        if (a == 0) {
            throw IllegalArgumentException("Cannot divide time series by zero")
        }
        return ImmutableLocalDateDoubleTimeSeries.fromMap(entries.map { (times, value) -> times to value / a.toDouble() }.toMap())
    }

}
// 3. Indexing Operator (`[]`):
//    - Signature: `operator fun get(index: Int): Double`
//    - Signature: `operator fun get(range: IntRange): TimeSeries`
//    - Convention: The indexing operator should return the value at the specified index or a new `TimeSeries` object representing a subsequence of the original time series.
//    - Error Handling: If the index is out of bounds, you can throw an `IndexOutOfBoundsException`.}
//    7. Function Call Operator (`()`):
//    - Signature: `operator fun invoke(transformation: (Double) -> Double): TimeSeries`
//    - Convention: The function call operator should apply the specified transformation function to each data point in the time series and return a new `TimeSeries` object with the transformed values.
//    - Error Handling: No specific error handling is required for this operator.

// 8. Iteration Operators:
//    - Signature: `operator fun iterator(): Iterator<Double>`
//    - Convention: The `iterator()` function should return an `Iterator` object that allows iterating over the data points of the time series.
//    - Error Handling: No specific error handling is required for this operator.



class ImmutableLocalDateIntTimeSeries private constructor(times: List<LocalDate>, values: List<Int>) :
    ImmutableLocalDateTimeSeries<Int>(times, values) {

    companion object {
        fun empty(): ImmutableLocalDateIntTimeSeries {
            return ImmutableLocalDateIntTimeSeries(emptyList(), emptyList())
        }

        fun fromMap(entries: Map<LocalDate, Int>): ImmutableLocalDateIntTimeSeries {
            return ImmutableLocalDateIntTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun of(keys: List<LocalDate>, values: List<Int>): ImmutableLocalDateIntTimeSeries {
            return ImmutableLocalDateIntTimeSeries(keys, values)
        }
    }

    operator fun plus(a: Int) = 
        ImmutableLocalDateIntTimeSeries.fromMap(entries.map { (times, value) -> times to value + a }.toMap())
    operator fun plus(a: Long) = 
        ImmutableLocalDateLongTimeSeries.fromMap(entries.map { (times, value) -> times to value + a }.toMap())
    operator fun plus(a: Double) = 
        ImmutableLocalDateDoubleTimeSeries.fromMap(entries.map { (times, value) -> times to value + a }.toMap())
}

class ImmutableLocalDateLongTimeSeries private constructor(times: List<LocalDate>, values: List<Long>) :
    ImmutableLocalDateTimeSeries<Long>(times, values) {

    companion object {
        fun empty(): ImmutableLocalDateLongTimeSeries {
            return ImmutableLocalDateLongTimeSeries(emptyList(), emptyList())
        }

        fun fromMap(entries: Map<LocalDate, Long>): ImmutableLocalDateLongTimeSeries {
            return ImmutableLocalDateLongTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        fun of(keys: List<LocalDate>, values: List<Long>): ImmutableLocalDateLongTimeSeries {
            return ImmutableLocalDateLongTimeSeries(keys, values)
        }
    }

    operator fun plus(a: Int) = 
        ImmutableLocalDateLongTimeSeries.fromMap(entries.map { (times, value) -> times to value + a }.toMap())
    operator fun plus(a: Long) = 
        ImmutableLocalDateLongTimeSeries.fromMap(entries.map { (times, value) -> times to value + a }.toMap())
    operator fun plus(a: Double) = 
        ImmutableLocalDateDoubleTimeSeries.fromMap(entries.map { (times, value) -> times to value + a }.toMap())

}

