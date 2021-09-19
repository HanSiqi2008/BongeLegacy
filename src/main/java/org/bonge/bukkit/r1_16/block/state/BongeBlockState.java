package org.bonge.bukkit.r1_16.block.state;

import org.bonge.Bonge;
import org.bonge.bukkit.r1_16.block.BongeBlock;
import org.bonge.bukkit.r1_16.block.state.states.BongeChestBlockState;
import org.bonge.bukkit.r1_16.block.state.states.BongeSignBlockState;
import org.bonge.bukkit.r1_16.world.BongeLocation;
import org.bonge.bukkit.r1_16.world.BongeWorld;
import org.bonge.util.exception.NotImplementedException;
import org.bonge.wrapper.BongeWrapper;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.entity.BlockEntity;
import org.spongepowered.api.block.entity.Sign;
import org.spongepowered.api.block.entity.carrier.chest.Chest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BongeBlockState<T extends BlockEntity> extends BongeWrapper<T> implements BlockState {

    protected Map<String, Object> changes = new HashMap<>();
    protected org.spongepowered.api.world.Location<? extends org.spongepowered.api.world.World, ? extends org.spongepowered.api.world.Location<?, ?>> position;

    public BongeBlockState(T value) {
        super(value);
        this.position = value.location();
    }

    public org.spongepowered.api.world.Location<? extends org.spongepowered.api.world.World, ? extends org.spongepowered.api.world.Location<?, ?>> getPosition(){
        return this.position;
    }

    public <E> Optional<E> getChanges(String key){
        Object obj = this.changes.get(key);
        if(obj == null){
            return Optional.empty();
        }
        return Optional.of((E)obj);
    }

    public void setChange(String key, Object value){
        if (this.changes.containsKey(key)){
            this.changes.replace(key, value);
            return;
        }
        this.changes.put(key, value);
    }

    public void removeChange(String key){
        this.changes.remove(key);
    }

    @NotNull
    @Override
    public Block getBlock() {
        return new BongeBlock(this.spongeValue.location());
    }

    @NotNull
    @Override
    @Deprecated
    public MaterialData getData() {
        return MaterialData.getData(this.spongeValue.block());
    }

    @NotNull
    @Override
    public BlockData getBlockData() {
        throw new NotImplementedException("BlockData is not implemented");
    }

    @NotNull
    @Override
    public Material getType() {
        try {
            return Bonge.getInstance().convert(Material.class, this.spongeValue.location().blockType());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public byte getLightLevel() {
        throw new NotImplementedException("Sponge does not support LightLevel as far as im aware");
    }

    @NotNull
    @Override
    public World getWorld() {
        return new BongeWorld(this.position.world());
    }

    @Override
    public int getX() {
        return this.position.blockX();
    }

    @Override
    public int getY() {
        return this.position.blockY();
    }

    @Override
    public int getZ() {
        return this.position.blockZ();
    }

    @NotNull
    @Override
    public Location getLocation() {
        return new BongeLocation(this.position);
    }

    @Nullable
    @Override
    public Location getLocation(@Nullable Location loc) {
        throw new NotImplementedException("Method BlockState.getLocation(Location) has not yet been looked at");
    }

    @NotNull
    @Override
    public Chunk getChunk() {
        return getLocation().getChunk();
    }

    @Override
    @Deprecated
    public void setData(@NotNull MaterialData data) {
        this.position.setBlock(data.getSpongeValue());
    }

    @Override
    public void setBlockData(@NotNull BlockData data) {
        throw new NotImplementedException("BlockData has not been implemented yet");
    }

    @Override
    public void setType(@NotNull Material type) {
        throw new NotImplementedException("BlockState.setType(Material) has not yet been looked at yet");
    }

    @Override
    @Deprecated
    public byte getRawData() {
        throw new NotImplementedException("Method BlockState.setRawData() has not yet been looked at");
    }

    @Override
    @Deprecated
    public void setRawData(byte data) {
        throw new NotImplementedException("Method BlockState.setRawData(byte) has not yet been looked at");
    }

    @Override
    public boolean isPlaced() {
        return this.changes.isEmpty();
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        throw new NotImplementedException("Method BlockState.setMetadata(String, MetadataValue) has not yet been looked at");
    }

    @NotNull
    @Override
    public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        throw new NotImplementedException("Method BlockState.getMetadata(String) has not yet been looked at");
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        throw new NotImplementedException("Method BlockState.removeMetadata(String, Plugin) has not yet been looked at");
    }

    @Override
    public boolean update() {
        return update(false);
    }

    @Override
    public boolean update(boolean force) {
        return update(force, true);
    }

    public static <T extends BlockEntity> Optional<BongeBlockState<T>> of(T tile) throws IOException {
        if(tile instanceof Sign){
            return Optional.of((BongeBlockState<T>) new BongeSignBlockState((Sign)tile));
        }
        if(tile instanceof Chest){
            if(tile.block().type().equals(BlockTypes.CHEST)){
                return Optional.of((BongeBlockState<T>)new BongeChestBlockState((Chest)tile));
            }
        }
        return Optional.empty();
    }
}
