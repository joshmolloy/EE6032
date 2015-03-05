/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securityproject;

import com.chilkatsoft.CkPrivateKey;
import com.chilkatsoft.CkPublicKey;
import com.chilkatsoft.CkRsa;

/**
 *
 * @author Josh
 */
public class RSA {

    String publicKeyXml;
    String privateKeyXml;
    
  static {
    try {
        System.loadLibrary("chilkat");
    } catch (UnsatisfiedLinkError e) {
      System.err.println("Native code library failed to load.\n" + e);
      System.exit(1);
    }
  }
        
    public void GenerateKeys(){
        CkRsa rsa = new CkRsa();

        boolean success = rsa.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            System.out.println("RSA component unlock failed");
            return;
        }

        //  Generate a 1024-bit key.  Chilkat RSA supports
        //  key sizes ranging from 512 bits to 4096 bits.
        success = rsa.GenerateKey(1024);
        if (success != true) {
            System.out.println(rsa.lastErrorText());
            return;
        }

        //  Keys are exported in XML format:
        String publicKeyXml = rsa.exportPublicKey();
        System.out.println(publicKeyXml);

        String privateKeyXml = rsa.exportPrivateKey();
        System.out.println(privateKeyXml);

        //  Save the private key in PEM format:
        CkPrivateKey privKey = new CkPrivateKey();
        privKey.LoadXml(privateKeyXml);
        privKey.SaveRsaPemFile("privateKey.pem");

        //  Save the public key in PEM format:
        CkPublicKey pubKey = new CkPublicKey();
        pubKey.LoadXml(publicKeyXml);
        pubKey.SaveOpenSslPemFile("publicKey.pem");
    }
    
    public void LoadKeys(){
        System.out.println("Entered LoadKeys");
        CkRsa rsa = new CkRsa();

        boolean success = rsa.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            System.out.println(rsa.lastErrorText());
            return;
        }

        //  First demonstrate importing a PEM public key:
        String publicKeyPem = "publicKey.pem";
        CkPublicKey pubkey = new CkPublicKey();
        pubkey.LoadOpenSslPem(publicKeyPem);

        publicKeyXml = pubkey.getXml();

        success = rsa.ImportPublicKey(publicKeyXml);
        if (success != true) {
            System.out.println("second error");
            System.out.println(rsa.lastErrorText());
            return;
        }

        //  Demonstrate importing a PEM private key:
        String privateKeyPem = "privateKey.pem";
        CkPrivateKey privkey = new CkPrivateKey();

        //  If the private key PEM is protected with a password, then
        //  call LoadEncryptedPem.  Otherwise call LoadPem.
        success = privkey.LoadPem(privateKeyPem);
        if (success != true) {
            System.out.println("third error");
            System.out.println(privkey.lastErrorText());
            return;
        }

        privateKeyXml = privkey.getXml();
        success = rsa.ImportPrivateKey(privateKeyXml);
        if (success != true) {
            System.out.println("fourth error");
            System.out.println(rsa.lastErrorText());
            return;
        }

        System.out.println("OK!");
    }
            
    public String Encrypt(String plaintext){
        
        CkRsa rsa = new CkRsa();
        boolean success = rsa.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            System.out.println("RSA component unlock failed");
        }

        //  Generate a 1024-bit key.  Chilkat RSA supports
        //  key sizes ranging from 512 bits to 4096 bits.
        success = rsa.GenerateKey(1024);
        if (success != true) {
            System.out.println(rsa.lastErrorText());
        }

        //  Keys are exported in XML format:
        String publicKey = rsa.exportPublicKey();
        String privateKey = rsa.exportPrivateKey();

        //  Start with a new RSA object to demonstrate that all we
        //  need are the keys previously exported:
        CkRsa rsaEncryptor = new CkRsa();

        //  Encrypted output is always binary.  In this case, we want
        //  to encode the encrypted bytes in a printable string.
        //  Our choices are "hex", "base64", "url", "quoted-printable".
        rsaEncryptor.put_EncodingMode("hex");

        //  We'll encrypt with the public key and decrypt with the private
        //  key.  It's also possible to do the reverse.
        rsaEncryptor.ImportPublicKey(publicKeyXml);

        boolean usePrivateKey = false;
        String encryptedStr = rsaEncryptor.encryptStringENC(plaintext,usePrivateKey);
        return encryptedStr;
    }
    
    public String Decrypt(String ciphertext){
        CkRsa rsa = new CkRsa();
        boolean success = rsa.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            System.out.println("RSA component unlock failed");
        }
        //  Now decrypt:
                CkPrivateKey privKey = new CkPrivateKey();

//        success = privKey.LoadXml(keyXml);
//        if (success != true) {
//            System.out.println(privKey.lastErrorText());
//        }

        String strEncPem = privKey.getPkcs8EncryptedPem("");
        System.out.println(strEncPem);
        CkRsa rsaDecryptor = new CkRsa();

        rsaDecryptor.put_EncodingMode("hex");
        rsaDecryptor.ImportPrivateKey(privateKeyXml);

        boolean usePrivateKey = true;
        String decryptedStr = rsaDecryptor.decryptStringENC(ciphertext,usePrivateKey);
        return decryptedStr;
    }

}
