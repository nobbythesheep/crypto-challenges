package crypto

import kotlin.random.Random
import crypto.AesCbc
import crypto.AesEbc

/**
 * Creates the black box required for Ex 11
 */
object RandomUtils {
    
    /**
     * Generates a byte array of length 16 filled with random bytes
     */
    fun generateIV() : ByteArray {
        var bytes = ByteArray(16)
        return Random.nextBytes(bytes, 0, 15)
    }
    
    /**
     * Generates a random 16 byte string
     */
    fun generateAesKey() : String {
        val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return alphaNumeric.shuffled().take(16).joinToString("")
    }
    
    class RandomEncryptionResult(val type: String, val ciphertext: ByteArray) {
        
    }
    /**
     * Encrypts a byte array using either AES CBC or EBC mode, decided at random
     */
    fun encrypt(input: ByteArray) : RandomEncryptionResult {
        val choice = (1..2).shuffled().first()
        if (choice == 1) {
            return RandomEncryptionResult("CBC", encryptWithRandomKey_CBC(input))
        } else {
            return RandomEncryptionResult("EBC", encryptWithRandomKey_EBC(input))
        }
    }
    
    /**
     * Adds a random set of bytes between 5 and 10 to the front and
     * end of the input byte array. The byte arrays being added will
     * be of different lengths but always between 5 and 10 long
     */
    private fun addRandomBytes(input: ByteArray) : ByteArray {
        
        var toEncrypt = ArrayList<Byte>()
        
        val firstRandomByteLen = Random.nextInt(5,11)
        val lastRandomByteLen = Random.nextInt(5,11)
        
        var firstBytes = ByteArray(firstRandomByteLen)
        firstBytes = Random.nextBytes(firstBytes)
        
        var lastBytes = ByteArray(lastRandomByteLen)
        lastBytes = Random.nextBytes(lastRandomByteLen)
        
        // add the random first bytes, then all the input plaintext, then the last random bytes
        // to form a new byte array that will be then encrypted
        // as per ex11's specification
        //
        for (b in firstBytes) {
            toEncrypt.add(b)
        }
        for (b in input) {
            toEncrypt.add(b)
        }
        for (b in lastBytes) {
            toEncrypt.add(b)
        }
        
        return toEncrypt.toByteArray()
    }
    
    /**
     * Encrypts an input byte array with a random 16 byte key, a random IV
     * and random padding before and after the main array to encrypt
     */
    fun encryptWithRandomKey_CBC(input: ByteArray) : ByteArray {
        
        val iv = generateIV()        
        val key = generateAesKey()
        
        var toEncrypt = addRandomBytes(input)
        return AesCbc.encrypt(toEncrypt, iv, key)
    }
    
    /**
     * Encrypts an input byte array with a random 16 byte key and
     * random bytes before and after the array to encrypt
     */
    fun encryptWithRandomKey_EBC(input: ByteArray) : ByteArray {
        val key = generateAesKey()
        var toEncrypt = addRandomBytes(input)
        return AesEbc.encrypt(toEncrypt, key)
    }
}