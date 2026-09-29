package need.clarity.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.awt.Color;
import need.clarity.Clarity;
import need.clarity.modules.visual.HandShaders;
import net.minecraft.client.Minecraft;
import org.joml.Vector3f;
import org.slf4j.Logger;

public final class HandShaderRenderer {
   private static ShaderProgram chromaShader;
   private static ShaderProgram invertShader;
   private static ShaderProgram outlineShader;
   private static ShaderProgram jfaInitShader;
   private static ShaderProgram jfaStepShader;
   private static ShaderProgram jfaCompositeShader;
   private static TextureTarget handCopyTarget;
   private static TextureTarget jfaTargetA;
   private static TextureTarget jfaTargetB;
   private static boolean initialized = false;

   public static boolean isInitialized() {
      return initialized;
   }

   public static void initializeShaders() {
      if (!initialized) {
         try {
            ShaderProgram shaderProgram = new ShaderProgram("hand", "chroma");
            chromaShader = shaderProgram;
            ShaderProgram shaderProgram1 = new ShaderProgram("hand", "invert");
            invertShader = shaderProgram1;
            ShaderProgram shaderProgram2 = new ShaderProgram("hand", "outline");
            outlineShader = shaderProgram2;
            ShaderProgram shaderProgram3 = new ShaderProgram("hand", "jfa_init");
            jfaInitShader = shaderProgram3;
            ShaderProgram shaderProgram4 = new ShaderProgram("hand", "jfa_step");
            jfaStepShader = shaderProgram4;
            ShaderProgram shaderProgram5 = new ShaderProgram("hand", "jfa_composite");
            jfaCompositeShader = shaderProgram5;
            initialized = true;
            Clarity.LOGGER.info("[HandShaders] All shaders loaded successfully");
         } catch (Exception exception) {
            Logger logger = Clarity.LOGGER;
            String string = exception.getMessage();
            logger.error("[HandShaders] Failed to initialize hand shaders: {}", string);
         }
      }
   }

