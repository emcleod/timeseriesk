package org.emcleod.timeseries.sample

import org.apache.commons.math3.random.GaussianRandomGenerator
import org.apache.commons.math3.random.UniformRandomGenerator
import org.apache.commons.math3.random.Well19937c

fun generateWithUniformErrors(size: Int, mean: Double = 0.0, scale: Double = 1.0): List<Pair<Int, Double>> {
    val generator = UniformRandomGenerator(Well19937c())
    return 0.until(size).map { it -> Pair(it, generator.nextNormalizedDouble() * scale + mean) }.toList()
}

fun generateWithGaussianErrors(size: Int, mean: Double = 0.0, scale: Double = 1.0): List<Pair<Int, Double>> {
    val generator = GaussianRandomGenerator(Well19937c())
    return 0.until(size).map { it -> Pair(it, generator.nextNormalizedDouble() * scale + mean) }.toList()
}