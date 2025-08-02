package ink.magma.zthendalone;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import javax.annotation.Nullable;

public final class ZthEndAlone extends JavaPlugin implements Listener {
    @Nullable
    World endWorld;
    @Nullable
    World mainWorld;

    @Override
    public void onEnable() {
        // Plugin startup logic
        endWorld = getServer().getWorld("world_the_end");
        mainWorld = getServer().getWorld("world");

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

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (endWorld == null || mainWorld == null) return;
        if (event.getTo() == null) return;

        // 检查实体是否是玩家
        if (event.getEntity() instanceof Player) {
            return; // 如果是玩家，则不执行任何操作
        }

        // 检查是否是从末地传送到主世界
        boolean fromEnd = event.getFrom().getWorld().equals(endWorld);
        boolean toMain = event.getTo().getWorld().equals(mainWorld);

        if (fromEnd && toMain) {
            // 直接修改传送事件的目标地点，而不是取消后重新传送
            // 这是更稳定、更推荐的做法
            event.setTo(EndPlatformManager.getNonPlayerReturnLocation(endWorld));
        }
    }
}