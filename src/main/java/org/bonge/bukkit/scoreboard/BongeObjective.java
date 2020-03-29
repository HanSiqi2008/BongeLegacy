package org.bonge.bukkit.scoreboard;

import org.bonge.convert.InterfaceConvert;
import org.bonge.wrapper.BongeWrapper;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class BongeObjective extends BongeWrapper<org.spongepowered.api.scoreboard.objective.Objective> implements Objective {

    private BongeScoreboard scoreboard;

    public BongeObjective(org.spongepowered.api.scoreboard.objective.Objective value, BongeScoreboard board) {
        super(value);
        this.scoreboard = board;
    }

    @Override
    public String getName() throws IllegalStateException {
        return this.spongeValue.getName();
    }

    @Override
    public String getDisplayName() throws IllegalStateException {
        return InterfaceConvert.toString(this.spongeValue.getDisplayName());
    }

    @Override
    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
        this.spongeValue.setDisplayName(InterfaceConvert.fromString(displayName));
    }

    @Override
    public String getCriteria() throws IllegalStateException {
        return InterfaceConvert.getCriteria(this.spongeValue.getCriterion());
    }

    @Override
    public boolean isModifiable() throws IllegalStateException {
        return true;
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public void unregister() throws IllegalStateException {
        this.scoreboard.getSpongeValue().removeObjective(this.spongeValue);
    }

    @Override
    public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
    }

    @Override
    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        return null;
    }

    @Override
    public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        return this.getScore(player.getName());
    }

    @Override
    public Score getScore(String entry) throws IllegalArgumentException, IllegalStateException {
        org.spongepowered.api.scoreboard.Score score = this.spongeValue.getOrCreateScore(InterfaceConvert.fromString(entry));
        return new BongeScore(score, this);

    }
}
