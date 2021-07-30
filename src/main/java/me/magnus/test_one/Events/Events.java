package me.magnus.test_one.Events;

import me.magnus.test_one.Methods.Methods;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class Events implements Listener {
    Methods m = new Methods();

    @EventHandler
    public void block_break(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if(!p.getGameMode().equals(GameMode.CREATIVE) && m.is_log(block) && m.is_proper_tree(block)) {
            ConcurrentHashMap<Location, Block> blocks_to_break = new ConcurrentHashMap<>();
            ConcurrentHashMap<Location, Block> prevent_stack_overflow = new ConcurrentHashMap<>();
            m.chopTree(blocks_to_break, block, prevent_stack_overflow);
            Collection<Block> to_break = blocks_to_break.values();
            for(Block b : to_break) {
                b.breakNaturally();
            }
        }
    }
}
