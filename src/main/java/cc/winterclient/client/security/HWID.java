package cc.winterclient.client.security;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HWID {

    private static byte[] encrypted = "ketamin3.top".getBytes();

    public static byte[] generateBytes(){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String string = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("HOMEDRIVE") +  System.getenv("OS")+ System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL") + new File(System.getProperty("user.home")).getTotalSpace() + System.getenv("PROCESSOR_REVISION") +System.getenv("PROCESSOR_ARCHITECTURE");
        md.update(string.getBytes());
        encrypted = md.digest();
        return encrypted;
    }

    public static String EncryptedByteArray(byte[] lol){
        String encodedString = Base64.getEncoder().encodeToString(lol);
    return encodedString;}

    public static byte[] getEncrypted() {
        return encrypted;
    }
}
