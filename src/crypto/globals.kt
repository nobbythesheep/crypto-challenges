package crypto

data class WordFrequencyScore(val toCheck: String, val key: Char, val score: Float) {
    override fun toString() : String {
        return "${toCheck} has score: ${score} and was decoded with: ${key}"
    }
}

fun wordFrequencyScore(toCheck: String, key: Char): WordFrequencyScore {
    
    var totalScore : Float
    totalScore = 0.0f
    
    for (c in toCheck.toCharArray()) {
        var lookup = c.toLowerCase()
        var score : Float
        score = getFrequencyMap().get(lookup) ?: 0.0f
        totalScore = totalScore.plus(score)
    }
    
    // store what we have calculated in the format decrypted:score
    //
    return WordFrequencyScore(toCheck, key, totalScore)
}

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

fun hammingDistance(str1: String, str2: String) : Int {
    var count: Int = 0
    for (i in 0..(str1.length - 1)) {
        val n1 = str1[i].toChar().toInt()
        val n2 = str2[i].toChar().toInt()
        val result = n1 xor n2
        val binaryString = result.toInt().toString(radix = 2)
        count += charCount(binaryString, '1')
    }
    return count
}