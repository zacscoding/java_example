package demo.blockchain;

import demo.event.BlockEvent;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface BlockEventListener {

    void onBlock(BlockEvent blockEvent);
}
