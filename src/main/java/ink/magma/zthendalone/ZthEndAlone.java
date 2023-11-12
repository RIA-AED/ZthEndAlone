package ink.magma.zthendalone;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import javax.annotation.Nullable;

public final class ZthEndAlone extends JavaPlugin implements Listener {
    @Nullable
    World endWorld;

    @Override
    public void onEnable() {
        // Plugin startup logic
        endWorld = getServer().getWorld("world_the_end");

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (endWorld == null) return;
        EndPlatformManager.generatePlatform(endWorld);
        event.setSpawnLocation(EndPlatformManager.getSpawnLocation(endWorld));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (endWorld == null) return;
        if (event.getRespawnReason() == PlayerRespawnEvent.RespawnReason.END_PORTAL) {
            event.getPlayer().kick(Component.text("已离开 \"第三终点站\""));
        }

        EndPlatformManager.generatePlatform(endWorld);
        event.setRespawnLocation(EndPlatformManager.getSpawnLocation(endWorld));
    }


    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (endWorld == null) return;
        // 如果玩家传送的目标世界不是末地
        if (!endWorld.equals(event.getTo().getWorld())) {
            event.setCancelled(true);
        }
        // 顺便检测一下玩家是否在末地
        if (!event.getPlayer().getWorld().equals(endWorld)) {
            EndPlatformManager.generatePlatform(endWorld);
            event.getPlayer().teleport(EndPlatformManager.getSpawnLocation(endWorld));
        }
    }
}