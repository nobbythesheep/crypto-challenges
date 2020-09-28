package crypto.exercises

import crypto.RandomUtils
import crypto.Assertions
import crypto.AesEbc
import crypto.HexUtils

// https://cryptopals.com/sets/2/challenges/11

/**
 * EBC detection challenge
 */
fun main() {
    
    testRandomAesKey()
    
    val text = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
    
    println("plain text length is ${text.length}")
    
    var foundECB = 0
    
    for (n in 1..1000000) {
    
        var randomEncryptionResult = RandomUtils.encrypt(text.toByteArray())
        
        val ecbResult = AesEbc.detectECB(randomEncryptionResult.ciphertext)
        
        if (ecbResult && randomEncryptionResult.type.equals("CBC")) {
            throw Exception("Failed to pick up on EBC encryption")
        }
        
        if (ecbResult && randomEncryptionResult.type.equals("EBC")) {
            foundECB += 1
            println("+++++++ EBC Correctly Detected ++++++++++")
            println(HexUtils.byteToHex(randomEncryptionResult.ciphertext))
        }
    }
    
    if (foundECB == 0) {
        throw Exception("Must have found at least one EBC!")
    }
    
    println("All is well with the EBC detection")
}


fun testRandomAesKey() {
    println("Testing random AES key length")
    val key = RandomUtils.generateAesKey()
    Assertions.assertEquals(16, key.length)
}
