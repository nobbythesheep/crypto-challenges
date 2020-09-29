package crypto

/**
 * Provides base64 encoding/decoding functions
 */
object Base64 {
    
    // create the lookup list of numbers to characters
    //
    val base64LookupTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()
    
    /**
     * For a given integer value, returns a string representation of the binary equivalent
     * e.g., 1 would return "00000001"
     */
    private fun Int.toBinary(len: Int): String {
        return String.format("%" + len + "s", this.toString(2)).replace(" ".toRegex(), "0")
    }
    
    /**
     * Encodes the string provided in the constructor
     */
    fun encode(target: String) : String {

        var encodedString = ""
        
        // we're going to get each byte in the string represented by the 2-character hex
        //
        var numCount = 0
        var intArray = IntArray(3)
        val theHexCharArray = target.toCharArray()
        for (i in 0..(theHexCharArray.size - 1) step 2) {
            
            var hexNum = "".plus(theHexCharArray[i]).plus(theHexCharArray[i+1])
            var num = Integer.parseInt(hexNum, 16)
            
            // we need to join groups of three numbers together so we can get a 24-bit binary sequence
            //
            intArray[numCount] = num
            numCount = numCount.inc()
            if (numCount > 2) {
                var binString = ""
                for (n in intArray) {
                    binString += n.toBinary(8)
                }
                
                var counter = 0
                var toEncode = ""
                for (index in binString.indices) {
                    toEncode += binString[index]
                    counter = counter.inc()
                    if (counter > 5) {
                        
                        // now we have the 6-bit value from the 24-bit block
                        //
                        var lookupVal = Integer.parseInt(toEncode, 2)
                        encodedString += base64LookupTable[lookupVal]
                        
                        // reset the block
                        //
                        counter = 0
                        toEncode = ""
                    }
                }

                // reset the blocks of three hex digits
                //
                numCount = 0
                intArray = IntArray(3)
            } 
        }
        
        return encodedString
    }
    
    /**
     * Decodes an incoming base64-encoded string
     */
    fun decode(base64: String): ByteArray {
        return java.util.Base64.getDecoder().decode(base64)
    }
}