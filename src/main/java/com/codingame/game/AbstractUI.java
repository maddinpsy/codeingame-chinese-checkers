package com.codingame.game;

import java.util.List;

public abstract class AbstractUI {

    public static final int[] PLAYER_COLORS = {
            0xff1d5c,
            0x22a1e4,
            0xff8f16,
            0x6ac371,
            0x9975e2,
            0x3ac5ca,
            0xde6ddf,
            0xff0000,
    };

    public abstract void init(Board b);

    public abstract void update(List<Hex> hops);

}
