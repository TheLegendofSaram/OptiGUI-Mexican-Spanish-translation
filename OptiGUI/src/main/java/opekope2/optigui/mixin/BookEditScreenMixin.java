package opekope2.optigui.mixin;

import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.entity.player.PlayerEntity;
import opekope2.lilac.api.tick.ITickNotifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BookEditScreen.class)
public abstract class BookEditScreenMixin {
    @Unique
    private static final ITickNotifier tickNotifier = ITickNotifier.getInstance();

    @Accessor
    abstract PlayerEntity getPlayer();

    @Inject(method = "changePage", at = @At("RETURN"))
    private void setPageMixin(CallbackInfo ci) {
        tickNotifier.tick(getPlayer().getWorld());
    }
}
