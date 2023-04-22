package cc.winterclient.client.util.animations;

public class Animation {

    /*
    Usage
After creating the field, u need to update the animation in some cyclic method.
This is done due to the fact that easings is using in these animations, but more on that later.
animation.update();
Animate
Next, of course, u need to animate it somehow. Now evrthing depends strictly on the type of ur animation.
Ye-ye, exactly what we talked bout above. Please note that the duration should be specified in seconds.
For example: Instead of 500 millis, u will have to specify 0.5 or .5. "Safe" means
whether the animation will be animated if it is already being animated at the moment.
 (This is done so that the animation does not lag if you call animate somewhere 5000 times per second).
 If u dont need it, u can use false, or just dont specify it as an arg. All easings u can find at https://easings.net/ ,
just use Easings.EASING. For example: Easings.NONE if u need just animation.
     */
    private long animationStart;
    private double duration;
    private double animationFromValue;
    private double animationToValue;
    private double lastValue;

    private Easing easing = Easings.NONE;

    public void animate(double value, double duration, boolean safe) {
        animate(value, duration, Easings.NONE, safe);
    }

    public void animate(double value, double duration, Easing easing) {
        animate(value, duration, easing, false);
    }

    public void animate(double value, double duration, Easing easing, boolean safe) {
        if(safe && isAlive()) return;

        setValue(getValue());
        setAnimationToValue(value);
        setAnimationStart(System.currentTimeMillis());
        setDuration(duration);
        setEasing(easing);
    }

    public boolean update() {
        double part = (double) (System.currentTimeMillis() - animationStart) / duration;
        double value;
        if(isAlive()) {
            part = this.easing.ease(part);
            value = animationFromValue + (animationToValue - animationFromValue) * part;
        } else {
            this.animationStart = 0;
            value = this.animationToValue;
        }
        this.lastValue = value;
        return isAlive();
    }

    public boolean isDone() {
        return !isAlive();
    }

    public boolean isAlive() {
        double part = (double) (System.currentTimeMillis() - animationStart) / duration;
        return part < 1.0;
    }

    public double getAnimationFromValue() {
        return animationFromValue;
    }

    public double getAnimationToValue() {
        return animationToValue;
    }

    public double getDuration() {
        return duration;
    }

    public Easing getEasing() {
        return easing;
    }

    public long getAnimationStart() {
        return animationStart;
    }

    public double getValue() {
        return this.lastValue;
    }

    public void setAnimationFromValue(double animationFromValue) {
        this.animationFromValue = animationFromValue;
    }

    public void setAnimationToValue(double animationToValue) {
        this.animationToValue = animationToValue;
    }

    public void setValue(double value) {
        setAnimationFromValue(value);
        setAnimationToValue(value);
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setAnimationStart(long animationStart) {
        this.animationStart = animationStart;
    }

    public void setEasing(Easing easing) {
        this.easing = easing;
    }
}

class QuadAnimation {

    private final PosAnimation pos1 = new PosAnimation();
    private final PosAnimation pos2 = new PosAnimation();

    public void animate(V2 V1, V2 V2, double duration, boolean safe) {
        animate(V1, V2, duration, Easings.NONE, safe);
    }

    public void animate(V2 V1, V2 V2, double duration) {
        animate(V1, V2, duration, Easings.NONE);
    }

    public void animate(V2 V1, V2 V2, double duration, Easing easing) {
        animate(V1, V2, duration, easing, false);
    }

    public void animate(V2 V1, V2 V2, double duration, Easing easing, boolean safe) {
        getPos1().animate(new V2(V1.getX(), V1.getY()), duration, easing, safe);
        getPos2().animate(new V2(V2.getX(), V2.getY()), duration, easing, safe);
    }

    public PosAnimation getPos1() {
        return pos1;
    }

    public PosAnimation getPos2() {
        return pos2;
    }

    public boolean isAlive() {
        return getPos1().isAlive() && getPos2().isAlive();
    }

    public void update() {
        getPos1().update();
        getPos2().update();
    }

    public V2 getValue1() {
        return getPos1().getValue();
    }

    public V2 getValue2() {
        return getPos2().getValue();
    }

}

class PosAnimation {

    private final Animation xPos = new Animation();
    private final Animation yPos = new Animation();

    public void animate(V2 V2, double duration, boolean safe) {
        animate(V2, duration, Easings.NONE, safe);
    }

    public void animate(V2 V2, double duration, Easing easing) {
        animate(V2, duration, easing, false);
    }

    public void animate(V2 V2, double duration, Easing easing, boolean safe) {
        xPos.animate(V2.getX(), duration, easing, safe);
        yPos.animate(V2.getY(), duration, easing, safe);
    }

    public void update() {
        xPos.update();
        yPos.update();
    }

    public Animation getXPos() {
        return xPos;
    }

    public Animation getYPos() {
        return yPos;
    }

    public boolean isAlive() {
        return getXPos().isAlive() && getYPos().isAlive();
    }

    public V2 getValue() {
        return new V2(xPos.getValue(), yPos.getValue());
    }
}