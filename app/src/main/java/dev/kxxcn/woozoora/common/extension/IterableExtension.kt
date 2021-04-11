package dev.kxxcn.woozoora.common.extension

fun <T> Iterable<T>.replace(item: T?, predicate: (T) -> Boolean): List<T> {
    return if (item == null) {
        this.toList()
    } else {
        val newList = mutableListOf<T>()
        for (target in this) {
            if (predicate(target)) {
                item
            } else {
                target
            }.also {
                newList.add(it)
            }
        }
        newList
    }
}
