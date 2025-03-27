package crying.tools.interfaces;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class SanityManager {
    private static Map<String, SanityManager> managers = new HashMap<>();
    private static final int FINAL_MAX_INT = 20;
    protected String uuid;

    private float sanityLevel = 0;
    private int sanityTickTimer = 0;
    private int cryingArmorCount = 0;
    private int maxLevel = 0;

    private int ticksHalfHealth = 0;

    private int permanentMaxLevel = 0;

    private boolean shouldRegen = true;

    public static SanityManager getFromUUID(String uuid) {
        return managers.get(uuid);
    }

    public SanityManager(String uuid) {
        this.uuid = uuid;
        updateThis();
    }

    public void damage(float damage) {
        decreaseLevel(damage / 4F);
        updateThis();
    }

    public void adjustsanityLevel(int armor) {
        if (armor < 0)
            armor = 0;
        if (armor > 4)
            armor = 4;

        this.cryingArmorCount = armor;
        this.maxLevel = armor * 5;
        if (this.sanityLevel < ((float) getPermanentMaxLevel()))
            this.sanityLevel = (float) getPermanentMaxLevel();
        else if (this.sanityLevel > (float) this.maxLevel)
            this.sanityLevel = (float) this.maxLevel;
                
        updateThis();
    }

    public void increasePermanentMaxLevel() {
        if (getPermanentMaxLevel() >= FINAL_MAX_INT) {
            return;
        }

        this.permanentMaxLevel += 2;
        if (getPermanentMaxLevel() >= FINAL_MAX_INT)
            this.permanentMaxLevel = FINAL_MAX_INT;
        updateThis();
    }
    
    public void update(ServerPlayerEntity player)
    {
        ServerWorld serverWorld = player.getServerWorld();
        Difficulty difficulty = serverWorld.getDifficulty();

        boolean bl = serverWorld.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        if (getsanityLevel() >= (float) getMaxLevel())
            setRegen(false);
        else
            setRegen(true);

        if (player.isCreative())
            setRegen(true);

        if (player.getMaxHealth() > 1F && (player.getHealth() / player.getMaxHealth()) < 0.4F) {
            ticksHalfHealth++;
        } 
        else 
            ticksHalfHealth = 0;

        if (ticksHalfHealth % 200 == 0 && ticksHalfHealth > 0) {
            decreaseLevel(1F);
        }

        if (bl && cryingArmorCount > 0 && getCryingArmorCount() >= 1 && getMaxLevel() > 0 && shouldRegen) 
        {
            ++this.sanityTickTimer;

            int ticktime = 400;
            if (difficulty == Difficulty.EASY)
                ticktime = 200;
            else if (difficulty == Difficulty.HARD)
                ticktime = 600;

            if (difficulty == Difficulty.PEACEFUL || player.isCreative())
                ticktime = 5;

            if (this.sanityTickTimer >= ticktime && ticksHalfHealth <= 0)
            {
                decreaseLevel(-1F);
                this.sanityTickTimer = 0;
            }
        }
    }

    public void decreaseLevel(float decrease) {
        this.sanityLevel -= decrease;
        if (this.sanityLevel <= 0) {
            this.sanityTickTimer = 0;
            this.sanityLevel = 0;
        }
        
        if (this.sanityLevel < ((float) getPermanentMaxLevel()))
            this.sanityLevel = (float) getPermanentMaxLevel();
        else if (this.sanityLevel > (float) this.maxLevel)
            this.sanityLevel = (float) this.maxLevel;

        updateThis();
    }

    public float getsanityLevel() {
        return this.sanityLevel;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public int getCryingArmorCount() {
        return this.cryingArmorCount;
    }

    public int getPermanentMaxLevel() {
        return this.permanentMaxLevel;
    }

    public void setPermanentMaxLevel(int level) {
        this.permanentMaxLevel = level;
        this.sanityLevel = (float) level;

        updateThis();
    }

    public void readNbt(NbtCompound nbt) {
        this.sanityLevel = nbt.getFloat("sanityLevel", 0F);
        this.sanityTickTimer = nbt.getInt("sanityTickTimer", 0);
        this.maxLevel = nbt.getInt("maxLevel", 0);
        this.permanentMaxLevel = nbt.getInt("permanentMaxLevel", 0);
        this.cryingArmorCount = nbt.getInt("cryingArmorCount", 0);
        this.shouldRegen = nbt.getBoolean("shouldRegen", false);
        this.ticksHalfHealth = nbt.getInt("ticksHalfHealth", 0);
            
        if (this.sanityLevel < (float) this.permanentMaxLevel)
            this.sanityLevel = (float) this.permanentMaxLevel;

        else if (this.sanityLevel > this.maxLevel)
            this.sanityLevel = this.maxLevel;

        if (this.permanentMaxLevel > FINAL_MAX_INT)
            this.permanentMaxLevel = FINAL_MAX_INT;

        updateThis();
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putFloat("sanityLevel", this.sanityLevel);
        nbt.putInt("sanityTickTimer", this.sanityTickTimer);
        nbt.putInt("permanentMaxLevel", this.permanentMaxLevel);
        nbt.putInt("maxLevel", this.maxLevel);
        nbt.putInt("cryingArmorCount", this.cryingArmorCount);
        nbt.putBoolean("shouldRegen", this.shouldRegen);
        nbt.putInt("ticksHalfHealth", this.ticksHalfHealth);
    }

    public void setRegen(boolean regen) {
        if (this.shouldRegen == regen) return;
        this.shouldRegen = regen;
        updateThis();
    }

    private void updateThis() {
        managers.put(uuid, this);
    }
}