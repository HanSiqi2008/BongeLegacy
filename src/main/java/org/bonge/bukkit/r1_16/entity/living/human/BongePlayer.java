package org.bonge.bukkit.r1_16.entity.living.human;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bonge.Bonge;
import org.bonge.bukkit.r1_16.block.data.BongeAbstractBlockData;
import org.bonge.bukkit.r1_16.entity.BongeAbstractEntity;
import org.bonge.bukkit.r1_16.entity.EntityManager;
import org.bonge.bukkit.r1_16.inventory.BongeInventory;
import org.bonge.bukkit.r1_16.inventory.BongeInventorySnapshot;
import org.bonge.bukkit.r1_16.inventory.BongeInventoryView;
import org.bonge.bukkit.r1_16.inventory.entity.living.player.BongePlayerInventory;
import org.bonge.bukkit.r1_16.inventory.entity.living.player.PlayerInventorySnapshot;
import org.bonge.bukkit.r1_16.scoreboard.BongeScoreboard;
import org.bonge.command.Permissions;
import org.bonge.util.exception.NotImplementedException;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.api.util.Tristate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class BongePlayer extends BongeAbstractEntity<org.spongepowered.api.entity.living.player.Player> implements IHuman<org.spongepowered.api.entity.living.player.Player>, Player {

    private static Set<BongePlayer> PLAYERS = new HashSet<>();

    private final Set<PermissionAttachment> permissionsOverride = new HashSet<>();
    private BongeInventoryView view;

    public BongePlayer(org.spongepowered.api.entity.living.player.Player entity) {
        super(entity);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return super.getUniqueId();
    }

    @Override
    @Deprecated
    public boolean isOnGround() {
        return super.isOnGround();
    }

    @Override
    public @NotNull PlayerInventory getInventory() {
        BongeInventorySnapshot<? extends org.spongepowered.api.item.inventory.Inventory> inventory = this.getData().get(EntityManager.INVENTORY);
        if (inventory != null) {
            return (PlayerInventorySnapshot) inventory;
        }
        return new BongePlayerInventory(this.spongeValue.inventory());
    }

    @Override
    public @NotNull String getDisplayName() {
        return Bonge.getInstance().convert(this.spongeValue.displayName().get());
    }

    @Override
    public void setDisplayName(@Nullable String name) {
        this.spongeValue.displayName().set(Bonge.getInstance().convertText(name));
    }

    @Override
    public @NotNull String getPlayerListName() {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void setPlayerListName(@Nullable String name) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public @Nullable String getPlayerListHeader() {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public @Nullable String getPlayerListFooter() {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void setPlayerListHeader(@Nullable String header) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void setPlayerListFooter(@Nullable String footer) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer) {
        this.setPlayerListFooter(footer);
        this.setPlayerListHeader(header);
    }

    @Override
    public void setCompassTarget(@NotNull Location loc) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public @NotNull Location getCompassTarget() {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public @Nullable InetSocketAddress getAddress() {
        if (this.spongeValue instanceof ServerPlayer) {
            return ((ServerPlayer) this.spongeValue).connection().address();
        }
        /*if(this.spongeValue instanceof ClientPlayer){
            //assume the clients ip ... prevents crash
            return new InetSocketAddress(25565);
        }*/
        //TODO get remoteplayers ip?
        return new InetSocketAddress(25565);
    }

    @Override
    public boolean isConversing() {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void acceptConversationInput(@NotNull String input) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void sendRawMessage(@NotNull String message) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void sendRawMessage(@Nullable UUID sender, @NotNull String message) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void sendMessage(@NotNull String message) {
        this.spongeValue.sendMessage(Bonge.getInstance().convertText(message));
    }

    @Override
    public void sendMessage(@NotNull String[] messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public void kickPlayer(@Nullable String message) {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer) this.spongeValue;
        if (message != null) {
            player.kick(Bonge.getInstance().convertText(message));
            return;
        }
        player.kick();
    }

    @Override
    public void chat(@NotNull String msg) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public boolean performCommand(@NotNull String command) {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            return false;
        }
        CommandResult result;
        try {
            result = Sponge.server().commandManager().process(((ServerPlayer) this.spongeValue), command);
        } catch (CommandException e) {
            Component comp = e.componentMessage();
            if (comp != null) {
                this.spongeValue.sendMessage(comp);
            }
            return false;
        }
        return !result.equals(CommandResult.success());
    }

    @Override
    public boolean isSneaking() {
        return this.spongeValue.get(Keys.IS_SNEAKING).get();
    }

    @Override
    public void setSneaking(boolean sneak) {
        this.spongeValue.offer(Keys.IS_SNEAKING, sneak);
    }

    @Override
    public boolean isSprinting() {
        return this.spongeValue.get(Keys.IS_SPRINTING).get();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.spongeValue.offer(Keys.IS_SNEAKING, sprinting);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            return true;
        }
        ServerPlayer player = (ServerPlayer) this.spongeValue;
        return player.hasPermission(permission);
    }

    @Override
    public void saveData() {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void loadData() {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        this.spongeValue.offer(Keys.IS_SLEEPING_IGNORED, isSleeping);
    }

    @Override
    public boolean isSleepingIgnored() {
        return this.spongeValue.get(Keys.IS_SLEEPING_IGNORED).get();
    }

    @Override
    @Deprecated
    public void playNote(@NotNull Location loc, byte instrument, byte note) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void stopSound(@NotNull String sound) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void stopSound(@NotNull Sound sound, @Nullable SoundCategory category) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void stopSound(@NotNull String sound, @Nullable SoundCategory category) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    @Deprecated
    public void playEffect(@NotNull Location loc, @NotNull Effect effect, int data) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public <T> void playEffect(@NotNull Location loc, @NotNull Effect effect, @Nullable T data) {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    @Deprecated
    public void sendBlockChange(@NotNull Location loc, @NotNull Material material, byte data) {
        this.sendBlockChange(loc, material.createBlockData());
    }

    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull BlockData block) {
        this.spongeValue.sendBlockChange(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), ((BongeAbstractBlockData) block).getSpongeValue());
    }

    @Override
    public void sendBlockDamage(@NotNull Location loc, float progress) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    @Deprecated
    public boolean sendChunkChange(@NotNull Location loc, int sx, int sy, int sz, @NotNull byte[] data) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void sendSignChange(@NotNull Location loc, @Nullable String[] lines) throws IllegalArgumentException {
        this.sendSignChange(loc, lines, DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(@NotNull Location loc, @Nullable String[] lines, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void sendMap(@NotNull MapView map) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    @Deprecated
    public void updateInventory() {

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) throws IllegalArgumentException {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public long getPlayerTime() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public long getPlayerTimeOffset() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public boolean isPlayerTimeRelative() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void resetPlayerTime() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setPlayerWeather(@NotNull WeatherType type) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public @Nullable WeatherType getPlayerWeather() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void resetPlayerWeather() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void giveExp(int amount) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void giveExpLevels(int amount) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public float getExp() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setExp(float exp) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public int getLevel() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setLevel(int level) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public int getTotalExperience() {
        return this.getSpongeValue().get(Keys.EXPERIENCE).orElse(0);
    }

    @Override
    public void setTotalExperience(int exp) {
        this.getSpongeValue().offer(Keys.EXPERIENCE, exp);
    }

    @Override
    public void sendExperienceChange(float progress) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void sendExperienceChange(float progress, int level) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public float getExhaustion() {
        return this.spongeValue.get(Keys.EXHAUSTION).get().floatValue();
    }

    @Override
    public void setExhaustion(float value) {
        this.spongeValue.offer(Keys.EXHAUSTION, (double) value);
    }

    @Override
    public float getSaturation() {
        return this.spongeValue.get(Keys.SATURATION).get().floatValue();
    }

    @Override
    public void setSaturation(float value) {
        this.spongeValue.offer(Keys.SATURATION, (double) value);
    }

    @Override
    public int getFoodLevel() {
        return this.spongeValue.get(Keys.FOOD_LEVEL).get();
    }

    @Override
    public void setFoodLevel(int value) {
        this.spongeValue.offer(Keys.FOOD_LEVEL, value);
    }

    @Override
    public int getSaturatedRegenRate() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void setSaturatedRegenRate(int ticks) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public int getUnsaturatedRegenRate() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void setUnsaturatedRegenRate(int ticks) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public int getStarvationRate() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void setStarvationRate(int ticks) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public Location getBedSpawnLocation() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public boolean sleep(@NotNull Location location, boolean force) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public @NotNull Location getBedLocation() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public float getAttackCooldown() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public boolean hasDiscoveredRecipe(@NotNull NamespacedKey recipe) {
        throw new NotImplementedException("yet to look at");
    }

    @NotNull
    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public boolean dropItem(boolean dropAll) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public boolean getAllowFlight() {
        return this.spongeValue.get(Keys.CAN_FLY).get();
    }

    @Override
    public void setAllowFlight(boolean flight) {
        this.spongeValue.offer(Keys.CAN_FLY, flight);
    }

    @Override
    @Deprecated
    public void hidePlayer(@NotNull Player player) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    @Deprecated
    public void showPlayer(@NotNull Player player) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
        throw new NotImplementedException("yet to look at");
    }

    @Override
    public boolean canSee(@NotNull Player player) {
        return this.spongeValue.canSee(((BongePlayer) player).spongeValue);
    }

    @Override
    public boolean isFlying() {
        return this.spongeValue.get(Keys.IS_FLYING).get();
    }

    @Override
    public void setFlying(boolean value) {
        this.spongeValue.offer(Keys.IS_FLYING, value);
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        this.spongeValue.offer(Keys.FLYING_SPEED, (double) value);
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        this.spongeValue.offer(Keys.WALKING_SPEED, (double) value);
    }

    @Override
    public float getFlySpeed() {
        return this.spongeValue.get(Keys.FLYING_SPEED).get().floatValue();
    }

    @Override
    public float getWalkSpeed() {
        return this.spongeValue.get(Keys.WALKING_SPEED).get().floatValue();
    }

    @Override
    @Deprecated
    public void setTexturePack(@NotNull String url) {
        this.setResourcePack(url);
    }

    @Override
    public void setResourcePack(@NotNull String url) {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer) this.spongeValue;
        try {
            ResourcePack resource = ResourcePack.fromUriUnchecked(new URI(url));
            player.sendResourcePack(resource);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setResourcePack(@NotNull String url, @NotNull byte[] hash) {
        this.setResourcePack(url);
    }

    @Override
    public @NotNull Scoreboard getScoreboard() {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            throw new NotImplementedException("Sponge does not have client only scoreboard support");
        }
        return new BongeScoreboard(((ServerPlayer) this.spongeValue).scoreboard());
    }

    @Override
    public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            throw new NotImplementedException("Sponge does not have client only scoreboard support");
        }
        ((ServerPlayer) this.spongeValue).setScoreboard(((BongeScoreboard) scoreboard).getSpongeValue());
    }

    @Override
    public boolean isHealthScaled() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setHealthScaled(boolean scale) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {
        this.spongeValue.offer(Keys.HEALTH_SCALE, scale);
    }

    @Override
    public double getHealthScale() {
        return this.spongeValue.get(Keys.HEALTH_SCALE).get();
    }

    @Override
    public @Nullable Entity getSpectatorTarget() {
        Optional<org.spongepowered.api.entity.Entity> opTarget = this.spongeValue.get(Keys.SPECTATOR_TARGET);
        if (!opTarget.isPresent()) {
            return null;
        }
        try {
            return Bonge.getInstance().convert(Entity.class, opTarget.get());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setSpectatorTarget(@Nullable Entity entity) {
        assert entity != null;
        this.spongeValue.offer(Keys.SPECTATOR_TARGET, ((BongeAbstractEntity<?>) entity).getSpongeValue());
    }

    @Override
    @Deprecated
    public void sendTitle(@Nullable String title, @Nullable String subtitle) {
        sendTitle(title, subtitle, -1, -1, -1);
    }

    @Override
    public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        Title sTitle = Title.title(
                Bonge.getInstance().convertText(title),
                Bonge.getInstance().convertText(subtitle),
                Title.Times.of(
                        Ticks.of(fadeIn).expectedDuration(Sponge.server()),
                        Ticks.of(stay).expectedDuration(Sponge.server()),
                        Ticks.of(fadeOut).expectedDuration(Sponge.server())));
        this.spongeValue.showTitle(sTitle);
    }

    @Override
    public void resetTitle() {
        this.spongeValue.resetTitle();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public @NotNull AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public int getClientViewDistance() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public int getPing() {
        throw new NotImplementedException("not got to yet");
    }

    @Override
    public @NotNull String getLocale() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void updateCommands() {

    }

    @Override
    public void openBook(@NotNull ItemStack book) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public boolean isOnline() {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            return true;
        }
        ServerPlayer player = (ServerPlayer) this.spongeValue;
        return player.user().isOnline();
    }

    @Override
    public boolean isBanned() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public boolean isWhitelisted() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void setWhitelisted(boolean value) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public @Nullable Player getPlayer() {
        return this;
    }

    @Override
    public long getFirstPlayed() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public long getLastPlayed() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public boolean hasPlayedBefore() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        throw new NotImplementedException("yet to look at");

    }

    @Override
    public @NotNull BongeInventoryView getOpenInventory() {
        return this.view;
    }

    @Override
    public BongeInventoryView openInventory(@NotNull Inventory inventory) {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            throw new NotImplementedException("Client mode doesn't support opening inventories");
        }
        if (!(inventory instanceof BongeInventory)) {
            throw new NotImplementedException("Custom inventories must implement BongeInventory");
        }
        ServerPlayer player = (ServerPlayer) this.spongeValue;
        BongeInventory<? extends org.spongepowered.api.item.inventory.Inventory> inv = (BongeInventory<? extends org.spongepowered.api.item.inventory.Inventory>) inventory;
        if (this.view != null) {
            player.closeInventory();
        }
        Optional<Container> opContainer = player.openInventory(inv.getSpongeValue());
        if (opContainer.isPresent()) {
            this.view = new BongeInventoryView(this, inv, opContainer.get());
        }

        return view;
    }

    @Override
    public @NotNull String getName() {
        return IHuman.super.getName();
    }

    @Override
    public void openInventory(@NotNull InventoryView inventory) {
        if (!(inventory instanceof BongeInventoryView)) {
            throw new IllegalStateException("All InventoryView's must be BongeInventoryViews");
        }
        if (!(this.spongeValue instanceof ServerPlayer)) {
            throw new IllegalStateException("opening inventories is not supported on the client");
        }
        ServerPlayer player = (ServerPlayer) this.spongeValue;
        BongeInventoryView view = (BongeInventoryView) inventory;
        Inventory top = view.getTopInventory();
        if (!(top instanceof BongeInventory)) {
            throw new IllegalStateException("All inventories must implement BongeInventory");
        }
        org.spongepowered.api.item.inventory.Inventory inv = ((BongeInventory<? extends org.spongepowered.api.item.inventory.Inventory>) top).getSpongeValue();
        player.openInventory(inv);
        this.view = view;
    }

    /**
     * This is for cases whereby the Inventory has opened by another means
     * (the player is already seeing the inventory) however the InventoryView
     * has not been applied. This allows the settings without opening the
     * InventoryView. This can be troublesome if not applied correctly
     *
     * @param view The new View
     */
    public void setInventoryView(BongeInventoryView view) {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            throw new IllegalStateException("opening inventories is not supported on the client");
        }
        this.view = view;
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        throw new NotImplementedException("Player.openWorkbench(Location, boolean) redoing Inventory link");
    }

    @Override
    public BongeInventoryView openEnchanting(Location location, boolean force) {
        throw new NotImplementedException("Player.openEnchanting(Location, boolean) redoing Inventory link");
    }

    @Override
    public BongeInventoryView openMerchant(@NotNull Villager trader, boolean force) {
        throw new NotImplementedException("Player.openMerchant(Villager, boolean) redoing Inventory link");
    }

    @Override
    public BongeInventoryView openMerchant(@NotNull Merchant merchant, boolean force) {
        throw new NotImplementedException("Player.openMerchant(Merchant, boolean) redoing Inventory link");
    }

    @Override
    public void closeInventory() {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            return;
        }
        ((ServerPlayer) this.spongeValue).closeInventory();
    }

    @Override
    @Deprecated
    public boolean isOp() {
        return this.hasPermission(Permissions.BONGE_OP);
    }

    @Override
    @Deprecated
    public void setOp(boolean value) {
        if (!(this.spongeValue instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer) this.spongeValue;
        player.subjectData().setPermission(SubjectData.GLOBAL_CONTEXT, Permissions.BONGE_OP, value ? Tristate.TRUE : Tristate.FALSE);
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return this.permissionsOverride.stream().anyMatch(a -> {
            Boolean value = a.getPermissions().get(s);
            if (value == null) {
                return false;
            }
            return value;
        });
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return this.isPermissionSet(permission.getName());
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, String s, boolean b) {
        PermissionAttachment attachment = new PermissionAttachment(plugin, this);
        attachment.setPermission(s, b);
        this.permissionsOverride.add(attachment);
        return attachment;
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        PermissionAttachment attachment = new PermissionAttachment(plugin, this);
        this.permissionsOverride.add(attachment);
        return attachment;
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        PermissionAttachment attachment = this.addAttachment(plugin, s, b);
        Task.builder().execute(() -> this.permissionsOverride.remove(attachment)).delay(Ticks.of(i)).build();
        return attachment;
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        PermissionAttachment attachment = this.addAttachment(plugin);
        Task.builder().execute(() -> this.permissionsOverride.remove(attachment)).delay(Ticks.of(i)).build();
        return attachment;
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        this.permissionsOverride.remove(permissionAttachment);
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        Set<PermissionAttachmentInfo> set = new HashSet<>();
        this.permissionsOverride.forEach(p -> {
            p.getPermissions().forEach((key, value) -> set.add(new PermissionAttachmentInfo(BongePlayer.this, key, p, value)));
        });
        return set;
    }

    private static void updatePlayerList() {
        PLAYERS = PLAYERS.stream().filter(p -> Sponge.server().player(p.getUniqueId()).isPresent()).collect(Collectors.toSet());
    }

    public static BongePlayer getPlayer(org.spongepowered.api.entity.living.player.Player player) {
        Optional<BongePlayer> opPlayer = PLAYERS.stream().filter(p -> p.getSpongeValue().equals(player)).findAny();
        if (opPlayer.isPresent()) {
            return opPlayer.get();
        }
        BongePlayer player2 = new BongePlayer(player);
        PLAYERS.add(player2);
        return player2;
    }

    @Override
    public @NotNull EntityType getType() {
        return EntityType.PLAYER;
    }
}
