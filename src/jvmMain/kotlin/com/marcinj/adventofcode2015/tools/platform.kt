package com.marcinj.adventofcode2015.tools

import java.math.BigInteger
import java.security.MessageDigest

internal actual fun readAllText(path: String) = object {}.javaClass.getResource(path).readText()

data class JavaMd5Data(val md: MessageDigest) : MD5Data

internal actual fun md5HashInBytes(md5Data: MD5Data, str: String): ByteArray {
    val md = md5Data as JavaMd5Data
    return md.md.digest(str.toByteArray(Charsets.UTF_8))
}

internal actual fun md5HashInit(): MD5Data = JavaMd5Data(MessageDigest.getInstance("MD5"))
internal actual fun md5HashInBytesToStr(bytes: ByteArray): String {
    val bigInt = BigInteger(1, bytes)
    return String.format("%032x", bigInt)
}