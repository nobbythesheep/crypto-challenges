package crypto

// TODO: what is the real Kotlin way to do this?
//
object Assertions {
    
    /**
     * Checks two floats against each other
     */
    fun assertEquals(n1: Float, n2: Float) : Boolean {
        if (n1 == n2) {
            println("All is well")
            return true
        } else {
            throw Exception("All is not well")
        }
    }
    
    /**
     * Simple Junit style unit test to assert two string values
     * and print out a message if they were/were not equal
     */
    fun assertThat(testValue: String, expectedValue: String) {
        println("Let's Test It Then!")
        if (expectedValue == testValue) {
            println("${testValue} - Yes, well done!")
        } else {
            println("That is really very wrong - try again!")
        } 
    }
    
    /**
     * Compares the first in with the second int and returns true if
     * the second is greater than the first, else throws an exception
     */
    fun assertGreaterThan(int1: Int, int2: Int) : Boolean {
        if (int1 > int2) {
            return true
        } else {
            throw IllegalArgumentException("WRONG!!!!")
        }
    }
    
}