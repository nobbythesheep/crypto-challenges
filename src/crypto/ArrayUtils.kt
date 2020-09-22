package crypto

/**
 * Utility methods to make dealing with common crypto array data structures easier
 */
object ArrayUtils {
    
    /**
     * For a given byte array, return a list of arrays where the new arrays are of
     * the length of the given key size i.e., chunk the array up into a set of blocks
     */
    fun getBlocks(input: ByteArray, keySize: Int) : List<ByteArray> {
        
        if (input.size % keySize > 0) {
            throw IllegalArgumentException("Input size must be divisible equally by keysize")
        }
    
        if (input.size < keySize) {
            throw IllegalArgumentException("input must be greater than keysize else we can't chunk it!")
        }
        
        var blocks = ArrayList<ByteArray>()
        
        for (startAt in 0..(input.size - 1) step keySize) {
            var nextBlock = input.copyOfRange(startAt, (startAt + keySize))
            blocks.add(nextBlock)
        }
        
        return blocks
    }
}