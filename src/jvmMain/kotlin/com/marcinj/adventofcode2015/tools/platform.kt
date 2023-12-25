package com.marcinj.adventofcode2015.tools

import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger
import java.nio.file.Paths
import java.security.MessageDigest

internal actual fun readAllText(path: String): String /*= object {}.javaClass.getResource(path).readText()*/
{
    val executablePath = File(System.getProperty("user.dir"))
    val fullPath = "/../adventofcode_input/2015/data" + path;
    val filePath = Paths.get(executablePath.absolutePath, fullPath).toFile()
    return if (filePath.exists() && filePath.isFile) {
        filePath.readText()
    } else {
        throw FileNotFoundException("File not found at path: ${filePath.absolutePath}")
    }
}

internal actual fun getIntFromFile(path: String, lineNumber: Int): Int {
    readAllText(path).lines().let {
        return it[lineNumber].toInt()
    }
}

internal actual fun getStringFromFile(path: String, lineStart: Int, lineEnd: Int): String {
    readAllText(path).lines().let {
        return it.subList(lineStart, lineEnd).joinToString("\n")
    }
}

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