package com.keiven.hackernewtopstory.di

object Injector {
    fun getApp() = DaggerAppComponent.builder().build()
}