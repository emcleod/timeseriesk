package org.emcleod.timeseries.analysis

import org.emcleod.timeseries.ImmutableLocalDateDoubleTimeSeries
import java.time.LocalDate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.random.Random

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

    @Test
    fun testAutocorrelationSeriesPerfectCorrelation() {
        val ts = ImmutableLocalDateDoubleTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2024, 1, 1) to 1.0,
                LocalDate.of(2024, 1, 2) to 2.0,
                LocalDate.of(2024, 1, 3) to 3.0,
                LocalDate.of(2024, 1, 4) to 4.0,
                LocalDate.of(2024, 1, 5) to 5.0,
                LocalDate.of(2024, 1, 6) to 6.0,
                LocalDate.of(2024, 1, 7) to 7.0,
                LocalDate.of(2024, 1, 8) to 8.0,
                LocalDate.of(2024, 1, 9) to 9.0,
                LocalDate.of(2024, 1, 10) to 10.0,
            )
        )
        val lag = 7
        val autocorrelations = generateAutocorrelations(ts, lag)
        assertEquals(lag + 1, autocorrelations.size)
        autocorrelations.forEachIndexed { index, pair ->
            assertEquals(index, pair.first)
            assertEquals(1.0, pair.second, 1e-10)
        }
    }

    @Test
    fun testAutocorrelationsOfRandomSeries() {
        val seed = 123
        val random = Random(seed)
        val size = 150
        val ts = ImmutableLocalDateDoubleTimeSeries.of(
            List(size) { index -> LocalDate.of(2023, 1, 1).plusDays(index.toLong()) },
            List(size) { _ -> random.nextDouble() }
        )
        val autocorrelations = generateAutocorrelations(ts, size / 2)
        autocorrelations.forEachIndexed { index, (lag, autocorrelation) ->
            assertEquals(index, lag)
            when (index) {
                0 -> assertEquals(1.0, autocorrelation, 1e-10)
                else -> assertEquals(0.0, abs(autocorrelation), 0.3)
            }
        }
    }

    @Test
    fun testAutocorrelationWithEmptySeries() {
        val ts = ImmutableLocalDateDoubleTimeSeries.empty()
        assertThrows(IllegalArgumentException::class.java) {
            autocorrelation(ts, 1)
        }
    }

    @Test
    fun testAutocorrelationWithInsufficientData() {
        val ts = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2023, 1, 1)), listOf(0.0)
        )
        assertThrows(IllegalArgumentException::class.java) {
            autocorrelation(ts, 1)
        }
    }
}