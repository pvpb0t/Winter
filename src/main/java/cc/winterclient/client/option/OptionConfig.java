package cc.winterclient.client.option;

import cc.winterclient.client.Winter;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.util.logger.Logger;
import com.google.common.collect.Multimap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.Locale;

/**
 * @Author pvpb0t, sqlskid
 * @Since 7/20/2022
 */
public class OptionConfig extends Thread{

    private static Multimap<Module, Option> options = OptionManager.getOptions();
    private static String path = Winter.instance.path.getName() + File.separator +  "Config";

    public void saveOptionConfig(){
        File mkdirpath = new File(path);
        if(mkdirpath.mkdir()){
        }
        Logger.getLogger().print("Saving Configs");

        for(Module m : Winter.instance.moduleManager.getModuleList()){
            String filepath =path + File.separator + m.getCategory().toString().toLowerCase(Locale.ROOT)+ File.separator+m.getName() + ".cllst";
            File filez = new File(filepath);
            filez.getParentFile().mkdirs();
            try{
                JSONArray arrayz = new JSONArray();
                JSONObject isEnabled = new JSONObject();
                JSONObject KeyBind = new JSONObject();

                isEnabled.put("Toggled", m.isEnabled());
                arrayz.add(isEnabled);

                KeyBind.put("Key", m.getKey());
                arrayz.add(KeyBind);

                if(options.keySet().contains(m)) {
                    for (Option op : OptionManager.getOptions(m)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Exact", op.getExact());
                        jsonObject.put("Name", op.getName());
                        arrayz.add(jsonObject);


                    }
                }


                FileWriter file = new FileWriter(filepath);
                file.write(JsonUtil.prettyPrintJSON(arrayz.toJSONString()));
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }



    public static void loadOptionConfig(){
        File mkdirpath = new File(path);
        if(mkdirpath.mkdir()){
        }
        Logger.getLogger().print("Loading Configs");

        JSONParser parser = new JSONParser();
        for(Module m : Winter.instance.moduleManager.getModuleList()){
            String filepath =path + File.separator + m.getCategory().toString().toLowerCase(Locale.ROOT)+ File.separator+m.getName() + ".cllst";
            if(new File(filepath).exists()) {
                JSONParser jsonParser = new JSONParser();
                try (FileReader reader = new FileReader(filepath))
                {
                    Object obj = jsonParser.parse(reader);
                   JSONArray array = (JSONArray) obj;
                    for(Object ob: array.toArray()){
                        JSONObject object = (JSONObject) ob;

                        Boolean toggled = (Boolean) object.get("Toggled");
                        if(toggled != null){
                            if((Boolean) object.get("Toggled")){
                                Winter.moduleManager.getToEnable().add(m);
                            }
                        }

                        Long intkey = (Long) object.get("Key");
                        if(intkey != null){
                            if(intkey != Keyboard.KEY_NONE){
                                m.setKey(Math.toIntExact(intkey));
                            }
                        }

                        String name = (String) object.get("Name");
                        if(object.get("Exact") != null && name != null){
                            for(Option option : OptionManager.getOptions(m)){
                                if(option.getName().equals(name)){
                                    option.setExact(object.get("Exact"));
                                }
                            }
                        }


                    }

                } catch (FileNotFoundException e) {
                    System.err.println("ERROR : FILE NOT FOUND.");
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }else {
                try{
                    Thread thread = new OptionConfig();
                    thread.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void run() {
        saveOptionConfig();
    }
}
