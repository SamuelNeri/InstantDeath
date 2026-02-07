package br.com.nerisoftware.www;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            handleDeath(player);
        }
    }

    private void handleDeath(Player player) {
        if (!Boolean.TRUE.equals(player.getWorld().getGameRuleValue(org.bukkit.GameRule.KEEP_INVENTORY))) {
            for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() != org.bukkit.Material.AIR) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
            player.getInventory().clear();

            int xpToDrop = Math.min(player.getLevel() * 7, 100);
            if (xpToDrop > 0) {
                player.getWorld().spawn(player.getLocation(), org.bukkit.entity.ExperienceOrb.class)
                        .setExperience(xpToDrop);
            }
            player.setLevel(0);
            player.setExp(0);
        }

        Location spawnLocation = player.getRespawnLocation();
        if (spawnLocation == null) {
            spawnLocation = player.getWorld().getSpawnLocation();
        }

        player.setHealth(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.teleport(spawnLocation);
    }
}
