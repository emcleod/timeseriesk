package org.emcleod.timeseries

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ImmutableLocalDateTimeSeriesOperatorTest {
    val dates = listOf(
        LocalDate.of(2023, 1, 1),
        LocalDate.of(2023, 1, 2),
        LocalDate.of(2023, 1, 3),
        LocalDate.of(2023, 1, 4),
        LocalDate.of(2023, 1, 5)
    )
    val timeSeries = ImmutableLocalDateDoubleTimeSeries.of(
        dates, listOf(10.0, 20.0, 30.0, 40.0, 50.0)
    )

    @Test
    fun testDoublePlusInt() {
        val summed = timeSeries + 2
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(12.0, 22.0, 32.0, 42.0, 52.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoublePlusLong() {
        val summed = timeSeries + 2L
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(12.0, 22.0, 32.0, 42.0, 52.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoublePlusDouble() {
        val summed = timeSeries + 2.0
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(12.0, 22.0, 32.0, 42.0, 52.0)
        )
        assertEquals(expected, summed)
    }
}