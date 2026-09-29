package need.clarity.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup.RenderSetupBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class BoxRenderer {
   private static final Snippet snippetK;
   public static final RenderPipeline renderPipelineA;
   public static final RenderPipeline renderPipelineB;
   public static final RenderType renderTypeC;
   public static final RenderType renderTypeD;
   private static final DepthStencilState depthStencilStateL;
   public static final RenderPipeline renderPipelineE;
   public static final RenderPipeline renderPipelineF;
   public static final RenderType renderTypeG;
   public static final RenderType renderTypeH;
   public static final RenderPipeline renderPipelineI;
   public static final RenderType renderTypeJ;

   public static Vec3 methodA() {
      Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
      Vec3 vec3;
      if (camera == null) {
         vec3 = Vec3.ZERO;
      } else {
         vec3 = camera.position();
      }

      return vec3;
   }

   private static BufferSource methodB() {
      return Minecraft.getInstance().renderBuffers().bufferSource();
   }

   public static void methodA(PoseStack poseStack, List<AABB> list, int i, int j, boolean bl, boolean bl1) {
      if (!list.isEmpty()) {
         Vec3 vec3 = methodA().reverse();
         BufferSource bufferSource = methodB();
         if (bl) {
            RenderType renderType = renderTypeC;
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

            for (AABB aABB : list) {
               double d = vec3.x;
               double f = vec3.y;
               double h = vec3.z;
               AABB aABB2 = aABB.move(d, f, h);
               methodA(poseStack, vertexConsumer, aABB2, i);
            }

            RenderType renderType1 = renderTypeC;
            bufferSource.endBatch(renderType1);
         }

         if (bl1) {
            RenderType renderType2 = renderTypeD;
            VertexConsumer vertexConsumer1 = bufferSource.getBuffer(renderType2);

            for (AABB aABB1 : list) {
               double e = vec3.x;
               double g = vec3.y;
               double k = vec3.z;
               AABB aABB3 = aABB1.move(e, g, k);
               methodB(poseStack, vertexConsumer1, aABB3, j);
            }

            RenderType renderType3 = renderTypeD;
            bufferSource.endBatch(renderType3);
         }
      }
   }

   public static void methodA(PoseStack poseStack, List<AABB> list, int i, int j) {
      methodA(poseStack, list, i, j, true, true);
   }

   public static void methodA(PoseStack poseStack, List<ColoredBox> list, boolean bl, int i, boolean bl1, boolean bl2, boolean bl3, float f) {
      if (!list.isEmpty()) {
         Vec3 vec3 = methodA().reverse();
         BufferSource bufferSource = methodB();
         if (bl) {
            RenderType renderType2;
            if (bl2) {
               renderType2 = renderTypeC;
            } else {
               renderType2 = renderTypeG;
            }

            RenderType renderType = renderType2;
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
               AABB aABB2 = ((ColoredBox)iterator.next()).getBounds();
               double e = vec3.x;
               double h = vec3.y;
               double m = vec3.z;
               AABB aABB3 = aABB2.move(e, h, m);
               methodA(poseStack, vertexConsumer, aABB3, i);
            }

            bufferSource.endBatch(renderType);
         }

         if (bl1) {
            RenderType renderType1;
            if (bl2) {
               renderType1 = renderTypeD;
            } else {
               renderType1 = renderTypeH;
            }

            RenderType renderType3 = renderType1;
            VertexConsumer vertexConsumer1 = bufferSource.getBuffer(renderType3);

            for (ColoredBox coloredBox : list) {
               AABB aABB = coloredBox.getBounds();
               double d = vec3.x;
               double g = vec3.y;
               double l = vec3.z;
               AABB aABB1 = aABB.move(d, g, l);
               if (bl3) {
                  int k = coloredBox.getColor();
                  methodB(poseStack, vertexConsumer1, aABB1, k, f);
               } else {
                  int j = coloredBox.getColor();
                  methodA(poseStack, vertexConsumer1, aABB1, j, f);
               }
            }

            bufferSource.endBatch(renderType3);
         }
      }
   }

   private static Vec3 methodA(Vec3 vec3) {
      double d = vec3.length();
      if (d < 2.0) {
         if (d < 1.0E-6) {
            return new Vec3(0.0, 0.0, -2.0);
         }

         double e = 2.0 / d;
         return vec3.scale(e);
      } else {
         return vec3;
      }
   }

   public static void methodA(PoseStack poseStack, Vec3 vec3, Vec3 vec31, int i, float f) {
      methodA(poseStack, vec3, vec31, i, f, true);
   }

   public static void methodA(PoseStack poseStack, Vec3 vec3, Vec3 vec31, int i, float f, boolean bl) {
      Vec3 vec32 = methodA().reverse();
      BufferSource bufferSource = methodB();
      RenderType renderType1;
      if (bl) {
         renderType1 = renderTypeD;
      } else {
         renderType1 = renderTypeH;
      }

      RenderType renderType = renderType1;
      VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
      Vec3 vec33 = vec3.add(vec32);
      Vec3 vec34 = vec31.add(vec32);
      methodA(poseStack, vertexConsumer, vec33, vec34, i, f);
      bufferSource.endBatch(renderType);
   }

   public static void methodA(PoseStack poseStack, float m, List<Vec3> list, int i, float f) {
      if (!list.isEmpty()) {
         Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
         if (camera != null) {
            Vec3 vec3 = methodA().reverse();
            double d = Math.sin(-Math.toRadians(camera.yRot()));
            double g = Math.cos(Math.toRadians(camera.xRot()));
            double e = d * g;
            double h = -Math.sin(Math.toRadians(camera.xRot()));
            double j = Math.cos(-Math.toRadians(camera.yRot()));
            double l = Math.cos(Math.toRadians(camera.xRot()));
            double k = j * l;
            Vec3 vec31 = new Vec3(e, h, k);
            Vec3 vec32 = vec31.scale(10.0);
            BufferSource bufferSource = methodB();
            RenderType renderType = renderTypeD;
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
               Vec3 vec33 = methodA(((Vec3)iterator.next()).add(vec3));
               methodA(poseStack, vertexConsumer, vec32, vec33, i, f);
            }

            RenderType renderType1 = renderTypeD;
            bufferSource.endBatch(renderType1);
         }
      }
   }

   private static void methodA(PoseStack poseStack, VertexConsumer vertexConsumer, AABB aABB, int i) {
      Pose pose = poseStack.last();
      float f = (float)aABB.minX;
      float g = (float)aABB.minY;
      float h = (float)aABB.minZ;
      float j = (float)aABB.maxX;
      float k = (float)aABB.maxY;
      float l = (float)aABB.maxZ;
      vertexConsumer.addVertex(pose, f, g, h).setColor(i);
      vertexConsumer.addVertex(pose, j, g, h).setColor(i);
      vertexConsumer.addVertex(pose, j, g, l).setColor(i);
      vertexConsumer.addVertex(pose, f, g, l).setColor(i);
      vertexConsumer.addVertex(pose, f, k, h).setColor(i);
      vertexConsumer.addVertex(pose, f, k, l).setColor(i);
      vertexConsumer.addVertex(pose, j, k, l).setColor(i);
      vertexConsumer.addVertex(pose, j, k, h).setColor(i);
      vertexConsumer.addVertex(pose, f, g, h).setColor(i);
      vertexConsumer.addVertex(pose, f, k, h).setColor(i);
      vertexConsumer.addVertex(pose, j, k, h).setColor(i);
      vertexConsumer.addVertex(pose, j, g, h).setColor(i);
      vertexConsumer.addVertex(pose, f, g, l).setColor(i);
      vertexConsumer.addVertex(pose, j, g, l).setColor(i);
      vertexConsumer.addVertex(pose, j, k, l).setColor(i);
      vertexConsumer.addVertex(pose, f, k, l).setColor(i);
      vertexConsumer.addVertex(pose, f, g, h).setColor(i);
      vertexConsumer.addVertex(pose, f, g, l).setColor(i);
      vertexConsumer.addVertex(pose, f, k, l).setColor(i);
      vertexConsumer.addVertex(pose, f, k, h).setColor(i);
      vertexConsumer.addVertex(pose, j, g, h).setColor(i);
      vertexConsumer.addVertex(pose, j, k, h).setColor(i);
      vertexConsumer.addVertex(pose, j, k, l).setColor(i);
      vertexConsumer.addVertex(pose, j, g, l).setColor(i);
   }

   private static void methodB(PoseStack poseStack, VertexConsumer vertexConsumer, AABB aABB, int i) {
      methodA(poseStack, vertexConsumer, aABB, i, 2.0F);
   }

   private static void methodA(PoseStack poseStack, VertexConsumer vertexConsumer, AABB aABB, int i, float f) {
      Pose pose = poseStack.last();
      float g = (float)aABB.minX;
      float h = (float)aABB.minY;
      float j = (float)aABB.minZ;
      float k = (float)aABB.maxX;
      float l = (float)aABB.maxY;
      float m = (float)aABB.maxZ;
      methodA(vertexConsumer, pose, g, h, j, k, h, j, 1.0F, 0.0F, 0.0F, i, f);
      methodA(vertexConsumer, pose, k, h, j, k, h, m, 0.0F, 0.0F, 1.0F, i, f);
      methodA(vertexConsumer, pose, k, h, m, g, h, m, -1.0F, 0.0F, 0.0F, i, f);
      methodA(vertexConsumer, pose, g, h, m, g, h, j, 0.0F, 0.0F, -1.0F, i, f);
      methodA(vertexConsumer, pose, g, l, j, k, l, j, 1.0F, 0.0F, 0.0F, i, f);
      methodA(vertexConsumer, pose, k, l, j, k, l, m, 0.0F, 0.0F, 1.0F, i, f);
      methodA(vertexConsumer, pose, k, l, m, g, l, m, -1.0F, 0.0F, 0.0F, i, f);
      methodA(vertexConsumer, pose, g, l, m, g, l, j, 0.0F, 0.0F, -1.0F, i, f);
      methodA(vertexConsumer, pose, g, h, j, g, l, j, 0.0F, 1.0F, 0.0F, i, f);
      methodA(vertexConsumer, pose, k, h, j, k, l, j, 0.0F, 1.0F, 0.0F, i, f);
      methodA(vertexConsumer, pose, k, h, m, k, l, m, 0.0F, 1.0F, 0.0F, i, f);
      methodA(vertexConsumer, pose, g, h, m, g, l, m, 0.0F, 1.0F, 0.0F, i, f);
   }

   private static void methodB(PoseStack poseStack, VertexConsumer vertexConsumer, AABB aABB, int i, float f) {
      Pose pose = poseStack.last();
      float q = (float)aABB.minX;
      float r = (float)aABB.minY;
      float s = (float)aABB.minZ;
      float t = (float)aABB.maxX;
      float u = (float)aABB.maxY;
      float v = (float)aABB.maxZ;
      float w = t - q;
      float x = u - r;
      float y = v - s;
      float bg = Math.min(Math.min(w, x), y) * 0.25F;
      float z = Math.max(0.1F, bg);

      for (int j = 0; j < 2; j++) {
         for (int k = 0; k < 2; k++) {
            for (int l = 0; l < 2; l++) {
               float be;
               if (j == 0) {
                  be = q;
               } else {
                  be = t;
               }

               float g = be;
               float bd;
               if (k == 0) {
                  bd = r;
               } else {
                  bd = u;
               }

               float h = bd;
               float ba;
               if (l == 0) {
                  ba = s;
               } else {
                  ba = v;
               }

               float m = ba;
               float bc;
               if (j == 0) {
                  bc = z;
               } else {
                  bc = z;
               }

               float n = bc;
               float bb;
               if (k == 0) {
                  bb = z;
               } else {
                  bb = z;
               }

               float o = bb;
               float bf;
               if (l == 0) {
                  bf = z;
               } else {
                  bf = z;
               }

               float p = bf;
               float bh = g + n;
               methodA(vertexConsumer, pose, g, h, m, bh, h, m, n, 0.0F, 0.0F, i, f);
               float bi = h + o;
               methodA(vertexConsumer, pose, g, h, m, g, bi, m, 0.0F, o, 0.0F, i, f);
               float bj = m + p;
               methodA(vertexConsumer, pose, g, h, m, g, h, bj, 0.0F, 0.0F, p, i, f);
            }
         }
      }
   }

   private static void methodA(PoseStack poseStack, VertexConsumer vertexConsumer, Vec3 vec3, Vec3 vec31, int i, float f) {
      Pose pose = poseStack.last();
      float g = (float)vec3.x;
      float h = (float)vec3.y;
      float j = (float)vec3.z;
      float k = (float)vec31.x;
      float l = (float)vec31.y;
      float m = (float)vec31.z;
      float n = k - g;
      float q = l - h;
      float t = m - j;
      Vector3f vector3f = new Vector3f(n, q, t);
      Vector3f vector3f1 = vector3f.normalize();
      VertexConsumer vertexConsumer1 = vertexConsumer.addVertex(pose, g, h, j).setColor(i);
      float o = vector3f1.x;
      float r = vector3f1.y;
      float u = vector3f1.z;
      vertexConsumer1.setNormal(pose, o, r, u).setLineWidth(f);
      VertexConsumer vertexConsumer2 = vertexConsumer.addVertex(pose, k, l, m).setColor(i);
      float p = vector3f1.x;
      float s = vector3f1.y;
      float v = vector3f1.z;
      vertexConsumer2.setNormal(pose, p, s, v).setLineWidth(f);
   }

   private static void methodA(
      VertexConsumer vertexConsumer, Pose pose, float f, float g, float h, float i, float j, float k, float l, float m, float n, int o, float p
   ) {
      vertexConsumer.addVertex(pose, f, g, h).setColor(o).setNormal(pose, l, m, n).setLineWidth(p);
      vertexConsumer.addVertex(pose, i, j, k).setColor(o).setNormal(pose, l, m, n).setLineWidth(p);
   }

   static {
      Snippet[] snippets = new Snippet[2];
      Snippet snippet = RenderPipelines.MATRICES_FOG_SNIPPET;
      snippets[0] = snippet;
      Snippet snippet1 = RenderPipelines.GLOBALS_SNIPPET;
      snippets[1] = snippet1;
      Builder builder = RenderPipeline.builder(snippets);
      Identifier identifier = Identifier.parse("clarity:core/fogless_lines");
      Builder builder1 = builder.withVertexShader(identifier);
      Identifier identifier1 = Identifier.parse("clarity:core/fogless_lines");
      Builder builder2 = builder1.withFragmentShader(identifier1);
      BlendFunction blendFunction = BlendFunction.TRANSLUCENT;
      ColorTargetState colorTargetState = new ColorTargetState(blendFunction);
      Builder builder3 = builder2.withColorTargetState(colorTargetState).withCull(false);
      VertexFormat vertexFormat = DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH;
      Mode mode = Mode.LINES;
      snippetK = builder3.withVertexFormat(vertexFormat, mode).buildSnippet();
      Snippet[] snippets1 = new Snippet[1];
      Snippet snippet2 = RenderPipelines.DEBUG_FILLED_SNIPPET;
      snippets1[0] = snippet2;
      Builder builder4 = RenderPipeline.builder(snippets1);
      Identifier identifier2 = Identifier.parse("clarity:pipeline/esp_filled");
      Builder builder5 = builder4.withLocation(identifier2);
      Optional optional = Optional.empty();
      renderPipelineA = RenderPipelines.register(builder5.withDepthStencilState(optional).withCull(true).build());
      Snippet[] snippets2 = new Snippet[1];
      Snippet snippet3 = snippetK;
      snippets2[0] = snippet3;
      Builder builder6 = RenderPipeline.builder(snippets2);
      Identifier identifier3 = Identifier.parse("clarity:pipeline/esp_lines");
      renderPipelineB = RenderPipelines.register(builder6.withLocation(identifier3).build());
      RenderSetupBuilder renderSetupBuilder = RenderSetup.builder(renderPipelineA);
      LayeringTransform layeringTransform = LayeringTransform.VIEW_OFFSET_Z_LAYERING;
      RenderSetupBuilder renderSetupBuilder1 = renderSetupBuilder.setLayeringTransform(layeringTransform);
      OutputTarget outputTarget = OutputTarget.ITEM_ENTITY_TARGET;
      RenderSetup renderSetup = renderSetupBuilder1.setOutputTarget(outputTarget).sortOnUpload().createRenderSetup();
      renderTypeC = RenderType.create("clarity:esp_quads", renderSetup);
      RenderSetupBuilder renderSetupBuilder2 = RenderSetup.builder(renderPipelineB);
      LayeringTransform layeringTransform1 = LayeringTransform.VIEW_OFFSET_Z_LAYERING;
      RenderSetupBuilder renderSetupBuilder3 = renderSetupBuilder2.setLayeringTransform(layeringTransform1);
      OutputTarget outputTarget1 = OutputTarget.ITEM_ENTITY_TARGET;
      RenderSetup renderSetup1 = renderSetupBuilder3.setOutputTarget(outputTarget1).createRenderSetup();
      renderTypeD = RenderType.create("clarity:esp_outline", renderSetup1);
      CompareOp compareOp = CompareOp.LESS_THAN_OR_EQUAL;
      DepthStencilState depthStencilState = new DepthStencilState(compareOp, false);
      depthStencilStateL = depthStencilState;
      Snippet[] snippets3 = new Snippet[1];
      Snippet snippet4 = RenderPipelines.DEBUG_FILLED_SNIPPET;
      snippets3[0] = snippet4;
      Builder builder7 = RenderPipeline.builder(snippets3);
      Identifier identifier4 = Identifier.parse("clarity:pipeline/esp_filled_occluded");
      Builder builder8 = builder7.withLocation(identifier4);
      DepthStencilState depthStencilState1 = depthStencilStateL;
      renderPipelineE = RenderPipelines.register(builder8.withDepthStencilState(depthStencilState1).withCull(true).build());
      Snippet[] snippets4 = new Snippet[1];
      Snippet snippet5 = snippetK;
      snippets4[0] = snippet5;
      Builder builder9 = RenderPipeline.builder(snippets4);
      Identifier identifier5 = Identifier.parse("clarity:pipeline/esp_lines_occluded");
      Builder builder10 = builder9.withLocation(identifier5);
      DepthStencilState depthStencilState2 = depthStencilStateL;
      renderPipelineF = RenderPipelines.register(builder10.withDepthStencilState(depthStencilState2).build());
      RenderSetupBuilder renderSetupBuilder4 = RenderSetup.builder(renderPipelineE);
      LayeringTransform layeringTransform2 = LayeringTransform.VIEW_OFFSET_Z_LAYERING;
      RenderSetupBuilder renderSetupBuilder5 = renderSetupBuilder4.setLayeringTransform(layeringTransform2);
      OutputTarget outputTarget2 = OutputTarget.ITEM_ENTITY_TARGET;
      RenderSetup renderSetup2 = renderSetupBuilder5.setOutputTarget(outputTarget2).sortOnUpload().createRenderSetup();
      renderTypeG = RenderType.create("clarity:esp_quads_occluded", renderSetup2);
      RenderSetupBuilder renderSetupBuilder6 = RenderSetup.builder(renderPipelineF);
      LayeringTransform layeringTransform3 = LayeringTransform.VIEW_OFFSET_Z_LAYERING;
      RenderSetupBuilder renderSetupBuilder7 = renderSetupBuilder6.setLayeringTransform(layeringTransform3);
      OutputTarget outputTarget3 = OutputTarget.ITEM_ENTITY_TARGET;
      RenderSetup renderSetup3 = renderSetupBuilder7.setOutputTarget(outputTarget3).createRenderSetup();
      renderTypeH = RenderType.create("clarity:esp_outline_occluded", renderSetup3);
      Snippet[] snippets5 = new Snippet[1];
      Snippet snippet6 = snippetK;
      snippets5[0] = snippet6;
      Builder builder11 = RenderPipeline.builder(snippets5);
      Identifier identifier6 = Identifier.parse("clarity:pipeline/tracer_lines");
      Builder builder12 = builder11.withLocation(identifier6);
      DepthStencilState depthStencilState3 = DepthStencilState.DEFAULT;
      renderPipelineI = RenderPipelines.register(builder12.withDepthStencilState(depthStencilState3).build());
      RenderSetupBuilder renderSetupBuilder8 = RenderSetup.builder(renderPipelineI);
      LayeringTransform layeringTransform4 = LayeringTransform.VIEW_OFFSET_Z_LAYERING;
      RenderSetupBuilder renderSetupBuilder9 = renderSetupBuilder8.setLayeringTransform(layeringTransform4);
      OutputTarget outputTarget4 = OutputTarget.ITEM_ENTITY_TARGET;
      RenderSetup renderSetup4 = renderSetupBuilder9.setOutputTarget(outputTarget4).createRenderSetup();
      renderTypeJ = RenderType.create("clarity:tracer_lines", renderSetup4);
   }
}
