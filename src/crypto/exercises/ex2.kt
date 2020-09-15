package crypto.exercises

import crypto.*

// https://cryptopals.com/sets/1/challenges/2

fun main() {
    
    val theString = "1c0111001f010100061a024b53535009181c"
    val xorAgainst = "686974207468652062756c6c277320657965"
    val expected = "746865206b696420646f6e277420706c6179"
    
    val xor = Xor()
    val myResult = xor.xorTogether(theString, xorAgainst)
    Assertions.assertThat(myResult, expected);
}