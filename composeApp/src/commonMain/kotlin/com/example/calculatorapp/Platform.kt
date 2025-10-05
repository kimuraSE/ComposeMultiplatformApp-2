package com.example.calculatorapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform