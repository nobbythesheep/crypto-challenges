package crypto

private val res1 = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"
private val key = "ICE".toCharArray()
private var keyCounter: Int = 0

fun main() {
    
    var toEncrypt : String = ""
    
    // read in our text from file and convert it into a continuous single line
    //
    val fileName = "src/data/ex5.txt"
    val bytes = java.io.File(fileName).readBytes()
    for (b in bytes) {
        toEncrypt += b.toChar()
    }
    
    // apply the rotating xor encryption
    //
    var hex = encrypt(toEncrypt)
    
    assertThat(res1, hex)
}

// for an incoming string, xor each character using the rotating key
//
private fun encrypt(input: String) : String {
    var result = ""
    for (i in input.toCharArray()) {
        val b = i.toInt()
        val c = getKey().toInt()
        val t = (b xor c).toChar()
        //println("== ${i} ${b} ${c} ::${t}::")
        result += t
    }
    return stringToHex(result)
}



// keeps track of the rotating key used to xor against
// this rotates around the given key, so I,C,E,I,C,E etc.,
//
private fun getKey(): Char {
    var k = key[keyCounter]
    if (keyCounter >= 2) {
        keyCounter = 0
    } else {
        keyCounter++
    }
    return k
}