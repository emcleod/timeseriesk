package org.emcleod.timeseries.analysis

import org.emcleod.timeseries.ImmutableLocalDateDoubleTimeSeries
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PearsonCorrelationTest {
    
    @Test
    fun testPerfectPositiveCorrelation() {
        val ts1 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2024, 1, 1), 
                LocalDate.of(2024, 1, 2), 
                LocalDate.of(2024, 1, 3), 
                LocalDate.of(2024, 1, 4),
                LocalDate.of(2024, 1, 5)),
            listOf(1.0, 2.0, 3.0, 4.0, 5.0))
            val ts2 = ImmutableLocalDateDoubleTimeSeries.of(
                listOf(LocalDate.of(2024, 1, 1), 
                    LocalDate.of(2024, 1, 2), 
                    LocalDate.of(2024, 1, 3), 
                    LocalDate.of(2024, 1, 4),
                    LocalDate.of(2024, 1, 5)),    
            listOf(2.0, 4.0, 6.0, 8.0, 10.0))
        val correlation = pearsonCorrelation(ts1, ts2)
        assertEquals(1.0, correlation, 1e-10)
    }

    @Test
    fun testPerfectNegativeCorrelation() {
        val ts1 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2024, 1, 1), 
                LocalDate.of(2024, 1, 2), 
                LocalDate.of(2024, 1, 3), 
                LocalDate.of(2024, 1, 4),
                LocalDate.of(2024, 1, 5)),
            listOf(1.0, 2.0, 3.0, 4.0, 5.0))
        val ts2 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2024, 1, 1), 
                LocalDate.of(2024, 1, 2), 
                LocalDate.of(2024, 1, 3), 
                LocalDate.of(2024, 1, 4),
                LocalDate.of(2024, 1, 5)),
            listOf(10.0, 8.0, 6.0, 4.0, 2.0))
        val correlation = pearsonCorrelation(ts1, ts2)
        assertEquals(-1.0, correlation, 1e-10)
    }

    @Test
    fun testNoCorrelation() {
        val ts1 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2024, 1, 1), 
                LocalDate.of(2024, 1, 2), 
                LocalDate.of(2024, 1, 3), 
                LocalDate.of(2024, 1, 4),
                LocalDate.of(2024, 1, 5)),
            listOf(1.0, 2.0, 3.0, 4.0, 5.0))
        val ts2 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2024, 1, 1), 
                LocalDate.of(2024, 1, 2), 
                LocalDate.of(2024, 1, 3), 
                LocalDate.of(2024, 1, 4),
                LocalDate.of(2024, 1, 5)),
            listOf(5.0, -1.0, 5.0, -1.0, 5.0))
        val correlation = pearsonCorrelation(ts1, ts2)
        assertEquals(0.0, correlation, 0.1)
    }

    @Test
    fun testArraysOfDifferentLengths() {
        val ts1 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2024, 1, 1), 
                LocalDate.of(2024, 1, 2), 
                LocalDate.of(2024, 1, 3), 
                LocalDate.of(2024, 1, 4),
                LocalDate.of(2024, 1, 5)),
            listOf(1.0, 2.0, 3.0, 4.0, 5.0))
        val ts2 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2024, 1, 1), 
                LocalDate.of(2024, 1, 2), 
                LocalDate.of(2024, 1, 3), 
                LocalDate.of(2024, 1, 4)),
            listOf(4.0, 5.0, 6.0, 7.0))
        assertThrows(IllegalArgumentException::class.java) {
            pearsonCorrelation(ts1, ts2)
        }
    }

    @Test
    fun testCorrelationWithEmptySeries() {
        val ts = ImmutableLocalDateDoubleTimeSeries.empty()
        assertThrows(IllegalArgumentException::class.java) {
            pearsonCorrelation(ts, ts)
        }
    }

    @Test
    fun testCorrelationWithInsufficientData() {
        val ts1 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2023, 1, 1)), listOf(0.0)
        )
        val ts2 = ImmutableLocalDateDoubleTimeSeries.of(
            listOf(LocalDate.of(2023, 1, 1)), listOf(0.1)
        )
        assertThrows(IllegalArgumentException::class.java) {
            pearsonCorrelation(ts1, ts2)
        }
    }

}
