package br.com.nerisoftware.www;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DeathListener implements Listener {

    private static final int MAX_XP_DROP = 100;
    private static final int XP_MULTIPLIER = 7;
    private static final int DEFAULT_FOOD_LEVEL = 20;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player
                && player.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            handleDeath(player);
        }
    }

    private void handleDeath(Player player) {
        World world = player.getWorld();

        if (!world.getGameRuleValue(GameRule.KEEP_INVENTORY)) {
            dropInventory(player, world);
            dropExperience(player, world);
        }

        resetPlayerState(player, world);
    }

    private void dropInventory(Player player, World world) {
        Location location = player.getLocation();
        PlayerInventory inventory = player.getInventory();

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                world.dropItemNaturally(location, item);
            }
        }
        inventory.clear();
    }

    private void dropExperience(Player player, World world) {
        int xpToDrop = Math.min(player.getLevel() * XP_MULTIPLIER, MAX_XP_DROP);
        if (xpToDrop > 0) {
            world.spawn(player.getLocation(), ExperienceOrb.class,
                    orb -> orb.setExperience(xpToDrop));
        }
        player.setLevel(0);
        player.setExp(0);
    }

    private void resetPlayerState(Player player, World world) {
        Location spawn = player.getRespawnLocation();
        if (spawn == null) {
            spawn = world.getSpawnLocation();
        }

        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(DEFAULT_FOOD_LEVEL);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.teleport(spawn);

        spawnRespawnEffects(player, spawn);
    }

    private static final double CYLINDER_RADIUS = 1.2;
    private static final double CYLINDER_HEIGHT = 2.0;
    private static final int CYLINDER_POINTS = 20;
    private static final int CYLINDER_LAYERS = 8;

    private void spawnRespawnEffects(Player player, Location location) {
        World world = location.getWorld();
        Location base = location.clone();

        spawnCylinder(world, base);
        world.spawnParticle(Particle.END_ROD, base.add(0, 1, 0), 15, 0.2, 0.5, 0.2, 0.02);
        player.playSound(base, Sound.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 1.0f, 1.2f);
    }

    private void spawnCylinder(World world, Location base) {
        double layerStep = CYLINDER_HEIGHT / CYLINDER_LAYERS;
        double angleStep = 2 * Math.PI / CYLINDER_POINTS;

        for (int layer = 0; layer < CYLINDER_LAYERS; layer++) {
            double y = layerStep * layer;
            for (int i = 0; i < CYLINDER_POINTS; i++) {
                double angle = angleStep * i;
                world.spawnParticle(Particle.TOTEM_OF_UNDYING,
                        base.getX() + CYLINDER_RADIUS * Math.cos(angle),
                        base.getY() + y,
                        base.getZ() + CYLINDER_RADIUS * Math.sin(angle),
                        1, 0, 0, 0, 0);
            }
        }
    }
}
