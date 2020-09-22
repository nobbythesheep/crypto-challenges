package crypto

/**
 * Xor utility class for encryption/decryption
 */
class Xor {
    
    // lower and upper characters as candidate xor values
    //
    // TODO: refactor this so we have all ASCII characters
    //
    private var lowerUpperCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 :".toCharArray()
    
    /**
     * Returns a set of candidate characters that can be used for XOR decryption, the result of which may be
     * piped into the word frequency score algorithm to see whether it was successful or usable
     */
    fun getXorCharset(): CharArray {
        return lowerUpperCharset
    }
    
    /**
     * Takes two strings and returns the resulting bye-for-byte xor result
     * The strings must the the same length
     */
    fun xorTogether(incoming: String, against: String) : String {
        
        if (incoming.length != against.length) {
            throw IllegalArgumentException("Input strings must be of equal length")
        }
    
        var myResult = ""
        
        val incomingAsIntArray = HexUtils.hexToIntArray(incoming)
        val againstAsIntArray = HexUtils.hexToIntArray(against)
                    
        for (i in incomingAsIntArray.indices) {
            val result = incomingAsIntArray[i] xor againstAsIntArray[i]
            val hexString = java.lang.Integer.toHexString(result)
            myResult += hexString
        }
        
        return myResult;
    }
    
    /**
     * Performs a XOR operation on two incoming byte arrays
     */
    fun xorBytesTogether(incoming: ByteArray, against: ByteArray) : ByteArray {
        
        if (incoming.size != against.size) {
            throw IllegalArgumentException("Input strings must be of equal length")
        }
        
        var result = ArrayList<Byte>()
        
        for (i in incoming.indices) {
            val xorResult = incoming[i].toInt() xor against[i].toInt()
            result.add(xorResult.toByte())
        }
        
        return result.toByteArray()
    }
    
    /**
     * For an incoming string, xor each character using the xor rotating key method
     *
     * @param input the string to encrypt
     * @param key the key to use for encryption
     */
    fun encrypt(input: String, key: String) : String {
        val rotatingKey = RotatingXorKey(key)
        var result = ""
        for (i in input.toCharArray()) {
            val b = i.toInt()
            val c = rotatingKey.getKey().toInt()
            val t = (b xor c).toChar()
            //println("== ${i} ${b} ${c} ::${t}::")
            result += t
        }
        return HexUtils.stringToHex(result)
    }
    
    /**
     * Decrypts a byte array using the xor-rotating key method
     *
     * @param encrypted a byte array to decrypt
     * @key the decryption key
     */
    fun decrypt(encrypted: ByteArray, key: String) : String {
        val rotatingKey = RotatingXorKey(key)
        var result : String = ""
        for (c in encrypted) {
            val kc = rotatingKey.getKey()
            result += (c.toInt() xor kc.toInt()).toChar()
        }
        return result
    }
}