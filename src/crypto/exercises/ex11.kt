package crypto.exercises

import crypto.AESDetectionOracle
import crypto.Assertions
import crypto.AesEcb
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
    
    // loop through tons of attempts to ensure we get good coverage
    // over the random byte data and lengths added to the plain text
    // before encrypting
    //
    for (n in 1..1000000) {
    
        // generate some ciphertext
        //
        var randomEncryptionResult = AESDetectionOracle.encrypt(text.toByteArray())
        
        // run through the detection algorithm
        //
        val ecbResult = AesEcb.detectECB(randomEncryptionResult.ciphertext)
        
        // run assertions to ensure all is good
        // we should never get a ECB method that isn't detected
        // for the input plain text string provided
        //
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

/**
 * Let's make sure the random key routing works
 */
fun testRandomAesKey() {
    println("Testing random AES key length")
    val key = AESDetectionOracle.generateAesKey()
    Assertions.assertEquals(16, key.length)
}
