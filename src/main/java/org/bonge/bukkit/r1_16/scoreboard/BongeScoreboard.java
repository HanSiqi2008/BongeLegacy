package org.bonge.bukkit.r1_16.scoreboard;

import org.bonge.Bonge;
import org.bonge.util.exception.NotImplementedException;
import org.bonge.wrapper.BongeWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.scoreboard.criteria.Criterion;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BongeScoreboard extends BongeWrapper<org.spongepowered.api.scoreboard.Scoreboard> implements Scoreboard {

    public BongeScoreboard(org.spongepowered.api.scoreboard.Scoreboard value) {
        super(value);
    }

    @Override
    public @NotNull Objective registerNewObjective(@NotNull String name, @NotNull String criteria) throws IllegalArgumentException {
        return registerNewObjective(name, criteria, name);
    }

    @Override
    public @NotNull Objective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName) throws IllegalArgumentException {
        Criterion criterion;
        try {
            criterion = Bonge.getInstance().convert(criteria, Criterion.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        org.spongepowered.api.scoreboard.objective.Objective objective = org.spongepowered.api.scoreboard.objective.Objective
                .builder()
                .displayName(Bonge.getInstance().convertText(displayName))
                .name(name)
                .criterion(criterion)
                .build();
        this.spongeValue.addObjective(objective);
        return new BongeObjective(objective, this);
    }

    @Override
    public @NotNull Objective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName, @NotNull RenderType renderType) throws IllegalArgumentException {
        throw new NotImplementedException("Scoreboard.registerNewObjective(String, String, String, RenderType) not looked at yet");
    }

    @Override
    public Objective getObjective(@NotNull String name) throws IllegalArgumentException {
        Optional<org.spongepowered.api.scoreboard.objective.Objective> opObj = this.spongeValue.objective(name);
        return opObj.map(objective -> new BongeObjective(objective, this)).orElse(null);
    }

    @Override
    public @NotNull Set<Objective> getObjectivesByCriteria(@NotNull String criteria) throws IllegalArgumentException {
        Set<org.spongepowered.api.scoreboard.objective.Objective> objectives;
        try {
            objectives = this.spongeValue.objectivesByCriterion(Bonge.getInstance().convert(criteria, Criterion.class));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return objectives.stream().map(o -> new BongeObjective(o, BongeScoreboard.this)).collect(Collectors.toSet());
    }

    @Override
    public @NotNull Set<Objective> getObjectives() {
        return this.spongeValue.objectives().stream().map(o -> new BongeObjective(o, this)).collect(Collectors.toSet());
    }

    @Override
    public Objective getObjective(@NotNull DisplaySlot slot) throws IllegalArgumentException {
        org.spongepowered.api.scoreboard.displayslot.DisplaySlot slot1;
        try {
            slot1 = Bonge.getInstance().convert(slot, org.spongepowered.api.scoreboard.displayslot.DisplaySlot.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        Optional<org.spongepowered.api.scoreboard.objective.Objective> opSlot = this.spongeValue.objective(slot1);
        return opSlot.map(objective -> new BongeObjective(objective, this)).orElse(null);
    }

    @Override
    public @NotNull Set<Score> getScores(OfflinePlayer player) throws IllegalArgumentException {
        return this.getScores(player.getName());
    }

    @Override
    public @NotNull Set<Score> getScores(@NotNull String entry) throws IllegalArgumentException {
        return this.spongeValue.objectives().stream().flatMap(objective -> {
            BongeObjective bongeObjective = new BongeObjective(objective, BongeScoreboard.this);
            return objective.scores().values().stream().filter(score -> Bonge.getInstance().convert(score.name()).equals(entry)).map(score -> new BongeScore(score, bongeObjective));
        }).collect(Collectors.toSet());
    }

    @Override
    public void resetScores(@NotNull OfflinePlayer player) throws IllegalArgumentException {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public void resetScores(@NotNull String entry) throws IllegalArgumentException {
        throw new NotImplementedException("Not got to yet");

    }

    @Override
    public Team getPlayerTeam(@NotNull OfflinePlayer player) throws IllegalArgumentException {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public Team getEntryTeam(@NotNull String entry) throws IllegalArgumentException {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public Team getTeam(@NotNull String teamName) throws IllegalArgumentException {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public @NotNull Set<Team> getTeams() {
        throw new NotImplementedException("Not got to yet");

    }

    @Override
    public @NotNull Team registerNewTeam(@NotNull String name) throws IllegalArgumentException {
        throw new NotImplementedException("Not got to yet");
    }

    @Override
    public @NotNull Set<OfflinePlayer> getPlayers() {
        return Bukkit.getOnlinePlayers().stream().filter(p -> p.getScoreboard().equals(this)).collect(Collectors.toSet());
    }

    @Override
    public @NotNull Set<String> getEntries() {
        return this.spongeValue.objectives().stream().flatMap(objective -> objective.scores().values().stream().map(score -> Bonge.getInstance().convert(score.name()))).collect(Collectors.toSet());
    }

    @Override
    public void clearSlot(@NotNull DisplaySlot slot) throws IllegalArgumentException {
        try {
            this.spongeValue.clearSlot(Bonge.getInstance().convert(slot, org.spongepowered.api.scoreboard.displayslot.DisplaySlot.class));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
