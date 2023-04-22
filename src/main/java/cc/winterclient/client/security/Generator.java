package cc.winterclient.client.security;

import cc.winterclient.client.Winter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Generator {

    private long seed;
    private Lisence lisence;
    private Random generator;

    public Generator(long seed, Lisence lisence) {
        this.lisence = lisence;
        this.generator = new Random(seed);

    }

    public void createLisence(){
        try {
            String checksum = getFileChecksum(MessageDigest.getInstance("MD5"),new File("mods" + File.separator + Winter.clientName+"-" + Winter.clientVersion+"release.jar"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };
        fis.close();
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }



}
