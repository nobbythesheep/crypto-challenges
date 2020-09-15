package crypto

/**
 * For a given byte array and keysize length, chunk the input array into a set of
 * blocks of keysize length
 *
 * Then turn the matrix of bytes 90 degrees, where the first column of each block
 * becomes the new row of bytes of blocks.length wide and keysize deep
 *
 * Return this as a new data structure - an array of an array of bytes
 */
class ArrayTransposer(val encrypted: ByteArray, val keysizeLength: Int) {
    
    val blocks : List<ByteArray> = ArrayUtils.getBlocks(encrypted, keysizeLength)
    
    fun transpose(): Array<Array<Byte>> {
        
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
        }
    //    for (array in transposedBlockList) {
    //        for (value in array) {
    //            print("$value ")
    //        }
    //        println()
    //    }
        
      return transposedBlockList
        
    }

}