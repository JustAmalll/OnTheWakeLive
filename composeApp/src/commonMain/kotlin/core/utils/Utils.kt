package core.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlin.collections.filter

fun <T> ImmutableList<T>.filter(predicate: (T) -> Boolean): ImmutableList<T> =
    filter(predicate = predicate).toPersistentList()
