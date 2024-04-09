package org.emcleod.timeseries

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateTimeSeriesFunctionalTest {

    @Test
    fun testFilter() {
        val timeSeries = LocalDateTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3),
                LocalDate.of(2023, 1, 4),
                LocalDate.of(2023, 1, 5)
            ),
            listOf(10.0, 20.0, 30.0, 40.0, 50.0)
        )

        val filteredTimeSeries = timeSeries.filter { entry -> entry.value > 25.0 }

        assertEquals(3, filteredTimeSeries.getSize())
        assertTrue(filteredTimeSeries.contains(LocalDate.of(2023, 1, 3)))
        assertTrue(filteredTimeSeries.contains(LocalDate.of(2023, 1, 4)))
        assertTrue(filteredTimeSeries.contains(LocalDate.of(2023, 1, 5)))
        assertEquals(30.0, filteredTimeSeries[LocalDate.of(2023, 1, 3)])
        assertEquals(40.0, filteredTimeSeries[LocalDate.of(2023, 1, 4)])
        assertEquals(50.0, filteredTimeSeries[LocalDate.of(2023, 1, 5)])
    }

    @Test
    fun testMap() {
        val timeSeries = LocalDateTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3)
            ),
            listOf(10.1, 20.2, 30.3)
        )

        val transform: (Map.Entry<LocalDate, Double>) -> Double = { (_, value) -> value * 2.0 }
        val mappedTimeSeries = timeSeries.map(transform)

        assertEquals(20.2, mappedTimeSeries[LocalDate.of(2023, 1, 1)])
        assertEquals(40.4, mappedTimeSeries[LocalDate.of(2023, 1, 2)])
        assertEquals(60.6, mappedTimeSeries[LocalDate.of(2023, 1, 3)])
    }

    @Test
    fun testFold() {
        val timeSeries = LocalDateTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3)
            ),
            listOf(10.0, 20.0, 30.0)
        )

        val initialValue = 0.0
        val operation = { acc: Double, value: Double -> acc + (value * 2) }

        val foldResult = timeSeries.fold(initialValue, operation)

        assertEquals(120.0, foldResult, 0.0)
    }

    @Test
    fun testReduce() {
        val timeSeries = LocalDateTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3)
            ),
            listOf(10.0, 20.0, 30.0)
        )

        val initialValue = 0.0
        val reduceResult = timeSeries.reduce(initialValue) { acc, value -> acc + value }

        assertEquals(60.0, reduceResult, 0.0)
    }

    @Test
    fun testForEach() {
        val timeSeries = LocalDateTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3)
            ),
            listOf(10.0, 20.0, 30.0)
        )

        val expectedDates = listOf(
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 1, 2),
            LocalDate.of(2023, 1, 3)
        )
        val expectedValues = listOf(10.0, 20.0, 30.0)

        var index = 0
        timeSeries.forEach { date, value ->
            assertEquals(expectedDates[index], date)
            assertEquals(expectedValues[index], value, 0.0)
            index++
        }
    }

    @Test
    fun testAny() {
        val timeSeries = LocalDateTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3)
            ),
            listOf(10.0, 20.0, 30.0)
        )

        val matchKey: (Map.Entry<LocalDate, Double>) -> Boolean = { (key, _) -> key == LocalDate.of(2023, 1, 1) }
        val noMatchKey: (Map.Entry<LocalDate, Double>) -> Boolean = { (key, _) -> key == LocalDate.of(2023, 1, 5) }
        val matchValue: (Map.Entry<LocalDate, Double>) -> Boolean = { (_, value) -> value == 20.0 }
        val noMatchValue: (Map.Entry<LocalDate, Double>) -> Boolean = { (_, value) -> value == 25.0 }

        assertTrue(timeSeries.any(matchKey))
        assertFalse(timeSeries.any(noMatchKey))
        assertTrue(timeSeries.any(matchValue))
        assertFalse(timeSeries.any(noMatchValue))
    }

    @Test
    fun testAll() {
        val timeSeries = LocalDateTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3)
            ),
            listOf(10.0, 20.0, 30.0)
        )

        val matchKey: (Map.Entry<LocalDate, Double>) -> Boolean = { (key, _) -> key.isBefore(LocalDate.of(2023, 1, 6)) }
        val noMatchKey: (Map.Entry<LocalDate, Double>) -> Boolean =
            { (key, _) -> key.isAfter(LocalDate.of(2023, 1, 2)) }
        val matchValue: (Map.Entry<LocalDate, Double>) -> Boolean = { (_, value) -> value > 0 }
        val noMatchValue: (Map.Entry<LocalDate, Double>) -> Boolean = { (_, value) -> value < 30 }

        assertTrue(timeSeries.all(matchKey))
        assertFalse(timeSeries.all(noMatchKey))
        assertTrue(timeSeries.all(matchValue))
        assertFalse(timeSeries.all(noMatchValue))
    }
}