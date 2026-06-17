package com.moonshot.kimiclaw.openclaw

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom
import java.util.Arrays
import java.util.concurrent.ConcurrentHashMap
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object BackupCrypto {
    private const val TAG = "BackupCrypto"
    private val MAGIC = byteArrayOf(75, 67, 66, 75)
    private val cache = ConcurrentHashMap<String, ByteArray>()

    init {
        System.loadLibrary("backup-crypto")
    }

    private external fun nativeGetKey(): ByteArray?

    @JvmStatic
    fun a(context: Context, scriptName: String): ByteArray? {
        cache[scriptName]?.let { return it }
        return try {
            val input = context.assets.open("$scriptName.enc")
            val data = input.readBytes()
            val decrypted = b(data) ?: return null
            cache[scriptName] = decrypted
            decrypted
        } catch (e: Exception) {
            Log.e(TAG, "decrypt_script", e)
            null
        }
    }

    @JvmStatic
    fun b(encData: ByteArray): ByteArray? {
        if (encData.size < 21) return null
        if (!Arrays.copyOfRange(encData, 0, 4).contentEquals(MAGIC)) return null
        if (encData[4] != 1.toByte()) return null
        val key = nativeGetKey() ?: return null
        try {
            val iv = Arrays.copyOfRange(encData, 5, 21)
            val ciphertext = Arrays.copyOfRange(encData, 21, encData.size)
            val secretKey = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
            return cipher.doFinal(ciphertext)
        } catch (e: Exception) {
            Log.e(TAG, "decrypt_bytes", e)
            return null
        } finally {
            Arrays.fill(key, 0.toByte())
        }
    }

    @JvmStatic
    fun c(inputFile: File, outputFile: File): Boolean {
        return try {
            val key = nativeGetKey() ?: return false
            val secretKey = SecretKeySpec(key, "AES")
            Arrays.fill(key, 0.toByte())

            FileInputStream(inputFile).use { fis ->
                val magic = ByteArray(4)
                if (fis.read(magic) != 4 || !magic.contentEquals(MAGIC)) return false
                if (fis.read() != 1) return false
                val iv = ByteArray(16)
                if (fis.read(iv) != 16) return false
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
                FileOutputStream(outputFile).use { fos ->
                    val buf = ByteArray(8192)
                    while (true) {
                        val n = fis.read(buf)
                        if (n == -1) break
                        cipher.update(buf, 0, n)?.let { fos.write(it) }
                    }
                    cipher.doFinal()?.let { fos.write(it) }
                }
            }
            outputFile.length()
            true
        } catch (e: Exception) {
            Log.e(TAG, "decrypt_file", e)
            outputFile.delete()
            false
        }
    }

    @JvmStatic
    fun d(inputFile: File, outputFile: File): Boolean {
        return try {
            val key = nativeGetKey() ?: return false
            val secretKey = SecretKeySpec(key, "AES")
            Arrays.fill(key, 0.toByte())

            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))

            FileOutputStream(outputFile).use { fos ->
                fos.write(MAGIC)
                fos.write(1)
                fos.write(iv)
                FileInputStream(inputFile).use { fis ->
                    val buf = ByteArray(8192)
                    while (true) {
                        val n = fis.read(buf)
                        if (n == -1) break
                        cipher.update(buf, 0, n)?.let { fos.write(it) }
                    }
                    cipher.doFinal()?.let { fos.write(it) }
                }
            }
            outputFile.length()
            true
        } catch (e: Exception) {
            Log.e(TAG, "encrypt_file", e)
            outputFile.delete()
            false
        }
    }
}
