package crypto

import javax.crypto.*
import javax.crypto.spec.*
import java.security.*

/**
 * Implements AES encrypt/decrypt methods using CBC mode
 */
object AesCbc {
    
    val cipher: Cipher = Cipher.getInstance("AES/ECB/NOPadding")
    
    fun encrypt(input: ByteArray, iv: ByteArray, key: String): ByteArray {
        
        println("encrypting")
        
        var ourBytes = input.copyOf()
        if (input.size < 16) {
            // we need to pad
            ourBytes = Padding.pad(input)
            println("Padding required. Size was < 16")
        }
        if (input.size % 16 == 0) {
            // do nothing
            println("No padding needed")
        } else {
            var padSize : Int = input.size % 16
            padSize = input.size.plus(16 - padSize)
            println("Having to pad ${padSize} bytes")
            //ourBytes = Padding.pad(input, padSize)
        }
        println("Byte array is now: ${ourBytes.size}")
        
        val blocks = ArrayUtils.getBlocks(ourBytes, 16)
        println("Number of blocks: ${blocks.size}")
        
        // now keep all our blocks as a list of bytes for combining back later
        //
        var xorText = ArrayList<Byte>()
    
        // handle the first block using the IV
        //
        val firstBlock = blocks[0]
        println("First block size: ${firstBlock.size}")
        var ciphertext = Xor().xorBytesTogether(firstBlock, iv)
        ciphertext = _encrypt(ciphertext, key)
        println("Ciphertext from first block size: ${ciphertext.size}")
        
        xorText.addAll(ciphertext.asList())
        println("xorText size: ${xorText.size}")

        for (i in 1..(blocks.size-1)) {
            println("Processing block: ${i} of size ${blocks[i].size}")
            var block = blocks[i]
            ciphertext = Xor().xorBytesTogether(ciphertext, block)
            ciphertext = _encrypt(ciphertext, key)
            xorText.addAll(ciphertext.asList())
            println("xorText size: ${xorText.size}")
        }
        
        val finalCipherText = xorText.toByteArray()
        return finalCipherText
    }
    
    fun decrypt(input: ByteArray, iv: ByteArray, key: String): ByteArray {
        
        println("decrypting")
        
        val blocks = ArrayUtils.getBlocks(input, 16)
        
        // operate in reverse
        //
        var xorText = ArrayList<Byte>()
        
        for (i in (blocks.size - 1)  downTo 1) {
            val block = blocks[i]
            println("block size ${block.size}")
            var plaintext = _decrypt(block, key)
            plaintext = Xor().xorBytesTogether(plaintext, blocks[i-1])
            xorText.addAll(plaintext.asList().reversed())
        }
        
        println("about to do the last block")
        
        // handle the first block last
        //
        val firstBlock = blocks[0]
        var plaintext = _decrypt(firstBlock, key)
        plaintext = Xor().xorBytesTogether(plaintext, iv)
        xorText.addAll(plaintext.asList().reversed())
        
        // we need to reverse the order of bytes before returning since we built it
        // going backwards
        //
        println(String(xorText.toByteArray()))
        println(iv[0].toInt())
        var decrypted = xorText.toByteArray().reversedArray()
        
        return decrypted
        
    }

    /**
     * Encrypts a given byte array with encryption key
     */
    private fun _encrypt(input: ByteArray, key: String): ByteArray {

        val encrypted: ByteArray = try {
            
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher.doFinal(input)
            
        } catch (e: InvalidKeyException) {
            throw e
        }

        return encrypted
    }

    /**
     * Decrypts a given byte array with decryption key
     */
    private fun _decrypt(input: ByteArray, key: String): ByteArray {
        val output: ByteArray = try {
            
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            cipher.doFinal(input)
            
        } catch (e: InvalidKeyException) {
            throw e
        }

        return output
    }

}