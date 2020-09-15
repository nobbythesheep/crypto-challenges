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
    
    /**
     * Returns a candidate key size for an encrypted string
     * using hamming distance - if all fails, return zero
     */
    fun getCandidateKeySize(encrypted: ByteArray) : Int {
        
        var keySizeDistanceMap = HashMap<Float, Int>()
    
        for (keySize in 2..40) {
            
            var startAt : Int = 0
            val firstBlock = encrypted.copyOfRange(0, keySize)
            var totalDistance : Float = 0.0f
    
            var counter : Int = 0
            while (startAt < (encrypted.size - keySize)) {
                var nextBlock = encrypted.copyOfRange(startAt, (startAt + keySize))
                var distance : Float = HammingDistance().calculate(String(firstBlock), String(nextBlock)) / keySize
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
}