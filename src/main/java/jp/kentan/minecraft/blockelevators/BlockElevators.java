package jp.kentan.minecraft.blockelevators;

import jp.kentan.minecraft.blockelevators.config.ConfigManager;
import jp.kentan.minecraft.blockelevators.config.ConfigUpdateListener;
import jp.kentan.minecraft.blockelevators.listener.ElevatorEventListener;
import jp.kentan.minecraft.blockelevators.listener.PlayerEventListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockElevators extends JavaPlugin implements ElevatorEventListener, ConfigUpdateListener {

    private Elevator mElevator;

    @Override
    public void onEnable() {
        ConfigManager.setup(this);

        if(!ConfigManager.load()){
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length > 0 && args[0].equals("reload")){
            boolean result = ConfigManager.load();

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', result ? "&aConfig updated!" : "&c Failed to update a config."));
            return true;
        }

        return false;
    }

    @Override
    public void onDownElevator(Player player, Block block) {
        block = block.getRelative(BlockFace.DOWN, mElevator.MIN_ELEVATION);

        int maxElevation = mElevator.MAX_ELEVATION;

        while (maxElevation > 0 && !isElevatorBlock(block)) {
            --maxElevation;
            block = block.getRelative(BlockFace.DOWN);
        }

        if(maxElevation > 0){
            final Location loc = player.getLocation();
            loc.setY(loc.getY() - (mElevator.MAX_ELEVATION - maxElevation + mElevator.MIN_ELEVATION));

            player.teleport(loc);
            player.getWorld().playSound(loc, mElevator.SOUND, 1f, 0f);
        }
    }

    @Override
    public void onUpElevator(Player player, Block block) {
        block = block.getRelative(BlockFace.UP, mElevator.MIN_ELEVATION);

        int maxElevation = mElevator.MAX_ELEVATION;

        while (maxElevation > 0 && !isElevatorBlock(block)) {
            --maxElevation;
            block = block.getRelative(BlockFace.UP);
        }

        if(maxElevation > 0){
            final Location loc = player.getLocation();
            loc.setY(loc.getY() + (mElevator.MAX_ELEVATION - maxElevation + mElevator.MIN_ELEVATION));

            player.teleport(loc);
            player.getWorld().playSound(loc, mElevator.SOUND, 1f, 0f);
        }
    }

    private boolean isElevatorBlock(Block b){
        return b.getType() == mElevator.MATERIAL && b.getRelative(BlockFace.UP).getType().isTransparent() && b.getRelative(BlockFace.UP, 2).getType().isTransparent();
    }

    @Override
    public void onUpdate(Elevator elevator) {
        mElevator = elevator;
        PlayerEventListener.setElevatorMaterial(elevator.MATERIAL);
    }

    public static class Elevator{

        private final int MIN_ELEVATION, MAX_ELEVATION;
        private final Material MATERIAL;
        private final Sound SOUND;

        public Elevator(int minElevation, int maxElevation, Material material, Sound sound){
            MIN_ELEVATION = minElevation;
            MAX_ELEVATION = maxElevation;
            MATERIAL = material;
            SOUND = sound;
        }
    }
}
