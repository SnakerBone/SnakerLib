package snaker.snakerlib.level.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import snaker.snakerlib.data.SnakerConstants;
import snaker.snakerlib.level.entity.ai.SnakerFlyControl;

import java.util.Objects;

/**
 * Created by SnakerBone on 26/05/2023
 **/
@SuppressWarnings("unused")
public class SnakerFlyingBoss extends FlyingMob
{
    private final ServerBossEvent BOSS_INFO = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);

    public SnakerFlyingBoss(EntityType<? extends FlyingMob> type, Level level, int xpReward)
    {
        super(type, level);
        this.xpReward = xpReward;
        this.moveControl = new SnakerFlyControl(this);
    }

    public SnakerFlyingBoss(EntityType<? extends FlyingMob> type, Level level)
    {
        this(type, level, SnakerConstants.BOSS_XP_REWARD.asInt());
    }

    public BossEvent.BossBarColor getBarColour()
    {
        return BossEvent.BossBarColor.BLUE;
    }

    public void extraHealth(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).addTransientModifier(new AttributeModifier("ExtraHealth", amount, operation));
    }

    public void extraAttackDamage(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.ATTACK_DAMAGE)).addTransientModifier(new AttributeModifier("ExtraAttackDamage", amount, operation));
    }

    public void extraAttackSpeed(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.ATTACK_SPEED)).addTransientModifier(new AttributeModifier("ExtraAttackSpeed", amount, operation));
    }

    public void extraMovementSpeed(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(new AttributeModifier("ExtraMovementSpeed", amount, operation));
    }

    public void extraFlyingSpeed(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.FLYING_SPEED)).addTransientModifier(new AttributeModifier("ExtraFlyingSpeed", amount, operation));
    }

    public void extraFollowRange(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.FOLLOW_RANGE)).addTransientModifier(new AttributeModifier("ExtraFollowRange", amount, operation));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer)
    {
        return false;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player)
    {
        super.startSeenByPlayer(player);
        BOSS_INFO.setColor(getBarColour());
        BOSS_INFO.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player)
    {
        super.stopSeenByPlayer(player);
        BOSS_INFO.removePlayer(player);
    }

    @Override
    public void tick()
    {
        super.tick();
        BOSS_INFO.setProgress(getHealth() / getMaxHealth());
    }

    @Override
    public boolean shouldDespawnInPeaceful()
    {
        return true;
    }
}