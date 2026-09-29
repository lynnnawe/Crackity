package need.clarity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import need.clarity.modules.visual.ChinaHat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector.CustomGeometryRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

public class ChinaHatLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
   private static final Minecraft minecraft = Minecraft.getInstance();

   public ChinaHatLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderLayerParent) {
      super(renderLayerParent);
   }

   public void methodA(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int o, AvatarRenderState avatarRenderState, float p, float q) {
      ChinaHat chinaHat = ChinaHat.instance;
      if (chinaHat != null) {
         if (chinaHat.isEnabled()) {
            if (minecraft.player != null) {
               int i = avatarRenderState.id;
               int n = minecraft.player.getId();
               if (i == n) {
                  poseStack.pushPose();
                  ((PlayerModel)this.getParentModel()).getHead().translateAndRotate(poseStack);
                  LocalPlayer localPlayer = minecraft.player;
                  EquipmentSlot equipmentSlot = EquipmentSlot.HEAD;
                  boolean bl;
                  if (!localPlayer.getItemBySlot(equipmentSlot).isEmpty()) {
                     bl = true;
                  } else {
                     bl = false;
                  }

                  float m;
                  if (bl) {
                     m = -0.78F;
                  } else {
                     m = -0.72F;
                  }

                  float f = m;
                  poseStack.translate(0.0F, f, 0.0F);
                  float g = chinaHat.getRotationSpeedSetting();
                  if (g != 0.0F) {
                     float h = (float)(System.currentTimeMillis() % 360000L) / 1000.0F * g * 360.0F;
                     Quaternionf quaternionf = Axis.YP.rotationDegrees(h);
                     poseStack.mulPose(quaternionf);
                  }

                  float j = chinaHat.getCenterAlphaSetting();
                  float k = chinaHat.getEdgeAlphaSetting();
                  int[] l = chinaHat.getColorCountSetting();
                  RenderType renderType = ChinaHatRenderTypes.renderTypeA;
                  CustomGeometryRenderer customGeometryRenderer = (pose, vertexConsumer) -> methodA(pose.pose(), vertexConsumer, j, k, l);
                  submitNodeCollector.submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
                  RenderType renderType1 = ChinaHatRenderTypes.renderTypeB;
                  CustomGeometryRenderer customGeometryRenderer1 = (pose, vertexConsumer) -> methodA(pose.pose(), vertexConsumer, k, l);
                  submitNodeCollector.submitCustomGeometry(poseStack, renderType1, customGeometryRenderer1);
                  poseStack.popPose();
               }
            }
         }
      }
   }

   private static void methodA(Matrix4f matrix4f, VertexConsumer vertexConsumer, float f, float g, int[] i) {
      for (byte b = 0; b < 360; b += 9) {
         float x = b * (float) (Math.PI / 180.0);
         float y = (b + 9) * (float) (Math.PI / 180.0);
         float z = Mth.sin(x) * 0.65F;
         float ba = Mth.cos(x) * 0.65F;
         float bb = Mth.sin(y) * 0.65F;
         float bc = Mth.cos(y) * 0.65F;
         int bi;
         if (i == null) {
            bi = methodA();
         } else {
            bi = methodA(b, i);
         }

         int bd = methodA(bi, g);
         int bh;
         if (i == null) {
            bh = methodA();
         } else {
            bh = methodA(b + 9, i);
         }

         int be = methodA(bh, g);
         int bf;
         if (i == null) {
            bf = methodA();
         } else {
            bf = methodA(b, i);
         }

         int bg = methodA(bf, f);
         vertexConsumer.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(bg);
         vertexConsumer.addVertex(matrix4f, z, 0.3F, ba).setColor(bd);
         vertexConsumer.addVertex(matrix4f, bb, 0.3F, bc).setColor(be);
      }

      for (byte c = 0; c < 360; c += 9) {
         float h = c * (float) (Math.PI / 180.0);
         float m = (c + 9) * (float) (Math.PI / 180.0);
         float n = Mth.sin(h) * 0.65F;
         float o = Mth.cos(h) * 0.65F;
         float p = Mth.sin(m) * 0.65F;
         float q = Mth.cos(m) * 0.65F;
         int r;
         if (i == null) {
            r = methodA();
         } else {
            r = methodA(c, i);
         }

         int j = r;
         int w;
         if (i == null) {
            w = methodA();
         } else {
            w = methodA(c + 9, i);
         }

         int k = w;
         int v;
         if (i == null) {
            v = methodA();
         } else {
            v = methodA(c, i);
         }

         int l = v;
         int s = methodA(methodA(j, 4), g);
         int t = methodA(methodA(k, 4), g);
         int u = methodA(methodA(l, 1), f);
         vertexConsumer.addVertex(matrix4f, p, 0.3F, q).setColor(t);
         vertexConsumer.addVertex(matrix4f, n, 0.3F, o).setColor(s);
         vertexConsumer.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(u);
      }
   }

   private static void methodA(Matrix4f matrix4f, VertexConsumer vertexConsumer, float q, int[] i) {
      Screen screen = minecraft.screen;
      if (!(screen instanceof CreativeModeInventoryScreen)) {
         if (!(screen instanceof InventoryScreen)) {
            for (byte b = 0; b < 360; b += 9) {
               float f = Mth.sin(b * (float) (Math.PI / 180.0)) * 0.65F;
               float g = Mth.cos(b * (float) (Math.PI / 180.0)) * 0.65F;
               float h = Mth.sin((b + 9) * (float) (Math.PI / 180.0)) * 0.65F;
               float l = Mth.cos((b + 9) * (float) (Math.PI / 180.0)) * 0.65F;
               int p;
               if (i == null) {
                  p = methodA();
               } else {
                  p = methodA(b, i);
               }

               int j = p;
               int o;
               if (i == null) {
                  o = methodA();
               } else {
                  o = methodA(b + 9, i);
               }

               int k = o;
               int m = methodA(j) | 0xFF000000;
               int n = methodA(k) | 0xFF000000;
               methodA(vertexConsumer, matrix4f, f, 0.3F, g, h, 0.3F, l, m, n, 4.0F);
            }
         }
      }
   }

   private static void methodA(VertexConsumer vertexConsumer, Matrix4f matrix4f, float f, float g, float h, float i, float j, float k, int l, int m, float n) {
      Vector4f vector4f = new Vector4f(f, g, h, 1.0F);
      Vector4f vector4f1 = vector4f.mul(matrix4f);
      Vector4f vector4f2 = new Vector4f(i, j, k, 1.0F);
      Vector4f vector4f3 = vector4f2.mul(matrix4f);
      float s = vector4f3.x;
      float bx = vector4f1.x;
      float t = s - bx;
      float u = vector4f3.y;
      float by = vector4f1.y;
      float v = u - by;
      float w = vector4f3.z;
      float bz = vector4f1.z;
      float x = w - bz;
      float y = t * t;
      float ca = v * v;
      float z = y + ca;
      float cb = x * x;
      if (!(z + cb <= 1.0E-6F)) {
         float ba = vector4f1.x;
         float cc = vector4f3.x;
         float bb = (ba + cc) * 0.5F;
         float bc = vector4f1.y;
         float cd = vector4f3.y;
         float bd = (bc + cd) * 0.5F;
         float be = vector4f1.z;
         float ce = vector4f3.z;
         float bf = (be + ce) * 0.5F;
         float bg = v * bf;
         float cf = x * bd;
         float o = bg - cf;
         float bh = x * bb;
         float cg = t * bf;
         float p = bh - cg;
         float bi = t * bd;
         float ch = v * bb;
         float q = bi - ch;
         float bj = o;
         float ci = o;
         float bk = bj * ci;
         float cj = p;
         float cw = p;
         float ck = cj * cw;
         float bl = bk + ck;
         float cl = q;
         float cx = q;
         float cm = cl * cx;
         float r = bl + cm;
         if (r <= 1.0E-6F) {
            o = x;
            p = 0.0F;
            q = t;
            float bm = o;
            float cn = o;
            float bn = bm * cn;
            float co = q;
            float cy = q;
            float cp = co * cy;
            r = bn + cp;
            if (r <= 1.0E-6F) {
               return;
            }
         }

         float cq = bb * bb;
         float cz = bd * bd;
         float cr = cq + cz;
         float da = bf * bf;
         float cs = (float)Math.sqrt(cr + da);
         float bo = Math.max(0.25F, cs);
         float ct = minecraft.getWindow().getHeight();
         float bp = Math.max(1.0F, ct);
         double db = ((Integer)minecraft.options.fov().get()).intValue();
         double d = Math.max(1.0, db);
         double dc = bo;
         double e = 2.0 * dc;
         double dd = Math.tan(Math.toRadians(d * 0.5));
         double bq = e * dd;
         double de = bp;
         float br = (float)(bq / de);
         float cu = Math.max(1.0F, n) * br * 0.5F;
         float bs = Math.max(5.0E-4F, cu);
         float cv = (float)Math.sqrt(r);
         float bt = bs / cv;
         float bu = o * bt;
         float bv = p * bt;
         float bw = q * bt;
         Matrix4f matrix4f1 = new Matrix4f();
         float df = vector4f1.x - bu;
         float dj = vector4f1.y - bv;
         float dn = vector4f1.z - bw;
         vertexConsumer.addVertex(matrix4f1, df, dj, dn).setColor(l);
         float dg = vector4f1.x + bu;
         float dk = vector4f1.y + bv;
         float do_xx = vector4f1.z + bw;
         vertexConsumer.addVertex(matrix4f1, dg, dk, do_xx).setColor(l);
         float dh = vector4f3.x + bu;
         float dl = vector4f3.y + bv;
         float do_x = vector4f3.z + bw;
         vertexConsumer.addVertex(matrix4f1, dh, dl, do_x).setColor(m);
         float di = vector4f3.x - bu;
         float dm = vector4f3.y - bv;
         float do_xx = vector4f3.z - bw;
         vertexConsumer.addVertex(matrix4f1, di, dm, do_xx).setColor(m);
      }
   }

   private static int methodA(float f, int[] i) {
      float g = (f % 360.0F + 360.0F) % 360.0F;
      float p = i.length;
      float h = 360.0F / p;
      int j = (int)(g / h);
      int q = i.length;
      int k = j % q;
      int l = k + 1;
      int r = i.length;
      int m = l % r;
      float n = g % h / h;
      int o = i[k];
      int s = i[m];
      return methodA(o, s, n);
   }

   private static int methodA(int i, int j, float f) {
      float g = i >> 24 & 0xFF;
      int s = j >> 24 & 0xFF;
      int bc = i >> 24 & 0xFF;
      float t = (s - bc) * f;
      int k = (int)(g + t);
      float h = i >> 16 & 0xFF;
      int u = j >> 16 & 0xFF;
      int bd = i >> 16 & 0xFF;
      float v = (u - bd) * f;
      int l = (int)(h + v);
      float m = i >> 8 & 0xFF;
      int w = j >> 8 & 0xFF;
      int be = i >> 8 & 0xFF;
      float x = (w - be) * f;
      int n = (int)(m + x);
      float o = i & 0xFF;
      int y = j & 0xFF;
      int bf = i & 0xFF;
      float z = (y - bf) * f;
      int p = (int)(o + z);
      int q = k << 24;
      int ba = l << 16;
      int r = q | ba;
      int bb = n << 8;
      return r | bb | p;
   }

   private static int methodA(int i, float f) {
      int j = i & 16777215;
      int k = (int)(f * 255.0F) << 24;
      return j | k;
   }

   private static int methodA(int i, int j) {
      int k = i & 0xFF000000;
      int n = (i >> 16 & 0xFF) / j << 16;
      int l = k | n;
      int o = (i >> 8 & 0xFF) / j << 8;
      int m = l | o;
      int p = (i & 0xFF) / j;
      return m | p;
   }

   private static int methodA(int i) {
      return methodA(i, 2);
   }

   private static int methodA() {
      return -16714844;
   }

   // $VF: synthetic method
   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, EntityRenderState entityRenderState, float f, float g) {
      AvatarRenderState avatarRenderState = (AvatarRenderState)entityRenderState;
      this.methodA(poseStack, submitNodeCollector, i, avatarRenderState, f, g);
   }
}
