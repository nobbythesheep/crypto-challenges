package crypto

// https://cryptopals.com/sets/2/challenges/10

import crypto.AesEbc

val key = "YELLOW SUBMARINE"

fun main() {

    verifyAES()
    
    val iv = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)

    //val fileName = "src/data/ex10.txt"
    val fileName = "src/data/ex10-sample.txt"
    val bytes = java.io.File(fileName).readBytes()
    println("File size: ${bytes.size}")

    val finalCipherText = AesCbc.encrypt(bytes, iv, key)
    println("Cipher text bytes: ${finalCipherText.size}")

    val decrypted = AesCbc.decrypt(finalCipherText, iv, key)
    println("============= Decrypted ================")
    println(String(decrypted))
    println("============= Decrypted ================")
    Assertions.assertBytesAreEqual(decrypted, bytes)
}

private fun verifyAES() {
    
    // quick test of the encryption/decryption
    //
    val encrypted = AesEbc.encrypt(key.toByteArray(), "arsearsearsearse") 
    Assertions.assertEquals(encrypted.size, 32)
    val decrypted = AesEbc.decrypt( encrypted, "arsearsearsearse")
    Assertions.assertThat(key, String(decrypted))  
}

