package org.emcleod.timeseries

import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.random.Random

class TimeSeriesUnzipTest {

    @Test
    fun testEmptyTs() {
        val ts = LocalDateDoubleTimeSeries.empty()
        val (keys, values) = ts.unzip()
        assertEquals(emptyList<LocalDate>(), keys)
        assertEquals(emptyList<Int>(), values)
    }

    @Test
    fun testUnzip() {
        val ts = LocalDateDoubleTimeSeries.Builder()
            .put(LocalDate.of(2024, 1, 1), 1.0)
            .put(LocalDate.of(2024, 1, 2), 2.0)
            .put(LocalDate.of(2024, 1, 3), 3.0)
            .build()
        val (keys, values) = ts.unzip()

        assertEquals(
            listOf(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 1, 3)
            ),
            keys
        )
        assertEquals(listOf(1.0, 2.0, 3.0), values)
    }

    // property-based tests
    class UnzipPropertiesTest : StringSpec({

        "unzip should return key and value lists of the same size as the input time series" {
            assertAll(TimeSeriesGenerator.localDateTimeSeriesGen()) { ts ->
                val (keys, values) = ts.unzip()
                keys.size.shouldBe(ts.getSize())
                values.size.shouldBe(ts.getSize())
            }
        }

        "unzip should return a key list with unique elements" {
            assertAll(TimeSeriesGenerator.localDateTimeSeriesGen()) { ts ->
                val (keys, _) = ts.unzip()
                keys.toSet().size.shouldBe(keys.size)
            }
        }

        "unzip should return key and value lists in the same order as the input time series" {
            assertAll(TimeSeriesGenerator.localDateTimeSeriesGen()) { ts ->
                val (keys, values) = ts.unzip()
                keys.zip(values).forEachIndexed { index, pair ->
                    ts.getKeyAtIndex(index).shouldBe(pair.first)
                    ts.getValueAtIndex(index).shouldBe(pair.second)
                }
            }
        }
    })

    object TimeSeriesGenerator {
        fun localDateTimeSeriesGen(): Gen<LocalDateTimeSeries<Long>> = Gen.choose(0, 100).map { size ->
            val builder = LocalDateLongTimeSeries.Builder()
            val startDate = LocalDate.of(2021, 1, 1)
            (0 until size).forEach { i ->
                builder.put(startDate.plusDays(i.toLong()), Random.nextLong())
            }
            builder.build()
        }
    }
}
