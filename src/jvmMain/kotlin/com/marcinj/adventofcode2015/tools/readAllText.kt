package com.marcinj.adventofcode2015.tools

internal actual fun readAllText(path: String) = object {}.javaClass.getResource(path).readText()