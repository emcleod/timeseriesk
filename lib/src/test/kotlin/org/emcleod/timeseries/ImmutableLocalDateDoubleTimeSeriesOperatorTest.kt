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

    @Test
    fun testDoubleMinusInt() {
        val summed = timeSeries - 2
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(8.0, 18.0, 28.0, 38.0, 48.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleMinusLong() {
        val summed = timeSeries - 2L
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(8.0, 18.0, 28.0, 38.0, 48.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleMinusDouble() {
        val summed = timeSeries - 2.0
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(8.0, 18.0, 28.0, 38.0, 48.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleTimesInt() {
        val summed = timeSeries * 2
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(20.0, 40.0, 60.0, 80.0, 100.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleTimesLong() {
        val summed = timeSeries * 2L
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(20.0, 40.0, 60.0, 80.0, 100.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleTimesDouble() {
        val summed = timeSeries * 2.0
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(20.0, 40.0, 60.0, 80.0, 100.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleDivideInt() {
        val summed = timeSeries / 2
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(5.0, 10.0, 15.0, 20.0, 25.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleDivideLong() {
        val summed = timeSeries / 2L
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(5.0, 10.0, 15.0, 20.0, 25.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testDoubleDivideDouble() {
        val summed = timeSeries / 2.0
        val expected = ImmutableLocalDateDoubleTimeSeries.of(
            dates, listOf(5.0, 10.0, 15.0, 20.0, 25.0)
        )
        assertEquals(expected, summed)
    }

    @Test
    fun testPlusOperatorWithSameDates() {
        val series1 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 5.0,
                LocalDate.of(2023, 1, 2) to 15.0
            )
        )

        val result = series1 + series2

        assertEquals(2, result.entries.size)
        assertEquals(15.0, result.entries[LocalDate.of(2023, 1, 1)])
        assertEquals(35.0, result.entries[LocalDate.of(2023, 1, 2)])
    }

    @Test
    fun testPlusOperatorWithDifferentDates() {
        val series1 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 2) to 15.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )

        val result = series1 + series2

        assertEquals(3, result.entries.size)
        assertEquals(10.0, result.entries[LocalDate.of(2023, 1, 1)])
        assertEquals(35.0, result.entries[LocalDate.of(2023, 1, 2)])
        assertEquals(30.0, result.entries[LocalDate.of(2023, 1, 3)])
    }

    @Test
    fun testPlusOperatorIsCommutative() {
        val series1 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 2) to 15.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )

        val result1 = series1 + series2
        val result2 = series2 + series1

        assertEquals(result1.entries, result2.entries)
    }

    @Test
    fun plusHandlesEmptyTimeSeries() {
        val series1 = ImmutableLocalDateDoubleTimeSeries.fromMap(mapOf(
            LocalDate.of(2023, 1, 1) to 10.0,
            LocalDate.of(2023, 1, 2) to 20.0,
            LocalDate.of(2023, 1, 3) to 30.0
        ))
        val series2 = ImmutableLocalDateDoubleTimeSeries.empty()
        val expected = series1

        val result = series1 + series2

        assertEquals(expected, result)
    }

    @Test
    fun testMinusOperatorWithSameDates() {
        val series1 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 5.0,
                LocalDate.of(2023, 1, 2) to 15.0
            )
        )

        val result = series1 - series2

        assertEquals(2, result.entries.size)
        assertEquals(5.0, result.entries[LocalDate.of(2023, 1, 1)])
        assertEquals(5.0, result.entries[LocalDate.of(2023, 1, 2)])
    }

    @Test
    fun testMinusOperatorWithDifferentDates() {
        val series1 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = ImmutableLocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 2) to 15.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )

        val result = series1 - series2

        assertEquals(3, result.entries.size)
        assertEquals(10.0, result.entries[LocalDate.of(2023, 1, 1)])
        assertEquals(5.0, result.entries[LocalDate.of(2023, 1, 2)])
        assertEquals(-30.0, result.entries[LocalDate.of(2023, 1, 3)])
    }

    @Test
    fun minusHandlesEmptyTimeSeries1() {
        val series1 = ImmutableLocalDateDoubleTimeSeries.fromMap(mapOf(
            LocalDate.of(2023, 1, 1) to 10.0,
            LocalDate.of(2023, 1, 2) to 20.0,
            LocalDate.of(2023, 1, 3) to 30.0
        ))
        val series2 = ImmutableLocalDateDoubleTimeSeries.empty()
        val expected = series1

        val result = series1 - series2

        assertEquals(expected, result)

    }

    // @Test
    // fun minusHandlesEmptyTimeSeries2() {
    //     val series1 = ImmutableLocalDateDoubleTimeSeries.empty()
    //     val series2 = ImmutableLocalDateDoubleTimeSeries.fromMap(mapOf(
    //         LocalDate.of(2023, 1, 1) to 10.0,
    //         LocalDate.of(2023, 1, 2) to 20.0,
    //         LocalDate.of(2023, 1, 3) to 30.0
    //     ))
    //     val expected = series1.times(-1)

    //     val result = series1 - series2

    //     assertEquals(expected, result)

    // }

}