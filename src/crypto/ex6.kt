package crypto

private var keyCounter: Int = 0
private var keySize: Int = 0
private var key: CharArray = "".toCharArray()

fun main() {
    
    // make sure our hamming algorithm is all good
    //
    assertEquals(37.0f, hammingDistance("this is a test", "wokka wokka!!!"))
    
    // read in the file
    //
    var encryptedBase64Encoded : String = ""
    
    // read in our text from file and convert it into a continuous single line
    //
    val fileName = "src/data/ex6.txt"

    val lines: List<String> = java.io.File(fileName).readLines()
    for (line in lines) {
        encryptedBase64Encoded += line
    }
    
    val encrypted = String(java.util.Base64.getDecoder().decode(encryptedBase64Encoded)).toByteArray()


    // calculate the keysize using hamming distance
    //
    val keysizeLength = getKeySize(encrypted)
    
    println("encrypted length is ${encrypted.size}")
    println("keysizeLength is ${keysizeLength}")

    val blocks : List<ByteArray> = getBlocks(encrypted, keysizeLength)
    println("block length is ${blocks.size}")


    // for every block
    // take the 0,1,2,3,4,5 elements (keysize) and push those onto a vertical stack
    
    // init the list
    //
    var transposedBlockList = arrayOf<Array<Byte>>()
    for (i in 0..(keysizeLength - 1)) {
        var array = arrayOf<Byte>()
        for (j in 0..(blocks.size - 1)) {
            array += 0.toByte()
        }
        transposedBlockList += array
    }
//    for (array in transposedBlockList) {
//        for (value in array) {
//            print("$value ")
//        }
//        println()
//    }

    
    for (col in 0..(keysizeLength - 1)) {
        
        // col is going across, from 0 to 28
        // the number of horizontal bytes
        
        for (row in 0..(blocks.size - 1)) {
            
            // row is going to be 0 - 98
            // the number of vertical blocks
            
            // go down the list, taking the vertical
            // byte from each block
            //
            var block = blocks[row]
            var b = block[col]
            
            // first time round, we'll have 0,0 then 1,0, 2,0, 3,0 ...99,0
            // where the first number is the block number down the list and the
            // zero is the byte we're picking up
            // row, col
            //
            // so now we have to invert
            //
            
            var newBlock = transposedBlockList.get(col)
            newBlock[row] = b
        }
        //println(col)
    }

//    for (array in transposedBlockList) {
//        for (value in array) {
//            print("$value ")
//        }
//        println()
//    }
 
    // now we should have a list of new strings
    // where each string has been built using
    // the columns of the initial set of blocks
    // we created by splitting the encrypted string
    // into the blocksize we figured out earlier
    //
    
    // now...
    //
    var finalKey : String = ""
    for (block in transposedBlockList) {
        
        val hex = byteToHex(block.toByteArray())

        // xor this with a single key
        // and find the key with the best number score
        //
        var wfScore : WordFrequencyScore = decrypt(hex)
        
        // add this key to a running string buffer
        // when we are done, this should be the key for the
        // entire text block that was encrypted
        //
        finalKey += wfScore.key
    }
    
    println("Final key: ${finalKey}")
    setKey(finalKey)
    
    println("============================")
    println(xorRotatingKeyDecrypt(encrypted))
}

fun getBlocks(input: ByteArray, keySize: Int) : List<ByteArray> {
    
    if (input.size < keySize) {
        throw IllegalArgumentException("input must be greater than keysize else we can't chunk it!")
    }
    
    var blocks = ArrayList<ByteArray>()
    
    var startAt : Int = 0
    while (startAt < (input.size - keySize)) {
        var nextBlock = input.copyOfRange(startAt, (startAt + keySize))
        blocks.add(nextBlock)
        startAt += keySize
    }
    
    return blocks
}

fun xorRotatingKeyDecrypt(encrypted: ByteArray) : String {
    var result : String = ""
    for (c in encrypted) {
        val kc = getKey()
        result += (c.toInt() xor kc.toInt()).toChar()
    }
    return result
}

private fun setKey(myKey: String) {
    key = myKey.toCharArray()
    keySize = myKey.toCharArray().size
}

private fun getKey(): Char {
    var k = key[keyCounter]
    if (keyCounter >= (keySize - 1)) {
        keyCounter = 0
    } else {
        keyCounter++
    }
    return k
}

// returns a candidate key size for an encrypted string
// using hamming distance - if all fails, return zero
//
fun getKeySize(encrypted: ByteArray) : Int {
    
    var keySizeDistanceMap = HashMap<Float, Int>()

    for (keySize in 2..40) {
        
        var startAt : Int = 0
        val firstBlock = encrypted.copyOfRange(0, keySize)
        var totalDistance : Float = 0.0f

        var counter : Int = 0
        while (startAt < (encrypted.size - keySize)) {
            var nextBlock = encrypted.copyOfRange(startAt, (startAt + keySize))
            var distance : Float = hammingDistance(String(firstBlock), String(nextBlock)) / keySize
            totalDistance += distance
            startAt += keySize
            counter++
        }
        val avgDistance : Float = totalDistance / counter
        //println("KeySize {$keySize} got avg distance of {$avgDistance}")
        keySizeDistanceMap.put(avgDistance, keySize);
        
    }
    var sorted = keySizeDistanceMap.toSortedMap()
    //println("${sorted.firstKey()} got value ${sorted.get(sorted.firstKey())}")
    
    // bloody kotlin non-null handling my arse
    //
    val k = sorted.firstKey()
    val x = sorted.get(k)
    val l: Int = if (k != null && x != null) x else 0
    return l
}


fun charCount(s: String, c: Char) : Int {
    return s.filter{ it == c}.count()
}

private fun assertEquals(n1: Float, n2: Float) : Boolean {
    if (n1 == n2) {
        println("All is well")
        return true
    } else {
        throw Exception("All is not well")
    }
}