package org.emcleod.timeseries

import java.util.*

/**
 * A time series interface that represents a collection of key-value pairs, where the keys are comparable
 * and represent timestamps, and the values are associated with each timestamp.
 *
 * @param <T> the type of the keys (timestamps), which must be comparable
 * @param <V> the type of the values associated with each timestamp
 */
interface TimeSeries<T : Comparable<T>, V> {
    /**
     * Checks if the time series is empty.
     *
     * @return `true` if the time series is empty, `false` otherwise
     */
    fun isEmpty(): Boolean

    /**
     * Checks if the time series contains a specific timestamp.
     *
     * @param key the timestamp to check for
     * @return `true` if the time series contains the specified timestamp, `false` otherwise
     */
    fun contains(key: T): Boolean

    /**
     * Retrieves the value associated with a specific timestamp.
     *
     * @param key the timestamp to retrieve the value for
     * @return the value associated with the specified timestamp, or `null` if the timestamp is not found
     */
    operator fun get(key: T): V?

    /**
     * Retrieves the value associated with a specific timestamp, or a default value if the timestamp is not found.
     *
     * @param key the timestamp to retrieve the value for
     * @param default the default value to return if the timestamp is not found
     * @return the value associated with the specified timestamp, or the default value if the timestamp is not found
     */
    fun getOrDefault(key: T, default: V): V

    /**
     * Retrieves the timestamp at a specific index in the time series.
     *
     * @param index the index of the timestamp to retrieve
     * @return the timestamp at the specified index
     */
    fun getKeyAtIndex(index: Int): T

    /**
     * Retrieves the value at a specific index in the time series.
     *
     * @param index the index of the value to retrieve
     * @return the value at the specified index
     */
    fun getValueAtIndex(index: Int): V

    /**
     * Returns a subset of the time series between a start timestamp (inclusive) and an end timestamp (exclusive).
     *
     * @param startTimeInclusive the start timestamp (inclusive) of the subset
     * @param endTimeExclusive the end timestamp (exclusive) of the subset
     * @return a new time series containing the subset of key-value pairs
     */
    fun subSeries(startTimeInclusive: T, endTimeExclusive: T): TimeSeries<T, V>

    /**
     * Returns a subset of the time series between a start timestamp and an end timestamp, with the option to include or exclude the start and end timestamps.
     *
     * @param startTime the start timestamp of the subset
     * @param includeStart `true` to include the start timestamp in the subset, `false` otherwise
     * @param endTime the end timestamp of the subset
     * @param includeEnd `true` to include the end timestamp in the subset, `false` otherwise
     * @return a new time series containing the subset of key-value pairs
     */
    fun subSeries(startTime: T, includeStart: Boolean, endTime: T, includeEnd: Boolean): TimeSeries<T, V>

    /**
     * Returns a new time series containing the first `numItems` key-value pairs.
     *
     * @param numItems the number of key-value pairs to include in the new time series
     * @return a new time series containing the first `numItems` key-value pairs
     */
    fun head(numItems: Int): TimeSeries<T, V>

    /**
     * Returns a new time series containing the key-value pairs up to (but not including) the specified end timestamp.
     *
     * @param endTimeExclusive the end timestamp (exclusive) of the new time series
     * @return a new time series containing the key-value pairs up to (but not including) the specified end timestamp
     */
    fun head(endTimeExclusive: T): TimeSeries<T, V>

    /**
     * Returns a new time series containing the last `numItems` key-value pairs.
     *
     * @param numItems the number of key-value pairs to include in the new time series
     * @return a new time series containing the last `numItems` key-value pairs
     */
    fun tail(numItems: Int): TimeSeries<T, V>

    /**
     * Returns a new time series containing the key-value pairs starting from (and including) the specified start timestamp.
     *
     * @param startTimeInclusive the start timestamp (inclusive) of the new time series
     * @return a new time series containing the key-value pairs starting from (and including) the specified start timestamp
     */
    fun tail(startTimeInclusive: T): TimeSeries<T, V>
}

/**
 * An abstract base class for implementing time series data structures.
 *
 * @param <T> the type of the keys (timestamps), which must be comparable
 * @param <V> the type of the values associated with each timestamp
 * @param <R> the type of the concrete subclass extending AbstractTimeSeries
 * @property times the list of timestamps
 * @property values the list of values associated with each timestamp
 */
