package de.maxhenkel.corelib.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

public class TileEntityContainerProvider implements INamedContainerProvider {

    private ContainerCreator container;
    private TileEntity tileEntity;

    public TileEntityContainerProvider(ContainerCreator container, TileEntity tileEntity) {
        this.container = container;
        this.tileEntity = tileEntity;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(tileEntity.getBlockState().getBlock().getDescriptionId());
    }

    public static void openGui(PlayerEntity player, TileEntity tileEntity, ContainerCreator containerCreator) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new TileEntityContainerProvider(containerCreator, tileEntity), packetBuffer -> packetBuffer.writeBlockPos(tileEntity.getBlockPos()));
        }
    }

    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return container.create(i, playerInventory, playerEntity);
    }

    public interface ContainerCreator {
        Container create(int i, PlayerInventory playerInventory, PlayerEntity playerEntity);
    }
}
