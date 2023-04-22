package cc.winterclient.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;


public class RenderUtil
{



	public static void drawBox(AxisAlignedBB box, float r, float g, float b, float a)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableDepth();
		GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glLineWidth(1.5f);

		RenderGlobal.renderFilledBox(box, r, g, b, a);
		RenderGlobal.drawSelectionBoundingBox(box, r, g, b, a * 1.5F);

		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBoxFromBlockpos(BlockPos blockPos, float r, float g, float b, float a)
	{
		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ, blockPos.getX() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosX, blockPos.getY() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosY, blockPos.getZ() + 1 - Minecraft.getMinecraft().getRenderManager().viewerPosZ);
		drawBox(axisAlignedBB, r, g, b, a);
	}

	public static void drawRect(float startX, float startY, float endX, float endY, int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(startX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(endX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(endX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(startX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
	}


}
