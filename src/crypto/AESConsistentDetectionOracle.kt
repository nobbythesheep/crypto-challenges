package crypto

import kotlin.random.Random
import crypto.AesCbc
import crypto.AesEcb
import crypto.Base64

// for ex12: https://cryptopals.com/sets/2/challenges/12

// set up a singleton key
//
var secretKey: String = AESConsistentDetectionOracle.generateAesKey()

/**
 * Creates the black box required for Ex 11
 */
class AESConsistentDetectionOracle {
    
    /**
     * Kotlin static method that generates a byte array of length 16 filled with random bytes
     */
    companion object {
        fun generateAesKey() : String {
            val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return alphaNumeric.shuffled().take(16).joinToString("")
        }   
    }
    
    private fun generateIV() : ByteArray {
        var bytes = ByteArray(16)
        return Random.nextBytes(bytes, 0, 15)
    }

    /**
     * Encrypts a byte array and appends a provided plaintext block that will be unknown
     * to the caller, encrypted with a random key that is also unknown
     */
    fun encrypt(input: ByteArray) : ByteArray {
        
        //println("Encrypting with random key: ${secretKey} content ${String(input)}")
        secretKey = "WteHJ6xwQcKO1lCR"
        
        val provided = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK"
        var toEncrypt = ArrayList<Byte>()
        toEncrypt.addAll(input.asList())
        toEncrypt.addAll(Base64.decode(provided).asList())
        
       return AesEcb.encrypt(toEncrypt.toByteArray(), secretKey)
    }
    
    /**
     * Encrypts an input byte array with a random 16 byte key and
     * random bytes before and after the array to encrypt
     */
    private fun encryptWithRandomKey_EBC(input: ByteArray) : ByteArray {
        return AesEcb.encrypt(input, secretKey)
    }
}