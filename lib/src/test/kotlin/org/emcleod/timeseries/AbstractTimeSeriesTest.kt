package org.emcleod.timeseries

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AbstractTimeSeriesTest {

    @Test
    fun testEqualsMethodWithSameImplementation() {
        val times = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2))
        val values = listOf(1.0, 2.0)

        val series1 = LocalDateTimeSeries.of(times, values)
        val series2 = LocalDateTimeSeries.of(times, values)

        assertEquals(series1, series2)
    }

    @Test
    fun testEqualsMethodWithDifferentImplementations() {
        val times = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2))
        val values = listOf(1.0, 2.0)

        val series1 = LocalDateTimeSeries.of(times, values)
        val series2 = MockDateTimeSeries(times, values)

        assertNotEquals(series1, series2)
    }

    @Test
    fun testEqualsMethodWithDifferentData() {
        val times1 = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2))
        val values1 = listOf(1.0, 2.0)

        val times2 = listOf(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 3))
        val values2 = listOf(1.0, 3.0)

        val series1 = LocalDateTimeSeries.of(times1, values1)
        val series2 = MockDateTimeSeries(times2, values2)

        assertNotEquals(series1, series2)
    }

    @Test
    fun testToString() {
        val entries = sortedMapOf(
            LocalDate.of(2024, 1, 1) to 13.0,
            LocalDate.of(2024, 1, 2) to 14.0,
            LocalDate.of(2024, 1, 3) to 15.0,
        )
        val ts = LocalDateTimeSeries.fromMap(entries)

        val result = ts.toString()

        val expected = "LocalDateTimeSeries[(2024-01-01, 13.0), (2024-01-02, 14.0), (2024-01-03, 15.0)]"
        assertEquals(expected, result)
    }

    @Test
    fun testHashCode() {
        val entries = sortedMapOf(
            LocalDate.of(2024, 1, 1) to 13.0,
            LocalDate.of(2024, 1, 2) to 14.0,
            LocalDate.of(2024, 1, 3) to 15.0,
        )
        val ts = LocalDateTimeSeries.fromMap(entries)

        val result = ts.hashCode()

        val expected = entries.hashCode()
        assertEquals(expected, result)
    }

    class MockDateTimeSeries<T : Comparable<T>, V>(times: List<T>, values: List<V>) :
        AbstractTimeSeries<T, V, MockDateTimeSeries<T, V>>(times, values) {

        override fun fromMap(entries: Map<T, V>): MockDateTimeSeries<T, V> {
            throw UnsupportedOperationException("Not implemented")
        }
    }
}