abstract class AbstractTimeSeries<T : Comparable<T>, V, R : AbstractTimeSeries<T, V, R>>(
    times: List<T>,
    values: List<V>
) : TimeSeries<T, V> {
    private val _entries: SortedMap<T, V>
    private val _size: Int

    init {
        require(times.size == values.size) { "Times and values lists must have the same length" }
        _entries = times.zip(values).toMap(sortedMapOf())
        _size = _entries.size
    }

    /**
     * Creates a new instance of the concrete subclass from a map of entries.
     *
     * @param entries the map of entries to create the new instance from
     * @return a new instance of the concrete subclass
     */
    protected abstract fun fromMap(entries: Map<T, V>): R

    /**
     * Returns the underlying sorted map of entries.
     *
     * @return the sorted map of entries
     */
    fun getEntries() = _entries

    /**
     * Returns the size of the time series.
     *
     * @return the size of the time series
     */
    fun getSize() = _size

    override fun isEmpty(): Boolean = _entries.isEmpty()

    override fun getOrDefault(key: T, default: V): V = _entries.getOrDefault(key, default)

    override fun get(key: T): V? = _entries.get(key)

    override fun contains(key: T): Boolean = _entries.containsKey(key)

    override fun getKeyAtIndex(index: Int): T = _entries.keys.elementAt(index)

    override fun getValueAtIndex(index: Int): V = _entries.values.elementAt(index)

    override fun tail(numItems: Int) = fromMap(_entries.toList().takeLast(numItems).toMap())

    override fun tail(startTimeInclusive: T) = subSeries(startTimeInclusive, true, _entries.lastKey(), true)

    override fun head(numItems: Int) = fromMap(_entries.toList().take(numItems).toMap())

    override fun head(endTimeExclusive: T) = subSeries(_entries.firstKey(), true, endTimeExclusive, false)

    override fun subSeries(startTimeInclusive: T, endTimeExclusive: T): R =
        subSeries(startTimeInclusive, true, endTimeExclusive, false)

    override fun subSeries(startTime: T, includeStart: Boolean, endTime: T, includeEnd: Boolean): R =
        fromMap(_entries.entries
            .dropWhile { (key, _) -> if (includeStart) key < startTime else key <= startTime }
            .takeWhile { (key, _) -> if (includeEnd) key <= endTime else key < endTime }
            .associate { it.toPair() })

    /**
     * Filters the time series based on a predicate applied to each entry.
     *
     * @param predicate the predicate to apply to each entry
     * @return a new instance of the concrete subclass containing the filtered entries
     */
    fun filter(predicate: (Map.Entry<T, V>) -> Boolean): R =
        fromMap(getEntries().filter { entry -> predicate(entry) }.toMap())

    /**
     * Maps the values of the time series using a transform function applied to each entry.
     *
     * @param transform the transform function to apply to each entry
     * @return a new instance of the concrete subclass with the mapped values
     */
    fun map(transform: (Map.Entry<T, V>) -> V): R =
        fromMap(getEntries().map { entry -> entry.key to transform(entry) }.toMap().toSortedMap())

    /**
     * Folds the values of the time series using an initial value and an operation.
     *
     * @param initial the initial value for the fold operation
     * @param operation the operation to apply to each value and the accumulated result
     * @return the final result of the fold operation
     */
    inline fun <U> fold(initial: U, operation: (acc: U, value: V) -> U) =
        getEntries().values.fold(initial) { acc, value -> operation(acc, value) }

    /**
     * Reduces the values of the time series using an initial value and an operation.
     *
     * @param initial the initial value for the reduce operation
     * @param operation the operation to apply to each value and the accumulated result
     * @return the final result of the reduce operation
     */
    inline fun <U> reduce(initial: U, operation: (acc: U, value: V) -> U): U =
        getEntries().values.fold(initial) { acc, value -> operation(acc, value) }

    /**
     * Performs an action for each entry in the time series.
     *
     * @param action the action to perform for each entry
     */
    inline fun forEach(action: (T, V) -> Unit) = getEntries().forEach { (date, value) -> action(date, value) }

    /**
     * Checks if any entry in the time series satisfies a given predicate.
     *
     * @param predicate the predicate to check for each entry
     * @return `true` if any entry satisfies the predicate, `false` otherwise
     */
    inline fun any(predicate: (Map.Entry<T, V>) -> Boolean) = getEntries().any { entry -> predicate(entry) }

    /**
     * Checks if all entries in the time series satisfy a given predicate.
     *
     * @param predicate the predicate to check for each entry
     * @return `true` if all entries satisfy the predicate, `false` otherwise
     */
    inline fun all(predicate: (Map.Entry<T, V>) -> Boolean) = getEntries().all { entry -> predicate(entry) }

    override fun toString(): String {
        val entries = _entries.map { (key, value) -> "($key, $value)" }.joinToString(", ")
        return "${this::class.simpleName}[$entries]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as AbstractTimeSeries<*, *, *>
        return (_entries == other.getEntries())
    }

    override fun hashCode(): Int = _entries.hashCode()
}
