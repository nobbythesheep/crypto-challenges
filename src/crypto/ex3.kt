package crypto

// https://cryptopals.com/sets/1/challenges/3

val encoded = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"

// lower and upper characters as candidate xor values
//
var lowerUpperCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()


fun main() {
    decrypt(encoded)
}

fun decrypt(hexString: String) {

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
    println(wordFrequencyScore[0])
}





