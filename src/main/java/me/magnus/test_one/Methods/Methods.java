package me.magnus.test_one.Methods;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class Methods {

    private final Material[] logs = {Material.ACACIA_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG,
            Material.CRIMSON_STEM, Material.WARPED_STEM, Material.MUSHROOM_STEM, Material.STRIPPED_CRIMSON_STEM,
            Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG,
            Material.STRIPPED_WARPED_STEM, Material.JUNGLE_LOG};
    private final Material[] leaves = {Material.ACACIA_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES,
            Material.DARK_OAK_LEAVES, Material.WARPED_WART_BLOCK, Material.SHROOMLIGHT, Material.AZALEA_LEAVES,
            Material.RED_MUSHROOM_BLOCK, Material.BROWN_MUSHROOM_BLOCK, Material.JUNGLE_LEAVES};
    private final BlockFace[] faces = {BlockFace.UP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH,
            BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST};


    private void add_leaf_blocks(ConcurrentHashMap<Location, Block> blocks, Block current, Location origin, Material origin_leaf,
                                 ConcurrentHashMap<Location, Block> removed_blocks) {
        ArrayList<Block> added = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (current.getRelative(x, y, z).getType() == origin_leaf && !blocks.containsValue(current.getRelative(x, y, z)) && !removed_blocks.containsValue(current.getRelative(x, y ,z))) {
                        blocks.put(current.getRelative(x, y, z).getLocation(), current.getRelative(x, y, z));
                        added.add(current.getRelative(x, y ,z));
                    } else if (is_log(current.getRelative(x, y, z)) && !blocks.containsValue(current.getRelative(x, y, z))) {//new tree
                        Collection<Block> blocks_iterable = blocks.values();
                        for (Block leaf : blocks_iterable) {
                            if (in_block_types(leaf.getType(), leaves)) {
                                double origin_dist = leaf.getLocation().subtract(origin).lengthSquared();
                                double current_dist = leaf.getLocation().subtract(current.getRelative(x, y , z).getLocation()).lengthSquared();
                                if (current_dist < origin_dist) {
                                    blocks.remove(leaf.getLocation());
                                    removed_blocks.put(leaf.getLocation(), leaf);
                                }
                            }
                        }
                    }
                }
            }
        }
        for(Block leaf : added) {
            if(leaf != current) {
                add_leaf_blocks(blocks, leaf, origin, origin_leaf, removed_blocks);
            }
        }
    }

    private void add_log_blocks(ConcurrentHashMap<Location, Block> blocks, Block current, Block last) {
        ArrayList<Block> added = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (!blocks.containsValue(current.getRelative(x, y, z)) && current.getRelative(x, y, z).getType() == last.getType()) {
                        blocks.put(current.getRelative(x, y, z).getLocation(), current.getRelative(x, y, z));
                        added.add(current.getRelative(x, y, z));
                    }
                }
            }
        }

        for (Block block : added) {
            if (block != current) {
                add_log_blocks(blocks, block, current);
            }
        }

    }

    private boolean in_block_types(Material m, Material[] materials) {
        for (Material mat : materials) {
            if (mat == m) {
                return true;
            }
        }
        return false;
    }

    public boolean is_log(Block b) {
        return in_block_types(b.getType(), logs);
    }

    //A proper tree is defined as one with a dirt base block, log trunk and at least 1 leaf on top
    public boolean is_proper_tree(Block block) {

        Block next = block.getRelative(BlockFace.UP);
        while (block.getType() == next.getType()) {
            next = next.getRelative(BlockFace.UP);
        }

        next = next.getRelative(BlockFace.DOWN);
        for (BlockFace face : faces) {
            if (in_block_types(next.getRelative(face).getType(), leaves)) {
                return true;
            }
        }


        return false;
    }

    public void chopTree(ConcurrentHashMap<Location, Block> blocks, Block current, ConcurrentHashMap<Location, Block> temp) {
        add_log_blocks(blocks, current, current);
        //leaves
        Collection<Block> log_blocks = blocks.values();
        for(Block log : log_blocks) {
            for(int x = -1; x <=1; x++) {
                for(int y = -1; y<=1; y++) {
                    for(int z = -1; z <=1; z++) {
                        if (!blocks.containsValue(log.getRelative(x, y ,z)) && in_block_types(log.getRelative(x, y ,z).getType(), leaves)) {
                            add_leaf_blocks(blocks, log.getRelative(x, y, z), log.getLocation(), log.getRelative(x, y, z).getType(), temp);
                        }
                    }
                }
            }
        }
    }

}
