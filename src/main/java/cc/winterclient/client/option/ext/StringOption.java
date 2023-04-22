package cc.winterclient.client.option.ext;

import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;

import java.util.function.Predicate;

/**
 * @Author pvpb0t
 * @Since 7/20/2022
 */
public class StringOption extends Option<String> {

    public StringOption(String name, String exact, Module module) {
        super(name, exact, module);
    }

    public StringOption(String name, String exact, Module module, Predicate shown) {
        super(name, exact, module, shown);
    }
}
