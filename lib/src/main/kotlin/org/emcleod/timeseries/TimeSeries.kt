package org.emcleod.timeseries

import java.time.LocalDate
import java.util.SortedMap

interface TimeSeries<T : Comparable<T>, out V> {
    // list-like methods
    fun isEmpty(): Boolean
    fun contains(key: T): Boolean
    fun get(key: T): V?
    // get keys and values
    fun getKeyAtIndex(index: Int): T
    fun getValueAtIndex(index: Int): V
    // sub-series
    fun subSeries(startTimeInclusive: T, endTimeExclusive: T): TimeSeries<T, V>
    fun subSeries(startTime: T, includeStart: Boolean, endTime: T, includeEnd: Boolean): TimeSeries<T, V>
    fun head(numItems: Int): TimeSeries<T, V>
    fun head(endTimeExclusive: T): TimeSeries<T, V>
    fun tail(numItems: Int): TimeSeries<T, V>
    fun tail(startTimeInclusive: T): TimeSeries<T, V>
}

abstract class AbstractDateTimeSeries<T : Comparable<T>, out V>(times: List<T>, values: List<V>) : TimeSeries<T, V> {
    val entries: SortedMap<T, @UnsafeVariance V>

    init {
        require(times.size == values.size) { "Times and values lists must have the same length" }
        entries = times.zip(values).toMap(sortedMapOf())
    }

    override fun isEmpty() = entries.isEmpty()

    override fun contains(key: T): Boolean = entries.containsKey(key)

    override fun getKeyAtIndex(index: Int) = entries.keys.elementAt(index)

    override fun getValueAtIndex(index: Int) = entries.values.elementAt(index)

    override fun get(key: T): V? = entries[key]

    protected fun tailMap(numItems: Int) = entries.toList().takeLast(numItems).toMap()

    protected fun headMap(numItems: Int) = entries.toList().take(numItems).toMap()

    protected fun subMap(startTimeInclusive: T, endTimeExclusive: T) = entries.subMap(startTimeInclusive, endTimeExclusive)

    override fun toString(): String {
        val sb = StringBuilder(this::class.simpleName)
        sb.append('[')
        entries.forEach { (key, value) -> sb.append("($key, $value),") }
        sb.append(']')
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as AbstractDateTimeSeries<*, *>
        if (entries != other.entries) return false
        return true
    }
    override fun hashCode(): Int {
        return entries.hashCode()
    }
}

class ImmutableLocalDateTimeSeries<out V> private constructor(times: List<LocalDate>, values: List<V>) :
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
    
}

// inline fun <T: Comparable<T>, V, U: Comparable<U>, R> TimeSeries<T, V>.map(crossinline transform: (TimeSeriesEntry<T, V?>) -> TimeSeriesEntry<U, R?>): TimeSeries<U, R> {
//     val mappedEntries = this.iterator().asSequence().map {
//         entry -> transform(entry)
//     }.toList()
//     return newInstance(mappedEntries)
// }

// // inline fun <T : Comparable<T>, V> TimeSeries<T, V>.filter(predicate: (TimeSeriesEntry<T, V>) -> Boolean): TimeSeries<T, V> {
// //     val filteredEntries = this.iterator().asSequence().filter(predicate).toList()

// //     val filteredTimes = filteredEntries.map { it.time }
// //     val filteredValues = filteredEntries.map { it.value }

// //     return newInstance(filteredTimes, filteredValues)
// // }