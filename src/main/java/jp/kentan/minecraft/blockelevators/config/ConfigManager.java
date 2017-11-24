package jp.kentan.minecraft.blockelevators.config;

import jp.kentan.minecraft.blockelevators.BlockElevators;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class ConfigManager {

    private static File sConfigFile;
    private static ConfigUpdateListener sListener;

    public static void setup(BlockElevators plugin){
        sConfigFile = new File(plugin.getDataFolder(), "config.yml");
        sListener = plugin;

        createDefaultConfigIfNeed(plugin);
    }

    private static void createDefaultConfigIfNeed(Plugin plugin){
        if(!sConfigFile.exists()){
            plugin.getDataFolder().mkdir();

            plugin.saveResource("config.yml", false);
            plugin.getLogger().info("Created a default config.");
        }
    }

    public static boolean load(){
        try (Reader reader = new InputStreamReader(new FileInputStream(sConfigFile), StandardCharsets.UTF_8)) {
            final FileConfiguration config = new YamlConfiguration();

            config.load(reader);

            final int minElevation, maxElevation;
            final Material material;
            final Sound sound;

            minElevation = config.getInt("minElevation");
            maxElevation = config.getInt("maxElevation");

            material = Material.valueOf(config.getString("elevatorMaterial", ""));
            sound = Sound.valueOf(config.getString("elevatorSound", ""));

            reader.close();

            sListener.onUpdate(new BlockElevators.Elevator(
                    minElevation,
                    maxElevation,
                    material,
                    sound
            ));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
