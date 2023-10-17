package com.ktvme.linkedin.util

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object SecurityUtil {
    private val sha1: MessageDigest = MessageDigest.getInstance("SHA-1")

    fun caiZipAndEncrypt(content: String, key: String): String = encrypt(zip(content).base64().toByteArray(), key)
    fun caiUnzipAndDecrypt(content: String, key: String): String = unzip(String(decrypt(content, key)).debase64())

    private fun zip(content: String): ByteArray {
        val bos = ByteArrayOutputStream()
        GZIPOutputStream(bos).bufferedWriter(StandardCharsets.UTF_8).use { it.write(content) }
        return bos.toByteArray()
    }

    private fun unzip(content: ByteArray): String =
        GZIPInputStream(content.inputStream()).bufferedReader(StandardCharsets.UTF_8).use { it.readText() }

    private fun encrypt(content: ByteArray, key: String): String {
        val secKey = generateSecKey(key)
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secKey)
        val cipherArray = cipher.doFinal(content)
        val base64 = Base64.getEncoder()
        return base64.encodeToString(cipherArray)
    }

    private fun decrypt(cipherText: String, key: String): ByteArray {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secKey = generateSecKey(key)
        cipher.init(Cipher.DECRYPT_MODE, secKey)
        val base64Decoder = Base64.getDecoder()
        val debase64 = base64Decoder.decode(cipherText)
        return cipher.doFinal(debase64)
    }

    private fun generateSecKey(key: String): SecretKeySpec {
        val keyByteArray = key.toByteArray()
        val keySha = sha1.digest(keyByteArray)
        val shortKey: ByteArray = Arrays.copyOf(keySha, 16)
        return SecretKeySpec(shortKey, "AES")
    }
}

fun String.debase64(): ByteArray = Base64.getDecoder().decode(this)

fun ByteArray.base64(): String = Base64.getEncoder().encodeToString(this)