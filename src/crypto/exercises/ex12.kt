package crypto.exercises

import crypto.*

// https://cryptopals.com/sets/2/challenges/12


fun main() {
    
    testPlainTextBlock()
    
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
    
    // keep track of the bytes we discovery as we break encryption
    //
    var discoveredBytes = ArrayList<Byte>()
    
    var blockCounter : Int = 0
    
    // if we knew the actual length of the string being decrypted, we'd
    // just loop until all characters were consumed
    // TOOD: cleanup?
    //
    loop@ while (true) {
        
        try {
    
            // craft blocks where bytes are gradually discovered and then shifted left
            // this will only build up 16 known bytes, but we'll deal with that later
            //
            for (byteOffset in 1..(blocksize)) {
                
                // create a input string that is one-byte less than the discovered
                // blocksize - fill it with 'A' characters
                //
                // q: what will the oracle put in that last byte position?
                // a: it will be the first byte of the provided string given in the example
                //    i.e., the one that is appended, then the entire string will be padded if needed
                //
                var textOneShortInBlockSize = generatePlainTextBlock(blocksize, byteOffset, discoveredBytes.toByteArray())
                //println("Created block: ${String(textOneShortInBlockSize)} of length ${textOneShortInBlockSize.size}")
                //Assertions.assertEquals(textOneShortInBlockSize.size, (blocksize - 1))
                
                //var oneCipherByteShort = AESConsistentDetectionOracle().encrypt(textOneShortInBlockSize)
                //println("Ciphertext produced by plaintext is ${oneCipherByteShort.size}")
                
                // try every last byte combination to create a dictionary of
                // first-16-byte-cipher => character that generated it
                // on each iteration, we shift the bytes left but also add a block of padding
                // so AAAAAAAAAAAAAAA?AAAAAAAAAAAAAAAAA
                //    AAAAAAAAAAAAAAR?AAAAAAAAAAAAAAAAA where 'R' is a discovered byte and ? is the next byte to discover
                //
                var dictionary : HashMap<String, Byte> = generateDictionary(blocksize, textOneShortInBlockSize, blockCounter)
                
                // now pump in the number of A characters we need to get at the next unknown byte
                // this will start out as 15-bytes long and gradually reduce. We'll add in the bytes we know about
                // to create a 16-byte block, then add a 16-byte padding block so the secret text is appended well out of our way
                // else we'll just keep finding the same byte over and over again
                //
                var cipherTest: ByteArray
                cipherTest = AESConsistentDetectionOracle().encrypt(getLookupBlock(byteOffset, blocksize)) // 14*A, grab 16 bytes of cipher
                
                // now grab the first block of ciphertext and look that up in the dictionary
                // to find the hidden byte that generated it
                //
                var firstBlock = cipherTest.copyOfRange(blockCounter, (blockCounter + blocksize))
                val charVal = dictionary.get(firstBlock.contentToString())
                if (charVal != null) {
                    //println("Found Char: ${charVal.toChar()}")
                    discoveredBytes.add(charVal)
                } else {
                    //println("No match Sad Panda!")
                }
            }
            
            // once we have run of bytes, we can start the process over again using the
            // block of bytes we've just discovered. The trick now is to
            // feed one less blocksize again but look at the next block of ciphertext
            // so if we know the first 16 bytes is xxxxxxxxxxxxxxxx, we can feed AAAAAAAAAAAAAAAx|xxxxxxxxxxxxxxx[unknown]|
            // and pump in every byte possible as the last byte, then see what the ciphertext is
            // then we go to 14, 13 A's etc., and just shift the block we look at along each time a block is figured out
            // nifty!

            blockCounter = blockCounter.plus(blocksize)
            
            
        } catch (e: IndexOutOfBoundsException) {
            println()
            println("Cracked the ciphertext! It is:")
            println()
            println(String(discoveredBytes.toByteArray()))
            break@loop
        }
        

    }

}

/**
 * Returns a byte array of given blocksize minus the offset value
 */
private fun getLookupBlock(byteOffset: Int, blocksize: Int, padByte: Char = 'A'): ByteArray {
    var block = ""
    for (n in 1..(blocksize - byteOffset)) {
        block += padByte
    }
    return block.toByteArray()
}

/**
 * Takes a crafted byte array that is one-byte short of the calculated blocksize
 * and adds every possible byte in a loop. That payload is then sent to the encryption
 * oracle and the first block is retrieved and stored as the value in a map, where the key
 * is the crafted block. This can then be used later to determine unknown bytes.
 */
private fun generateDictionary(blocksize: Int, craftedBlock: ByteArray, blockCounter: Int): HashMap<String, Byte> {
    var dictionary = HashMap<String, Byte>()
    for (i in 0..255) {
        var lastByteText = String(craftedBlock) + i.toChar() // eg., AAAAAAAAAAAAAAA[a-zA-Z0-9]
        lastByteText += getLookupBlock(0, blocksize) // the magic padding block!
        var cipherTest = AESConsistentDetectionOracle().encrypt(lastByteText.toByteArray())        
        var firstBlock = cipherTest.copyOfRange(blockCounter, (blockCounter + blocksize))
        dictionary.put(firstBlock.contentToString(), i.toChar().toByte())
    }
    return dictionary
}

/**
 * For a given blocksize, create a set of 'A' characters where the length is governed by the blocksize and
 * desired offset. If you want 15 A characters, pass in a blocksize of 16 and an offset of 1. The result
 * will be a 15-byte block that can be used to feed to the ECB oracle for breaking encryption.
 *
 * If you want to add your own characters when bytes are revealed, you can add an array of additional characters.
 * So for one byte shifted left, pass in blocksize of 16, offset of 2 and then an array of one extra chracters. As
 * bytes of ciphertext are revealed, the optional array will grow and the offset will increase.
 *
 * To create a 16-byte block of A's, provide 16 as blocksize and 0 as the offset with no additional chars.
 */
private fun generatePlainTextBlock(blocksize: Int, offset: Int, extraChars: ByteArray = ByteArray(0)) : ByteArray {
    
    var generatedBlock = ""
    
    for (x in 1..(blocksize - offset)) {
        generatedBlock += 'A'
    }
    
    if (extraChars.size > 0) {
        for (c in extraChars) {
            generatedBlock += c.toChar()
        }
    }
    
    return generatedBlock.toByteArray()
}

/**
 * Make sure the plain text block function does what it says on the tin
 */
private fun testPlainTextBlock() {
    var output = generatePlainTextBlock(16, 1)
    Assertions.assertEquals(15, output.size)
    
    output = generatePlainTextBlock(16, 2)
    Assertions.assertEquals(14, output.size)
    
    output = generatePlainTextBlock(16, 2, byteArrayOf('B'.toByte()))
    Assertions.assertThat("AAAAAAAAAAAAAAB", String(output))
    Assertions.assertEquals(15, output.size)
    
    output = generatePlainTextBlock(16, 3, byteArrayOf('B'.toByte()))
    Assertions.assertThat("AAAAAAAAAAAAAB", String(output))
    Assertions.assertEquals(14, output.size)
    
    output = generatePlainTextBlock(16, 3, byteArrayOf('B'.toByte(), 'C'.toByte()))
    Assertions.assertThat("AAAAAAAAAAAAABC", String(output))
    Assertions.assertEquals(15, output.size)
    
    output = generatePlainTextBlock(16, 0)
    Assertions.assertThat("AAAAAAAAAAAAAAAA", String(output))
    Assertions.assertEquals(16, output.size)
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

