package crypto

fun main() {
    
    // read in our text from file and convert it into a continuous single line
    //
    val fileName = "src/data/ex8.txt"

    var counter = 0
    java.io.File(fileName).forEachLine(Charsets.UTF_8) {
        counter += detectECB(it).size
    }
    Assertions.assertGreaterThan(counter, 1)
    
}

/**
 * For a given input string, break it down into blocks of 16
 * and store in a hash map with an incrementing count.
 * Once done, any counter that is > 1 is a likely ECB hit
 */
fun detectECB(input: String): List<String> {
    
    var toReturn : ArrayList<String> = ArrayList<String>()
    
    var map = HashMap<String, Int>()
    val blocks : List<ByteArray> = ArrayUtils.getBlocks(input.toByteArray(), 16)
    for (block in blocks) {
        val content = String(block)
        var count = map.get(content) ?: 0
        count = count.plus(1)
        map.put(content, count)
    }
    
    val result = map.toList().sortedBy { (_, value) -> value}.toMap()
    for (r in result) {
        if (r.value > 1) {
            println("Possible ECB detected: ${input}")
            println("Got duplicate ${r.key} ${r.value}")
            toReturn.add(r.key)
        }
    }
    return toReturn
}