package crypto

// https://cryptopals.com/sets/1/challenges/2

val theString = "1c0111001f010100061a024b53535009181c"
val xorAgainst = "686974207468652062756c6c277320657965"
val expected = "746865206b696420646f6e277420706c6179"

fun main() {
    val myResult = myXor(theString, xorAgainst)
    assertThat(myResult, expected);
}

private fun myXor(incoming: String, against: String) : String {
    
    var myResult = ""
    
    val incomingAsIntArray = hexToIntArray(incoming)
    val againstAsIntArray = hexToIntArray(against)
                
    for (i in incomingAsIntArray.indices) {
        val result = incomingAsIntArray[i] xor againstAsIntArray[i]
        val hexString = java.lang.Integer.toHexString(result)
        myResult += hexString
    }
    
    return myResult;
}


    
