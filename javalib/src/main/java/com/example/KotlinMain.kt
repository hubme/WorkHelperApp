package com.example

import kotlin.properties.Delegates

fun main(args: Array<String>) {
    var observableProp: String by Delegates.observable("默认") { property, oldValue, newValue ->
        println("property: $property: $oldValue -> $newValue ")
    }

    observableProp = "第一次修改值"
    observableProp = "第一次修改值"
}