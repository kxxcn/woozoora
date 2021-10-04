package dev.kxxcn.woozoora.util

import dev.kxxcn.woozoora.R
import kotlin.random.Random

object ColorGenerator {

    private val COLOR_TEMPLATE = listOf(
        R.color.generate_color_01,
        R.color.generate_color_02,
        R.color.generate_color_03,
        R.color.generate_color_04,
        R.color.generate_color_05,
        R.color.generate_color_06,
        R.color.generate_color_07,
        R.color.generate_color_08,
        R.color.generate_color_09,
        R.color.generate_color_10,
        R.color.generate_color_11,
        R.color.generate_color_12,
    )

    fun generate(size: Int = COLOR_TEMPLATE.size): List<Int> {
        return try {
            COLOR_TEMPLATE.subList(0, size)
        } catch (e: Exception) {
            COLOR_TEMPLATE
        }
    }

    fun random(): Int {
        return try {
            val size = COLOR_TEMPLATE.size
            val index = Random.nextInt(0, size - 1)
            COLOR_TEMPLATE[index]
        } catch (e: Exception) {
            COLOR_TEMPLATE[0]
        }
    }
}
