package need.clarity.render;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

public final class NanoVGTextRenderer {
   public static final float[] floatArrayA = new float[4];

   public static float methodA(long l, String string, float f, float g, NVGColor nVGColor, int i, float h, float j, String string1) {
      NanoVG.nvgFontFace(l, string);
      NanoVG.nvgFontSize(l, f);
      NanoVG.nvgTextLetterSpacing(l, g);
      NanoVG.nvgTextAlign(l, i);
      NanoVG.nvgFillColor(l, nVGColor);
      return NanoVG.nvgText(l, h, j, string1);
   }
}
