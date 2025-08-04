package crying.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.LightType;

public class SanityManager {
    private static final Map<String, SanityManager> managers = new HashMap<>();
    private static final int FINAL_MAX_INT = 20;
    protected String uuid;

    private float sanityLevel = 0;
    private int sanityTickTimer = 0;
    private int cryingArmorCount = 0;
    private int maxLevel = 0;

    private int ticksHalfHealth = 0;

    private int permanentMaxLevel = 0;

    private int preventRegenTicks = 0;
    private int preventMultiplier = 1;

    private int darkTicks = 0;

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

    public void adjustSanityLevel(int armor) {
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
        increasePermanentMaxLevel(2);
    }

    public void increasePermanentMaxLevel(int increase) {
        if (getPermanentMaxLevel() >= FINAL_MAX_INT) {
            return;
        }

        this.permanentMaxLevel += increase;
        if (getPermanentMaxLevel() >= FINAL_MAX_INT)
            this.permanentMaxLevel = FINAL_MAX_INT;
        updateThis();
    }
    
    public void update(ServerPlayerEntity player)
    {
        ServerWorld serverWorld = player.getServerWorld();
        Difficulty difficulty = serverWorld.getDifficulty();

        if (getPreventRegenTicks() >= 1 * preventMultiplier && !player.isDead()) {
            preventRegenTicks -= 1 * preventMultiplier;
            if (preventRegenTicks < 1) {
                goBackToNormal();
            }
        }

        boolean bl = serverWorld.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        if (getSanityLevel() >= (float) getMaxLevel())
            setRegen(false);
        else
            setRegen(true);

        if (player.getMaxHealth() > 1F && (player.getHealth() / player.getMaxHealth()) < 0.5F) {
            ticksHalfHealth++;
        } 
        else 
            ticksHalfHealth = 0;

        if (ticksHalfHealth % 200 == 0 && ticksHalfHealth > 0) {
            decreaseLevel(1F);
        }

        if (serverWorld.getLightLevel(LightType.SKY, player.getBlockPos()) <= 4 && serverWorld.getLightLevel(LightType.BLOCK, player.getBlockPos()) <= 4) {
            darkTicks++;
            if (darkTicks == Crying.tickSecond(30)) {
                setPreventRegenTicks(Crying.tickSecond(480), player);
                darkTicks = 0;
            }
        }
        else
            darkTicks = 0;

        if (bl && cryingArmorCount > 0 && getCryingArmorCount() >= 1 && getMaxLevel() > 0 && shouldRegen) 
        {
            ++this.sanityTickTimer;

            int ticktime = Crying.tickSecond(20);
            if (difficulty == Difficulty.EASY)
                ticktime = Crying.tickSecond(10);
            else if (difficulty == Difficulty.HARD)
                ticktime = Crying.tickSecond(30);

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
        if (decrease < 0 && getPreventRegenTicks() > 0) {
            updateThis();
            return;
        }
        else if (decrease >= getMaxLevel() / 2F && maxLevel >= 10) {
            setPreventRegenTicks(Crying.tickSecond(300), null);
        }
        this.sanityLevel -= decrease;
        if (this.sanityLevel <= 0) {
            this.sanityTickTimer = 0;
            this.sanityLevel = 0;
        }
        
        float pmax = (float) getPermanentMaxLevel();
        float max = (float) getMaxLevel();
        if (this.sanityLevel < pmax)
            this.sanityLevel = pmax;
        else if (this.sanityLevel > max)
            this.sanityLevel = max;

        updateThis();
    }

    private void goBackToNormal() {
        this.sanityLevel = 0;
        setPreventRegenTicks(0, null);
        setPreventMultiplier(1);
        updateThis();
    }

    public float getSanityLevel() {
        if (getPreventRegenTicks() > 0)
            return 0F;
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

    public int getPreventRegenTicks() {
        return this.preventRegenTicks;
    }

    public int getPreventMultiplier() {
        return this.preventMultiplier;
    }

    public void setPermanentMaxLevel(int level) {
        this.permanentMaxLevel = level;
        this.sanityLevel = (float) level;

        updateThis();
    }

    public void setPreventRegenTicks(int tick, @Nullable LivingEntity entity) {
        if (getPreventRegenTicks() > 0 || Crying.nextBetween(1, 10) <= 5) 
            return;

        this.sanityLevel = 0F;
        this.preventRegenTicks = tick;
        updateThis();

        if (entity != null) {
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_HURT, entity.getSoundCategory());
        }
    }

    public void setPreventMultiplier(int m) {
        if (getPreventRegenTicks() >= m)
            this.preventMultiplier = m;
        else 
            this.preventMultiplier = 1;

        updateThis();
    }

    public void setRegen(boolean regen) {
        if (this.shouldRegen == regen) 
            return;
        this.shouldRegen = regen;
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
        this.preventRegenTicks = nbt.getInt("preventRegenTicks", 0);
        this.darkTicks = nbt.getInt("darkTicks", 0);
        this.preventMultiplier = nbt.getInt("preventMultiplier", 1);
            
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
        nbt.putInt("preventRegenTicks", this.preventRegenTicks);
        nbt.putInt("darkTicks", this.darkTicks);
        nbt.putInt("preventMultiplier", this.preventMultiplier);
    }

    private void updateThis() {
        managers.put(uuid, this);
    }
}