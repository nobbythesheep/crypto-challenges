package crypto

import javax.crypto.*
import javax.crypto.spec.*
import java.security.*

/**
 * Implements AES encrypt/decrypt methods
 */
object AES {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

    /**
     * Encrypts a given byte array with encryption key
     */
    fun encrypt(input: ByteArray, key: String): String {
        val encrypted: ByteArray = try {
            
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher.doFinal(input)
            
        } catch (e: InvalidKeyException) {
            throw e
        }

        return String(encrypted)
    }

    /**
     * Decrypts a given byte array with decryption key
     */
    fun decrypt(input: ByteArray, key: String): String {
        val output: ByteArray = try {
            
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            cipher.doFinal(input)
            
        } catch (e: InvalidKeyException) {
            throw e
        }

        return String(output)
    }

}