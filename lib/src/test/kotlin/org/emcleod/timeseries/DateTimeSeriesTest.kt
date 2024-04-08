package org.emcleod.timeseries

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateTimeSeriesEqualsTest {

    @Test
    fun `test equals method with same implementation`() {
        val times = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2))
        val values = listOf(1.0, 2.0)

        val series1 = ImmutableLocalDateTimeSeries.of(times, values)
        val series2 = ImmutableLocalDateTimeSeries.of(times, values)

        assertEquals(series1, series2)
    }

    @Test
    fun `test equals method with different implementations`() {
        val times = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2))
        val values = listOf(1.0, 2.0)

        val series1 = ImmutableLocalDateTimeSeries.of(times, values)
        val series2 = MockDateTimeSeries(times, values)

        assertNotEquals(series1, series2)
    }

    @Test
    fun `test equals method with different data`() {
        val times1 = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2))
        val values1 = listOf(1.0, 2.0)

        val times2 = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 3))
        val values2 = listOf(1.0, 3.0)

        val series1 = ImmutableLocalDateTimeSeries.of(times1, values1)
        val series2 = MockDateTimeSeries(times2, values2)

        assertNotEquals(series1, series2)
    }

    // Mock implementation of AbstractDateTimeSeries
    class MockDateTimeSeries<T : Comparable<T>, V>(times: List<T>, values: List<V>) :
        AbstractDateTimeSeries<T, V>(times, values) {

        override fun subSeries(startTimeInclusive: T, endTimeExclusive: T): TimeSeries<T, V> {
            throw UnsupportedOperationException("Not implemented")
        }

        override fun subSeries(startTime: T, includeStart: Boolean, endTime: T, includeEnd: Boolean): TimeSeries<T, V> {
            throw UnsupportedOperationException("Not implemented")
        }

        override fun head(numItems: Int): TimeSeries<T, V> {
            throw UnsupportedOperationException("Not implemented")
        }

        override fun head(endTimeExclusive: T): TimeSeries<T, V> {
            throw UnsupportedOperationException("Not implemented")
        }

        override fun tail(numItems: Int): TimeSeries<T, V> {
            throw UnsupportedOperationException("Not implemented")
        }

        override fun tail(startTimeInclusive: T): TimeSeries<T, V> {
            throw UnsupportedOperationException("Not implemented")
        }
    }
}