package org.emcleod.timeseries

import java.time.LocalDate
import java.util.SortedMap

interface TimeSeries<T : Comparable<T>, out V> {
    // list-like methods
    fun isEmpty(): Boolean
    fun contains(key: T): Boolean
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
    val size: Int

    init {
        require(times.size == values.size) { "Times and values lists must have the same length" }
        entries = times.zip(values).toMap(sortedMapOf())
        size = entries.size
    }

    override fun isEmpty() = entries.isEmpty()

    override fun contains(key: T): Boolean = entries.containsKey(key)

    override fun getKeyAtIndex(index: Int) = entries.keys.elementAt(index)

    override fun getValueAtIndex(index: Int) = entries.values.elementAt(index)

    protected fun tailMap(numItems: Int) = entries.toList().takeLast(numItems).toMap()

    protected fun headMap(numItems: Int) = entries.toList().take(numItems).toMap()

    protected fun subMap(startTimeInclusive: T, endTimeExclusive: T) = entries.subMap(startTimeInclusive, endTimeExclusive)

    operator fun get(key: T): V? = entries[key]

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
