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
public class SHA1 {
    public String Hash(String plaintext){
    CkCrypt2 crypt = new CkCrypt2();

    //  Any string argument automatically begins the 30-day trial.
    boolean success = crypt.UnlockComponent("30-day trial");
    if (success != true) {
        System.out.println(crypt.lastErrorText());
    }

    crypt.put_HashAlgorithm("sha1");
    crypt.put_EncodingMode("hex");
    
    String hash = crypt.hashStringENC(plaintext);
    return hash;    
    }
}
