package crypto

/**
 * Provides common padding functions for crypto
 */
object Padding {

    /**
     * Implements PKCS#7 Padding
     * For an array of 16 bytes, we add another 16 bytes where each byte is int value 16
     *
     * For an array of < 16 bytes, we calculate the difference and add that many bytes of padding
     * setting each byte to that int value. The post-condition will be a byte array of 16 bytes long
     *
     * For an array of > 16 bytes, we calculate the modulo and figure out how many bytes are needed
     * to make the new array a multiple of 16 bytes. Whatever the number of bytes are required to pad
     * is the int value of the bytes added
     *
     */
    fun pad(input: ByteArray) : ByteArray {
        
        var ourBytes = ArrayList<Byte>()
        ourBytes.addAll(input.asList())
        
        // we need to add a block of padding regardless
        // this will be 16 bytes of the value 16
        //
        if (input.size == 16) {
            for (i in 0..15) {
                ourBytes.add(16.toByte())
            }
            return ourBytes.toByteArray()
        }
        
        if (input.size < 16) {
            // we need to pad
            //println("Padding required. Size was < 16")
            
            val toPad = 16 - input.size
            for (b in 0..(toPad - 1)) {
                ourBytes.add(toPad.toByte())
            }
            return ourBytes.toByteArray()
        }
        
        // if we get here, more work to do
        //
        var padSize : Int = input.size % 16
        //println("Modulo is ${padSize}")
        //println("Having to pad ${16 - padSize} bytes")
        
        val padByte = 16 - padSize
        //println("Padding value ${padByte}")
        for (b in 0..(16 - padSize - 1)) {
            ourBytes.add(padByte.toByte())
        }
        return ourBytes.toByteArray()
    }
    
    fun strip(input: ByteArray) : ByteArray {
        
        // get the last byte int value
        //
        val lastByteIntValue = input[input.size - 1].toInt()
        //println("Last byte int value ${lastByteIntValue}")
        
        var isPadded = true
        
        for (n in (input.size - 1) downTo (input.size - lastByteIntValue)) {
            //println("Pad byte value: ${input[n].toInt()}")
            isPadded = input[n].toInt() == lastByteIntValue
        }
        //println("Padded state: ${isPadded}")
        
        if (isPadded) {
            return input.copyOfRange(0, (input.size - lastByteIntValue))
        } else {
            return input
        }
    }

}