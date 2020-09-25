package crypto

import kotlin.random.Random
import crypto.AesCbc
import crypto.AesEbc

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
    
    /**
     * Encrypts a byte array using either AES CBC or EBC mode, decided at random
     */
    fun encrypt(input: ByteArray) : ByteArray {
        val choice = (1..2).shuffled().first()
        if (choice == 1) {
            println("Using CBC")
            return encryptWithRandomKey_CBC(input)
        } else {
            println("Using EBC")
            return encryptWithRandomKey_EBC(input)
        }
    }
    
    /**
     * Adds a random set of bytes between 5 and 10 to the front and
     * end of the input byte array. The byte arrays being added will
     * be of different lengths but always between 5 and 10 long
     */
    private fun addRandomBytes(input: ByteArray) : ByteArray {
        
        var toEncrypt = ArrayList<Byte>()
        
        val firstRandomByteLen = Random.nextInt(5,10)
        val lastRandomByteLen = Random.nextInt(5,10)
        
        var firstBytes = ByteArray(firstRandomByteLen)
        firstBytes = Random.nextBytes(firstBytes)
        var lastBytes = ByteArray(lastRandomByteLen)
        lastBytes = Random.nextBytes(lastRandomByteLen)
        
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