package org.emcleod.timeseries.analysis

import org.emcleod.timeseries.LocalDateDoubleTimeSeries
import org.emcleod.timeseries.lag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LagTest {
    @Test
    fun zeroLag() {
        val series = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.1,
                LocalDate.of(2024, 1, 2) to 2.2,
                LocalDate.of(2024, 1, 3) to 2.3,
                LocalDate.of(2024, 1, 4) to 2.4
            )
        )
        val lagged = series.lag(0)
        assertEquals(series, lagged)
    }

    @Test
    fun negativeLagOne() {
        val series = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.1,
                LocalDate.of(2024, 1, 2) to 2.2,
                LocalDate.of(2024, 1, 3) to 2.3,
                LocalDate.of(2024, 1, 4) to 2.4
            )
        )
        val lagged = series.lag(-1)
        val expected = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 2) to 2.1,
                LocalDate.of(2024, 1, 3) to 2.2,
                LocalDate.of(2024, 1, 4) to 2.3
            )
        )
        assertEquals(expected, lagged)
    }

    @Test
    fun negativeLagTwo() {
        val series = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.1,
                LocalDate.of(2024, 1, 2) to 2.2,
                LocalDate.of(2024, 1, 3) to 2.3,
                LocalDate.of(2024, 1, 4) to 2.4
            )
        )
        val lagged = series.lag(-2)
        val expected = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 3) to 2.1,
                LocalDate.of(2024, 1, 4) to 2.2
            )
        )
        assertEquals(expected, lagged)
    }

    @Test
    fun positiveLagOne() {
        val series = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.1,
                LocalDate.of(2024, 1, 2) to 2.2,
                LocalDate.of(2024, 1, 3) to 2.3,
                LocalDate.of(2024, 1, 4) to 2.4
            )
        )
        val lagged = series.lag(1)
        val expected = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.2,
                LocalDate.of(2024, 1, 2) to 2.3,
                LocalDate.of(2024, 1, 3) to 2.4
            )
        )
        assertEquals(expected, lagged)
    }

    @Test
    fun positiveLagTwo() {
        val series = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.1,
                LocalDate.of(2024, 1, 2) to 2.2,
                LocalDate.of(2024, 1, 3) to 2.3,
                LocalDate.of(2024, 1, 4) to 2.4
            )
        )
        val lagged = series.lag(2)
        val expected = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.3,
                LocalDate.of(2024, 1, 2) to 2.4
            )
        )
        assertEquals(expected, lagged)
    }

    @Test
    fun lagLargerThanSeriesSize() {
        val series = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.1,
                LocalDate.of(2024, 1, 2) to 2.2,
                LocalDate.of(2024, 1, 3) to 2.3
            )
        )
        val lagged = series.lag(4)
        val expected = LocalDateDoubleTimeSeries.empty()
        assertEquals(expected, lagged)
    }

    @Test
    fun negativeLagLargerThanSeriesSize() {
        val series = LocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 2.1,
                LocalDate.of(2024, 1, 2) to 2.2,
                LocalDate.of(2024, 1, 3) to 2.3
            )
        )
        val lagged = series.lag(-4)
        val expected = LocalDateDoubleTimeSeries.empty()
        assertEquals(expected, lagged)
    }
}