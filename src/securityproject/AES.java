/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securityproject;

import com.chilkatsoft.CkCrypt2;



/**
 *
 * @author Josh
 */
public class AES {
    
     static {
    try {
        System.loadLibrary("chilkat");
    } catch (UnsatisfiedLinkError e) {
      System.err.println("Native code library failed to load.\n" + e);
      System.exit(1);
    }
  }
    
    public String Encrypt(String plaintext){
    CkCrypt2 crypt = new CkCrypt2();

    boolean success = crypt.UnlockComponent("Anything for 30-day trial");
    if (success != true) {
        System.out.println(crypt.lastErrorText());
    }
    //  AES is also known as Rijndael.
    crypt.put_CryptAlgorithm("aes");

    //  CipherMode may be "ecb" or "cbc"
    crypt.put_CipherMode("cbc");

    //  KeyLength may be 128, 192, 256
    crypt.put_KeyLength(256);
    crypt.put_PaddingScheme(0);
    crypt.put_EncodingMode("hex");
    String ivHex = "000102030405060708090A0B0C0D0E0F";
    crypt.SetEncodedIV(ivHex,"hex");
    String keyHex = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F";
    crypt.SetEncodedKey(keyHex,"hex");
    
    String encStr = crypt.encryptStringENC(plaintext); 
    return encStr;
    }
    
    public String Decrypt(String ciphertext){
        CkCrypt2 crypt = new CkCrypt2();

        boolean success = crypt.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            System.out.println(crypt.lastErrorText());
        }
        //  AES is also known as Rijndael.
        crypt.put_CryptAlgorithm("aes");

        //  CipherMode may be "ecb" or "cbc"
        crypt.put_CipherMode("cbc");

        //  KeyLength may be 128, 192, 256
        crypt.put_KeyLength(256);
        crypt.put_PaddingScheme(0);
        crypt.put_EncodingMode("hex");
        String ivHex = "000102030405060708090A0B0C0D0E0F";
        crypt.SetEncodedIV(ivHex,"hex");
        String keyHex = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F";
        crypt.SetEncodedKey(keyHex,"hex");
        String decStr = crypt.decryptStringENC(ciphertext);
        return decStr;
        }
    
}
