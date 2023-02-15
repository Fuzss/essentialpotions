package fuzs.essentialpotions.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;



@Mixin(Font.class)
public class FontMixin {

    @ModifyArg(method = "drawInternal(Ljava/lang/String;FFILcom/mojang/math/Matrix4f;ZZ)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Ljava/lang/String;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZIIZ)I"), index = 3)
    public int drawInternalA(int color) {
        return modifyColor(color);
    }


    @ModifyArg(method = "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFILcom/mojang/math/Matrix4f;Z)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZII)I"), index = 3)
    public int drawInternal(int color) {
        return modifyColor(color);
    }

    private int modifyColor(int color){
        if (color == 0) return color;
        if (Minecraft.getInstance().screen != null) {
//            int thre = 255;
////            ShaderConfig.Value value = ClientProxy.SHADER_VALUES.get(ClientProxy.SELECTED_SHADER);
////            if (value.darkColorReplacement == -1) return color;
//            if (FastColor.ARGB32.red(color) < thre && FastColor.ARGB32.green(color)  < thre && FastColor.ARGB32.blue(color)  < thre){
//                return 0xFFFFFFFF;
//            }
            return brightenColor(color);
        }
        return color;
    }

    private static int brightenColor(int potionColor) {
        int red = potionColor >> 16 & 255;
        int green = potionColor >> 8 & 255;
        int blue = potionColor & 255;
        if (red + green + blue <= 127) {
            int min = Math.min(red, Math.min(green, blue));
            int increase = 255 - min;
            final int[] color = {red + increase, green + increase, blue + increase};
            redistributeColors(color);
            return color[0] << 16 | color[1] << 8 | color[0];
        }
        return potionColor;
    }

    /**
     * from <a href="https://stackoverflow.com/questions/141855/programmatically-lighten-a-color">programmatically-lighten-a-color</a>
     */
    private static void redistributeColors(int[] color) {
        int max = Math.max(color[0], Math.max(color[1], color[2]));
        if (max > 255) {
            int total = color[0] + color[1] + color[2];
            if (total > 255 * 3) {
                color[0] = color[1] = color[2] = 255;
            } else {
                int x = (3 * 255 - total) / (3 * max - total);
                int gray = 255 - x * max;
                color[0] = gray + x * color[0];
                color[1] = gray + x * color[1];
                color[2] = gray + x * color[2];
            }
        }
    }
}
