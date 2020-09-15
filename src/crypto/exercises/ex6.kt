package crypto.exercises

import crypto.*


fun main() {
    
    // make sure our hamming algorithm is all good
    //
    Assertions.assertEquals(37.0f, HammingDistance().calculate("this is a test", "wokka wokka!!!"))
    
    // read in the file
    //
    var encryptedBase64Encoded : String = ""
    
    // read in our text from file and convert it into a continuous single line
    //
    val fileName = "src/data/ex6.txt"

    val lines: List<String> = java.io.File(fileName).readLines()
    for (line in lines) {
        encryptedBase64Encoded += line
    }
    
    val encrypted = String(java.util.Base64.getDecoder().decode(encryptedBase64Encoded)).toByteArray()

    // calculate the keysize using hamming distance
    //
    val keysizeLength = HammingDistance().getCandidateKeySize(encrypted)

    // take the encrypted string and candidate keysize
    // and transpose from a set of chunked blocks of keysize-length
    // and turn them 90 degrees
    //
    val transposer: ArrayTransposer = ArrayTransposer(encrypted, keysizeLength)
    var transposedBlockList = transposer.transpose()
 
    // now we should have a list of new strings
    // where each string has been built using
    // the columns of the initial set of blocks
    // we created by splitting the encrypted string
    // into the blocksize we figured out earlier
    //
    // now...
    //
    var finalKey : String = ""
    for (block in transposedBlockList) {
        
        val hex = HexUtils.byteToHex(block.toByteArray())

        // xor this with a single key
        // and find the key with the best number score
        //
        var wfScore : WordFrequencyScoreResult = HexUtils.decrypt(hex)
        
        // add this key to a running string buffer
        // when we are done, this should be the key for the
        // entire text block that was encrypted
        //
        finalKey += wfScore.key
    }
    
    println("Final key: ${finalKey}")
    Assertions.assertThat("Terminator X: Bring the noise", finalKey)
    
    println("============================")
    println(Xor().decrypt(encrypted, finalKey))
}








