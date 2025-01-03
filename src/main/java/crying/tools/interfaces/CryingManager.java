package crying.tools.interfaces;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class CryingManager {
    private static Map<String, CryingManager> managers = new HashMap<>();
    protected String uuid;

    private float cryingLevel = 0;
    private int cryingTickTimer = 0;
    private int cryingArmorCount = 0;
    private int maxLevel = 0;

    private boolean shouldRegen = false;

    public static CryingManager getFromUUID(String uuid) {
        return managers.get(uuid);
    }

    public CryingManager(String uuid) {
        this.uuid = uuid;
        updateThis();
    }

    public void damage(float damage) {
        decreaseLevel(damage / 4F);
        setRegen(false);
        updateThis();
    }

    public void adjustCryingLevel(int armor) {
        if (armor < 0)
            armor = 0;
        if (armor > 4)
            armor = 4;

        this.cryingArmorCount = armor;
        this.maxLevel = armor * 5;
        if (this.cryingLevel > this.maxLevel)
            this.cryingLevel = this.maxLevel;
                
        updateThis();
    }
    
    public void update(ServerPlayerEntity player)
    {
        ServerWorld serverWorld = player.getServerWorld();
        Difficulty difficulty = serverWorld.getDifficulty();

        boolean bl = serverWorld.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        if (getCryingLevel() < 1 || difficulty == Difficulty.PEACEFUL)
            setRegen(true);

        if (getCryingLevel() >= (float) getMaxLevel())
            setRegen(false);

        if (bl && cryingArmorCount > 0 && getCryingArmorCount() >= 1 && getMaxLevel() > 0 && shouldRegen) 
        {
            ++this.cryingTickTimer;

            int ticktime = 160 / (4 / (getMaxLevel() / 5));
            if (difficulty == Difficulty.EASY)
                ticktime = 80 / (4 / (getMaxLevel() / 5));
            else if (difficulty == Difficulty.HARD)
                ticktime = 320 / (4 / (getMaxLevel() / 5));

            if (player.isSprinting())
                ticktime += 20;

            if (difficulty == Difficulty.PEACEFUL)
                ticktime = 20;

            if (this.cryingTickTimer >= ticktime)
            {
                decreaseLevel(-1F);
                this.cryingTickTimer = 0;
            }
        }
    }

    public void decreaseLevel(float decrease) {
        this.cryingLevel -= decrease;
        if (this.cryingLevel < 0)
            this.cryingLevel = 0;
        else if (this.cryingLevel > this.maxLevel)
            this.cryingLevel = this.maxLevel;

        updateThis();
    }

    public float getCryingLevel() {
        return this.cryingLevel;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public int getCryingArmorCount() {
        return this.cryingArmorCount;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("cryingLevel", 99)) {
            this.cryingLevel = nbt.getFloat("cryingLevel");
            this.cryingTickTimer = nbt.getInt("cryingTickTimer");
            this.maxLevel = nbt.getInt("maxLevel");
            this.cryingArmorCount = nbt.getInt("cryingArmorCount");
            this.shouldRegen = nbt.getBoolean("shouldRegen");

            if (this.cryingLevel > this.maxLevel)
                this.cryingLevel = this.maxLevel;

            updateThis();
        }
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putFloat("cryingLevel", this.cryingLevel);
        nbt.putInt("cryingTickTimer", this.cryingTickTimer);
        nbt.putInt("maxLevel", this.maxLevel);
        nbt.putInt("cryingArmorCount", this.cryingArmorCount);
        nbt.putBoolean("shouldRegen", this.shouldRegen);
    }

    public void setRegen(boolean regen) {
        this.shouldRegen = regen;
        updateThis();
    }

    private void updateThis() {
        managers.put(uuid, this);
    }
}
