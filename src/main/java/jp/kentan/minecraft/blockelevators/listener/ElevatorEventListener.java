package jp.kentan.minecraft.blockelevators.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface ElevatorEventListener {
    void onDownElevator(Player player, Block block);
    void onUpElevator(Player player, Block block);
}
