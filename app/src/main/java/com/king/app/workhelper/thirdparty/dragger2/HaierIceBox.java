package com.king.app.workhelper.thirdparty.dragger2;

/**
 * @author VanceKing
 * @since 2020/2/27.
 */
public class HaierIceBox implements IceBox {
    Ice ice;

    public HaierIceBox(Ice ice) {
        this.ice = ice;
    }

    @Override
    public void addIce() {
        ice.add();
    }
}
