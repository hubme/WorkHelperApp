package com.example.kotlin

/**
 *创建 DTOs（POJOs/POCOs）
 * 1. 所有属性的 getters （对于 var 定义的还有 setters）
 * 2. equals()、hashCode()、toString()
 * 3. copy()
 * 4. 所有属性的 component1()、 component2()
 *
 * https://www.kotlincn.net/docs/reference/data-classes.html
 * @author VanceKing
 * @since 2019/4/15.
 */
data class Customer(val name: String = "unknown", val age: Int = 18) {
    
}

internal object SingleInstance{
    
}