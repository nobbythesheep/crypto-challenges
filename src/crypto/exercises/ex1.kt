package crypto.exercises

import java.util.Arrays
import crypto.*

val theHexString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
val theBase64Output = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"

// https://cryptopals.com/sets/1/challenges/1

fun main() {
    val myOutput = Base64.encode(theHexString);
    Assertions.assertThat(theBase64Output, myOutput);
}
