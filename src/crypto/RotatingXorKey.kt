package crypto

/**
 * For a given string key value, each call to getKey() will return a character of that key
 * in order, and then when the last character is returned, the first will be returned and so on.
 *
 * Used for xor rotating-key encryption/decryption
 *
 * @param keyString the encryption/decryption key
 */
class RotatingXorKey(val keyString: String) {
    
    private var keyCounter: Int = 0
    private val key: CharArray = keyString.toCharArray()
    private val keySize: Int = key.size
    
    /**
     * Returns the next key in the cycle, starting at the far left and moving right
     */
    fun getKey(): Char {
        var k = key[keyCounter]
        if (keyCounter >= (keySize - 1)) {
            keyCounter = 0
        } else {
            keyCounter++
        }
        return k  
    }
}