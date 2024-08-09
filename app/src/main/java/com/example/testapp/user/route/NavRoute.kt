package com.example.testapp.user.route

abstract class NavRoute<T : NavParams> {

    abstract val route: String
    abstract fun build(params: T): String
}

interface NavParams

object EmptyNavParams : NavParams