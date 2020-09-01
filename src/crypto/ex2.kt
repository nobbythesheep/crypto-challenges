package crypto

// https://cryptopals.com/sets/1/challenges/2

val theString = "1c0111001f010100061a024b53535009181c"
val xorAgainst = "686974207468652062756c6c277320657965"
val expected = "746865206b696420646f6e277420706c6179"

fun main() {
    val myResult = myXor(theString, xorAgainst)
    assertThat(myResult, expected);
}

fun myXor(incoming: String, against: String) : String {
    
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
    
