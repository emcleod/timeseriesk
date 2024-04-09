package org.emcleod.timeseries.sample

fun generateMA1Series(size: Int, theta: Double, mean: Double = 0.0, scale: Double = 1.0): List<Pair<Int, Double>> {
    val whiteNoise = generateWithUniformErrors(size + 1, mean, scale)

    return whiteNoise.drop(1).mapIndexed { index, (_, current) ->
        Pair(index, current + theta * whiteNoise[index].second)
    }
}

fun generateMASeries(
    size: Int,
    thetas: List<Double>,
    mean: Double = 0.0,
    scale: Double = 1.0
): List<Pair<Int, Double>> {
    val q = thetas.size
    val whiteNoise = generateWithUniformErrors(size + q, mean, scale)
    return whiteNoise.drop(q).mapIndexed { index, (_, current) ->
        val maValue = current + (0 until q).sumOf { i ->
            thetas[i] * whiteNoise[index + i].second
        }
        Pair(index, maValue)
    }
}

//fun generateMAQSeries(
//    size: Int,
//    thetas: List<Double>,
//    mean: Double = 0.0,
//    scale: Double = 1.0
//): List<Pair<Int, Double>> {
//    val q = thetas.size
//    val whiteNoise = generateWithUniformErrors(size + q, mean, scale)
//
//    return whiteNoise.take(size).mapIndexed { index, (_, current) ->
//        val maValue = current + (1..q).sumOf { i ->
//            if (index - i >= 0) thetas[i - 1] * whiteNoise[index - i].second else 0.0
//        }
//        Pair(index, maValue)
//    }
//}

fun generateMAQSeries(
    size: Int,
    thetas: List<Double>,
    mean: Double = 0.0,
    scale: Double = 1.0
): List<Pair<Int, Double>> {
    val q = thetas.size
    val whiteNoise = generateWithUniformErrors(size, mean, scale)
    return whiteNoise.mapIndexed { index, (_, current) ->
        val maxOffset = minOf(index, q)
        val maValue = current + (0 until maxOffset).sumOf { i ->
            thetas[i] * whiteNoise[index - maxOffset].second
        }
        Pair(index, maValue)
    }
}