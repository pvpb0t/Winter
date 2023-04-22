package cc.winterclient.client.command.ext;

import cc.winterclient.client.command.Command;
import cc.winterclient.client.util.logger.Logger;

public class Hclip extends Command {
    public Hclip() {
        super("hclip", "Teleports you forward a certain ammount of blocks", "h");
    }

    @Override
    public void execute(String[] args) {
        if(args.length > 0){
            //got angle, then have hypo so do cos for x then sin for z
            double travel = Double.parseDouble(args[0]);
            Logger.getLogger().printToChat("Travel is " + travel);

            double angle = mc.player.rotationYaw;
            Logger.getLogger().printToChat(String.valueOf(angle));

            if(angle<0){
                angle = 360+angle;
            }

            if(angle > 360){
                int anglem = (int) (angle/360);
                angle = angle - anglem*360;
            }


            mc.player.setPosition(mc.player.posX + travel * -Math.sin(Math.toRadians(angle)), mc.player.posY, mc.player.posZ + travel * Math.cos(Math.toRadians(angle)));

            //mc.player.rotationYaw = 270;
        }
    }
}
