package org.emcleod.timeseries

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateTimeSeriesOperatorTest {
    private val _dates = listOf(
        LocalDate.of(2023, 1, 1),
        LocalDate.of(2023, 1, 2),
        LocalDate.of(2023, 1, 3),
        LocalDate.of(2023, 1, 4),
        LocalDate.of(2023, 1, 5)
    )
    private val _timeSeries = LocalDateDoubleTimeSeries.of(
        _dates, listOf(10.0, 20.0, 30.0, 40.0, 50.0)
    )

    @Test
    fun testDoublePlusInt() {
        val result = _timeSeries + 2
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(12.0, 22.0, 32.0, 42.0, 52.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoublePlusLong() {
        val result = _timeSeries + 2L
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(12.0, 22.0, 32.0, 42.0, 52.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoublePlusDouble() {
        val result = _timeSeries + 2.0
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(12.0, 22.0, 32.0, 42.0, 52.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleMinusInt() {
        val result = _timeSeries - 2
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(8.0, 18.0, 28.0, 38.0, 48.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleMinusLong() {
        val result = _timeSeries - 2L
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(8.0, 18.0, 28.0, 38.0, 48.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleMinusDouble() {
        val result = _timeSeries - 2.0
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(8.0, 18.0, 28.0, 38.0, 48.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleTimesInt() {
        val result = _timeSeries * 2
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(20.0, 40.0, 60.0, 80.0, 100.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleTimesLong() {
        val result = _timeSeries * 2L
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(20.0, 40.0, 60.0, 80.0, 100.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleTimesDouble() {
        val result = _timeSeries * 2.0
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(20.0, 40.0, 60.0, 80.0, 100.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleDivideInt() {
        val result = _timeSeries / 2
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(5.0, 10.0, 15.0, 20.0, 25.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleDivideLong() {
        val result = _timeSeries / 2L
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(5.0, 10.0, 15.0, 20.0, 25.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleDivideDouble() {
        val result = _timeSeries / 2.0
        val expected = LocalDateDoubleTimeSeries.of(
            _dates, listOf(5.0, 10.0, 15.0, 20.0, 25.0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun testPlusOperatorWithSameDates() {
        val series1 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 5.0,
                LocalDate.of(2023, 1, 2) to 15.0
            )
        )

        val result = series1 + series2

        assertEquals(2, result.getSize())
        assertEquals(15.0, result[LocalDate.of(2023, 1, 1)])
        assertEquals(35.0, result[LocalDate.of(2023, 1, 2)])
    }

    @Test
    fun testPlusOperatorWithDifferentDates() {
        val series1 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 2) to 15.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )

        val result = series1 + series2

        assertEquals(1, result.getSize())
        assertEquals(35.0, result[LocalDate.of(2023, 1, 2)])
    }

    @Test
    fun testPlusOperatorIsCommutative() {
        val series1 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 2) to 15.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )

        val result1 = series1 + series2
        val result2 = series2 + series1

        assertEquals(result1, result2)
    }

    @Test
    fun plusHandlesEmptyTimeSeries() {
        val series1 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )
        val series2 = LocalDateDoubleTimeSeries.empty()
        val result = series1 + series2

        assertEquals(LocalDateDoubleTimeSeries.empty(), result)
    }

    @Test
    fun testMinusOperatorWithSameDates() {
        val series1 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 5.0,
                LocalDate.of(2023, 1, 2) to 15.0
            )
        )

        val result = series1 - series2

        assertEquals(2, result.getSize())
        assertEquals(5.0, result[LocalDate.of(2023, 1, 1)])
        assertEquals(5.0, result[LocalDate.of(2023, 1, 2)])
    }

    @Test
    fun testMinusOperatorWithDifferentDates() {
        val series1 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0
            )
        )
        val series2 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 2) to 15.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )

        val result = series1 - series2

        assertEquals(1, result.getSize())
        assertEquals(5.0, result[LocalDate.of(2023, 1, 2)])
    }

    @Test
    fun minusHandlesEmptyTimeSeries1() {
        val series1 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )
        val series2 = LocalDateDoubleTimeSeries.empty()
        val result = series1 - series2
        assertEquals(LocalDateDoubleTimeSeries.empty(), result)

    }

    @Test
    fun minusHandlesEmptyTimeSeries2() {
        val series1 = LocalDateDoubleTimeSeries.empty()
        val series2 = LocalDateDoubleTimeSeries.fromMap(
            mapOf(
                LocalDate.of(2023, 1, 1) to 10.0,
                LocalDate.of(2023, 1, 2) to 20.0,
                LocalDate.of(2023, 1, 3) to 30.0
            )
        )
        val expected = series1.times(-1)

        val result = series1 - series2

        assertEquals(expected, result)

    }

}