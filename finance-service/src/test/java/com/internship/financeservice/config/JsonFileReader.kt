package com.internship.financeservice.config

object JsonFileReader {
    fun readJsonFile(path: String): String {
        val inputStream = JsonFileReader.javaClass.getResourceAsStream(path)
            ?: throw IllegalArgumentException("Resource not found: $path")

        return inputStream.bufferedReader().use { it.readText() }
    }
}