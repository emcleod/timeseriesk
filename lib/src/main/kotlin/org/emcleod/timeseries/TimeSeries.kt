package org.emcleod.timeseries

import java.util.*

interface TimeSeries<T : Comparable<T>, out V> {
    fun isEmpty(): Boolean
    fun contains(key: T): Boolean
    fun getKeyAtIndex(index: Int): T
    fun getValueAtIndex(index: Int): V
    fun subSeries(startTimeInclusive: T, endTimeExclusive: T): TimeSeries<T, V>
    fun subSeries(startTime: T, includeStart: Boolean, endTime: T, includeEnd: Boolean): TimeSeries<T, V>
    fun head(numItems: Int): TimeSeries<T, V>
    fun head(endTimeExclusive: T): TimeSeries<T, V>
    fun tail(numItems: Int): TimeSeries<T, V>
    fun tail(startTimeInclusive: T): TimeSeries<T, V>
}

abstract class AbstractDateTimeSeries<T : Comparable<T>, V>(times: List<T>, values: List<V>) : TimeSeries<T, V> {
    private val _entries: SortedMap<T, V>
    private val _size: Int

    init {
        require(times.size == values.size) { "Times and values lists must have the same length" }
        _entries = times.zip(values).toMap(sortedMapOf())
        _size = _entries.size
    }

    fun getEntries() = _entries

    fun getSize() = _size

    override fun isEmpty() = _entries.isEmpty()

    override fun contains(key: T): Boolean = _entries.containsKey(key)

    override fun getKeyAtIndex(index: Int): T = _entries.keys.elementAt(index)

    override fun getValueAtIndex(index: Int): V = _entries.values.elementAt(index)

    protected fun tailMap(numItems: Int) = _entries.toList().takeLast(numItems).toMap()

    protected fun headMap(numItems: Int) = _entries.toList().take(numItems).toMap()

    protected fun subMap(startTimeInclusive: T, endTimeExclusive: T): SortedMap<T, V> = _entries.subMap(startTimeInclusive, endTimeExclusive)

    operator fun get(key: T): V? = _entries[key]

    override fun toString(): String {
        val sb = StringBuilder(this::class.simpleName)
        sb.append('[')
        _entries.forEach { (key, value) -> sb.append("($key, $value),") }
        sb.append(']')
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as AbstractDateTimeSeries<*, *>
        return (_entries == other.getEntries())
    }
    override fun hashCode(): Int {
        return _entries.hashCode()
    }
}
