package org.emcleod.timeseries

import java.time.LocalDate

/**
 * Represents a set of operators for performing operations on a LocalDateTimeSeries.
 *
 * @param <T> the type of numbers stored in the LocalDateTimeSeries, which must extend Number
 */
interface LocalDateTimeSeriesOperators<T : Number> {
    /**
     * Adds a scalar value to each element of the LocalDateTimeSeries.
     *
     * @param a the scalar value to be added
     * @return a new LocalDateTimeSeries with the scalar value added to each element
     */
    operator fun plus(a: Number): LocalDateTimeSeries<T>

    /**
     * Adds the values of another LocalDateTimeSeries to this LocalDateTimeSeries element-wise.
     *
     * @param <V> the type of numbers stored in the other LocalDateTimeSeries, which must extend Number
     * @param other the other LocalDateTimeSeries to be added
     * @return a new LocalDateTimeSeries with the values of the other LocalDateTimeSeries added element-wise
     */
    operator fun <V : Number> plus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>

    /**
     * Subtracts a scalar value from each element of the LocalDateTimeSeries.
     *
     * @param a the scalar value to be subtracted
     * @return a new LocalDateTimeSeries with the scalar value subtracted from each element
     */
    operator fun minus(a: Number): LocalDateTimeSeries<T>

