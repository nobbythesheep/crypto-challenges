package crypto.exercises

import crypto.Padding
import crypto.Assertions

// https://cryptopals.com/sets/2/challenges/9

fun main() {
    
    // test a byte array that is less than 16 bytes long
    // actually 5 bytes, and should have 11 bytes of padding
    // with each byte of value 11
    //
    var padded = Padding.pad("HELP!".toByteArray())
    if (padded.size != 16) {
        throw Exception("Did not add extra padding block for 16 byte array")
    }
    if (padded[15].toInt() != 11) {
        throw Exception("Padded but not with the bytes that signify the number i.e., not 16")
    }
    
    // now remove the padding and make sure all looks good
    //
    var original = Padding.strip(padded)
    Assertions.assertEquals(5, original.size)
    Assertions.assertThat(String(original), "HELP!")
    
    // ===========================================================
    
    // test a basic byte array that is actually 16 bytes long
    // and requires just a padded block
    //
    padded = Padding.pad("YELLOW SUBMARINE".toByteArray())
    if (padded.size != 32) {
        throw Exception("Did not add extra padding block for 16 byte array")
    }
    if (padded[31].toInt() != 16) {
        throw Exception("Padded but not with the bytes that signify the number i.e., not 16")
    }
    original = Padding.strip(padded)
    Assertions.assertEquals(16, original.size)
    Assertions.assertThat(String(original), "YELLOW SUBMARINE")
    
    // test a byte array that is between 22 bytes long
    // we should have 6 padding bytes of value 6
    //
    padded = Padding.pad("YELLOW SUBMARINEYELLOW".toByteArray())
    if (padded.size != 32) {
        throw Exception("Did not add extra padding block for 22 byte array")
    }
    if (padded[31].toInt() != 10) {
        throw Exception("Did not add the correct byte value for the padding bytes")
    }
    original = Padding.strip(padded)
    Assertions.assertEquals(22, original.size)
    Assertions.assertThat(String(original), "YELLOW SUBMARINEYELLOW")

    println("What a star! All looks good")
    println("Have A Gorilla!")
}

