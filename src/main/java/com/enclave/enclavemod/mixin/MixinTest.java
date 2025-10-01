//package com.enclave.enclavemod.mixin;
//
//import net.minecraft.entity.player.EntityPlayer;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//@Mixin(EntityPlayer.class)
//public abstract class MixinTest {
//
//    /**
//     * Наше новое поле в классе EntityPlayer - эссенция
//     */
//    private float essence;
//
//    /**
//     * Виртуальное поле, которое ссылается на реальное поле experienceLevel в классе EntityPlayer
//     */
//    @Shadow(remap = true)
//    public int experienceLevel;
//
//    public float getEssence() {
//        return this.essence;
//    }
//
//    /**
//     * Изменяет текущее количество эссенции игрока. Если оно больше, чем максимально возможное для игрока - ставится максимальное
//     */
//    public void setEssence(float value) {
//        this.essence = Math.min(value, getMaxEssence());
//    }
//
//    /**
//     * Максимальное количество эссенции игрока равно его уровню
//     */
//    public float getMaxEssence() {
//        return this.experienceLevel;
//    }
//
//    public void setMaxEssence(float value) {
//        // NOOP
//        System.out.println("Максимальное количество эссенции зависит от уровня игрока!");
//    }
//}