package com.github.choonster.entitylivingbaseitemhandlertestmod.client.gui.inventory;

import com.github.choonster.entitylivingbaseitemhandlertestmod.EntityLivingBaseItemHandlerTestMod;
import com.github.choonster.entitylivingbaseitemhandlertestmod.inventory.ContainerEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * A {@link GuiScreen} for a living entity's inventory.
 *
 * @author Choonster
 */
public class GuiEntityInventory extends GuiContainer {
	private static final ResourceLocation ENTITY_INVENTORY_GUI_TEXTURE = new ResourceLocation(EntityLivingBaseItemHandlerTestMod.MODID, "textures/gui/container/entity_inventory.png");

	/**
	 * The colour of the inventory names.
	 */
	private static final int TEXT_COLOUR = 0x404040;

	/**
	 * The entity.
	 */
	private final EntityLivingBase entity;

	public GuiEntityInventory(final ContainerEntityInventory container) {
		super(container);
		entity = container.getEntity();

		xSize = 178;
		ySize = 159;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 *
	 * @param mouseX Mouse x coordinate
	 * @param mouseY Mouse y coordinate
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		this.fontRenderer.drawString(entity.getDisplayName().getUnformattedText(), 8, 6, TEXT_COLOUR);
		this.fontRenderer.drawString(mc.player.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, TEXT_COLOUR);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 *
	 * @param partialTicks How far into the current tick the game is, with 0.0 being the start of the tick and 1.0 being the end.
	 * @param mouseX       Mouse x coordinate
	 * @param mouseY       Mouse y coordinate
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1, 1, 1, 1);

		this.mc.getTextureManager().bindTexture(ENTITY_INVENTORY_GUI_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 58, 20, this.guiLeft + 88 - mouseX, this.guiTop + 45 - 30 - mouseY, entity);
	}
}
