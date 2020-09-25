package crypto.exercises

import crypto.RandomUtils
import crypto.Assertions
import crypto.AesEbc
import crypto.HexUtils

fun main() {
    
    testRandomAesKey()
    
    val text = "This is some plain text This is some plain text we are going to encrypt at random and then try to detect later on"
    
    for (n in 1..100) {
        var bytes = RandomUtils.encrypt(text.toByteArray())
        println(HexUtils.byteToHex(bytes))
        
        // since we may have padding, we'll need to cycle through the bytes
        // and test out different variations
        //
        loop@ for (x in 0..(bytes.size - 1)) {
            val toTest = bytes.copyOfRange(x, (bytes.size - 1))
            println(HexUtils.byteToHex(toTest))
            try {
                val count = AesEbc.detectECB(toTest).size
                if (count > 1) {
                    println("+++++++ ECB Detected ++++++++++")
                    continue@loop
                }
            } catch (e: IllegalArgumentException) {
                // probaby uneven byte lengths
                println(e)
            }
        }
    }
    
}

fun testRandomAesKey() {
    val key = RandomUtils.generateAesKey()
    println("Random key is ${key}")
    Assertions.assertEquals(16, key.length)
}