    /**
     * Subtracts the values of another LocalDateTimeSeries from this LocalDateTimeSeries element-wise.
     *
     * @param <V> the type of numbers stored in the other LocalDateTimeSeries, which must extend Number
     * @param other the other LocalDateTimeSeries to be subtracted
     * @return a new LocalDateTimeSeries with the values of the other LocalDateTimeSeries subtracted element-wise
     */
    operator fun <V : Number> minus(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>

    /**
     * Multiplies each element of the LocalDateTimeSeries by a scalar value.
     *
     * @param a the scalar value to multiply by
     * @return a new LocalDateTimeSeries with each element multiplied by the scalar value
     */
    operator fun times(a: Number): LocalDateTimeSeries<T>

    /**
     * Multiplies the values of this LocalDateTimeSeries with another LocalDateTimeSeries element-wise.
     *
     * @param <V> the type of numbers stored in the other LocalDateTimeSeries, which must extend Number
     * @param other the other LocalDateTimeSeries to multiply with
     * @return a new LocalDateTimeSeries with the values of this LocalDateTimeSeries multiplied by the other LocalDateTimeSeries element-wise
     */
    operator fun <V : Number> times(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>

    /**
     * Divides each element of the LocalDateTimeSeries by a scalar value.
     *
     * @param a the scalar value to divide by
     * @return a new LocalDateTimeSeries with each element divided by the scalar value
     */
    operator fun div(a: Number): LocalDateTimeSeries<T>

    /**
     * Divides the values of this LocalDateTimeSeries by another LocalDateTimeSeries element-wise.
     *
     * @param <V> the type of numbers stored in the other LocalDateTimeSeries, which must extend Number
     * @param other the other LocalDateTimeSeries to divide by
     * @return a new LocalDateTimeSeries with the values of this LocalDateTimeSeries divided by the other LocalDateTimeSeries element-wise
     */
    operator fun <V : Number> div(other: LocalDateTimeSeries<V>): LocalDateTimeSeries<T>
}

/**
 * Represents a time series of values associated with LocalDate timestamps.
 *
 * @param <V> the type of values stored in the time series, which must extend Number
 * @param times the list of LocalDate timestamps
 * @param values the list of values corresponding to the timestamps
 */
open class LocalDateTimeSeries<V : Number> protected constructor(times: List<LocalDate>, values: List<V>) :
    AbstractTimeSeries<LocalDate, V, LocalDateTimeSeries<V>>(times, values) {

    companion object {
        /**
         * Creates an empty LocalDateTimeSeries.
         *
         * @param <V> the type of values stored in the time series, which must extend Number
         * @return an empty LocalDateTimeSeries
         */
        fun <V : Number> empty(): LocalDateTimeSeries<V> {
            return LocalDateTimeSeries(emptyList(), emptyList())
        }

        /**
         * Creates a LocalDateTimeSeries from a map of LocalDate keys and values.
         *
         * @param <V> the type of values stored in the time series, which must extend Number
         * @param entries the map of LocalDate keys and values
         * @return a LocalDateTimeSeries created from the map entries
         */
        fun <V : Number> fromMap(entries: Map<LocalDate, V>): LocalDateTimeSeries<V> {
            return LocalDateTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        /**
         * Creates a LocalDateTimeSeries from lists of LocalDate keys and values.
         *
         * @param <V> the type of values stored in the time series, which must extend Number
         * @param keys the list of LocalDate keys
         * @param values the list of values corresponding to the keys
         * @return a LocalDateTimeSeries created from the keys and values
         */
        fun <V : Number> of(keys: List<LocalDate>, values: List<V>): LocalDateTimeSeries<V> {
            return LocalDateTimeSeries(keys, values)
        }
    }

    /**
     * Creates a LocalDateTimeSeries from a map of LocalDate keys and values.
     *
     * @param entries the map of LocalDate keys and values
     * @return a LocalDateTimeSeries created from the map entries
     */
    override fun fromMap(entries: Map<LocalDate, V>): LocalDateTimeSeries<V> {
        return LocalDateTimeSeries.fromMap(entries)
    }

    /**
     * Performs an operation on this LocalDateTimeSeries and another LocalDateTimeSeries.
     *
     * @param <R> the type of values stored in the other LocalDateTimeSeries, which must extend Number
     * @param other the other LocalDateTimeSeries to perform the operation with
     * @param transform a function to transform the values before performing the operation
     * @param operation the operation to be performed on the transformed values
     * @return a new LocalDateTimeSeries with the result of the operation
     */
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

/**
 * Represents a time series of Double values associated with LocalDate timestamps.
 *
 * @param times the list of LocalDate timestamps
 * @param values the list of Double values corresponding to the timestamps
 */
class LocalDateDoubleTimeSeries private constructor(times: List<LocalDate>, values: List<Double>) :
    LocalDateTimeSeries<Double>(times, values),
    LocalDateTimeSeriesOperators<Double> {

    companion object {
        /**
         * Creates an empty LocalDateDoubleTimeSeries.
         *
         * @return an empty LocalDateDoubleTimeSeries
         */
        fun empty(): LocalDateDoubleTimeSeries {
            return LocalDateDoubleTimeSeries(emptyList(), emptyList())
        }

        /**
         * Creates a LocalDateDoubleTimeSeries from a map of LocalDate keys and Double values.
         *
         * @param entries the map of LocalDate keys and Double values
         * @return a LocalDateDoubleTimeSeries created from the map entries
         */
        fun fromMap(entries: Map<LocalDate, Double>): LocalDateDoubleTimeSeries {
            return LocalDateDoubleTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        /**
         * Creates a LocalDateDoubleTimeSeries from lists of LocalDate keys and Double values.
         *
         * @param keys the list of LocalDate keys
         * @param values the list of Double values corresponding to the keys
         * @return a LocalDateDoubleTimeSeries created from the keys and values
         */
        fun of(keys: List<LocalDate>, values: List<Double>): LocalDateDoubleTimeSeries {
            return LocalDateDoubleTimeSeries(keys, values)
        }
    }

    /**
     * Creates a LocalDateDoubleTimeSeries from a map of LocalDate keys and Double values.
     *
     * @param entries the map of LocalDate keys and Double values
     * @return a LocalDateDoubleTimeSeries created from the map entries
     */
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

/**
 * Represents a time series of Int values associated with LocalDate timestamps.
 *
 * @param times the list of LocalDate timestamps
 * @param values the list of Int values corresponding to the timestamps
 */
class LocalDateIntTimeSeries private constructor(times: List<LocalDate>, values: List<Int>) :
    LocalDateTimeSeries<Int>(times, values),
    LocalDateTimeSeriesOperators<Int> {

    companion object {
        /**
         * Creates an empty LocalDateIntTimeSeries.
         *
         * @return an empty LocalDateIntTimeSeries
         */
        fun empty(): LocalDateIntTimeSeries {
            return LocalDateIntTimeSeries(emptyList(), emptyList())
        }

        /**
         * Creates a LocalDateIntTimeSeries from a map of LocalDate keys and Int values.
         *
         * @param entries the map of LocalDate keys and Int values
         * @return a LocalDateIntTimeSeries created from the map entries
         */
        fun fromMap(entries: Map<LocalDate, Int>): LocalDateIntTimeSeries {
            return LocalDateIntTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        /**
         * Creates a LocalDateIntTimeSeries from lists of LocalDate keys and Int values.
         *
         * @param keys the list of LocalDate keys
         * @param values the list of Int values corresponding to the keys
         * @return a LocalDateIntTimeSeries created from the keys and values
         */
        fun of(keys: List<LocalDate>, values: List<Int>): LocalDateIntTimeSeries {
            return LocalDateIntTimeSeries(keys, values)
        }
    }

    /**
     * Creates a LocalDateIntTimeSeries from a map of LocalDate keys and Int values.
     *
     * @param entries the map of LocalDate keys and Int values
     * @return a LocalDateIntTimeSeries created from the map entries
     */
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

/**
 * Represents a time series of Long values associated with LocalDate timestamps.
 *
 * @param times the list of LocalDate timestamps
 * @param values the list of Long values corresponding to the timestamps
 */
class LocalDateLongTimeSeries private constructor(times: List<LocalDate>, values: List<Long>) :
    LocalDateTimeSeries<Long>(times, values),
    LocalDateTimeSeriesOperators<Long> {

    companion object {
        /**
         * Creates an empty LocalDateLongTimeSeries.
         *
         * @return an empty LocalDateLongTimeSeries
         */
        fun empty(): LocalDateLongTimeSeries {
            return LocalDateLongTimeSeries(emptyList(), emptyList())
        }

        /**
         * Creates a LocalDateLongTimeSeries from a map of LocalDate keys and Long values.
         *
         * @param entries the map of LocalDate keys and Long values
         * @return a LocalDateLongTimeSeries created from the map entries
         */
        fun fromMap(entries: Map<LocalDate, Long>): LocalDateLongTimeSeries {
            return LocalDateLongTimeSeries(entries.keys.toList(), entries.values.toList())
        }

        /**
         * Creates a LocalDateLongTimeSeries from lists of LocalDate keys and Long values.
         *
         * @param keys the list of LocalDate keys
         * @param values the list of Long values corresponding to the keys
         * @return a LocalDateLongTimeSeries created from the keys and values
         */
        fun of(keys: List<LocalDate>, values: List<Long>): LocalDateLongTimeSeries {
            return LocalDateLongTimeSeries(keys, values)
        }
    }

    /**
     * Creates a LocalDateLongTimeSeries from a map of LocalDate keys and Long values.
     *
     * @param entries the map of LocalDate keys and Long values
     * @return a LocalDateLongTimeSeries created from the map entries
     */
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
