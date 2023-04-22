package cc.winterclient.client.option.ext;

import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;

import java.util.function.Predicate;

/**
 * @Author pvpb0t
 * @Since 7/20/2022
 */
public class DoubleOption extends Option<Double> {

    private Double min, max;
    private Boolean rounded;

    public DoubleOption(String name, Double exact, Double min, Double max, Module module, Boolean rounded) {
        super(name, exact, module);
        this.min = min;
        this.max = max;
        this.rounded = rounded;
    }

    public DoubleOption(String name, Double exact, Double min, Double max, Module module, Boolean rounded, Predicate shown) {
        super(name, exact, module, shown);
        this.min = min;
        this.max = max;
        this.rounded = rounded;

    }

    public Boolean getRounded() {
        return rounded;
    }

    public void setRounded(Boolean rounded) {
        this.rounded = rounded;
    }

    public Double getMin() {
        return min;
    }


    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
}
