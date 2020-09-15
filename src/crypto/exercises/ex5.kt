package crypto.exercises

import crypto.*

private val res1 = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"
private val key = "ICE"

fun main() {
  
    // read in our text from file and convert it into a continuous single line
    //
    var toEncrypt : String = ""
    val fileName = "src/data/ex5.txt"
    val bytes = java.io.File(fileName).readBytes()
    for (b in bytes) {
        toEncrypt += b.toChar()
    }
    
    // apply the rotating xor encryption
    //
    var hex = Xor().encrypt(toEncrypt, key)
    Assertions.assertThat(res1, hex)
}

