package me.alek.security.operator;

import me.alek.AntiMalwarePlugin;
import me.alek.utils.AsynchronousTask;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OperatorManager {

    private static OperatorManager instance;

    public static synchronized OperatorManager get() {
        if (instance == null) {
            instance = new OperatorManager();
        }
        return instance;
    }

    private final ArrayList<String> allowedOpPlayers = new ArrayList<>();
    private final ArrayList<OpPlayerChange> opPlayerChanges = new ArrayList<>();

    private OperatorManager() {
        new GarbageRemoverTask().runAsync();
    }

    public void setAllowedOpPlayers(List<String> players) {
        this.allowedOpPlayers.addAll(players);
    }

    public boolean isPlayerAllowed(OfflinePlayer player) {
        boolean isOpProxyEnabled = AntiMalwarePlugin.getSecurityManager().getOptions().isOpProxyBlockerEnabled();

        if (!isOpProxyEnabled) return true;
        return this.allowedOpPlayers.contains(player.getName()) || this.allowedOpPlayers.contains(player.getUniqueId().toString());
    }

    public void put(OfflinePlayer player, boolean isOp) {
        new ArrayList<>(opPlayerChanges)
                .stream()
                .filter(change -> change.getPlayer().getName().equals(player.getName()))
                .forEach(opPlayerChanges::remove);
        Instant instant = Instant.now();

        opPlayerChanges.add(new OpPlayerChange() {
            @Override
            public OfflinePlayer getPlayer() {
                return player;
            }

            @Override
            public Instant getInstant() {
                return instant;
            }

            @Override
            public boolean isOp() {
                return isOp;
            }
        });
    }

    public List<OpPlayerChange> getLatestOpChanges(Duration duration) {
        final Instant now = Instant.now();
        final List<OpPlayerChange> latestOpChanges = new ArrayList<>();
        opPlayerChanges
                .stream()
                .filter(change -> isNewerThan(change.getInstant(), now, duration))
                .forEach(latestOpChanges::add);
        return latestOpChanges;
    }

    private boolean isOlderThan(Instant instant1, Instant instant2, Duration duration) {
        return Duration.between(instant1, instant2).compareTo(duration) > 0;
    }

    private boolean isNewerThan(Instant instant1, Instant instant2, Duration duration) {
        return Duration.between(instant1, instant2).compareTo(duration) < 0;
    }

    public void removeOld() {
        final Instant now = Instant.now();
        opPlayerChanges
                .stream()
                .filter(change -> isOlderThan(change.getInstant(), now, Duration.ofMinutes(30)))
                .forEach(opPlayerChanges::remove);
    }

    public interface OpPlayerChange {

        OfflinePlayer getPlayer();

        Instant getInstant();

        boolean isOp();
    }

    private class GarbageRemoverTask extends AsynchronousTask {

        @Override
        public BukkitRunnable getRunnable() {
            return new BukkitRunnable() {

                @Override
                public void run() {
                    removeOld();
                }
            };
        }

        @Override
        public long getPeriod() {
            return 18000L;
        }
    }

}
