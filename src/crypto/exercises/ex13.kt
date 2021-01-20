package crypto.exercises

import crypto.*

// https://cryptopals.com/sets/2/challenges/13

fun main() {
    
    // to parse
    //
    var toParse : String = "foo=bar&baz=qux&zap=zazzle"
    
    val json : String = parse(toParse)
    println(json)
    
    val profileJson = profile_for("foo@bar.com")
    
    var secretKey: String = AESConsistentDetectionOracle.generateAesKey()
    
    val profileCiphertext = encryptProfile(secretKey, profileJson)
    
    val profileDecrypted = decryptProfile(secretKey, profileCiphertext)
    println(String(profileDecrypted))
    
    val adminJson = profile_for("admin@bar.com&uid=10&role=admin")
    println(adminJson)
    
}

// creates a JSON object for a user, uid and role
//
private fun profile_for(email: String) : String {
    return parse("email=${email}&uid=10&role=user")
}

// encrypts incoming JSON profile objects
//
private fun encryptProfile(secretKey: String, json : String) : ByteArray {
    return AesEcb.encrypt(json.toByteArray(), secretKey)
}

// decrypts an encrypted profile back to JSON
//
private fun decryptProfile(secretKey: String, ciphertext: ByteArray) : ByteArray {
    return AesEcb.decrypt(ciphertext, secretKey)
}

// generates JSON from an incoming query string
// key=val&key=val etc.,
//
private fun parse(input: String) : String {
    var json = "{"
    val parts = input.split('&')
    for (part in parts) {
        val pairs = part.split('=')
        json += "\"${pairs[0]}\":\"${pairs[1]}\","
    }
    json += ",}"
    json = json.replace(",,", "")
    
    return json
}