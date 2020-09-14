package crypto

// lower and upper characters as candidate xor values
//
// TODO: refactor this so we have all ASCII characters
//
var lowerUpperCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 :".toCharArray()

/**
 * Contains a string that has been through the word frequency score algorithm
 * having been decoded with a given key. The tuple also contains the score
 * This is used to determine the key most likely used to have decrypted a
 * string.
 */
data class WordFrequencyScore(val toCheck: String, val key: Char, val score: Float) {
    override fun toString() : String {
        return "${toCheck} has score: ${score} and was decoded with: ${key}"
    }
}

// given a string, applies the word frequency score algorithm and comes back
// with a score while storing the key the string was decrypted with
// for tracking and later calculations
//
fun wordFrequencyScore(toCheck: String, key: Char): WordFrequencyScore {
    
    var totalScore : Float
    totalScore = 0.0f
    
    for (c in toCheck.toCharArray()) {
        var lookup = c.toLowerCase()
        var score : Float
        score = getFrequencyMap().get(lookup) ?: 0.0f
        totalScore = totalScore.plus(score)
    }
    //println("Score: ${totalScore} for ${toCheck} and key: ${key}")
    
    // store what we have calculated in the format decrypted:score
    //
    return WordFrequencyScore(toCheck, key, totalScore)
}

// the character frequency map for the English language
//
fun getFrequencyMap(): HashMap<Char, Float> {
    
    // https://en.wikipedia.org/wiki/Letter_frequency
    
    // init every character with at least a zero
    //
    var fMap = HashMap<Char, Float>()
    for (c in lowerUpperCharset) {
        fMap.put(c, 0.0f)
    }
    fMap.put('a', 8.16f)
    fMap.put('b', 1.49f)
    fMap.put('c', 2.78f)
    fMap.put('d', 4.25f)
    fMap.put('e', 12.70f)
    fMap.put('f', 2.22f)
    fMap.put('g', 2.01f)
    fMap.put('h', 6.09f)
    fMap.put('i', 6.96f)
    fMap.put('j', 0.15f)
    fMap.put('k', 0.77f)
    fMap.put('l', 4.02f)
    fMap.put('m', 2.40f)
    fMap.put('n', 6.74f)
    fMap.put('o', 7.50f)
    fMap.put('p', 1.92f)
    fMap.put('q', 0.09f)
    fMap.put('r', 5.98f)
    fMap.put('s', 6.32f)
    fMap.put('t', 9.05f)
    fMap.put('u', 2.75f)
    fMap.put('v', 0.97f)
    fMap.put('w', 2.36f)
    fMap.put('x', 0.15f)
    fMap.put('y', 1.97f)
    fMap.put('z', 0.07f)
    fMap.put(' ', 8.0f)
    
    return fMap
    
}

// takes in incoming string in HEX and returns an array where each 2-char hex representation
// is converted to a number
//
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

// for a given integer value, returns a string representation of the binary equivalent
// e.g., 1 would return "00000001"
//
fun Int.toBinary(len: Int): String {
    return String.format("%" + len + "s", this.toString(2)).replace(" ".toRegex(), "0")
}

// simple Junit style unit test to assert two string values
// and print out a message if they were/were not equal
//
fun assertThat(testValue: String, expectedValue: String) {
    println("Let's Test It Then!")
    if (expectedValue == testValue) {
        println("${testValue} - Yes, well done!")
    } else {
        println("That is really very wrong - try again!")
    } 
}

// converts a string to hex representation, making sure each hex value is 2-characters
//
fun stringToHex(input: String): String {
    var toReturn: String = ""
    var ca = input.toCharArray()
    for (c in ca) {
        toReturn += "%02x".format(c.toInt())
    }
    return toReturn;
}

fun byteToHex(input : ByteArray) : String {
    var toReturn = ""
    for (b in input) {
        val st = String.format("%02X", b)
        toReturn += st
    }
    return toReturn
}


// given a string as HEX, loop through a charset of [A-Za-z0-9] and for each key, decrypt
// the string. Once decrupted, calculate the word frequency score and return
//
fun decrypt(hexString: String) : WordFrequencyScore {

    var encodedNumbers = hexToIntArray(hexString)
    var wordFrequencyScore = ArrayList<WordFrequencyScore>()
    
    // for each character, xor that with each hex value
    //
    for (x in lowerUpperCharset) {

        var result = ""
        
        // get char value as integer
        var y = x.toInt()
        
        // xor on every character in the encoded string and build up the decrypted value
        //
        for (i in encodedNumbers) {
            result += (i xor y).toChar()
        }
        wordFrequencyScore.add(wordFrequencyScore(result, x))
    }
    
    wordFrequencyScore.sortByDescending { it.score }
    return wordFrequencyScore[0]
}

// calculates the word distance between two strings and returns
// the value
//
fun hammingDistance(str1: String, str2: String) : Float {
    var count: Float = 0.0f
    for (i in 0..(str1.length - 1)) {
        val n1 = str1[i].toChar().toInt()
        val n2 = str2[i].toChar().toInt()
        val result = n1 xor n2
        val binaryString = result.toInt().toString(radix = 2)
        count += charCount(binaryString, '1')
    }
    return count
}



