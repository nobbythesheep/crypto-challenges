package crypto

// https://cryptopals.com/sets/1/challenges/3

val encoded = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"

// lower and upper characters as candidate xor values
//
var lowerUpperCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()

// will store the results of decrypted values and their scores so we can sort them
// once complete and get the likely candidates, which will have the highest score
//
var decodedMap = HashMap<String, Float>()
var decodedWithMap = HashMap<String, Char>()

fun main() {
    decrypt()
}

fun decrypt() {

    var encodedNumbers = hexToIntArray(encoded)
    
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
        wordFrequencyScore(result, x)
    }
    
    // sort the keys into order - the map in Kotlin is really clunky and not good
    // for this type of thing
    //
    var decryptedList = ArrayList<String>()
    val result = decodedMap.toList().sortedBy { (_, value) -> value}.toMap()
    for (entry in result) {
        decryptedList.add(entry.key)
    }
    decryptedList.reverse()
    
    // print out the top n results
    printCandidates(decryptedList, decodedMap, 3) 
}

fun printCandidates(list: ArrayList<String>, map: HashMap<String, Float>, toPrint: Int) {
    for (i in 0..toPrint) {
        val score = map.get(list.get(i))
        var key: Char?
        key = decodedWithMap.get(list.get(i))
        println("${list.get(i)} has score:${score} and was decoded with: ${key}")
    }
}

fun wordFrequencyScore(toCheck: String, key: Char): Float {
    
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
    decodedMap.put(toCheck, totalScore)
    decodedWithMap.put(toCheck, key)
    return totalScore
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

fun nowReallyDecrypt() {
    val key = 'x'
    var encodedNumbers = hexToIntArray(encoded)
    for (n in encodedNumbers) {
        var result = n xor key.toInt()
        print(result)
    }
}



