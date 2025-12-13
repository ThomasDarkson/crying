package crying.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import crying.Crying;
import crying.enums.CollapsingReason;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SanityManager {
    private static final Map<String, SanityManager> managers = new HashMap<>();
    protected String uuid;

    public boolean isActive = true;

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
        if (!this.isActive)
            return;

        decreaseLevel(damage / 4F);
        updateThis();
    }

    public void adjustSanityLevel(int armor) {
        if (!this.isActive)
            return;
            
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
    
    public void tick(ServerPlayer player)
    {
        if (!this.isActive)
            return;
            
        ServerLevel serverWorld = player.level();
        Difficulty difficulty = serverWorld.getDifficulty();

        Biome biome = serverWorld.getBiome(player.blockPosition()).value();
        if (biome != null) {
            if (biome.getBaseTemperature() <= 0.3F) {
                int coldTicks = ((BiomeVars) player).getTicksInColdBiome();
                coldTicks++;
                ((BiomeVars) player).setTicksInColdBiome(coldTicks);

                if (coldTicks >= Crying.tickSecond(360)) {
                    this.collapse(Crying.tickSecond(420), player, CollapsingReason.HYPOTHERMIA);
                    ((BiomeVars) player).setTicksInColdBiome(0);
                }
            }
            else
                ((BiomeVars) player).setTicksInColdBiome(0);
        }

        if (getCollapseRegenTicks() >= 1 && !player.isDeadOrDying()) {
            collapseRegenTicks -= 1 * collapseMultiplier;
            if (collapseRegenTicks < 0)
                collapseRegenTicks = 0;

            updateThis();
            if (collapseRegenTicks < 1) {
                goBackToNormal(player);
            }
        }

        boolean bl = serverWorld.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.NATURAL_HEALTH_REGENERATION);
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

        if (serverWorld.getBrightness(LightLayer.SKY, player.blockPosition()) <= 4 && serverWorld.getBrightness(LightLayer.BLOCK, player.blockPosition()) <= 4) {
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
        if (!this.isActive)
            return;
            
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
        if (!this.isActive)
            return;
            
        this.sanityLevel = 0;
        this.collapseRegenTicks = 0;
        this.collapsingReason = CollapsingReason.UNKNOWN;
        setCollapseMultiplier(1);
        updateThis();
    }

    public float getSanityLevel() {
        if (!this.isActive)
            return 0F;
            
        if (getCollapseRegenTicks() > 0)
            return 0F;
        return this.sanityLevel;
    }

    public int getMaxLevel() {            
        return this.maxLevel;
    }

    public int getCryingArmorCount() {
        if (!this.isActive)
            return 0;
            
        return this.cryingArmorCount;
    }

    public int getCollapseRegenTicks() {
        if (!this.isActive)
            return 0;

        return this.collapseRegenTicks;
    }

    public int getCollapseMultiplier() {
        if (!this.isActive)
            return 0;

        return this.collapseMultiplier;
    }

    public CollapsingReason getCollapsingReason() {
        if (!this.isActive)
            return null;

        return this.collapsingReason;
    }

    public int setSanityLevel(int level) { 
        if (!this.isActive)
            return 1;

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
        if (!this.isActive)
            return;

        this.sanityLevel = 0F;
        this.collapseRegenTicks = tick;
        this.collapsingReason = reason;
        updateThis();

        if (entity != null) {
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_HURT, entity.getSoundSource());
        }
    }

    public int setCollapseTicks(int ticks, CollapsingReason reason, Player player) {
        if (!this.isActive)
            return 1;

        try {
            collapse(ticks, player, reason);
            return 0;
        }
        catch (Exception e) {
            return 1;
        }
    }

    public void setCollapseMultiplier(int m) {
        if (!this.isActive)
            return;

        if (getCollapseRegenTicks() >= m)
            this.collapseMultiplier = m;
        else 
            this.collapseMultiplier = 1;

        updateThis();
    }

    public void setRegen(boolean regen) {
        if (!this.isActive)
            return;

        if (this.shouldRegen == regen) 
            return;
        this.shouldRegen = regen;
        updateThis();
    }

    public int setRegenCommand(boolean regen) {
        if (!this.isActive)
            return 1;

        try {
            this.shouldRegenCommand = regen;
            updateThis();
            return 0;
        } 
        catch (Exception e) {
            return 1;
        }
    }

    public void readNbt(ValueInput nbt) {
        this.isActive = nbt.getBooleanOr("isActive", true);
        this.sanityTickTimer = nbt.getIntOr("sanityTickTimer", 0);
        this.maxLevel = nbt.getIntOr("maxLevel", 0);
        this.shouldRegen = nbt.getBooleanOr("shouldRegen", false);
        this.shouldRegenCommand = nbt.getBooleanOr("shouldRegenCommand", true);
        this.ticksHalfHealth = nbt.getIntOr("ticksHalfHealth", 0);
        this.collapseRegenTicks = nbt.getIntOr("collapseRegenTicks", 0);
        this.darkTicks = nbt.getIntOr("darkTicks", 0);
        this.collapseMultiplier = nbt.getIntOr("collapseMultiplier", 1);
        this.collapsingReason = CollapsingReason.fromString(nbt.getStringOr("collapsingReason", null));

        if (this.sanityLevel > this.maxLevel)
            this.sanityLevel = this.maxLevel;

        updateThis();
    }

    public void writeNbt(ValueOutput nbt) {
        nbt.putBoolean("isActive", this.isActive);
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
        if (!this.isActive)
            return 1;
            
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