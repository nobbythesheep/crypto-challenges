package crypto.exercises

import crypto.AesEbc
import crypto.Assertions

fun main() {
    
    // read in our text from file and convert it into a continuous single line
    //
    val fileName = "src/data/ex8.txt"

    var counter = 0
    java.io.File(fileName).forEachLine(Charsets.UTF_8) {
        counter += AesEbc.detectECB(it.toByteArray()).size
    }
    Assertions.assertGreaterThan(counter, 1)
    
}

