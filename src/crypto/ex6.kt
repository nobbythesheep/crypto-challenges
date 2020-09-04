package crypto

fun main() {
    
    // make sure our hamming algorithm is all good
    //
    assertEquals(37, hammingDistance("this is a test", "wokka wokka!!!"))
    
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
    val encrypted = String(java.util.Base64.getDecoder().decode(encryptedBase64Encoded))
    
    for (keySize in 2..40) {
        var index = 0
        var firstBlock = encrypted.substring(index, keySize)
        index += keySize
        var secondBlock = encrypted.substring(index, (index + keySize))
        
        val distance = hammingDistance(firstBlock, secondBlock) / keySize
        println("KeySize {$keySize} got distance of {$distance}")
    }
    
    // todo: have a function return the likely keysize
    //
    // running that script gives the likely keysize as 5
    // KeySize {5} got distance of {1}

    val keysizeLength = 5
    val blocks = encrypted.chunked(keysizeLength)
    
    var transposedBlockList = ArrayList<String>()
    
    for (i in 0..keysizeLength - 1) {
        
        var transposedBlock: String = ""
        
        for (b in blocks) {
            
            // drop anything that is not the right size
            // and this will be the last block
            //
            if (b.length < keysizeLength) {
                continue
            }
            
            transposedBlock += b.toCharArray()[i]
        }
        transposedBlockList.add(stringToHex(transposedBlock))
        
    }
    
    // now we should have a list of new strings
    // where each string has been built using
    // the columns of the initial set of blocks
    // we created by splitting the encrypted string
    // into the blocksize we figured out earlier
    //
    
    // now...
    //
    for (block in transposedBlockList) {

        decrypt(block)
        // xor this with a single key
        // and find the key with the best number score
        // add this key to a running string buffer
        // when we are done, this should be the key for the
        // entire text block that was encrypted
    }

}




fun charCount(s: String, c: Char) : Int {
    return s.filter{ it == c}.count()
}

private fun assertEquals(n1: Int, n2: Int) : Boolean {
    if (n1 == n2) {
        println("All is well")
        return true
    } else {
        throw Exception("All is not well")
        return false
    }
}