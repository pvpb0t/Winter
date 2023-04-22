package cc.winterclient.client.option;

import cc.winterclient.client.module.Module;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;

/**
 * @Author pvpb0t, sqlskid
 * @Since 7/20/2022
 */
public class OptionManager {

    private static Multimap<Module, Option> options = ArrayListMultimap.create();

    public static Option addOption(Option option){
        options.put(option.getModule(), option);
        return option;
    }

    public static Multimap<Module, Option> getOptions() {
        return options;
    }

    public static Collection<Option> getOptionsList(){
        return  options.values();
    }

    public static void setOptions(Multimap<Module, Option> options){
        OptionManager.options = options;
    }

    public static List<Option> getOptions(Module m){
        List<Option> f = (List<Option>) options.get(m);
    return f; }

}
