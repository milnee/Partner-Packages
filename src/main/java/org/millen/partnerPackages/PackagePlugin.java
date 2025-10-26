package org.millen.partnerPackages;

import org.millen.partnerPackages.util.ItemBuilder;
import org.millen.partnerPackages.commands.Command;
import org.millen.partnerPackages.crate.Package;
import org.millen.partnerPackages.listeners.PlayerInteractListener;
import org.millen.partnerPackages.util.Color;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

@Getter
public class PackagePlugin extends JavaPlugin {

    @Getter
    private static PackagePlugin instance;

    public static PackagePlugin getInstance(){
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        createConfig();
        getCommand("package").setExecutor(new Command());
        Bukkit.getConsoleSender().sendMessage(Color.translate("&f[&2&lPartnerPackages&f] Successfully loaded partner packages.."));
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Package.loadPackages();
    }

    public ItemStack packageItem(int amt) {
        return new ItemBuilder(Material.valueOf(getConfig().getString("general.material")), Color.translate(getConfig().getString("general.name")), amt, Color.translate("&fRight click to open a partner package")).getItem();
    }

    public Package getRandom() {
        if (Package.getPackages().size() == 1)
            return Package.getPackages().get(0);
        return Package.getPackages().get(new Random().nextInt(Package.getPackages().size()));
    }

    private void createConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            getLogger().info("Config.yml not found, creating!");
            saveDefaultConfig();
        } else {
            getLogger().info("Config.yml found, loading!");
        }

    }

 //   public static PackagePlugin getInstance(){
 //       return instance;
 //   }


}
