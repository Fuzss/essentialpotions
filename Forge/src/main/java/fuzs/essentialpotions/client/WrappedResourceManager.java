package fuzs.essentialpotions.client;

import com.mojang.blaze3d.platform.NativeImage;
import fuzs.essentialpotions.EssentialPotions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;

import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WrappedResourceManager implements CloseableResourceManager {
    private final CloseableResourceManager wrapped;

    public WrappedResourceManager(CloseableResourceManager wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void close() {
        this.wrapped.close();
    }

    @Override
    public Set<String> getNamespaces() {
        return this.wrapped.getNamespaces();
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation pLocation) {
        return this.wrapped.getResourceStack(pLocation);
    }

    @Override
    public Map<ResourceLocation, Resource> listResources(String pPath, Predicate<ResourceLocation> pFilter) {
        return this.wrapped.listResources(pPath, pFilter);
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listResourceStacks(String pPath, Predicate<ResourceLocation> pFilter) {
        return this.wrapped.listResourceStacks(pPath, pFilter);
    }

    @Override
    public Stream<PackResources> listPacks() {
        return this.wrapped.listPacks();
    }

    @Override
    public Optional<Resource> getResource(ResourceLocation pLocation) {
        Optional<Resource> resource = this.wrapped.getResource(pLocation);
        if (resource.isPresent() && (pLocation.getPath().matches("textures/gui/[^/]*\\.png") || pLocation.getPath().matches("textures/gui/container/.*\\.png"))) {
            EssentialPotions.LOGGER.info("{}", pLocation);

            try (InputStream open = resource.get().open()) {
                NativeImage read = NativeImage.read(open);
                for (int i = 0; i < read.getWidth(); i++) {
                    for (int j = 0; j < read.getHeight(); j++) {
                        int pixelRGBA = read.getPixelRGBA(i, j);
                        if (NativeImage.getA(pixelRGBA) != 0 && pixelRGBA != -16777216) {
                            int i1 = reduceGrayscale(pixelRGBA);
                            if (i1 == pixelRGBA) {

                                int pAbgrColor = darken2(pixelRGBA) | 0xFF000000;
                                read.setPixelRGBA(i, j, pAbgrColor);
                            } else
                            read.setPixelRGBA(i, j, i1 | 0xFF000000);
                        }
                    }
                }
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(read.asByteArray());
                return Optional.of(new ResourceWrapped(resource.get(), () -> byteArrayInputStream));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        return resource;
    }

    private static int reduceGrayscale(int color) {
        int r = NativeImage.getR(color);
        int g = NativeImage.getG(color);
        int b = NativeImage.getB(color);
        float mult = .2F;
        if (r == g && r == b) {
            return (int) (r * mult) << 16 | (int) (g * mult) << 8 | (int) (b * mult);
        }
        return color;
    }

    private static int reduceGrayscale2(int color) {
        int r = NativeImage.getR(color);
        int g = NativeImage.getG(color);
        int b = NativeImage.getB(color);
        float mult = .35F;
        if (r == g && r == b) {
        }
        return (int) (r * mult) << 16 | (int) (g * mult) << 8 | (int) (b * mult);
//        return color;
    }

    private static int reduceGrayscale3(int color) {
        int minus = 127;
        int r = NativeImage.getR(color);
        int g = NativeImage.getG(color);
        int b = NativeImage.getB(color);
        int rn = r - minus;
        int gn = g - minus;
        int bn = b - minus;
        int overflow = Math.min(0, rn) + Math.min(0, gn) + Math.min(0, bn);

        float mult = .35F;
        if (r == g && r == b) {
        }
        return (int) (r * mult) << 16 | (int) (g * mult) << 8 | (int) (b * mult);
//        return color;
    }

    private static int convertColor(int color) {
        if (color == 0xFFFFFF) {
            return 0x6B6B6B;
        } else if (color == 0xC6C6C6) {
            return 0x535353;
        } else if (color == 0x8B8B8B) {
            return 0x333333;
        } else if (color == 0x373737) {
            return 0x262626;
        }
        return color;
    }

    private static int brightenColor(int potionColor) {
        int red = potionColor >> 16 & 255;
        int green = potionColor >> 8 & 255;
        int blue = potionColor & 255;
        if (red + green + blue > 127) {
            int max = Math.max(red, Math.max(green, blue));
            int decrease = 127 + max;
            final int[] color = {red - decrease, green - decrease, blue - decrease};
//            redistributeColors(color);
            return color[0] << 16 | color[1] << 8 | color[0];
        }
        return potionColor;
    }

    private static int darken(int potionColor) {
        int alpha = potionColor >> 24 & 255;
        int red = potionColor >> 16 & 255;
        int green = potionColor >> 8 & 255;
        int blue = potionColor & 255;
        double[] doubles0 = new double[]{red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0};
        double[] doubles = FromRGB(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0);
        double[] doubles1 = ToRGB(doubles[0], doubles[1], doubles[2], doubles[3]);
        return (int) (doubles1[0] * 255.0) << 16 | (int) (doubles1[1] * 255.0) << 8 | (int) (doubles1[2] * 255.0) << 0 | (int) (doubles1[3] * 255.0) << 24;
//        float[] floats = Color.RGBtoHSB(red, green, blue, null);
//        return Color.HSBtoRGB(floats[0], floats[1], floats[2] * 0.25F);
    }

    private static int darken2(int potionColor) {
        int alpha = potionColor >> 24 & 255;
        int red = potionColor >> 16 & 255;
        int green = potionColor >> 8 & 255;
        int blue = potionColor & 255;
        double[] doubles0 = new double[]{red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0};
        double[] doubles = RGBtoHSP(red / 255.0, green / 255.0, blue / 255.0);
        double[] doubles1 = HSPtoRGB(doubles[0], doubles[1], doubles[2] * .5);
        return (int) (doubles1[0] * 255.0) << 16 | (int) (doubles1[1] * 255.0) << 8 | (int) (doubles1[2] * 255.0);
//        float[] floats = Color.RGBtoHSB(red, green, blue, null);
//        return Color.HSBtoRGB(floats[0], floats[1], floats[2] * 0.25F);
    }

    /**
     * from <a href="https://stackoverflow.com/questions/141855/programmatically-lighten-a-color">programmatically-lighten-a-color</a>
     */
    private static void redistributeColors(int[] color) {
        int min = Math.min(color[0], Math.min(color[1], color[2]));
        if (min < 0) {
            int total = color[0] + color[1] + color[2];
            if (total < 0) {
                color[0] = color[1] = color[2] = 0;
            } else {
                int x = (3 * 255 - total) / (3 * min - total);
                int gray = 255 - x * min;
                color[0] = gray + x * color[0];
                color[1] = gray + x * color[1];
                color[2] = gray + x * color[2];
            }
        }
    }

//    private static int darkenColor(int color) {
//        int red = color >> 16 & 255;
//        int green = color >> 8 & 255;
//        int blue = color & 255;
//        int[] newColor = new int[]{red, green, blue};
//        float threshhold = 0.54F;
//        float res = 0.35F;
//        float div = 3.5F;
//        float[] perceptionScale = new float[]{0.299F, 0.587F, 0.114F};
//        float grey = newColor[0] * perceptionScale[0] + newColor[1] * perceptionScale[1] + newColor[2] * perceptionScale[2];
//        float[] dif = new float[]{Math.abs(newColor[0] - grey), Math.abs(newColor[1] - grey), Math.abs(newColor[2] - grey)};
//        vec3 scaled = pow(dif, 1.0 - perceptionScale);
//        float perceivedSaturation = length(scaled);
//    }

    private static int brightenColor2(int potionColor) {
        int red = potionColor >> 16 & 255;
        int green = potionColor >> 8 & 255;
        int blue = potionColor & 255;
        int max = Math.min(red, Math.min(green, blue));
//        max = (red + green + blue) / 3;
        int decrease = (int) (max * 0.75F);
        final int[] color = {red - decrease, green - decrease, blue - decrease};
        redistributeColors2(color);
        return color[0] << 16 | color[1] << 8 | color[2];
    }

    /**
     * from <a href="https://stackoverflow.com/questions/141855/programmatically-lighten-a-color">programmatically-lighten-a-color</a>
     */
    private static void redistributeColors2(int[] color) {
        int min = Math.min(color[0], Math.min(color[1], color[2]));
        if (min < 0) {
            int total = color[0] + color[1] + color[2];
            if (total < 0) {
                color[0] = color[1] = color[2] = 0;
            } else {
                int x = (total) / (total - 3 * min);
                int gray = x * min;
                color[0] = gray - x * color[0];
                color[1] = gray - x * color[1];
                color[2] = gray - x * color[2];
            }
        }
    }

//Perceived brightness to Red ratio.
    private static final double Pr = .299;
//Perceived brightness to Green ratio.
    private static final double Pg = .587;
//Perceived brightness to Blue ratio.
    private static final double Pb = .114;

    //Expected ranges: Hue = 0-359... Other values = 0-1
    public static double[] ToRGB(double hue, double saturation, double perceivedBrightness, double alpha) {
        //Check values within expected range
        hue = hue < 0 ? 0 : hue > 359 ? 359 : hue;
        saturation = saturation < 0 ? 0 : saturation > 1 ? 1 : saturation;
        perceivedBrightness = perceivedBrightness < 0 ? 0 : perceivedBrightness > 1 ? 1 : perceivedBrightness;
        alpha = alpha < 0 ? 0 : alpha > 1 ? 1 : alpha;
        //Conversion
        var minOverMax = 1 - saturation;
        double r, g, b;
        if (minOverMax > 0) {
            double part;
            if (hue < 0.166666666666667D) { //R>G>B
                hue = 6 * (hue - 0); part = 1 + hue * (1 / minOverMax - 1);
                b = perceivedBrightness / Math.sqrt(Pr / minOverMax / minOverMax + Pg * part * part + Pb);
                r = b / minOverMax; g = b + hue * (r - b);
            }
            else if (hue < 0.333333333333333D) { //G>R>B
                hue = 6 * (-hue + 0.333333333333333D); part = 1 + hue * (1 / minOverMax - 1);
                b = perceivedBrightness / Math.sqrt(Pg / minOverMax / minOverMax + Pr * part * part + Pb);
                g = b / minOverMax; r = b + hue * (g - b);
            }
            else if (hue < 0.5D) {   //  G>B>R
                hue = 6 * (hue - 0.333333333333333D); part = 1 + hue * (1 / minOverMax - 1);
                r = perceivedBrightness / Math.sqrt(Pg / minOverMax / minOverMax + Pb * part * part + Pr);
                g = r / minOverMax; b = r + hue * (g - r);
            }
            else if (hue < 0.666666666666667D) { //B>G>R
                hue = 6 * (-hue + 0.666666666666667D); part = 1 + hue * (1 / minOverMax - 1);
                r = perceivedBrightness / Math.sqrt(Pb / minOverMax / minOverMax + Pg * part * part + Pr);
                b = r / minOverMax; g = r + hue * (b - r);
            }
            else if (hue < 0.833333333333333D) { //B>R>G
                hue = 6 * (hue - 0.666666666666667D); part = 1 + hue * (1 / minOverMax - 1);
                g = perceivedBrightness / Math.sqrt(Pb / minOverMax / minOverMax + Pr * part * part + Pg);
                b = g / minOverMax; r = g + hue * (b - g);
            }
            else { //R>B>G
                hue = 6 * (-hue + 1D); part = 1 + hue * (1 / minOverMax - 1);
                g = perceivedBrightness / Math.sqrt(Pr / minOverMax / minOverMax + Pb * part * part + Pg);
                r = g / minOverMax; b = g + hue * (r - g);
            }
        }
        else {
            if (hue < 0.166666666666667D) { //R>G>B
                hue = 6 * (hue - 0D); r = Math.sqrt(perceivedBrightness * perceivedBrightness / (Pr + Pg * hue * hue)); g = r * hue; b = 0;
            }
            else if (hue < 0.333333333333333D) { //G>R>B
                hue = 6 * (-hue + 0.333333333333333D); g = Math.sqrt(perceivedBrightness * perceivedBrightness / (Pg + Pr * hue * hue)); r = g * hue; b = 0;
            }
            else if (hue < 0.5D) { //G>B>R
                hue = 6 * (hue - 0.333333333333333D); g = Math.sqrt(perceivedBrightness * perceivedBrightness / (Pg + Pb * hue * hue)); b = g * hue; r = 0;
            }
            else if (hue < 0.666666666666667D) { //B>G>R
                hue = 6 * (-hue + 0.666666666666667D); b = Math.sqrt(perceivedBrightness * perceivedBrightness / (Pb + Pg * hue * hue)); g = b * hue; r = 0;
            }
            else if (hue < 0.833333333333333D) { //B>R>G
                hue = 6 * (hue - 0.666666666666667D); b = Math.sqrt(perceivedBrightness * perceivedBrightness / (Pb + Pr * hue * hue)); r = b * hue; g = 0;
            }
            else { //R>B>G
                hue = 6 * (-hue + 1D); r = Math.sqrt(perceivedBrightness * perceivedBrightness / (Pr + Pb * hue * hue)); b = r * hue; g = 0;
            }
        }
        return new double[]{r, g, b, alpha};
    }

    //Expected ranges: 0-1 on all components
    public static double[] FromRGB(double red, double green, double blue, double alpha) {
        //Guarantee RGB values are in the correct ranges
        red = red < 0 ? 0 : red > 1 ? 1 : red;
        green = green < 0 ? 0 : green > 1 ? 1 : green;
        blue = blue < 0 ? 0 : blue > 1 ? 1 : blue;
        alpha = alpha < 0 ? 0 : alpha > 1 ? 1 : alpha;
        //Prepare & cache values for conversion
        var max = Math.max(red, Math.max(green, blue));
        var min = Math.min(red, Math.min(green, blue));
        var delta = max - min;
        double h, s, p = Math.sqrt(0.299 * red + 0.587 * green + 0.114 * blue);
        //Conversion
        if (delta == 0) h = 0;
        else if (max == red) {
            h = (green - blue) / delta % 6;
        }
        else if (max == green) h = (blue - red) / delta + 2;
        else h = (red - green) / delta + 4;
        h *= 60;
        if (h < 0) h += 360;
        if (p == 0)
            s = 0;
        else
            s = delta / p;
        //Result
        return new double[]{h, s, p, alpha};
    }

/*      public domain function by Darel Rex Finley, 2006

  This function expects the passed-in values to be on a scale
  of 0 to 1, and uses that same scale for the return values.

  See description/examples at alienryderflex.com/hsp.html*/

    public static double[] RGBtoHSP(
            double  R, double  G, double  B) {

        double H, S, P;

        //  Calculate the Perceived brightness.
        P=Math.sqrt(R*R*Pr+G*G*Pg+B*B*Pb);

        //  Calculate the Hue and Saturation.  (This part works
        //  the same way as in the HSV/B and HSL systems???.)
        if      (R==G && R==B) {
            H=0.; S=0.; return new double[]{H, S, P}; }
        if      (R>=G && R>=B) {   //  R is largest
            if    (B>=G) {
                H=6./6.-1./6.*(B-G)/(R-G); S=1.-G/R; }
            else         {
                H=0./6.+1./6.*(G-B)/(R-B); S=1.-B/R; }}
        else if (G>=R && G>=B) {   //  G is largest
            if    (R>=B) {
                H=2./6.-1./6.*(R-B)/(G-B); S=1.-B/G; }
            else         {
                H=2./6.+1./6.*(B-R)/(G-R); S=1.-R/G; }}
        else                   {   //  B is largest
            if    (G>=R) {
                H=4./6.-1./6.*(G-R)/(B-R); S=1.-R/B; }
            else         {
                H=4./6.+1./6.*(R-G)/(B-G); S=1.-G/B; }}
        return new double[]{H, S, P};
    }

/*      public domain function by Darel Rex Finley, 2006

  This function expects the passed-in values to be on a scale
  of 0 to 1, and uses that same scale for the return values.

  Note that some combinations of HSP, even if in the scale
  0-1, may return RGB values that exceed a value of 1.  For
  example, if you pass in the HSP color 0,1,1, the result
  will be the RGB color 2.037,0,0.

  See description/examples at alienryderflex.com/hsp.html*/

    public static double[] HSPtoRGB(
            double  H, double  S, double  P) {

        double R, G, B;

        double  part, minOverMax=1.-S ;

        if (minOverMax>0.) {
            if      ( H<1./6.) {   //  R>G>B
                H= 6.*( H-0./6.); part=1.+H*(1./minOverMax-1.);
                B=P/Math.sqrt(Pr/minOverMax/minOverMax+Pg*part*part+Pb);
                R=(B)/minOverMax; G=(B)+H*((R)-(B)); }
            else if ( H<2./6.) {   //  G>R>B
                H= 6.*(-H+2./6.); part=1.+H*(1./minOverMax-1.);
                B=P/Math.sqrt(Pg/minOverMax/minOverMax+Pr*part*part+Pb);
                G=(B)/minOverMax; R=(B)+H*((G)-(B)); }
            else if ( H<3./6.) {   //  G>B>R
                H= 6.*( H-2./6.); part=1.+H*(1./minOverMax-1.);
                R=P/Math.sqrt(Pg/minOverMax/minOverMax+Pb*part*part+Pr);
                G=(R)/minOverMax; B=(R)+H*((G)-(R)); }
            else if ( H<4./6.) {   //  B>G>R
                H= 6.*(-H+4./6.); part=1.+H*(1./minOverMax-1.);
                R=P/Math.sqrt(Pb/minOverMax/minOverMax+Pg*part*part+Pr);
                B=(R)/minOverMax; G=(R)+H*((B)-(R)); }
            else if ( H<5./6.) {   //  B>R>G
                H= 6.*( H-4./6.); part=1.+H*(1./minOverMax-1.);
                G=P/Math.sqrt(Pb/minOverMax/minOverMax+Pr*part*part+Pg);
                B=(G)/minOverMax; R=(G)+H*((B)-(G)); }
            else               {   //  R>B>G
                H= 6.*(-H+6./6.); part=1.+H*(1./minOverMax-1.);
                G=P/Math.sqrt(Pr/minOverMax/minOverMax+Pb*part*part+Pg);
                R=(G)/minOverMax; B=(G)+H*((R)-(G)); }}
        else {
            if      ( H<1./6.) {   //  R>G>B
                H= 6.*( H-0./6.); R=Math.sqrt(P*P/(Pr+Pg*H*H)); G=(R)*H; B=0.; }
            else if ( H<2./6.) {   //  G>R>B
                H= 6.*(-H+2./6.); G=Math.sqrt(P*P/(Pg+Pr*H*H)); R=(G)*H; B=0.; }
            else if ( H<3./6.) {   //  G>B>R
                H= 6.*( H-2./6.); G=Math.sqrt(P*P/(Pg+Pb*H*H)); B=(G)*H; R=0.; }
            else if ( H<4./6.) {   //  B>G>R
                H= 6.*(-H+4./6.); B=Math.sqrt(P*P/(Pb+Pg*H*H)); G=(B)*H; R=0.; }
            else if ( H<5./6.) {   //  B>R>G
                H= 6.*( H-4./6.); B=Math.sqrt(P*P/(Pb+Pr*H*H)); R=(B)*H; G=0.; }
            else               {   //  R>B>G
                H= 6.*(-H+6./6.); R=Math.sqrt(P*P/(Pr+Pb*H*H)); B=(R)*H; G=0.; }}
    return new double[]{R, G, B};
    }

    private static class ResourceWrapped extends Resource {
        private final Resource wrapped;
//        private final Resource.IoSupplier<InputStream> streamSupplier;

        public ResourceWrapped(Resource wrapped, IoSupplier<InputStream> pStreamSupplier) {
            super(wrapped.sourcePackId(), pStreamSupplier, null);
            this.wrapped = wrapped;
//            this.streamSupplier = pStreamSupplier;
        }

        @Override
        public String sourcePackId() {
            return this.wrapped.sourcePackId();
        }

//        @Override
//        public InputStream open() throws IOException {
//            return this.streamSupplier.get();
//        }

        @Override
        public BufferedReader openAsReader() throws IOException {
            return this.wrapped.openAsReader();
        }

        @Override
        public ResourceMetadata metadata() throws IOException {
            return this.wrapped.metadata();
        }
    }
}
