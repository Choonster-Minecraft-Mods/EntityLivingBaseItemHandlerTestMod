package com.github.choonster.entitylivingbaseitemhandlertestmod.inventory;

import com.github.choonster.entitylivingbaseitemhandlertestmod.client.TextureAtlasSprites;
import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

/**
 * A {@link Container} for a living entity's armour and hand inventories.
 *
 * @author Choonster
 */
public class ContainerEntityInventory extends Container {
	/**
	 * The number of slots per row.
	 */
	private static final int SLOTS_PER_ROW = 9;

	/**
	 * The armour {@link EntityEquipmentSlot}s, in the order that they appear in the GUI.
	 */
	private static final EntityEquipmentSlot[] ARMOUR_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

	/**
	 * The slot index of the entity's main hand.
	 */
	private static final int MAIN_HAND_INDEX = 0;

	/**
	 * The slot index of the entity's off hand.
	 */
	private static final int OFF_HAND_INDEX = 1;

	/**
	 * The inclusive slot index of the end of the entity's equipment inventories.
	 */
	private static final int LAST_ENTITY_INDEX = 5;

	/**
	 * The exclusive slot index of the end of the entity's equipment inventories.
	 */
	private static final int ENTITY_END_INDEX = LAST_ENTITY_INDEX + 1;

	/**
	 * The inclusive slot index for the start of the player's inventory.
	 */
	private static final int PLAYER_START_INDEX = 6;

	/**
	 * The inclusive slot index for the start of the player's hotbar inventory section.
	 */
	private static final int PLAYER_HOTBAR_START_INDEX = PLAYER_START_INDEX + 27;
	/**
	 * The exclusive slot index for the end of the player's inventory.
	 */
	private static final int PLAYER_END_INDEX = PLAYER_START_INDEX + 36;

	/**
	 * The entity.
	 */
	private final EntityLivingBase entity;

	public ContainerEntityInventory(final EntityLivingBase entity, final EntityPlayer player) {
		this.entity = entity;

		final IItemHandlerModifiable entityHandInventory = Preconditions.checkNotNull((IItemHandlerModifiable) entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP));

		addSlotToContainer(new SlotItemHandler(entityHandInventory, 0, 127, 20) {
			@SideOnly(Side.CLIENT)
			@Override
			public TextureAtlasSprite getBackgroundSprite() {
				return TextureAtlasSprites.EMPTY_SLOT_SWORD;
			}
		});

		addSlotToContainer(new SlotItemHandler(entityHandInventory, 1, 35, 20) {
			@SideOnly(Side.CLIENT)
			@Override
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_shield";
			}
		});


		final IItemHandlerModifiable entityArmourInventory = Preconditions.checkNotNull((IItemHandlerModifiable) entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));

		for (int i = 0; i < ARMOUR_EQUIPMENT_SLOTS.length; i++) {
			final int col = i / 2;
			final int row = i % 2;

			final EntityEquipmentSlot equipmentSlot = ARMOUR_EQUIPMENT_SLOTS[i];

			addSlotToContainer(new SlotItemHandler(entityArmourInventory, 3 - i, 54 + col * 54, 6 + row * 27) {
				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(final ItemStack stack) {
					return stack.getItem().isValidArmor(stack, equipmentSlot, entity);
				}

				@Override
				public boolean canTakeStack(final EntityPlayer playerIn) {
					final ItemStack stack = getStack();
					return (stack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(stack)) && super.canTakeStack(playerIn);
				}

				@Nullable
				@SideOnly(Side.CLIENT)
				public String getSlotTexture() {
					return ItemArmor.EMPTY_SLOT_NAMES[equipmentSlot.getIndex()];
				}
			});
		}


		final IItemHandlerModifiable playerInventory = Preconditions.checkNotNull((IItemHandlerModifiable) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP));

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < SLOTS_PER_ROW; ++col) {
				addSlotToContainer(new SlotItemHandler(playerInventory, col + row * SLOTS_PER_ROW + SLOTS_PER_ROW, 9 + col * 18, 54 + row * 18));
			}
		}

		for (int col = 0; col < SLOTS_PER_ROW; ++col) {
			addSlotToContainer(new SlotItemHandler(playerInventory, col, 9 + col * 18, 112));
		}
	}

	/**
	 * Get the entity.
	 *
	 * @return The entity.
	 */
	public EntityLivingBase getEntity() {
		return entity;
	}

	@Override
	public boolean canInteractWith(final EntityPlayer player) {
		return !entity.isDead && player.getDistanceSqToEntity(entity) <= 64;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack stack = slot.getStack();
			final ItemStack originalStack = stack.copy();
			final EntityEquipmentSlot equipmentSlot = EntityLiving.getSlotForItemStack(originalStack);

			if (index < ENTITY_END_INDEX) {
				// If one of the entity's slots was clicked, attempt to transfer it to the player's inventory
				if (!this.mergeItemStack(stack, PLAYER_START_INDEX, PLAYER_END_INDEX, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (equipmentSlot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(LAST_ENTITY_INDEX - equipmentSlot.getIndex()).getHasStack()) {
					// If an armour item was clicked and the corresponding slot of the entity's armour inventory is empty, attempt to transfer it to that slot.
					final int destinationIndex = LAST_ENTITY_INDEX - equipmentSlot.getIndex();

					if (!this.mergeItemStack(stack, destinationIndex, destinationIndex + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (equipmentSlot == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(OFF_HAND_INDEX).getHasStack()) {
					// If a shield was clicked and entity's off hand slot is empty, attempt to transfer it to that slot.
					if (!this.mergeItemStack(stack, OFF_HAND_INDEX, OFF_HAND_INDEX + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
					// If any other item was clicked , attempt to transfer it to the entity's main hand slot.
					// If the transfer fails, fall through to the hotbar/main inventory transfers.
					this.mergeItemStack(stack, MAIN_HAND_INDEX, MAIN_HAND_INDEX + 1, false);
				}

				// Only transfer between the hotbar/main inventory if no transfer has been made yet.
				// This prevents a stack in the player's inventory jumping between sections if part of it was transferred to the entity's main hand slot.
				if (stack.getCount() == originalStack.getCount()) {
					if (index >= PLAYER_START_INDEX && index < PLAYER_HOTBAR_START_INDEX) {
						// If a slot in the player's main inventory section was clicked, attempt to transfer it to the hotbar.
						if (!this.mergeItemStack(stack, PLAYER_HOTBAR_START_INDEX, PLAYER_END_INDEX, false)) {
							return ItemStack.EMPTY;
						}
					} else if (index >= PLAYER_HOTBAR_START_INDEX && index < PLAYER_END_INDEX) {
						// If a slot in the player's hotbar was clicked, attempt to transfer it to the main inventory section.
						if (!this.mergeItemStack(stack, PLAYER_START_INDEX, PLAYER_HOTBAR_START_INDEX, false)) {
							return ItemStack.EMPTY;
						}

						// Else attempt to transfer it to the player's inventory.
					} else if (!this.mergeItemStack(stack, PLAYER_START_INDEX, PLAYER_END_INDEX, false)) {
						return ItemStack.EMPTY;
					}
				}
			}

			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (stack.getCount() == originalStack.getCount()) {
				return ItemStack.EMPTY;
			}
		}

		return ItemStack.EMPTY;
	}
}
