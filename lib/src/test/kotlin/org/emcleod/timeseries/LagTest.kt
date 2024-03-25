package org.emcleod.timeseries.analysis

import org.emcleod.timeseries.ImmutableLocalDateDoubleTimeSeries
import org.emcleod.timeseries.lag
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LagTest {
    @Test
    fun zeroLag() {
        val series = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.1,
                        LocalDate.of(2024, 1, 2) to 2.2,
                        LocalDate.of(2024, 1, 3) to 2.3,
                        LocalDate.of(2024, 1, 4) to 2.4)
        )
        val lagged = series.lag(0)
        assertEquals(series, lagged)
    }

    @Test
    fun negativeLagOne() {
        val series = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.1,
                        LocalDate.of(2024, 1, 2) to 2.2,
                        LocalDate.of(2024, 1, 3) to 2.3,
                        LocalDate.of(2024, 1, 4) to 2.4)
        )
        val lagged = series.lag(-1)
        val expected = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 2) to 2.1,
                        LocalDate.of(2024, 1, 3) to 2.2,
                        LocalDate.of(2024, 1, 4) to 2.3)
        )        
        assertEquals(expected, lagged)
    }

    @Test
    fun negativeLagTwo() {
        val series = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.1,
                        LocalDate.of(2024, 1, 2) to 2.2,
                        LocalDate.of(2024, 1, 3) to 2.3,
                        LocalDate.of(2024, 1, 4) to 2.4)
        )
        val lagged = series.lag(-2)
        val expected = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 3) to 2.1,
                        LocalDate.of(2024, 1, 4) to 2.2)
        )        
        assertEquals(expected, lagged)
    }

    @Test
    fun positiveLagOne() {
        val series = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.1,
                        LocalDate.of(2024, 1, 2) to 2.2,
                        LocalDate.of(2024, 1, 3) to 2.3,
                        LocalDate.of(2024, 1, 4) to 2.4)
        )
        val lagged = series.lag(1)
        val expected = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.2,
                        LocalDate.of(2024, 1, 2) to 2.3,
                        LocalDate.of(2024, 1, 3) to 2.4)
        )        
        assertEquals(expected, lagged)
    }

    @Test
    fun positiveLagTwo() {
        val series = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.1,
                        LocalDate.of(2024, 1, 2) to 2.2,
                        LocalDate.of(2024, 1, 3) to 2.3,
                        LocalDate.of(2024, 1, 4) to 2.4)
        )
        val lagged = series.lag(2)
        val expected = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.3,
                        LocalDate.of(2024, 1, 2) to 2.4)
        )        
        assertEquals(expected, lagged)
    }

    @Test
    fun lagLargerThanSeriesSize() {
        val series = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.1,
                        LocalDate.of(2024, 1, 2) to 2.2,
                        LocalDate.of(2024, 1, 3) to 2.3)
        )
        val lagged = series.lag(4)
        val expected = ImmutableLocalDateDoubleTimeSeries.empty()
        assertEquals(expected, lagged)
    }

    @Test
    fun negativeLagLargerThanSeriesSize() {
        val series = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(LocalDate.of(2024, 1, 1) to 2.1,
                        LocalDate.of(2024, 1, 2) to 2.2,
                        LocalDate.of(2024, 1, 3) to 2.3)
        )
        val lagged = series.lag(-4)
        val expected = ImmutableLocalDateDoubleTimeSeries.empty()
        assertEquals(expected, lagged)
    }
}