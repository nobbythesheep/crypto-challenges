package crypto.exercises

import crypto.*

// https://cryptopals.com/sets/1/challenges/3

val encoded = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"

fun main() {
    HexUtils().decrypt(encoded)
}
