package org.emcleod.timeseries

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateTimeSeriesBuilderTest {

    @Test
    fun testBuilderPutAndBuild() {
        val builder = LocalDateLongTimeSeries.Builder()
        builder.put(LocalDate.of(2023, 6, 1), 10L)
        builder.put(LocalDate.of(2023, 6, 2), 20L)

        val ts = builder.build()

        val expectedTs = LocalDateLongTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2023, 6, 1) to 10L,
                LocalDate.of(2023, 6, 2) to 20L
            )
        )
        assertEquals(expectedTs, ts)
    }

    @Test
    fun testBuilderPutAllAndBuild() {
        val builder = LocalDateIntTimeSeries.Builder()
        val map = mapOf(
            LocalDate.of(2023, 6, 1) to 10,
            LocalDate.of(2023, 6, 2) to 20
        )
        builder.putAll(map)

        val ts = builder.build()

        val expectedTs = LocalDateIntTimeSeries.fromMap(
            sortedMapOf(
                LocalDate.of(2023, 6, 1) to 10,
                LocalDate.of(2023, 6, 2) to 20
            )
        )
        assertEquals(expectedTs, ts)
    }

    @Test
    fun testBuilderEquality() {
        val builder1 = LocalDateDoubleTimeSeries.Builder()
        builder1.put(LocalDate.of(2023, 6, 1), 10.5)
        builder1.put(LocalDate.of(2023, 6, 2), 20.0)

        val builder2 = LocalDateDoubleTimeSeries.Builder()
        builder2.put(LocalDate.of(2023, 6, 1), 10.5)
        builder2.put(LocalDate.of(2023, 6, 2), 20.0)

        val dateValueMap1 = builder1.build()
        val dateValueMap2 = builder2.build()

        assertEquals(dateValueMap1, dateValueMap2)
        assertEquals(dateValueMap1.hashCode(), dateValueMap2.hashCode())
    }
}