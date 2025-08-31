package crying.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.enums.CollapsingReason;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.LightType;

public class SanityManager {
    private static final Map<String, SanityManager> managers = new HashMap<>();
    protected String uuid;

    private CollapsingReason collapsingReason = CollapsingReason.UNKNOWN;

    private float sanityLevel = 0;
    private int sanityTickTimer = 0;
    private int cryingArmorCount = 0;
    private int maxLevel = 0;

    private int ticksHalfHealth = 0;

    private int collapseRegenTicks = 0;
    private int collapseMultiplier = 1;

    private int darkTicks = 0;

    private boolean shouldRegenCommand = true;
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
        if (this.sanityLevel > (float) this.maxLevel)
            this.sanityLevel = (float) this.maxLevel;
                
        updateThis();
    }
    
    public void update(ServerPlayerEntity player)
    {
        ServerWorld serverWorld = player.getWorld();
        Difficulty difficulty = serverWorld.getDifficulty();

        if (getCollapseRegenTicks() >= 1 && !player.isDead()) {
            collapseRegenTicks -= 1 * collapseMultiplier;
            if (collapseRegenTicks < 0)
                collapseRegenTicks = 0;
            if (collapseRegenTicks < 1) {
                goBackToNormal(player);
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
                collapse(Crying.tickSecond(480), player, CollapsingReason.LOW_LIGHT);
                darkTicks = 0;
            }
        }
        else
            darkTicks = 0;

        if (bl && cryingArmorCount > 0 && getCryingArmorCount() >= 1 && getMaxLevel() > 0 && shouldRegen && shouldRegenCommand) 
        {
            ++this.sanityTickTimer;

            int ticktime = Crying.tickSecond(20);
            if (difficulty == Difficulty.EASY)
                ticktime = Crying.tickSecond(10);
            else if (difficulty == Difficulty.HARD)
                ticktime = Crying.tickSecond(30);

            if (difficulty == Difficulty.PEACEFUL)
                ticktime = 5;

            if (this.sanityTickTimer >= ticktime && ticksHalfHealth <= 0)
            {
                decreaseLevel(-1F);
                this.sanityTickTimer = 0;
            }
        }
    }

    public void decreaseLevel(float decrease) {
        if (decrease < 0 && getCollapseRegenTicks() > 0) {
            updateThis();
            return;
        }
        else if (decrease >= getMaxLevel() / 2F && maxLevel >= 10) {
            collapse(Crying.tickSecond(300), null, CollapsingReason.HIGH_DAMAGE);
        }
        this.sanityLevel -= decrease;
        if (this.sanityLevel <= 0) {
            this.sanityTickTimer = 0;
            this.sanityLevel = 0;
        }
        
        float max = (float) getMaxLevel();
        if (this.sanityLevel > max)
            this.sanityLevel = max;

        updateThis();
    }

    private void goBackToNormal(LivingEntity entity) {
        this.sanityLevel = 0;
        collapse(0, null, CollapsingReason.UNKNOWN);
        setCollapseMultiplier(1);
        updateThis();
    }

    public float getSanityLevel() {
        if (getCollapseRegenTicks() > 0)
            return 0F;
        return this.sanityLevel;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public int getCryingArmorCount() {
        return this.cryingArmorCount;
    }

    public int getCollapseRegenTicks() {
        return this.collapseRegenTicks;
    }

    public int getCollapseMultiplier() {
        return this.collapseMultiplier;
    }

    public CollapsingReason getCollapsingReason() {
        return this.collapsingReason;
    }

    public int setSanityLevel(int level) { 
        try {
            this.sanityLevel = level;
            if (this.sanityLevel > this.maxLevel)
                this.sanityLevel = this.maxLevel;

            updateThis();
            return 0;
        }
        catch (Exception e) {
            return 1;
        }
    }

    public void collapse(int tick, @Nullable LivingEntity entity, CollapsingReason reason) {
        if (getCollapseRegenTicks() > 0 || Crying.nextBetween(1, 10) <= 8) 
            return;

        this.sanityLevel = 0F;
        this.collapseRegenTicks = tick;
        this.collapsingReason = reason;
        updateThis();

        if (entity != null) {
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_HURT, entity.getSoundCategory());
        }
    }

    public int setCollapseTicks(int ticks, CollapsingReason reason, PlayerEntity player) {
        try {
            this.collapseRegenTicks = 0;
            collapse(ticks, player, reason);
            updateThis();
            return 0;
        }
        catch (Exception e) {
            return 1;
        }
    }

    public void setCollapseMultiplier(int m) {
        if (getCollapseRegenTicks() >= m)
            this.collapseMultiplier = m;
        else 
            this.collapseMultiplier = 1;

        updateThis();
    }

    public void setRegen(boolean regen) {
        if (this.shouldRegen == regen) 
            return;
        this.shouldRegen = regen;
        updateThis();
    }

    public int setRegenCommand(boolean regen) {
        try {
            this.shouldRegenCommand = regen;
            updateThis();
            return 0;
        } 
        catch (Exception e) {
            return 1;
        }
    }

    public void readNbt(ReadView nbt) {
        this.sanityLevel = nbt.getFloat("sanityLevel", 0F);
        this.sanityTickTimer = nbt.getInt("sanityTickTimer", 0);
        this.maxLevel = nbt.getInt("maxLevel", 0);
        this.cryingArmorCount = nbt.getInt("cryingArmorCount", 0);
        this.shouldRegen = nbt.getBoolean("shouldRegen", false);
        this.shouldRegenCommand = nbt.getBoolean("shouldRegenCommand", true);
        this.ticksHalfHealth = nbt.getInt("ticksHalfHealth", 0);
        this.collapseRegenTicks = nbt.getInt("collapseRegenTicks", 0);
        this.darkTicks = nbt.getInt("darkTicks", 0);
        this.collapseMultiplier = nbt.getInt("collapseMultiplier", 1);
        this.collapsingReason = CollapsingReason.fromString(nbt.getString("collapsingReason", null));

        if (this.sanityLevel > this.maxLevel)
            this.sanityLevel = this.maxLevel;

        updateThis();
    }

    public void writeNbt(WriteView nbt) {
        nbt.putBoolean("shouldRegen", this.shouldRegen);
        nbt.putBoolean("shouldRegenCommand", this.shouldRegenCommand);
        nbt.putString("collapsingReason", this.collapsingReason.getName());
        nbt.putFloat("sanityLevel", this.sanityLevel);
        nbt.putInt("sanityTickTimer", this.sanityTickTimer);
        nbt.putInt("maxLevel", this.maxLevel);
        nbt.putInt("cryingArmorCount", this.cryingArmorCount);
        nbt.putInt("ticksHalfHealth", this.ticksHalfHealth);
        nbt.putInt("collapseRegenTicks", this.collapseRegenTicks);
        nbt.putInt("darkTicks", this.darkTicks);
        nbt.putInt("collapseMultiplier", this.collapseMultiplier);
    }

    public int clear() {
        try {
            this.collapseMultiplier = 0;
            this.sanityLevel = 0;
            this.collapseRegenTicks = 0;
            this.collapsingReason = CollapsingReason.UNKNOWN;
            updateThis();

            return 0;
        }
        catch (Exception e) {
            return 1;
        }
    }

    public void updateThis() {
        managers.put(uuid, this);
    }
}