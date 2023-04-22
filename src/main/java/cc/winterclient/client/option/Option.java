package cc.winterclient.client.option;

import cc.winterclient.client.module.Module;

import java.util.function.Predicate;

/**
 * @Author pvpb0t, sqlskid
 * @Since 7/12/2022
 * @param <T>
 */

public class Option<T> {


    private String Name;
    private T exact;
    private Module module;
    private Predicate<T> shown;

    public Option(String name, T exact, Module module) {
        Name = name;
        this.exact = exact;
        this.module = module;
        OptionManager.addOption(this);
    }

    public Option(String name, T exact, Module module, Predicate<T> visibility) {
        Name = name;
        this.exact = exact;
        this.module = module;
        this.shown = visibility;
        OptionManager.addOption(this);

    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public T getExact() {
        return exact;
    }

    public void setExact(T type) {
        this.exact = type;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setShown(Predicate<T> shown) {
        this.shown = shown;
    }

    public boolean isShown(){
        if(shown==null){
            return true;
        }
        return this.shown.test(this.getExact());
    }

}
