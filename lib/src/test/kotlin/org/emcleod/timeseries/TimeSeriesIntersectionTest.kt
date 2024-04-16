package org.emcleod.timeseries

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.random.Random

class TimeSeriesIntersectionTest {

    @Test
    fun testOverlappingDates() {
        val series1 = LocalDateDoubleTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3)
            ),
            listOf(1.0, 2.0, 3.0)
        )

        val series2 = LocalDateDoubleTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 3),
                LocalDate.of(2023, 1, 4)
            ),
            listOf(4.0, 5.0, 6.0)
        )

        val intersected = series1.intersect(series2)

        assertEquals(2, intersected.getSize())
        assertEquals(2.0, intersected.get(LocalDate.of(2023, 1, 2)))
        assertEquals(3.0, intersected.get(LocalDate.of(2023, 1, 3)))
    }

    @Test
    fun testNoOverlappingDates() {
        val series1 = LocalDateDoubleTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2)
            ),
            listOf(1.0, 2.0)
        )

        val series2 = LocalDateDoubleTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 3),
                LocalDate.of(2023, 1, 4)
            ),
            listOf(3.0, 4.0)
        )
        val intersected = series1.intersect(series2)
        assertTrue(intersected.isEmpty())
    }

    @Test
    fun testEmptyTimeSeries() {
        val series1 = LocalDateDoubleTimeSeries.of(
            listOf(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2)
            ),
            listOf(1.0, 2.0)
        )
        val series2 = LocalDateDoubleTimeSeries.empty()
        val intersected = series1.intersect(series2)
        assertTrue(intersected.isEmpty())
    }

    // property-based tests
    class IntersectPropertyTest : StringSpec({

        "intersect should return a subset of the original time series" {
            forAll(
                TimeSeriesGenerator.localDateTimeSeriesGen(),
                TimeSeriesGenerator.localDateTimeSeriesGen()
            ) { series1, series2 ->
                val intersected = series1.intersect(series2)
                intersected.getSize() <= series1.getSize() &&
                        intersected.getSize() <= series2.getSize() &&
                        intersected.all { (date, value) ->
                            series1.contains(date) && series1.get(date) == value
                        }
            }
        }

        "intersect should be commutative" {
            forAll(
                TimeSeriesGenerator.localDateTimeSeriesGen(),
                TimeSeriesGenerator.localDateTimeSeriesGen()
            ) { series1, series2 ->
                val intersected1 = series1.intersect(series2)
                val intersected2 = series2.intersect(series1)
                intersected1.getEntries().keys == intersected2.getEntries().keys
            }
        }

        "intersect with an empty time series should return an empty time series" {
            forAll(TimeSeriesGenerator.localDateTimeSeriesGen()) { series ->
                val emptyTimeSeries = LocalDateIntTimeSeries.empty()
                val intersected = series.intersect(emptyTimeSeries)
                intersected.isEmpty()
            }
        }
    })

    object TimeSeriesGenerator {
        fun localDateTimeSeriesGen(): Gen<LocalDateIntTimeSeries> = Gen.choose(0, 100).map { size ->
            val builder = LocalDateIntTimeSeries.Builder()
            val startDate = LocalDate.of(2021, 1, 1)
            (0 until size).forEach { i ->
                builder.put(startDate.plusDays(i.toLong()), Random.nextInt())
            }
            builder.build()
        }
    }
}