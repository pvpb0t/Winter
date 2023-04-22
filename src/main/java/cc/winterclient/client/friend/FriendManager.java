package cc.winterclient.client.friend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    public List<String> friends = new ArrayList<>();

    public File friendFile = new File("Winter" + System.getProperty("file.separator") + "friends.txt");

    public FriendManager(){
        if(!friendFile.exists()) {
            try {
                friendFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        load();
    }

    public void load(){

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(friendFile));
            String s;
            while ((s = bufferedReader.readLine()) != null){
                friends.add(s);
            }
            bufferedReader.close();
        }
        catch (FileNotFoundException e){
            try {
                friendFile.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(friendFile));
            for (String s : friends) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addFriend(String name)
    {
        if(!isFriend(name))
        {
            friends.add(name);
            return true;
        }
        return false;
    }

    public boolean removeFriend(String name){
        if(isFriend(name))
        {
            friends.remove(name);
            return true;
        }
        return false;
    }

    public boolean isFriend(String username) {
        for (String s : friends) {
            if (s.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getFriends() {
        return friends;
    }
}
