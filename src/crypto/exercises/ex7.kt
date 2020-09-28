package crypto.exercises

import crypto.*
import javax.crypto.*
import javax.crypto.spec.*


fun main() {
    
    val fileName = "src/data/ex7.txt"
    
    var base64: String = ""
    java.io.File(fileName).forEachLine(Charsets.UTF_8) { base64 += it }
    val encrypted = java.util.Base64.getDecoder().decode(base64)
    
    var result = AesEcb.decrypt(encrypted, "YELLOW SUBMARINE")
    println(result)
}