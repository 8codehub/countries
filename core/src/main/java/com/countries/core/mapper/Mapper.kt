package com.countries.core.mapper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Mapper<I, O> {

    fun map(from: I): O
    fun mapList(from: List<I>): List<O> = from.map { map(it) }
}
