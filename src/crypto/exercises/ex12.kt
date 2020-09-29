package crypto.exercises

import crypto.*

// https://cryptopals.com/sets/2/challenges/12


fun main() {
    
    val plaintext = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    
    // discover the block size by feeding every character one at a time
    // to the encryption routine
    //
    val blocksize = findBlockSize(plaintext.toCharArray())
    println("Found block size to be: ${blocksize}")
    Assertions.assertEquals(blocksize, 16)
    
    // make sure we can detect ECB
    //
    var ciphertext = AESConsistentDetectionOracle().encrypt(plaintext.toByteArray())
    val isECB: Boolean = AesEcb.detectECB(ciphertext)
    if (!isECB) {
        throw Exception("Arrrgh! Did not detect ECB")
    }
    
    // create a input string that is one-byte less than the discovered
    // blocksize - fill it with 'A' characters
    //
    // q: what will the oracle put in that last byte position?
    // a: it will be the first byte of the provided string given in the example
    //    i.e., the one that is appended, then the entire string will be padded if needed
    //
    var textOneShortInBlockSize = ""
    for (x in 1..(blocksize - 1)) {
        textOneShortInBlockSize += 'A'
    }
    Assertions.assertEquals(textOneShortInBlockSize.length, 15)
    //println("Made new text block: ${textOneShortInBlockSize}")
    
    
    var oneCipherByteShort = AESConsistentDetectionOracle().encrypt(textOneShortInBlockSize.toByteArray())
    println("Ciphertext produced by a 15 char length plaintext is ${oneCipherByteShort.size}")
    //println("Last byte value as int is: ${oneCipherByteShort[15].toChar()}")
    
    
    var dictionary = HashMap<String, Char>()
    
    // try every last byte combination to create a dictionary of
    // first-16-byte-cipher => character that generated it
    //
    for (i in 0..255) {
        var lastByteText = textOneShortInBlockSize + i.toChar() // eg., AAAAAAAAAAAAAAA[a-zA-Z0-9]
        var cipherTest = AESConsistentDetectionOracle().encrypt(lastByteText.toByteArray())        
        var firstBlock = cipherTest.copyOfRange(0, 16)
        dictionary.put(firstBlock.contentToString(), i.toChar())
    }
    
    // now pump in the 15-byte long text, which will end up being 15 bytes plus the first byte
    // of the hidden appended string in the black box
    // what comes out we can then lookup in the dictionary
    //
    var cipherTest = AESConsistentDetectionOracle().encrypt(textOneShortInBlockSize.toByteArray())
    var firstBlock = cipherTest.copyOfRange(0, 16)
    val charVal = dictionary.get(firstBlock.contentToString())
    if (charVal != null) {
        println("Found Char: ${charVal}")
    } else {
        println("No match Sad Panda!")
    }
    
}


/**
 * Pumps characters into the encryption routine for ECB to find the blocksize
 * by examining the padding increments as more bytes are added. As we go over 16 bytes, the padding
 * increases, so we can find out how much is required as bytes are added
 *
 * Returns the discovered block size value
 */
fun findBlockSize(input: CharArray): Int {
    
    var blocksize = 0
    var plaintext = ""
    
    // feed in every character of our input one character at a time
    // decrypt and get back a byte array of ciphertext
    // first time round, we take that size and then continue
    // if more padding is needed, the ciphertext value will end up
    // jumping by the size of padding
    // once we detect a change in blocksize, we can calculate the padding
    // size by subtracting the new length from the first length we saw
    //
    for (x in 0..(input.size - 1)) {
        var ciphertext = AESConsistentDetectionOracle().encrypt(plaintext.toByteArray())
        if (blocksize == 0) {
            blocksize = ciphertext.size
        }
        if (ciphertext.size > blocksize) {
            var paddingSize = ciphertext.size - blocksize
            return paddingSize
        }
        plaintext += input[x]
    }
    if (blocksize == 0) {
        throw Exception("Could not determine blocksize")
    }
    return blocksize
}

