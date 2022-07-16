package net.fabricmc.wildmod_copper.mixin;

import net.fabricmc.wildmod_copper.imixin.IButtonsMixin;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractButtonBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractButtonBlock.class)
public class AbstractButtonBlockMixin implements IButtonsMixin {
  private int grade = 6;

  @Inject(at = @At("RETURN"), method = "getPressTicks", cancellable = true)
  public void makeTickFlexible(CallbackInfoReturnable<Integer> cir){
    cir.setReturnValue(grade * 5);
  }

  public void setGrade(int grade){
    this.grade = grade;
  }
}
