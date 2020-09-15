package crypto.exercises

import crypto.*

// https://cryptopals.com/sets/1/challenges/4

fun main() {

    val fileName = "src/data/ex4.txt"
    val lines: List<String> = java.io.File(fileName).readLines()
    
    var wordFrequencyScore = ArrayList<WordFrequencyScoreResult>()
    
    // for each line in the file
    //
    for (line in lines) {
        
        // convert to binary
        //
        var encodedNumbers = HexUtils.hexToIntArray(line)
        
        // for each candidate key
        // decrypt using xor
        // pipe the result into the word frequency algorithm
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
            
            wordFrequencyScore.add(WordFrequencyScore().wordFrequencyScore(result, x))
        }
        
    } // end line in file
    
    wordFrequencyScore.sortByDescending { it.score }
    Assertions.assertThat("Now that the party is jumping", wordFrequencyScore[0].toCheck.trim())
 
}


