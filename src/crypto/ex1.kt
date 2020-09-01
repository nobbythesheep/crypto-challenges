package crypto

import java.util.Arrays

val theHexString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
val theBase64Output = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"

// https://cryptopals.com/sets/1/challenges/1

fun main() {
    val myOutput = base64Encode(theHexString);
    assertThat(theBase64Output, myOutput);
}

fun base64Encode(theString: String) : String {
    
    // create the lookup list of numbers to characters
    //
    val base64LookupTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()
    
    var encodedString = ""
    
    // we're going to get each byte in the string represented by the 2-character hex
    //
    var numCount = 0
    var intArray = IntArray(3)
    val theHexCharArray = theHexString.toCharArray()
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
            for (i in binString.indices) {
                toEncode += binString[i]
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

fun Int.toBinary(len: Int): String {
    return String.format("%" + len + "s", this.toString(2)).replace(" ".toRegex(), "0")
}

fun assertThat(testValue: String, expectedValue: String) {
    println("Let's Test It Then!")
    if (expectedValue == testValue) {
        println("${testValue} - Yes, well done!")
    } else {
        println("That is really very wrong - try again!")
    } 
}