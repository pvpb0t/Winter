package cc.winterclient.client.module.ext.combat;


import cc.winterclient.client.Winter;
import cc.winterclient.client.event.EventTarget;
import cc.winterclient.client.event.ext.update.EventPre;
import cc.winterclient.client.event.ext.scenarios.EventReceivePacket;
import cc.winterclient.client.event.ext.render.EventRender2D;
import cc.winterclient.client.event.ext.update.EventUpdate;
import cc.winterclient.client.module.Category;
import cc.winterclient.client.module.Module;
import cc.winterclient.client.option.Option;
import cc.winterclient.client.option.ext.BoolOption;
import cc.winterclient.client.option.ext.DoubleOption;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class CrystalAura extends Module {
    private Option<Double> range = new DoubleOption("Range", 5D, 0D, 6D, this, false);
    private Option<Double> placeRange = new DoubleOption("Place Range", 4.25D, 0D, 6D, this, false);
    private Option<Double> breakRange = new DoubleOption("Break Range", 4.25D, 0D, 6D, this, false);
    private Option<Double> facePlaceHealth = new DoubleOption("FC Health", 10D, 0D, 20D, this, true);
    private Option<Double> facePlaceRange = new DoubleOption("FC Range", 2D, 0D, 6D, this, false);


    private Option<Boolean> placing = new BoolOption("Place", true, this);
    private Option<Boolean> breaking = new BoolOption("Breaking", true, this);
    private Option<Boolean> facePlacing = new BoolOption("Face Place", false, this);
    private Option<Boolean> rotations = new BoolOption("Rotations", false, this);




    public CrystalAura() {
        super("CrystalAura", Category.COMBAT, 0);
    }

    EntityPlayer target;
    boolean targetExposed = false;

    public List<Long> brokenCrystals = new ArrayList<>();
    public List<Long> placedCrystals = new ArrayList<>();

    private final Map<Integer, EntityTime> killed = new ConcurrentHashMap<>();

    @EventTarget
    public void onReceive(EventReceivePacket e) {
        if (e.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) e.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                Vec3d pos = new Vec3d(packet.getX(), packet.getY(), packet.getZ());
                mc.addScheduledTask(() -> {
                    removeCrystals(pos, 11.0f, mc.world.loadedEntityList);
                });
            }
        }
    }

    public void removeCrystals(Vec3d pos, float range, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof EntityEnderCrystal
                    && entity.getDistanceSq(pos.x, pos.y, pos.z)
                    <= (range * range)) {
                setDead(entity);
            }
        }
    }

    public void setDead(Entity entity) {
        EntityTime time = killed.get(entity.getEntityId());
        if (time != null) {
            time.getEntity().setDead();
            time.reset();
        } else if (!entity.isDead) {
            entity.setDead();
            killed.put(entity.getEntityId(), new EntityTime(entity));
        }
    }

    @EventTarget
    public void onPre(EventPre e) {
        target = getEntities();

        if (target != null) {
            targetExposed = isExposed(target);

            if (breaking.getExact()) {
                EntityEnderCrystal crystal = getCrystal();
                if(crystal != null &&rotations.getExact()) {
                    float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), crystal.getPositionVector());
                    e.setYaw(angle[0]);
                    e.setPitch(angle[1]);
                }

                if (crystal != null) {
                    mc.playerController.attackEntity(mc.player, crystal);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    brokenCrystals.add(System.currentTimeMillis());
                }
            }

            if (targetExposed) {
                if (placing.getExact()) {
                    Vec3d placePos = posForCrystal(target);
                    if(placePos != null && rotations.getExact())
                    {
                        float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) (placePos.x + 0.5f), ((float)placePos.y - 0.5f), ((float)placePos.z + 0.5f)));
                        e.setYaw(angle[0]);
                        e.setPitch(angle[1]);
                    }

                    if (placePos != null) {
                        placeCrystalOnBlock(new BlockPos(placePos), EnumHand.MAIN_HAND, true, true);
                        placedCrystals.add(System.currentTimeMillis());
                    }
                }
            }
            else
            {
                if(facePlacing.getExact()){
                    if(target.getHealth() <= facePlaceHealth.getExact()){
                        Vec3d placePos = posForCrystalFacePlace(target);

                        if(placePos != null && rotations.getExact())
                        {
                            float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) (placePos.x + 0.5f), ((float)placePos.y - 0.5f), ((float)placePos.z + 0.5f)));
                            e.setYaw(angle[0]);
                            e.setPitch(angle[1]);
                        }

                        if (placePos != null) {
                            placeCrystalOnBlock(new BlockPos(placePos), EnumHand.MAIN_HAND, true, true);
                            placedCrystals.add(System.currentTimeMillis());
                        }
                    }
                }
            }

        }
    }

    int cps = 0;

    private int getBroken() {
        final long time = System.currentTimeMillis();
        this.brokenCrystals.removeIf(aLong -> aLong + 1000 < time);
        return this.brokenCrystals.size();
    }

    private int getPlaced() {
        final long time2 = System.currentTimeMillis();
        this.placedCrystals.removeIf(aLong2 -> aLong2 + 1000 < time2);
        return this.placedCrystals.size();
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        cps = (getBroken() + getPlaced()) / 2;
    }

    private Vec3d posForCrystalFacePlace(EntityPlayer entityPlayer) {
        List<Vec3d> freeLocs = new ArrayList<>();

        for (double x = -facePlaceRange.getExact(); x < facePlaceRange.getExact(); x++) {
            for (double y = 0; y < 1; y++) {
                for (double z = -facePlaceRange.getExact(); z < facePlaceRange.getExact(); z++) {
                    BlockPos blockPos = new BlockPos(entityPlayer.posX + x, entityPlayer.posY + y, entityPlayer.posZ + z);
                    BlockPos blockPos2 = new BlockPos(entityPlayer.posX + x, entityPlayer.posY + y + 1, entityPlayer.posZ + z);

                    if (mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) {
                        if (mc.world.getBlockState(blockPos2).getBlock() == Blocks.AIR) {
                            freeLocs.add(new Vec3d(blockPos));
                        }
                    }
                }
            }
        }


        Vec3d closest = closest(entityPlayer.getPositionVector(), freeLocs);
        return closest;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        String text;
        int color;

        if (target == null) {
            text = "target not found";
            color = 0xFF0000;
        } else {
            if (targetExposed) {
                text = "exposed";
                color = 0x00FF00;
            } else {
                text = "not exposed";
                color = 0xFF0000;
            }
        }


        mc.fontRenderer.drawStringWithShadow(text, 25, 25, color);

        mc.fontRenderer.drawStringWithShadow("CPS: " + cps, 25, 45, 0xFF00FF);
    }

    private EntityPlayer getEntities() {
        double distance = range.getExact();

        EntityPlayer targetEntity = null;

        if (mc.world != null) {
            for (Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) e;

                    if (isValid(player)) {
                        double currentDist = mc.player.getDistance(player);

                        if (currentDist <= distance) {
                            distance = currentDist;
                            targetEntity = player;
                        }
                    }
                }
            }
        }

        return targetEntity;
    }

    private EntityEnderCrystal getCrystal() {
        double distance = breakRange.getExact();

        EntityEnderCrystal targetEntity = null;

        if (mc.world != null) {
            for (Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityEnderCrystal) {
                    EntityEnderCrystal crystal = (EntityEnderCrystal) e;
                    double currentDist = mc.player.getDistance(crystal);

                    if (currentDist <= distance) {
                        distance = currentDist;
                        targetEntity = crystal;
                    }
                }
            }
        }

        return targetEntity;
    }

    public Vec3d posForCrystal(EntityPlayer entityPlayer) {
        List<Vec3d> freeLocs = new ArrayList<>();

        for (double x = -placeRange.getExact(); x < placeRange.getExact(); x++) {
            for (double y = -4; y < 0; y++) {
                for (double z = -placeRange.getExact(); z < placeRange.getExact(); z++) {
                    BlockPos blockPos = new BlockPos(entityPlayer.posX + x, entityPlayer.posY + y, entityPlayer.posZ + z);
                    BlockPos blockPos2 = new BlockPos(entityPlayer.posX + x, entityPlayer.posY + y + 1, entityPlayer.posZ + z);

                    if (mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK) {
                        if (mc.world.getBlockState(blockPos2).getBlock() == Blocks.AIR) {
                            freeLocs.add(new Vec3d(blockPos));
                        }
                    }
                }
            }
        }


        Vec3d closest = closest(entityPlayer.getPositionVector(), freeLocs);

        return closest;
    }

    public static Vec3d closest(Vec3d target, List<Vec3d> list) {
        Vec3d closest = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            Vec3d curr = list.get(i);
            if (target.distanceTo(curr) < target.distanceTo(closest))
                closest = curr;
        }

        return closest;
    }


    public boolean isValid(EntityPlayer entityLiving) {
        if ((entityLiving == mc.player || entityLiving.isDead))
            return false;

        //if(!mc.player.canEntityBeSeen(entityLiving))
        //    return false;

        if(Winter.instance.friendManager.isFriend(entityLiving.getName()))
            return false;

        if (!(entityLiving.getDistance(mc.player) <= range.getExact()))
            return false;


        return true;
    }

    public boolean isExposed(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west()}) {
            IBlockState iBlockState = mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.BEDROCK || iBlockState.getBlock() == Blocks.OBSIDIAN) continue;
            return true;
        }
        return false;
    }

    public void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing, boolean exactHand) {
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double) pos.getX() + 0.5, (double) pos.getY() - 0.5, (double) pos.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if (swing) {
            mc.player.connection.sendPacket(new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public class EntityTime {
        private final AtomicBoolean valid = new AtomicBoolean(true);
        private final Entity entity;
        public long time;

        public EntityTime(Entity entity) {
            this.entity = entity;
            this.time = System.currentTimeMillis();
        }

        public boolean passed(long ms) {
            return ms <= 0 || System.currentTimeMillis() - time > ms;
        }

        public Entity getEntity() {
            return entity;
        }

        public void setValid(boolean valid) {
            this.valid.set(valid);
        }

        public boolean isValid() {
            return valid.get();
        }

        public void reset() {
            this.time = System.currentTimeMillis();
        }
    }

}
