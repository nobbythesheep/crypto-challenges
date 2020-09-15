package crypto

/**
 * Contains a string that has been through the word frequency score algorithm
 * having been decoded with a given key. The tuple also contains the score
 * This is used to determine the key most likely used to have decrypted a
 * string.
 */
data class WordFrequencyScoreResult(val toCheck: String, val key: Char, val score: Float) {
    override fun toString() : String {
        return "${toCheck} has score: ${score} and was decoded with: ${key}"
    }
}