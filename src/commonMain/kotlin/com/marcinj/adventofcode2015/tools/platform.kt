package com.marcinj.adventofcode2015.tools

internal expect fun readAllText(path: String): String
internal expect fun getIntFromFile(path: String, lineNumber: Int): Int
internal expect fun getStringFromFile(path: String, lineStart: Int, lineEnd: Int): String


interface MD5Data;
internal expect fun md5HashInit(): MD5Data
internal expect fun md5HashInBytes(md5Data: MD5Data, str: String): ByteArray
internal expect fun md5HashInBytesToStr(bytes: ByteArray): String