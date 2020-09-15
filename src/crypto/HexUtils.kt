package crypto

/**
 * Utility methods to make working with hex easier
 */
class HexUtils {
    
    /**
     * Converts a string to hex representation, making sure each hex value is 2-characters
     */
    fun stringToHex(input: String): String {
        var toReturn: String = ""
        var ca = input.toCharArray()
        for (c in ca) {
            toReturn += "%02x".format(c.toInt())
        }
        return toReturn;
    }
    
    /**
     * Converts a byte array to 2-char HEX format
     */
    fun byteToHex(input : ByteArray) : String {
        var toReturn = ""
        for (b in input) {
            val st = String.format("%02X", b)
            toReturn += st
        }
        return toReturn
    }
    
    
    /**
     * Takes in incoming string in HEX and returns an array where each 2-char hex representation
     * is converted to a number
     */
    fun hexToIntArray(input: String) : IntArray {
        
        var intArray = IntArray( input.length / 2)
        var count = 0;
        
        val theHexCharArray = input.toCharArray()
        for (i in 0..(theHexCharArray.size - 1) step 2) {
            var hexNum = "".plus(theHexCharArray[i]).plus(theHexCharArray[i+1])
            var num = Integer.parseInt(hexNum, 16)
            intArray[count] = num
            count = count.inc()  
        }
        return intArray;
    }
    
    /**
     * Given a string as HEX, loop through a charset of [A-Za-z0-9] and for each key, decrypt
     * the string. Once decrypted, calculate the word frequency score and return
     */
    fun decrypt(hexString: String) : WordFrequencyScoreResult {
    
        var encodedNumbers = HexUtils().hexToIntArray(hexString)
        var wordFrequencyScoreResults = ArrayList<WordFrequencyScoreResult>()
        
        // for each character, xor that with each hex value
        //
        for (x in Xor().getXorCharset()) {
    
            var result = ""
            
            // get char value as integer
            var y = x.toInt()
            
            // xor on every character in the encoded string and build up the decrypted value
            //
            for (i in encodedNumbers) {
                result += (i xor y).toChar()
            }
            wordFrequencyScoreResults.add(WordFrequencyScore().wordFrequencyScore(result, x))
        }
        
        wordFrequencyScoreResults.sortByDescending { it.score }
        return wordFrequencyScoreResults[0]
    }
    
}