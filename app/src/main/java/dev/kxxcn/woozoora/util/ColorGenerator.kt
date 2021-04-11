package dev.kxxcn.woozoora.util

import dev.kxxcn.woozoora.R

object ColorGenerator {

    private val COLOR_TEMPLATE = listOf(
        R.color.generate_color_01,
        R.color.generate_color_02,
        R.color.generate_color_03,
        R.color.generate_color_04,
        R.color.generate_color_01,
        R.color.generate_color_02,
        R.color.generate_color_03,
        R.color.generate_color_04,
        R.color.generate_color_01,
        R.color.generate_color_02,
        R.color.generate_color_03,
        R.color.generate_color_04,
    )

    fun generate(size: Int = COLOR_TEMPLATE.size): List<Int> {
        return try {
            COLOR_TEMPLATE.subList(0, size)
        } catch (e: Exception) {
            COLOR_TEMPLATE
        }
    }
}
