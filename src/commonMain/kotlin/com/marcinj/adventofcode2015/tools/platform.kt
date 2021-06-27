package com.marcinj.adventofcode2015.tools

internal expect fun readAllText(path: String): String

interface MD5Data;
internal expect fun md5HashInit(): MD5Data
internal expect fun md5HashInBytes(md5Data: MD5Data, str: String): ByteArray
internal expect fun md5HashInBytesToStr(bytes: ByteArray): String