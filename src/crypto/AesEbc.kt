package crypto

import javax.crypto.*
import javax.crypto.spec.*
import java.security.*

/**
 * Implements AES encrypt/decrypt methods using EBC mode
 */
object AesEbc {
    
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

    /**
     * Encrypts a given byte array with encryption key
     */
    fun encrypt(input: ByteArray, key: String): ByteArray {
        val encrypted: ByteArray = try {
            
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher.doFinal(input)
            
        } catch (e: InvalidKeyException) {
            throw e
        }

        return encrypted
    }

    /**
     * Decrypts a given byte array with decryption key
     */
    fun decrypt(input: ByteArray, key: String): ByteArray {
        val output: ByteArray = try {
            
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            cipher.doFinal(input)
            
        } catch (e: InvalidKeyException) {
            throw e
        }

        return output
    }
    
    /**
     * For a given input string, break it down into blocks of 16
     * and store in a hash map with an incrementing count.
     * Once done, any counter that is > 1 is a likely ECB hit
     */
    fun detectECB(input: ByteArray): List<String> {
        
        var toReturn : ArrayList<String> = ArrayList<String>()
        
        var map = HashMap<String, Int>()
        val blocks : List<ByteArray> = ArrayUtils.getBlocks(input, 16)
        for (block in blocks) {
            val content = String(block)
            var count = map.get(content) ?: 0
            count = count.plus(1)
            map.put(content, count)
        }
        
        val result = map.toList().sortedBy { (_, value) -> value}.toMap()
        for (r in result) {
            if (r.value > 1) {
                println("Possible ECB detected: ${String(input)}")
                println("Got duplicate ${r.key} ${r.value}")
                toReturn.add(r.key)
            }
        }
        return toReturn
    }

}