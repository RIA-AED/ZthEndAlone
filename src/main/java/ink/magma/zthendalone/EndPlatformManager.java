package ink.magma.zthendalone;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class EndPlatformManager {

    static void generatePlatform(@NotNull World endWorld) {
        Location obsidianCorner1 = new Location(endWorld, 102, 48, 2);
        Location obsidianCorner2 = new Location(endWorld, 98, 48, -2);

        Location airCorner1 = new Location(endWorld, 102, 49, 2);
        Location airCorner2 = new Location(endWorld, 98, 51, -2);

        // 填充黑曜石
        for (int x = obsidianCorner1.getBlockX(); x >= obsidianCorner2.getBlockX(); x--) {
            for (int y = obsidianCorner1.getBlockY(); y <= obsidianCorner2.getBlockY(); y++) {
                for (int z = obsidianCorner1.getBlockZ(); z >= obsidianCorner2.getBlockZ(); z--) {
//                    System.out.println("黑曜石: " + x + " " + y + " " + z);
                    endWorld.getBlockAt(x, y, z).setType(Material.OBSIDIAN);
                }
            }
        }

        // 填充空气
        for (int x = airCorner1.getBlockX(); x >= airCorner2.getBlockX(); x--) {
            for (int y = airCorner1.getBlockY(); y <= airCorner2.getBlockY(); y++) {
                for (int z = airCorner1.getBlockZ(); z >= airCorner2.getBlockZ(); z--) {
//                    System.out.println("Air: " + x + " " + y + " " + z);
                    endWorld.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

    static Location getSpawnLocation(@NotNull World endWorld) {
        return new Location(endWorld, 100, 49, 0);
    }

    /**
     * 获取非玩家实体返回末地的位置.
     * 这是末地中央返回传送门基岩柱子的顶部.
     *
     * @param endWorld 末地世界
     * @return a safe location on top of the bedrock pillar.
     */
    static Location getNonPlayerReturnLocation(@NotNull World endWorld) {
        // 坐标 (0,0) 是末地主岛的中心
        // getHighestBlockAt 会返回该坐标上最高的非空气方块
        // +1 确保实体生成在方块之上而不是之内
        int y = endWorld.getHighestBlockYAt(0, 0) + 1;
        return new Location(endWorld, 0.5, y, 0.5); // 使用 0.5, 0.5 确保实体在方块中央
    }
}
