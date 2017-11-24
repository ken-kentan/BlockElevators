package jp.kentan.minecraft.blockelevators.config;

import jp.kentan.minecraft.blockelevators.BlockElevators;

public interface ConfigUpdateListener {
    void onUpdate(BlockElevators.Elevator elevator);
}
