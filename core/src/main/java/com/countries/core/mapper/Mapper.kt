package com.countries.core.mapper

interface Mapper<I, O> {

    fun map(from: I): O
    fun mapList(from: List<I>): List<O> = from.map { map(it) }
}
