package org.emcleod.timeseries

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateTimeSeriesTest {
    private val startDate = LocalDate.of(2023, 1, 1)
    private val endDate = LocalDate.of(2023, 1, 10)
    private val keys = (0..9).map { startDate.plusDays(it.toLong()) }
    private val values = (0..9).toList()

    @Test
    fun testEmptySeries() {
        val series = LocalDateTimeSeries.empty<Int>()
        assertTrue(series.isEmpty())
        assertEquals(0, series.getSize())
    }

    @Test
    fun testCreatingSeriesFromMap() {
        val map = keys.zip(values).toMap().toSortedMap()
        val series = LocalDateTimeSeries.fromMap(map)
        assertFalse(series.isEmpty())
        assertEquals(10, series.getSize())
        assertEquals(startDate, series.getKeyAtIndex(0))
        assertEquals(0, series.getValueAtIndex(0))
        assertEquals(endDate, series.getKeyAtIndex(9))
        assertEquals(9, series.getValueAtIndex(9))
    }

    @Test
    fun testCreatingSeriesFromLists() {
        val series = LocalDateTimeSeries.of(keys, values)
        assertFalse(series.isEmpty())
        assertEquals(10, series.getSize())
        assertEquals(startDate, series.getKeyAtIndex(0))
        assertEquals(0, series.getValueAtIndex(0))
        assertEquals(endDate, series.getKeyAtIndex(9))
        assertEquals(9, series.getValueAtIndex(9))
    }

    @Test
    fun testSubSeriesWithInclusiveStartAndExclusiveEnd1() {
        val series = LocalDateTimeSeries.of(keys, values)
        val subSeries = series.subSeries(startDate.plusDays(2), endDate.minusDays(2))
        assertEquals(5, subSeries.getSize())
        assertEquals(startDate.plusDays(2), subSeries.getKeyAtIndex(0))
        assertEquals(2, subSeries.getValueAtIndex(0))
        assertEquals(endDate.minusDays(3), subSeries.getKeyAtIndex(4))
        assertEquals(6, subSeries.getValueAtIndex(4))
    }

    @Test
    fun testSubSeriesWithInclusiveStartAndExclusiveEnd() {
        val series = LocalDateTimeSeries.of(keys, values)
        val subSeries = series.subSeries(startDate.plusDays(2), true, endDate.minusDays(2), false)
        assertEquals(5, subSeries.getSize())
        assertEquals(startDate.plusDays(2), subSeries.getKeyAtIndex(0))
        assertEquals(2, subSeries.getValueAtIndex(0))
        assertEquals(endDate.minusDays(3), subSeries.getKeyAtIndex(4))
        assertEquals(6, subSeries.getValueAtIndex(4))
    }

    @Test
    fun testSubSeriesWithInclusiveStartAndInclusiveEnd() {
        val series = LocalDateTimeSeries.of(keys, values)
        val subSeries = series.subSeries(startDate.plusDays(2), true, endDate.minusDays(2), true)
        assertEquals(6, subSeries.getSize())
        assertEquals(startDate.plusDays(2), subSeries.getKeyAtIndex(0))
        assertEquals(2, subSeries.getValueAtIndex(0))
        assertEquals(endDate.minusDays(2), subSeries.getKeyAtIndex(5))
        assertEquals(7, subSeries.getValueAtIndex(5))
    }

    @Test
    fun testSubSeriesWithExclusiveStartAndInclusiveEnd() {
        val series = LocalDateTimeSeries.of(keys, values)
        val subSeries = series.subSeries(startDate.plusDays(2), false, endDate.minusDays(2), true)
        assertEquals(5, subSeries.getSize())
        assertEquals(startDate.plusDays(3), subSeries.getKeyAtIndex(0))
        assertEquals(3, subSeries.getValueAtIndex(0))
        assertEquals(endDate.minusDays(2), subSeries.getKeyAtIndex(4))
        assertEquals(7, subSeries.getValueAtIndex(4))
    }

    @Test
    fun testSubSeriesWithExclusiveStartAndExclusiveEnd() {
        val series = LocalDateTimeSeries.of(keys, values)
        val subSeries = series.subSeries(startDate.plusDays(2), false, endDate.minusDays(2), false)
        assertEquals(4, subSeries.getSize())
        assertEquals(startDate.plusDays(3), subSeries.getKeyAtIndex(0))
        assertEquals(3, subSeries.getValueAtIndex(0))
        assertEquals(endDate.minusDays(3), subSeries.getKeyAtIndex(3))
        assertEquals(6, subSeries.getValueAtIndex(3))
    }

    @Test
    fun testHeadWithNumberOfItems() {
        val series = LocalDateTimeSeries.of(keys, values)
        val headSeries = series.head(3)
        assertEquals(3, headSeries.getSize())
        assertEquals(startDate, headSeries.getKeyAtIndex(0))
        assertEquals(0, headSeries.getValueAtIndex(0))
        assertEquals(startDate.plusDays(2), headSeries.getKeyAtIndex(2))
        assertEquals(2, headSeries.getValueAtIndex(2))
    }

    @Test
    fun testHeadWithEndTime() {
        val series = LocalDateTimeSeries.of(keys, values)
        val headSeries = series.head(startDate.plusDays(5))
        assertEquals(5, headSeries.getSize())
        assertEquals(startDate, headSeries.getKeyAtIndex(0))
        assertEquals(0, headSeries.getValueAtIndex(0))
        assertEquals(startDate.plusDays(4), headSeries.getKeyAtIndex(4))
        assertEquals(4, headSeries.getValueAtIndex(4))
    }

    @Test
    fun testTailWithNumberOfItems() {
        val series = LocalDateTimeSeries.of(keys, values)
        val tailSeries = series.tail(3)
        assertEquals(3, tailSeries.getSize())
        assertEquals(endDate.minusDays(2), tailSeries.getKeyAtIndex(0))
        assertEquals(7, tailSeries.getValueAtIndex(0))
        assertEquals(endDate, tailSeries.getKeyAtIndex(2))
        assertEquals(9, tailSeries.getValueAtIndex(2))
    }

    @Test
    fun testTailWithStartTime() {
        val series = LocalDateTimeSeries.of(keys, values)
        val tailSeries = series.tail(startDate.plusDays(6))
        assertEquals(4, tailSeries.getSize())
        assertEquals(startDate.plusDays(6), tailSeries.getKeyAtIndex(0))
        assertEquals(6, tailSeries.getValueAtIndex(0))
        assertEquals(endDate, tailSeries.getKeyAtIndex(3))
        assertEquals(9, tailSeries.getValueAtIndex(3))
    }

}