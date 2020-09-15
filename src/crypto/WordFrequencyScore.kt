package crypto

class WordFrequencyScore {
    
    /**
     * Given a string, applies the word frequency score algorithm and comes back
     * with a score while storing the key the string was decrypted with
     * for tracking and later calculations
     */
    fun wordFrequencyScore(toCheck: String, key: Char): WordFrequencyScoreResult {
    
        var totalScore : Float
        totalScore = 0.0f
        
        for (c in toCheck.toCharArray()) {
            var lookup = c.toLowerCase()
            var score : Float
            score = getFrequencyMap().get(lookup) ?: 0.0f
            totalScore = totalScore.plus(score)
        }
        //println("Score: ${totalScore} for ${toCheck} and key: ${key}")
        
        // store what we have calculated in the format decrypted:score
        //
        return WordFrequencyScoreResult(toCheck, key, totalScore)
    }
    
    /**
     * The character frequency map for the English language
     */
    fun getFrequencyMap(): HashMap<Char, Float> {
        
        // https://en.wikipedia.org/wiki/Letter_frequency
        
        // init every character with at least a zero
        //
        var fMap = HashMap<Char, Float>()
        for (c in Xor().getXorCharset()) {
            fMap.put(c, 0.0f)
        }
        fMap.put('a', 8.16f)
        fMap.put('b', 1.49f)
        fMap.put('c', 2.78f)
        fMap.put('d', 4.25f)
        fMap.put('e', 12.70f)
        fMap.put('f', 2.22f)
        fMap.put('g', 2.01f)
        fMap.put('h', 6.09f)
        fMap.put('i', 6.96f)
        fMap.put('j', 0.15f)
        fMap.put('k', 0.77f)
        fMap.put('l', 4.02f)
        fMap.put('m', 2.40f)
        fMap.put('n', 6.74f)
        fMap.put('o', 7.50f)
        fMap.put('p', 1.92f)
        fMap.put('q', 0.09f)
        fMap.put('r', 5.98f)
        fMap.put('s', 6.32f)
        fMap.put('t', 9.05f)
        fMap.put('u', 2.75f)
        fMap.put('v', 0.97f)
        fMap.put('w', 2.36f)
        fMap.put('x', 0.15f)
        fMap.put('y', 1.97f)
        fMap.put('z', 0.07f)
        fMap.put(' ', 8.0f)
        
        return fMap
    
    }
}