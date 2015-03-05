/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securityproject;

import com.chilkatsoft.*;


/**
 *
 * @author Josh
 */
public class SecurityProject {
    
String message = "This is the test message";
  
public SecurityProject(){
   // System.out.println("-----------\nAES\n-----------");
   // AES(message);
    System.out.println("\n\n-----------\nSHA1\n-----------");
    SHA1(message);
    System.out.println("\n\n-----------\nRSA\n-----------");
    RSA(message);
    System.out.println("\n\n\n");
}
static {
    try {
        System.loadLibrary("chilkat");
    } catch (UnsatisfiedLinkError e) {
      System.err.println("Native code library failed to load.\n" + e);
      System.exit(1);
    }
  }
  
  public String AES(String message){
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

    //  Encrypt a string...
    //  The input string is 44 ANSI characters (i.e. 44 bytes), so
    //  the output should be 48 bytes (a multiple of 16).
    //  Because the output is a hex string, it should
    //  be 96 characters long (2 chars per byte).
    String encStr = crypt.encryptStringENC(message);
    System.out.println(encStr);

    //  Now decrypt:
    String decStr = crypt.decryptStringENC(encStr);
    System.out.println(decStr);  
    return encStr;
  }

  public void SHA1(String message){
      CkCrypt2 crypt = new CkCrypt2();

    //  Any string argument automatically begins the 30-day trial.
    boolean success = crypt.UnlockComponent("30-day trial");
    if (success != true) {
        System.out.println(crypt.lastErrorText());
        return;
    }

    crypt.put_HashAlgorithm("sha1");
    crypt.put_EncodingMode("hex");
    
    String hash = crypt.hashStringENC(message);
    System.out.println("SHA1: " + hash);

    //  Hash using HAVAL
    //  There are two additional properties relevant to HAVAL:
    //  HavalRounds, and KeyLength.
    //  HavalRounds can have values of 3, 4, or 5.
    //  KeyLength can have values of 128, 160, 192, 224, or 256
    crypt.put_HashAlgorithm("haval");
    crypt.put_HavalRounds(5);
    crypt.put_KeyLength(256);
    hash = crypt.hashStringENC(message);
    System.out.println("Haval: " + hash);
  }  
  
  public void RSA(String message){
    CkRsa rsa = new CkRsa();

    boolean success = rsa.UnlockComponent("Anything for 30-day trial");
    if (success != true) {
        System.out.println("RSA component unlock failed");
        return;
    }

    //  This example also generates the public and private
    //  keys to be used in the RSA encryption.
    //  Normally, you would generate a key pair once,
    //  and distribute the public key to your partner.
    //  Anything encrypted with the public key can be
    //  decrypted with the private key.  The reverse is
    //  also true: anything encrypted using the private
    //  key can be decrypted using the public key.

    //  Generate a 1024-bit key.  Chilkat RSA supports
    //  key sizes ranging from 512 bits to 4096 bits.
    success = rsa.GenerateKey(1024);
    if (success != true) {
        System.out.println(rsa.lastErrorText());
        return;
    }

    //  Keys are exported in XML format:
    String publicKey = rsa.exportPublicKey();
    String privateKey = rsa.exportPrivateKey();

    String plainText = message;

    //  Start with a new RSA object to demonstrate that all we
    //  need are the keys previously exported:
    CkRsa rsaEncryptor = new CkRsa();

    //  Encrypted output is always binary.  In this case, we want
    //  to encode the encrypted bytes in a printable string.
    //  Our choices are "hex", "base64", "url", "quoted-printable".
    rsaEncryptor.put_EncodingMode("hex");

    //  We'll encrypt with the public key and decrypt with the private
    //  key.  It's also possible to do the reverse.
    rsaEncryptor.ImportPublicKey(publicKey);

    boolean usePrivateKey = false;
    String encryptedStr = rsaEncryptor.encryptStringENC(plainText,usePrivateKey);
    System.out.println(encryptedStr);

    //  Now decrypt:
    CkRsa rsaDecryptor = new CkRsa();

    rsaDecryptor.put_EncodingMode("hex");
    rsaDecryptor.ImportPrivateKey(privateKey);

    usePrivateKey = true;
    String decryptedStr = rsaDecryptor.decryptStringENC(encryptedStr,usePrivateKey);

    System.out.println(decryptedStr);

  }
  
  public static void main(String argv[])
  {
      SecurityProject sp = new SecurityProject();

  }
}
