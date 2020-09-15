package crypto

// TODO: what is the real Kotlin way to do this?
//
class Assertions {
    
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
    
}