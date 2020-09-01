package crypto

// https://cryptopals.com/sets/1/challenges/4

fun main() {

    val fileName = "src/data/4.txt"
    val lines: List<String> = java.io.File(fileName).readLines()
    
    // for each line in the file
    //
    for (line in lines) {
        
        // convert to binary
        //
        var encodedNumbers = hexToIntArray(line)
        
        // for each candidate key
        // decrypt using xor
        // pipe the result into the word frequency algorithm
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
        
    } // end line in file
 
}


