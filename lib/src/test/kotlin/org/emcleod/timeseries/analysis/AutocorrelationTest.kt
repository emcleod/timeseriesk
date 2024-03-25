package org.emcleod.timeseries.analysis

import org.emcleod.timeseries.ImmutableLocalDateDoubleTimeSeries
import java.time.LocalDate
import org.emcleod.timeseries.ImmutableLocalDateLongTimeSeries
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AutocorrelationTest {
    
    @Test
    fun testAutocorrelationWithLag0() {
        val timeSeries = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2023, 1, 1) to 1.0,
                LocalDate.of(2023, 1, 2) to 2.0,
                LocalDate.of(2023, 1, 3) to 3.0,
                LocalDate.of(2023, 1, 4) to 4.0,
                LocalDate.of(2023, 1, 5) to 5.0
            )
        )
        val autocorrelation = autocorrelation(timeSeries, 0)
        assertEquals(1.0, autocorrelation, 1e-10)
    }

    @Test
    fun testAutocorrelationWithPositiveLag() {
        val timeSeries = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2023, 1, 1) to 1.0,
                LocalDate.of(2023, 1, 2) to 2.0,
                LocalDate.of(2023, 1, 3) to 3.0,
                LocalDate.of(2023, 1, 4) to 4.0,
                LocalDate.of(2023, 1, 5) to 5.0
            )
        )
        val autocorrelation = autocorrelation(timeSeries, 1)
        assertEquals(0.9, autocorrelation, 0.1)
    }

    @Test
    fun testAutocorrelationWithNegativeLag() {
        val timeSeries = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2023, 1, 1) to 1.0,
                LocalDate.of(2023, 1, 2) to 2.0,
                LocalDate.of(2023, 1, 3) to 3.0,
                LocalDate.of(2023, 1, 4) to 4.0,
                LocalDate.of(2023, 1, 5) to 5.0
            )
        )
        val autocorrelation = autocorrelation(timeSeries, -1)
        // Adjust the expected value based on your lag implementation
        assertEquals(0.9, autocorrelation, 0.1)
    }
}