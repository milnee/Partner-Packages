package org.millen.partnerPackages.crate;

import org.millen.partnerPackages.PackagePlugin;
import org.millen.partnerPackages.util.ItemUtils;
import lombok.Data;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Data
public class Package {

    @Getter
    private static List<Package> packages = new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();
    private List<String> commands = new ArrayList<>();

    private String name, displayName;

    public Package(String name) {
        this.name = name;
    }

    public static void loadPackages() {
        ConfigurationSection section = PackagePlugin.getInstance().getConfig().getConfigurationSection("packages");

        section.getKeys(false).forEach(key -> {
            Package p = new Package(key);
            PackagePlugin.getInstance().getConfig().getStringList("packages." + key + ".items").forEach(str -> p.getItems().add(ItemUtils.deserialize(str)));
            PackagePlugin.getInstance().getConfig().getStringList("packages." + key + ".commands").forEach(str -> p.getCommands().add(str));
            packages.add(p);
        });
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public void setItems(List<ItemStack> items){
        this.items = items;
    }
    public void setCommands(List<String> commands){
        this.commands = commands;
    }

    public void setName(String name){
        this.name = name;
    }


    public static List<Package> getPackages(){
        return packages;
    }

    public String getName(String name){
        return this.name;
    }

    public String getDisplayName(String displayName){
        return this.displayName;
    }

    public List<ItemStack> getItems(){
        return this.items;
    }

    public List<String> getCommands(){
        return this.commands;
    }


}
