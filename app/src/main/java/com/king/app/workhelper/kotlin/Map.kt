package com.king.app.workhelper.kotlin

/**
 * https://grokonez.com/kotlin/kotlin-transform-list-map-methods-example
 *
 * @author VanceKing
 * @since 2019/10/9.
 */

val customers = listOf(
        Customer("Jack", 25),
        Customer("Mary", 37),
        Customer("Peter", 18),
        null)

fun main() {
    mapIndexedNotNullTo()
}

fun map() {
    customers.map { if (it != null) "${it.name} - ${it.age}" else null }
            .forEach { println(it) }
}

fun mapto() {
    customers.mapTo(mutableListOf(), { if (it != null) "${it.name} - ${it.age}" else "The value is null" })
            .forEach { println(it) }
}

fun mapNotNull() {
    customers.mapNotNull { if (it != null) "${it.name} - ${it.age}" else null }
            .forEach { println(it) }
}

fun mapNotNullTo() {
    val destination = mutableListOf<String>()
    customers.mapNotNullTo(destination, { if (it != null) "${it.name} - ${it.age}" else null })
    destination.forEach { println(it) }
}

fun mapIndexed() {
    customers.mapIndexed { index, it ->
        if (it != null) "[$index] ${it.name} - ${it.age}" else "[$index] null"
    }.forEach { println(it) }
}

fun mapIndexedTo() {
    val destination = mutableListOf<String>()
    customers.mapIndexedTo(destination, { index, it ->
        if (it != null) "[$index] ${it.name} - ${it.age}" else "[$index] null"
    })
    destination.forEach { println(it) }
}

fun mapIndexedNotNull() {
    customers.mapIndexedNotNull { index, it ->
        if (it != null) "[$index] ${it.name} - ${it.age}" else null
    }.forEach { println(it) }
}

fun mapIndexedNotNullTo() {
    val destination = mutableListOf<String>()
    customers.mapIndexedNotNullTo(destination, { index, it ->
        if (it != null) "[$index] ${it.name} - ${it.age}" else null
    })
    destination.forEach { println(it) }
}