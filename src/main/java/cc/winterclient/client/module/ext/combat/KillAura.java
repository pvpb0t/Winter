package cc.winterclient.client.module.ext.combat;


import cc.winterclient.client.Winter;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.event.ext.update.EventUpdate;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import cc.winterclient.client.util.DamageUtil;
import cc.winterclient.client.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;


public class KillAura extends Module {


    private Option<Double> range = new DoubleOption("Range", 4.5D, 0D, 6D, this, false);
    private Option<Double> cps = new DoubleOption("CPS", 12D, 0D, 20D, this, true);
    private Option<Boolean> hitDelay = new BoolOption("1.9 Delay", true, this);
    private Option<Boolean> throughWalls = new BoolOption("Through Walls", false, this);
    private Option<Boolean> rotate = new BoolOption("Rotate", true, this);
    private Option<Boolean> animals = new BoolOption("Animals", true, this);
    private Option<Boolean> hostiles = new BoolOption("Hostiles", true, this);
    private Option<Boolean> players = new BoolOption("Players", true, this);


    private EntityLivingBase target;
    private int attackSpeed = 0;
    private final Timer timer = new Timer();


    public KillAura() {
        super("KillAura", Category.COMBAT, 0);
    }


    @EventTarget
    public void onPre(EventPre event){

        if(target != null){
            if(rotate.getExact()){
                float[] rotations = rotateToPlayer(target);

                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
                //mc.player.setRenderYawOffset(rotations[0]);
                //mc.player.setRotationYawHead(rotations[0]);
            }

            if(hitDelay.getExact())
            {
                if(timer.passedMs(DamageUtil.getCooldownByWeapon(mc.player))){
                    this.attackEntity(target);
                    timer.reset();
                }
            }
            else {
                for (int attackTimer = 0; attackTimer < MathHelper.clamp(cps.getExact(), 1, 20); attackTimer++) {
                    if (Math.abs(attackSpeed) % 20 == 0) {
                        this.attackEntity(target);
                    }

                    if (attackSpeed >= Integer.MAX_VALUE - 1024) {
                        attackSpeed = 0;
                    }

                    attackSpeed++;
                }
            }
        }
    }

    private void attackEntity(EntityLivingBase target) {
        mc.playerController.attackEntity(mc.player, target);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        target = getEntities();
    }

    private EntityLivingBase getEntities()
    {
        double distance = range.getExact();

        EntityLivingBase targetEntity = null;

        if(mc.world != null)
        {
            for(Entity e : mc.world.loadedEntityList)
            {
                if (e instanceof EntityLivingBase)
                {
                    EntityLivingBase player = (EntityLivingBase) e;

                    if (isValid(player))
                    {
                        double currentDist = mc.player.getDistance(player);

                        if (currentDist <= distance)
                        {
                            distance = currentDist;
                            targetEntity = player;
                        }
                    }
                }
            }
        }

        return targetEntity;
    }

    public boolean isValid(EntityLivingBase entityLiving){
        if((entityLiving == mc.player || entityLiving.isDead))
            return false;

        if(!mc.player.canEntityBeSeen(entityLiving) && !throughWalls.getExact())
            return false;

        if(!(entityLiving.getDistance(mc.player) <= range.getExact()))
            return false;

        if(entityLiving instanceof EntityAnimal && animals.getExact())
            return true;

        if(entityLiving instanceof EntityMob && hostiles.getExact())
            return true;

        if(entityLiving instanceof EntityPlayer && players.getExact() && !Winter.instance.friendManager.isFriend(entityLiving.getName()))
            return true;

        return false;
    }

    private float[] rotateToPlayer(EntityLivingBase target)
    {
        double x = (target.posX) - mc.player.lastTickPosX;
        double y = (target.posY) + target.getEyeHeight() - (mc.player.lastTickPosY + mc.player.getEyeHeight());
        double z = (target.posZ) - mc.player.lastTickPosZ;
        double diff = MathHelper.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0F / 3.14) - 90.0F;
        float pitch = (float) ((float) -(Math.atan2(y, diff) * 180.0F) / 3.14);
        return new float[] { mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
    }

}
