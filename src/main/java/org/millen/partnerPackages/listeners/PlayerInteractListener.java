package org.millen.partnerPackages.listeners;

import org.millen.partnerPackages.PackagePlugin;
import org.millen.partnerPackages.crate.Package;
import org.millen.partnerPackages.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    PackagePlugin plugin = PackagePlugin.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        // Check if plugin is enabled
        if (!plugin.getConfig().getBoolean("general.enabled", true)) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            ItemStack hand = e.getItem();

            if (hand == null)
                return;

            if (!hand.isSimilar(plugin.packageItem(hand.getAmount())))
                return;

            Player player = e.getPlayer();
            
            // Broadcast functionality
            if (plugin.getConfig().getBoolean("general.enable-broadcast", false)) {
                List<String> broadcastMessages = plugin.getConfig().getStringList("general.broadcast");
                broadcastMessages.forEach(str -> 
                    Bukkit.broadcastMessage(Color.translate(str.replaceAll("%player%", player.getName())))
                );
            }

            if (player.getItemInHand().getAmount() == 1) {
                player.setItemInHand(null);
            } else {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }
            Package p = plugin.getRandom();

            int minItems = plugin.getConfig().getInt("general.min-items-per-package", 1);
            int maxItems = plugin.getConfig().getInt("general.max-items-per-package", 5);
            
            // Shuffle items to randomize selection
            List<ItemStack> shuffledItems = new ArrayList<>(p.getItems());
            Collections.shuffle(shuffledItems);
            
            // Determine number of items to give
            int itemCount = new Random().nextInt(maxItems - minItems + 1) + minItems;
            itemCount = Math.min(itemCount, shuffledItems.size());
            
            // Give selected items
            for (int i = 0; i < itemCount; i++) {
                ItemStack itemStack = shuffledItems.get(i);
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(itemStack);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }

            p.getCommands().forEach(str -> {
                String command = str.replaceAll("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            });

            player.updateInventory();

            e.setCancelled(true);
        }
    }

}