   public static void resizeFramebuffers() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft != null) {
         if (minecraft.getWindow() != null) {
            int i = minecraft.getWindow().getWidth();
            int j = minecraft.getWindow().getHeight();
            if (handCopyTarget == null || handCopyTarget.width != i || handCopyTarget.height != j) {
               if (handCopyTarget != null) {
                  handCopyTarget.destroyBuffers();
               }

               TextureTarget textureTarget = new TextureTarget("clarity_hand_copy", i, j, true);
               handCopyTarget = textureTarget;
            }

            if (jfaTargetA == null || jfaTargetA.width != i || jfaTargetA.height != j) {
               if (jfaTargetA != null) {
                  jfaTargetA.destroyBuffers();
               }

               TextureTarget textureTarget1 = new TextureTarget("clarity_jfa_a", i, j, false);
               jfaTargetA = textureTarget1;
            }

            if (jfaTargetB == null || jfaTargetB.width != i || jfaTargetB.height != j) {
               if (jfaTargetB != null) {
                  jfaTargetB.destroyBuffers();
               }

               TextureTarget textureTarget2 = new TextureTarget("clarity_jfa_b", i, j, false);
               jfaTargetB = textureTarget2;
            }
         }
      }
   }

   public static void renderHandEffect() {
      if (initialized) {
         try {
            GlStateSnapshot glStateSnapshot = GlStateSnapshot.capture();

            try {
               String string = HandShaders.getShaderSetting();
               byte b = -1;
               int i = string.hashCode();
               switch (i) {
                  case -2099835914:
                     if (string.equals("Invert")) {
                        b = 1;
                     }
                     break;
                  case 2222509:
                     if (string.equals("Glow")) {
                        b = 2;
                     }
                     break;
                  case 558407714:
                     if (string.equals("Outline")) {
                        b = 3;
                     }
                     break;
                  case 2017705622:
                     if (string.equals("Chroma")) {
                        b = 0;
                     }
               }

               byte c = b;
               switch (c) {
                  case 0:
                     renderChroma();
                     break;
                  case 1:
                     renderInvert();
                     break;
                  case 2:
                     renderOutline();
                     break;
                  case 3:
                     renderOutline();
               }
            } catch (Throwable throwable1) {
               if (glStateSnapshot != null) {
                  try {
                     glStateSnapshot.restore();
                  } catch (Throwable throwable) {
                     throwable1.addSuppressed(throwable);
                  }
               }

               throw throwable1;
            }

            if (glStateSnapshot != null) {
               glStateSnapshot.restore();
            }
         } catch (Exception exception) {
            Logger logger = Clarity.LOGGER;
            String string1 = exception.getMessage();
            logger.error("[HandShaders] Error rendering hand shader: {}", string1);
         }
      }
   }

   private static void methodA(RenderTarget renderTarget, TextureTarget textureTarget) {
      FramebufferUtils.methodA(textureTarget);
      FramebufferUtils.methodB(textureTarget);
      RenderState.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
      RenderState.clear(16640);
      FramebufferUtils.methodA(renderTarget, textureTarget, true);
      FramebufferUtils.methodB(renderTarget);
      RenderState.enableBlend();
      RenderState.defaultBlendFunction();
      RenderState.disableDepthTest();
   }

   private static void methodA(TextureTarget textureTarget) {
      RenderState.setActiveTexture(33984);
      RenderState.bindTexture(textureTarget.getColorTexture());
      RenderState.setActiveTexture(33985);
      RenderState.bindTexture(textureTarget.getDepthTexture());
   }

   private static void restoreRenderState() {
      RenderState.setActiveTexture(33984);
      RenderState.disableBlend();
      RenderState.enableDepthTest();
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.getMainRenderTarget() != null) {
         FramebufferUtils.methodB(minecraft.getMainRenderTarget());
      }
   }

   private static Vector3f methodA(int i) {
      Color color = new Color(i, true);
      float f = color.getRed() / 255.0F;
      float g = color.getGreen() / 255.0F;
      float h = color.getBlue() / 255.0F;
      return new Vector3f(f, g, h);
   }

   private static void renderChroma() {
      RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
      if (renderTarget != null) {
         if (chromaShader != null) {
            if (handCopyTarget != null) {
               TextureTarget textureTarget = handCopyTarget;
               methodA(renderTarget, textureTarget);
               methodA(handCopyTarget);
               chromaShader.methodA();
               chromaShader.methodA("ColorTexture", 0);
               chromaShader.methodA("DepthTexture", 1);
               ShaderProgram shaderProgram = chromaShader;
               float f = (float)System.nanoTime() / 1.0E9F;
               shaderProgram.methodA("time", f);
               ShaderProgram shaderProgram1 = chromaShader;
               float g = HandShaders.getChromaSpeedSetting();
               shaderProgram1.methodA("chromaSpeed", g);
               ShaderProgram shaderProgram2 = chromaShader;
               float h = HandShaders.getChromaSaturationSetting();
               shaderProgram2.methodA("chromaSaturation", h);
               ShaderProgram shaderProgram3 = chromaShader;
               float i = HandShaders.getChromaBrightnessSetting();
               shaderProgram3.methodA("chromaBrightness", i);
               drawFullscreenQuad();
               chromaShader.methodB();
               restoreRenderState();
            }
         }
      }
   }

   private static void renderInvert() {
      RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
      if (renderTarget != null) {
         if (invertShader != null) {
            if (handCopyTarget != null) {
               TextureTarget textureTarget = handCopyTarget;
               methodA(renderTarget, textureTarget);
               methodA(handCopyTarget);
               invertShader.methodA();
               invertShader.methodA("ColorTexture", 0);
               invertShader.methodA("DepthTexture", 1);
               ShaderProgram shaderProgram = invertShader;
               float f = (float)System.nanoTime() / 1.0E9F;
               shaderProgram.methodA("time", f);
               ShaderProgram shaderProgram1 = invertShader;
               boolean bl = HandShaders.isHideHandSetting();
               shaderProgram1.methodA("hideHand", bl);
               ShaderProgram shaderProgram2 = invertShader;
               float g = HandShaders.getInvertIntensitySetting();
               shaderProgram2.methodA("invertIntensity", g);
               ShaderProgram shaderProgram3 = invertShader;
               boolean bl1 = HandShaders.isInvertHueSetting();
               shaderProgram3.methodA("invertHue", bl1);
               ShaderProgram shaderProgram4 = invertShader;
               boolean bl2 = HandShaders.isInvertSaturationSetting();
               shaderProgram4.methodA("invertSaturation", bl2);
               ShaderProgram shaderProgram5 = invertShader;
               boolean bl3 = HandShaders.isInvertBrightnessSetting();
               shaderProgram5.methodA("invertBrightness", bl3);
               ShaderProgram shaderProgram6 = invertShader;
               float h = HandShaders.getEdgeGlowSetting();
               shaderProgram6.methodA("edgeGlow", h);
               ShaderProgram shaderProgram7 = invertShader;
               Vector3f vector3f = methodA(HandShaders.methodL());
               shaderProgram7.methodA("glowColor", vector3f);
               drawFullscreenQuad();
               invertShader.methodB();
               restoreRenderState();
            }
         }
      }
   }

   private static void renderOutline() {
      RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
      if (renderTarget != null) {
         if (outlineShader != null) {
            if (handCopyTarget != null) {
               TextureTarget textureTarget = handCopyTarget;
               methodA(renderTarget, textureTarget);
               methodA(handCopyTarget);
               outlineShader.methodA();
               outlineShader.methodA("ColorTexture", 0);
               outlineShader.methodA("DepthTexture", 1);
               ShaderProgram shaderProgram = outlineShader;
               Vector3f vector3f = methodA(HandShaders.methodY());
               shaderProgram.methodA("outlineColor", vector3f);
               ShaderProgram shaderProgram1 = outlineShader;
               float f = HandShaders.getOutlineThicknessSetting();
               shaderProgram1.methodA("outlineThickness", f);
               ShaderProgram shaderProgram2 = outlineShader;
               boolean bl = HandShaders.isTintSetting();
               shaderProgram2.methodA("tintEnabled", bl);
               ShaderProgram shaderProgram3 = outlineShader;
               Vector3f vector3f1 = methodA(HandShaders.methodUpperB());
               shaderProgram3.methodA("tintColor", vector3f1);
               ShaderProgram shaderProgram4 = outlineShader;
               float g = HandShaders.getTintAmountSetting();
               shaderProgram4.methodA("tintAmount", g);
               drawFullscreenQuad();
               outlineShader.methodB();
               restoreRenderState();
            }
         }
      }
   }

   public static void drawFullscreenQuad() {
      RenderSystem.assertOnRenderThread();
      Tesselator tesselator = Tesselator.getInstance();
      Mode mode = Mode.QUADS;
      VertexFormat vertexFormat = DefaultVertexFormat.POSITION;
      BufferBuilder bufferBuilder = tesselator.begin(mode, vertexFormat);
      bufferBuilder.addVertex(-1.0F, -1.0F, 0.0F);
      bufferBuilder.addVertex(1.0F, -1.0F, 0.0F);
      bufferBuilder.addVertex(1.0F, 1.0F, 0.0F);
      bufferBuilder.addVertex(-1.0F, 1.0F, 0.0F);
      MeshRenderer.methodA(bufferBuilder.buildOrThrow());
   }
}
