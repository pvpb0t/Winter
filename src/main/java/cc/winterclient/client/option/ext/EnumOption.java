package cc.winterclient.client.option.ext;

import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;

import java.util.List;
import java.util.function.Predicate;

/**
 * @Author pvpb0t
 * @Since 7/20/2022
 */
public class EnumOption extends Option<String> {

    private List<String> enums;

    public EnumOption(String name, String exact, List<String> list, Module module) {
        super(name, exact, module);
        this.enums = list;
    }

    public EnumOption(String name, String exact, Module module, List<String> list, Predicate shown) {
        super(name, exact, module, shown);
        this.enums = list;
    }

    public List<String> getEnums() {
        return enums;
    }

    public void setEnums(List<String> enums) {
        this.enums = enums;
    }
}
