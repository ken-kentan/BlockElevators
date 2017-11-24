package jp.kentan.minecraft.blockelevators.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerEventListener implements Listener {

    private final ElevatorEventListener LISTENER;
    private static Material sElevatorMaterial;

    public PlayerEventListener(ElevatorEventListener listener){
        LISTENER = listener;
    }

    public static void setElevatorMaterial(Material material){
        sElevatorMaterial = material;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();

        if(player.isSneaking() || !player.isOnGround()) return;

        final Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if(block.getType() == sElevatorMaterial && player.hasPermission("elevator.use")) {
            LISTENER.onDownElevator(player, block);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if(player.getVelocity().getY() <= 0D) return;

        final Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if(block.getType() == sElevatorMaterial && player.hasPermission("elevator.use")) {
            LISTENER.onUpElevator(player, block);
        }
    }
}
