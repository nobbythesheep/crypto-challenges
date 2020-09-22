package crypto.exercises

import crypto.Padding
import crypto.Assertions

// https://cryptopals.com/sets/2/challenges/9

fun main() {
    val padded = Padding.pad("YELLOW SUBMARINE", 20)
    if (padded.size != 20) {
        throw Exception("Oh dear - something broke!")
    }
    Assertions.assertThat(String(padded), "YELLOW SUBMARINE")
}

