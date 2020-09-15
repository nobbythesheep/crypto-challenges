package crypto

class HammingDistance {
    
    /**
     * Calculates the word distance between two strings and returns the value
     */
    fun calculate(str1: String, str2: String) : Float {
        var count: Float = 0.0f
        for (i in 0..(str1.length - 1)) {
            val n1 = str1[i].toChar().toInt()
            val n2 = str2[i].toChar().toInt()
            val result = n1 xor n2
            val binaryString = result.toInt().toString(radix = 2)
            count += charCount(binaryString, '1')
        }
        return count
    }

    private fun charCount(s: String, c: Char) : Int {
        return s.filter{ it == c}.count()
    }
}