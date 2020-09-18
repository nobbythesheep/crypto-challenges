package crypto

/**
 * Provides common padding functions for crypto
 */
object Padding {

    /**
     * For a given string, ensure the string is padded with the provided character so
     * that it becomes the desired length.
     * The default pad character is 04, translates to "\x04" (end of word)
     */
    fun pad(input: String, blockSize: Int, padWith: Int = 4) : ByteArray {
        
        var padded = ArrayList<Byte>()
        if (input.length >= blockSize) {
            throw IllegalArgumentException("Input stream should be less than the blocksize")
        }
        
        padded.addAll(input.toByteArray().toList())
        
        for (i in 1..(blockSize - input.length)) {
            padded.add(padWith.toByte())
        }    
        return padded.toByteArray()
        
    }

}