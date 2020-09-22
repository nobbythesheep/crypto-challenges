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
        return padBytes(input.toByteArray(), blockSize, padWith)
    }
    
    /**
     * For a given byte array, ensure the array is padded with the provided character so
     * that it becomes the desired length.
     * The default pad character is 04, translates to "\x04" (end of word)
     */
    fun padBytes(input: ByteArray, blockSize: Int, padWith: Int = 4) : ByteArray {
        
        var padded = ArrayList<Byte>()
        if (input.size >= blockSize) {
            throw IllegalArgumentException("Input string should be less than the blocksize")
        }
        padded.addAll(input.toList())
        
        for (i in 1..(blockSize - input.size)) {
            padded.add(padWith.toByte())
        }    
        return padded.toByteArray()
    }

}