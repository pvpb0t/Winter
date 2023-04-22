package cc.winterclient.client.util.world;

import cc.winterclient.client.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class BlockUtil implements Wrapper {

    public static Block getBlock(BlockPos blockPos){
        return mc.world.getBlockState(blockPos).getBlock();
    }


    public static Block getStandingBlock(){
        return mc.world.getBlockState(mc.player.getPosition().down()).getBlock();
    }

}
