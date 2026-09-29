package need.clarity.modules.visual;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.ModuleRegistry;
import need.clarity.render.NanoVGTextRenderer;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

public class ArrayList extends ModuleBase {
   public static ArrayList instance;
   private static final NVGColor nVGColorB = NVGColor.create();
   private static final NVGColor nVGColorC = NVGColor.create();
   private static final Map<String, Float> mapD;
   private static final Map<String, Float> mapE;
   private static final Map<String, Float> mapF;
   private static final Map<String, Float> mapG;
   private static Set<String> setH;
   private static float floatI = 0.0F;
   private static final Map<String, Float> mapJ;
   private static final Map<String, Float> mapK;
   private static float floatL = -1.0F;
   private static final LinkedList<String> linkedListM;
   private static float floatN = 0.0F;
   private static long longO = 0L;

   public ArrayList() {
      super("ArrayList", "Visual");
      instance = this;
   }

   public static void methodA(long l, float f, float do_) {
      ArrayList arrayList = instance;
      if (arrayList != null) {
         String string = arrayList.getSetting("Side", "Right");
         boolean bl8;
         if (!"Left".equalsIgnoreCase(string)) {
            bl8 = true;
         } else {
            bl8 = false;
         }

         boolean bl;
         float r;
         int s;
         int t;
         float u;
         float v;
         float w;
         float x;
         String string1;
         float y;
         boolean bl2;
         boolean bl9;
         label282: {
            bl = bl8;
            Float float27 = 1.0F;
            float dd = arrayList.getSetting("Scale", float27);
            r = Math.max(0.1F, dd);
            Integer integer = 2;
            s = arrayList.getSetting("Margin X", integer);
            Integer integer1 = 2;
            t = arrayList.getSetting("Margin Y", integer1);
            Float float1 = 13.0F;
            u = arrayList.getSetting("Row Height", float1);
            Float float2 = 12.0F;
            v = arrayList.getSetting("H Padding", float2);
            Float float3 = 7.0F;
            w = arrayList.getSetting("Text Size", float3);
            Float float4 = 0.22F;
            x = methodA(arrayList.getSetting("Anim Speed", float4), 0.01F, 1.0F);
            string1 = arrayList.getSetting("Animation", "Slide");
            y = u * 9.0F / 64.0F;
            String string2 = arrayList.getSetting("Background", "Solid");
            "Outline Only".equalsIgnoreCase(string2);
            bl2 = "None".equalsIgnoreCase(string2);
            if ("Gradient".equalsIgnoreCase(string2)) {
               Boolean boolean1 = false;
               if (arrayList.getSetting("Gradient", boolean1)) {
                  bl9 = true;
                  break label282;
               }
            }

            bl9 = false;
         }

         boolean bl1 = bl9;
         Integer integer2 = 82;
         float z = methodA(arrayList.getSetting("BG Opacity", integer2).intValue(), 0.0F, 100.0F) / 100.0F;
         Float float5 = 1.0F;
         float ba = arrayList.getSetting("Gradient Speed", float5);
         Float float6 = 0.8F;
         methodA(arrayList.getSetting("Gradient Saturation", float6), 0.0F, 1.0F);
         Float float7 = 1.0F;
         arrayList.getSetting("Outline Width", float7);
         Integer integer3 = 100;
         float bb = methodA(arrayList.getSetting("Text Opacity", integer3).intValue(), 0.0F, 100.0F) / 100.0F;
         Boolean boolean2 = false;
         boolean bl3 = arrayList.getSetting("Shadow", boolean2);
         Integer integer4 = 60;
         float bc = methodA(arrayList.getSetting("Shadow Opacity", integer4).intValue(), 0.0F, 100.0F) / 100.0F;
         Boolean boolean3 = false;
         arrayList.getSetting("Accent Bar", boolean3);
         Float float8 = 2.0F;
         arrayList.getSetting("Accent Width", float8);
         Boolean boolean4 = false;
         boolean bl4 = arrayList.getSetting("Show Suffix", boolean4);
         Integer integer5 = 55;
         float bd = methodA(arrayList.getSetting("Suffix Opacity", integer5).intValue(), 0.0F, 100.0F) / 100.0F;
         int be = ThemeColors.getAccentColor();
         int bf = methodA(1448742, be, 0.08F);
         int bg = methodA(2765122, be, 0.25F);
         Object object = arrayList.getSetting("BG Color", null);
         int de = 0xFF000000 | bf;
         ThemeColors.resolveArgb(object, de, true);
         Object object1 = arrayList.getSetting("Outline Color", null);
         int df = 0xFF000000 | bg;
         ThemeColors.resolveArgb(object1, df, false);
         ThemeColors.resolveArgb(arrayList.getSetting("Text Color", null), -1184272, false);
         int bh = ThemeColors.resolveArgb(arrayList.getSetting("Shadow Color", null), -16777216, false);
         Object object2 = arrayList.getSetting("Accent Color", null);
         int dg = 0xFF000000 | be;
         ThemeColors.resolveArgb(object2, dg, false);
         long bi = System.currentTimeMillis();
         if (bl1 && longO > 0L) {
            long do_xxxxxxxxxxxxxxxxx = longO;
            float bj = Math.min((float)(bi - do_xxxxxxxxxxxxxxxxx) / 1000.0F, 0.1F);
            float bk = floatN;
            float dh = ba * 54.0F * bj;
            floatN = (bk + dh) % 360.0F;
         }

         longO = bi;
         List list = ModuleRegistry.getModules();
         int do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = list.size();
         java.util.ArrayList arrayList1 = new java.util.ArrayList(do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
         int i = 0;

         while (true) {
            int bm = i;
            int di = list.size();
            if (bm >= di) {
               if (arrayList1.isEmpty()) {
                  methodA(mapD, x);
                  linkedListM.clear();
                  return;
               }

               float dk = floatL;
               if (w != dk) {
                  mapJ.clear();
                  mapK.clear();
                  floatL = w;
               }

               NanoVG.nvgFontFace(l, "inter-sb");
               NanoVG.nvgTextLetterSpacing(l, 0.0F);
               NanoVG.nvgTextAlign(l, 9);
               byte do_x = 0;

               while (true) {
                  byte b = do_x;
                  int dl = arrayList1.size();
                  if (b >= dl) {
                     Comparator comparator = (moduleBase2, moduleBase3) -> {
                        float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = methodA(moduleBase3.getName(), bl4);
                        float do_x = methodA(moduleBase2.getName(), bl4);
                        return Float.compare(do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, do_x);
                     };
                     arrayList1.sort(comparator);
                     byte do_xx = 0;

                     while (true) {
                        byte c = do_xx;
                        int dn = arrayList1.size();
                        if (c >= dn) {
                           LinkedList linkedList = linkedListM;
                           Predicate predicate = string23 -> {
                              boolean bl11;
                              if (!methodA(arrayList1, string23)) {
                                 bl11 = true;
                              } else {
                                 bl11 = false;
                              }

                              return bl11;
                           };
                           linkedList.removeIf(predicate);
                           String string12;
                           if (linkedListM.isEmpty()) {
                              string12 = null;
                           } else {
                              string12 = linkedListM.getFirst();
                           }

                           String string22 = string12;
                           byte do_xxx = 0;

                           while (true) {
                              byte d = do_xxx;
                              int do_xxxx = arrayList1.size();
                              if (d >= do_xxxx) {
                                 boolean bl5 = "None".equalsIgnoreCase(string1);
                                 boolean bl6 = "Fade".equalsIgnoreCase(string1);
                                 boolean bl7;
                                 if (!bl5 && !bl6) {
                                    bl7 = true;
                                 } else {
                                    bl7 = false;
                                 }

                                 boolean bl10 = bl7;
                                 byte do_xxxxx = 0;

                                 while (true) {
                                    byte e = do_xxxxx;
                                    int do_xxxxxx = arrayList1.size();
                                    if (e >= do_xxxxxx) {
                                       HashSet hashSet = new HashSet();
                                       byte do_xxxxxxx = 0;

                                       while (true) {
                                          byte br = do_xxxxxxx;
                                          int do_xxxxxxxx = arrayList1.size();
                                          if (br >= do_xxxxxxxx) {
                                             if (!bl5) {
                                                for (String string11 : setH) {
                                                   if (!hashSet.contains(string11)) {
                                                      Map map17 = mapG;
                                                      Float float26 = 1.0F;
                                                      map17.putIfAbsent(string11, float26);
                                                   }
                                                }
                                             }

                                             setH = hashSet;
                                             Set set = mapG.keySet();
                                             String[] strings3 = new String[0];
                                             String[] strings = set.toArray(strings3);
                                             int bs = strings.length;

                                             for (int k = 0; k < bs; k++) {
                                                int do_xxxxxxxxx = k;
                                                String string10 = strings[do_xxxxxxxxx];
                                                float cw = mapG.get(string10);
                                                float do_xxxxxxxxxx = x * 1.4F;
                                                float do_xxxxxxxxxxx = 1.0F - do_xxxxxxxxxx;
                                                float cx = cw * do_xxxxxxxxxxx;
                                                if (cx < 0.03F) {
                                                   mapG.remove(string10);
                                                   mapD.remove(string10);
                                                   mapJ.remove(string10);
                                                   mapK.remove(string10);
                                                   mapE.remove(string10);
                                                   mapF.remove(string10);
                                                } else {
                                                   Map map16 = mapG;
                                                   Float float25 = cx;
                                                   map16.put(string10, float25);
                                                }
                                             }

                                             Set set1 = mapD.keySet();
                                             String[] strings4 = new String[0];
                                             String[] strings1 = set1.toArray(strings4);
                                             int bt = strings1.length;

                                             for (byte do_xxxxxxxxx = 0; do_xxxxxxxxx < bt; do_xxxxxxxxx += 1) {
                                                byte do_xxxxxxxxxx = do_xxxxxxxxx;
                                                String string9 = strings1[do_xxxxxxxxxx];
                                                if (!mapG.containsKey(string9) && (!mapJ.containsKey(string9) || !methodA(arrayList1, string9))) {
                                                   float cu = mapD.get(string9);
                                                   float do_xxxxxxxxxxx = 1.0F - x;
                                                   float cv = cu * do_xxxxxxxxxxx;
                                                   if (cv < 0.5F) {
                                                      mapD.remove(string9);
                                                      mapJ.remove(string9);
                                                      mapK.remove(string9);
                                                      mapE.remove(string9);
                                                   } else {
                                                      Map map15 = mapD;
                                                      Float float24 = cv;
                                                      map15.put(string9, float24);
                                                   }
                                                }
                                             }

                                             NanoVG.nvgSave(l);
                                             NanoVG.nvgScale(l, r, r);
                                             float do_xxxxxxxxx = t / r;
                                             NanoVG.nvgTranslate(l, 0.0F, do_xxxxxxxxx);
                                             float bu = f / r;
                                             float dc;
                                             if (bl) {
                                                float do_xxxxxxxxxx = s / r;
                                                dc = bu - do_xxxxxxxxxx;
                                             } else {
                                                dc = s / r;
                                             }

                                             float do_xxxxxxxxxx = dc;
                                             int bv = methodA(1448742, be, 0.08F);
                                             int bw = methodA(2765122, be, 0.4F);
                                             float do_xxxxxxxxxxx = v * 0.45F;
                                             float bx = Math.max(3.0F, do_xxxxxxxxxxx);
                                             float do_xxxxxxxxxxxx = v * 0.55F;
                                             float by = Math.max(3.0F, do_xxxxxxxxxxxx);
                                             int m = 0;
                                             int n = 0;

                                             while (true) {
                                                int bz = n;
                                                int do_xxxxxxxxxxxxx = arrayList1.size();
                                                if (bz >= do_xxxxxxxxxxxxx) {
                                                   Set set2 = mapG.keySet();
                                                   String[] strings5 = new String[0];
                                                   String[] strings2 = set2.toArray(strings5);
                                                   int ca = strings2.length;

                                                   for (int o = 0; o < ca; o++) {
                                                      int do_xxxxxxxxxxxxxx = o;
                                                      String string7 = strings2[do_xxxxxxxxxxxxxx];
                                                      Map map8 = mapD;
                                                      Float float17 = 0.0F;
                                                      float cb = map8.getOrDefault(string7, float17);
                                                      if (!(cb < 1.0F)) {
                                                         Map map9 = mapG;
                                                         Float float18 = 0.0F;
                                                         float cc = map9.getOrDefault(string7, float18);
                                                         if (!(cc < 0.03F)) {
                                                            Map map10 = mapE;
                                                            Float float19 = 0.0F;
                                                            float cd = map10.getOrDefault(string7, float19);
                                                            float db;
                                                            if (bl6) {
                                                               db = cc;
                                                            } else {
                                                               db = 1.0F;
                                                            }

                                                            float p = db;
                                                            float da;
                                                            if (bl10) {
                                                               float ce = (1.0F - cc) * cb;
                                                               float do_xxxxxxxxxxxxxxx;
                                                               if (bl) {
                                                                  do_xxxxxxxxxxxxxxx = 1.0F;
                                                               } else {
                                                                  do_xxxxxxxxxxxxxxx = -1.0F;
                                                               }

                                                               da = ce * do_xxxxxxxxxxxxxxx;
                                                            } else {
                                                               da = 0.0F;
                                                            }

                                                            float q = da;
                                                            float cf;
                                                            if (bl) {
                                                               cf = do_xxxxxxxxxx - cb;
                                                            } else {
                                                               cf = do_xxxxxxxxxx;
                                                            }

                                                            float cg = cf + q;
                                                            if (!bl2) {
                                                               NVGColor nVGColor = nVGColorB;
                                                               float do_xxxxxxxxxxxxxxx = z * p;
                                                               methodA(nVGColor, bv, do_xxxxxxxxxxxxxxx);
                                                               NanoVG.nvgBeginPath(l);
                                                               NanoVG.nvgRoundedRect(l, cg, cd, cb, u, y);
                                                               NVGColor nVGColor8 = nVGColorB;
                                                               NanoVG.nvgFillColor(l, nVGColor8);
                                                               NanoVG.nvgFill(l);
                                                            }

                                                            NVGColor nVGColor1 = nVGColorB;
                                                            float do_xxxxxxxxxxxxxxx = 0.9F * p;
                                                            methodA(nVGColor1, bw, do_xxxxxxxxxxxxxxx);
                                                            NanoVG.nvgBeginPath(l);
                                                            float do_xxxxxxxxxxxxxxxx = cg + 0.5F;
                                                            float do_xxxxxxxxxxxxxxxxx = cd + 0.5F;
                                                            float do_xxxxxxxxxxxxxxxxxx = cb - 1.0F;
                                                            float do_xxxxxxxxxxxxxxxxxxx = Math.max(0.0F, do_xxxxxxxxxxxxxxxxxx);
                                                            float do_xxxxxxxxxxxxxxxxxxxx = u - 1.0F;
                                                            float do_xxxxxxxxxxxxxxxxxxxxx = Math.max(0.0F, do_xxxxxxxxxxxxxxxxxxxx);
                                                            float do_xxxxxxxxxxxxxxxxxxxxxx = y - 0.5F;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxx = Math.max(0.0F, do_xxxxxxxxxxxxxxxxxxxxxx);
                                                            NanoVG.nvgRoundedRect(
                                                               l,
                                                               do_xxxxxxxxxxxxxxxx,
                                                               do_xxxxxxxxxxxxxxxxx,
                                                               do_xxxxxxxxxxxxxxxxxxx,
                                                               do_xxxxxxxxxxxxxxxxxxxxx,
                                                               do_xxxxxxxxxxxxxxxxxxxxxxx
                                                            );
                                                            NVGColor nVGColor9 = nVGColorB;
                                                            NanoVG.nvgStrokeColor(l, nVGColor9);
                                                            NanoVG.nvgStrokeWidth(l, 1.0F);
                                                            NanoVG.nvgStroke(l);
                                                            NVGColor nVGColor2 = nVGColorB;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxxx = bb * p;
                                                            methodA(nVGColor2, -1184272, do_xxxxxxxxxxxxxxxxxxxxxxxx);
                                                            NVGColor nVGColor12 = nVGColorB;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxxxx = cg + bx;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxxxxx = u * 0.5F;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxxxxxx = cd + do_xxxxxxxxxxxxxxxxxxxxxxxxxx;
                                                            NanoVGTextRenderer.methodA(
                                                               l,
                                                               "inter-sb",
                                                               w,
                                                               0.0F,
                                                               nVGColor12,
                                                               17,
                                                               do_xxxxxxxxxxxxxxxxxxxxxxxxx,
                                                               do_xxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                                               string7
                                                            );
                                                         }
                                                      }
                                                   }

                                                   NanoVG.nvgRestore(l);
                                                   return;
                                                }

                                                int do_xxxxxxxxxxxxxx = n;
                                                ModuleBase moduleBase1 = (ModuleBase)arrayList1.get(do_xxxxxxxxxxxxxx);
                                                Map map11 = mapD;
                                                String string13 = moduleBase1.getName();
                                                Float float20 = 0.0F;
                                                float ch = map11.getOrDefault(string13, float20);
                                                float ci = n;
                                                float do_xxxxxxxxxxxxxxx = u + 2.0F;
                                                float cj = ci * do_xxxxxxxxxxxxxxx;
                                                Map map12 = mapE;
                                                String string14 = moduleBase1.getName();
                                                Float float21 = cj;
                                                float ck = map12.getOrDefault(string14, float21);
                                                float do_xxxxxxxxxxxxxxxx = (cj - ck) * x;
                                                float cl = ck + do_xxxxxxxxxxxxxxxx;
                                                Map map13 = mapE;
                                                String string15 = moduleBase1.getName();
                                                Float float22 = cl;
                                                map13.put(string15, float22);
                                                if (!(ch < 1.0F)) {
                                                   if (string22 != null) {
                                                      String string16 = moduleBase1.getName();
                                                      if (string22.equals(string16)) {
                                                      }
                                                   }

                                                   Map map14 = mapF;
                                                   String string17 = moduleBase1.getName();
                                                   Float float23 = 1.0F;
                                                   float cm = map14.getOrDefault(string17, float23);
                                                   if (!(cm < 0.03F)) {
                                                      float cy;
                                                      if (bl6) {
                                                         cy = cm;
                                                      } else {
                                                         cy = 1.0F;
                                                      }

                                                      float do_xxxxxxxxxxxxxxxxx = cy;
                                                      float cz;
                                                      if (bl10) {
                                                         float cn = (1.0F - cm) * ch;
                                                         float do_xxxxxxxxxxxxxxxxxx;
                                                         if (bl) {
                                                            do_xxxxxxxxxxxxxxxxxx = 1.0F;
                                                         } else {
                                                            do_xxxxxxxxxxxxxxxxxx = -1.0F;
                                                         }

                                                         cz = cn * do_xxxxxxxxxxxxxxxxxx;
                                                      } else {
                                                         cz = 0.0F;
                                                      }

                                                      float do_xxxxxxxxxxxxxxxxxx = cz;
                                                      float ct;
                                                      if (bl) {
                                                         ct = do_xxxxxxxxxx - ch;
                                                      } else {
                                                         ct = do_xxxxxxxxxx;
                                                      }

                                                      float co = ct + do_xxxxxxxxxxxxxxxxxx;
                                                      if (!bl2) {
                                                         NVGColor nVGColor3 = nVGColorB;
                                                         float do_xxxxxxxxxxxxxxxxxxx = z * do_xxxxxxxxxxxxxxxxx;
                                                         methodA(nVGColor3, bv, do_xxxxxxxxxxxxxxxxxxx);
                                                         NanoVG.nvgBeginPath(l);
                                                         NanoVG.nvgRoundedRect(l, co, cl, ch, u, y);
                                                         NVGColor nVGColor10 = nVGColorB;
                                                         NanoVG.nvgFillColor(l, nVGColor10);
                                                         NanoVG.nvgFill(l);
                                                      }

                                                      NVGColor nVGColor4 = nVGColorB;
                                                      float do_xxxxxxxxxxxxxxxxxxx = 0.9F * do_xxxxxxxxxxxxxxxxx;
                                                      methodA(nVGColor4, bw, do_xxxxxxxxxxxxxxxxxxx);
                                                      NanoVG.nvgBeginPath(l);
                                                      float do_xxxxxxxxxxxxxxxxxxxx = 1.0F * 0.5F;
                                                      float do_xxxxxxxxxxxxxxxxxxxxx = co + do_xxxxxxxxxxxxxxxxxxxx;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxx = 1.0F * 0.5F;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxx = cl + do_xxxxxxxxxxxxxxxxxxxxxx;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxx = ch - 1.0F;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxx = Math.max(0.0F, do_xxxxxxxxxxxxxxxxxxxxxxxx);
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxxx = u - 1.0F;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxxxx = Math.max(0.0F, do_xxxxxxxxxxxxxxxxxxxxxxxxxx);
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxx = 1.0F * 0.5F;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = y - do_xxxxxxxxxxxxxxxxxxxxxxxxxxxx;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = Math.max(0.0F, do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
                                                      NanoVG.nvgRoundedRect(
                                                         l,
                                                         do_xxxxxxxxxxxxxxxxxxxxx,
                                                         do_xxxxxxxxxxxxxxxxxxxxxxx,
                                                         do_xxxxxxxxxxxxxxxxxxxxxxxxx,
                                                         do_xxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                                         do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                      );
                                                      NVGColor nVGColor11 = nVGColorB;
                                                      NanoVG.nvgStrokeColor(l, nVGColor11);
                                                      NanoVG.nvgStrokeWidth(l, 1.0F);
                                                      NanoVG.nvgStroke(l);
                                                      float cp = co + bx;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = u * 0.5F;
                                                      float cq = cl + do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
                                                      if (bl3) {
                                                         methodA(nVGColorB, bh, bc);
                                                         NVGColor nVGColor13 = nVGColorB;
                                                         float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = cp + 1.0F;
                                                         float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = cq + 1.0F;
                                                         String string20 = moduleBase1.getName();
                                                         NanoVGTextRenderer.methodA(
                                                            l,
                                                            "inter-sb",
                                                            w,
                                                            0.0F,
                                                            nVGColor13,
                                                            17,
                                                            do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                                            do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                                            string20
                                                         );
                                                      }

                                                      NVGColor nVGColor5 = nVGColorB;
                                                      float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = bb * do_xxxxxxxxxxxxxxxxx;
                                                      methodA(nVGColor5, -1184272, do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
                                                      NVGColor nVGColor14 = nVGColorB;
                                                      String string21 = moduleBase1.getName();
                                                      NanoVGTextRenderer.methodA(l, "inter-sb", w, 0.0F, nVGColor14, 17, cp, cq, string21);
                                                      if (bl4) {
                                                         String string8 = methodA(moduleBase1.getName());
                                                         float cr = w * 0.8F;
                                                         float cs = co + ch - by;
                                                         if (bl3) {
                                                            NVGColor nVGColor6 = nVGColorB;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = bc * bd;
                                                            methodA(nVGColor6, bh, do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
                                                            NVGColor nVGColor15 = nVGColorB;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = cs + 1.0F;
                                                            float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = cq + 1.0F;
                                                            NanoVGTextRenderer.methodA(
                                                               l,
                                                               "inter-med",
                                                               cr,
                                                               0.0F,
                                                               nVGColor15,
                                                               20,
                                                               do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                                               do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
                                                               string8
                                                            );
                                                         }

                                                         NVGColor nVGColor7 = nVGColorB;
                                                         float do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = bb * bd;
                                                         methodA(nVGColor7, -1184272, do_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx);
                                                         NVGColor nVGColor16 = nVGColorB;
                                                         NanoVGTextRenderer.methodA(l, "inter-med", cr, 0.0F, nVGColor16, 20, cs, cq, string8);
                                                      }

                                                      m++;
                                                   }
                                                }

                                                n++;
                                             }
                                          }

                                          byte do_xxxxxxxxx = do_xxxxxxx;
                                          String string18 = ((ModuleBase)arrayList1.get(do_xxxxxxxxx)).getName();
                                          hashSet.add(string18);
                                          do_xxxxxxx += 1;
                                       }
                                    }

                                    byte do_xxxxxxx = do_xxxxx;
                                    String string6 = ((ModuleBase)arrayList1.get(do_xxxxxxx)).getName();
                                    if (bl5) {
                                       Map map5 = mapF;
                                       Float float14 = 1.0F;
                                       map5.put(string6, float14);
                                    } else {
                                       Map map6 = mapF;
                                       Float float15 = 0.0F;
                                       float bq = map6.getOrDefault(string6, float15);
                                       Map map7 = mapF;
                                       float do_xxxxxxxx = (1.0F - bq) * x;
                                       Float float16 = bq + do_xxxxxxxx;
                                       map7.put(string6, float16);
                                    }

                                    do_xxxxx += 1;
                                 }
                              }

                              byte do_xxxxx = do_xxx;
                              String string5 = ((ModuleBase)arrayList1.get(do_xxxxx)).getName();
                              Map map2 = mapJ;
                              Float float11 = 0.0F;
                              float h = map2.getOrDefault(string5, float11) + v;
                              if (bl4) {
                                 float bp = h;
                                 Map map18 = mapK;
                                 Float float28 = 0.0F;
                                 float do_xxxxxx = map18.getOrDefault(string5, float28) + 4.0F;
                                 h = bp + do_xxxxxx;
                              }

                              Map map3 = mapD;
                              Float float12 = 0.0F;
                              float j = map3.getOrDefault(string5, float12);
                              if (j == 0.0F) {
                                 j = h;
                              }

                              Map map4 = mapD;
                              float do_xxxxxx = j;
                              float do_xxxxxxx = h;
                              float do_xxxxxxxx = j;
                              float do_xxxxxxxxx = (do_xxxxxxx - do_xxxxxxxx) * x;
                              Float float13 = do_xxxxxx + do_xxxxxxxxx;
                              map4.put(string5, float13);
                              do_xxx += 1;
                           }
                        }

                        byte do_xxx = do_xx;
                        String string4 = ((ModuleBase)arrayList1.get(do_xxx)).getName();
                        if (!linkedListM.contains(string4)) {
                           linkedListM.addFirst(string4);
                        }

                        do_xx += 1;
                     }
                  }

                  byte dm = do_x;
                  String string3 = ((ModuleBase)arrayList1.get(dm)).getName();
                  if (!mapJ.containsKey(string3)) {
                     NanoVG.nvgFontSize(l, w);
                     float[] g = NanoVGTextRenderer.floatArrayA;
                     float bn = NanoVG.nvgTextBounds(l, 0.0F, 0.0F, string3, g);
                     Map map = mapJ;
                     Float float9 = bn;
                     map.put(string3, float9);
                  }

                  if (bl4 && !mapK.containsKey(string3)) {
                     float do_xx = w * 0.8F;
                     NanoVG.nvgFontSize(l, do_xx);
                     String string19 = methodA(string3);
                     float[] do_xxx = NanoVGTextRenderer.floatArrayA;
                     float bo = NanoVG.nvgTextBounds(l, 0.0F, 0.0F, string19, do_xxx);
                     Map map1 = mapK;
                     Float float10 = bo;
                     map1.put(string3, float10);
                  }

                  do_x += 1;
               }
            }

            int dj = i;
            ModuleBase moduleBase = (ModuleBase)list.get(dj);
            if (moduleBase.isEnabled() && !(moduleBase instanceof ArrayList)) {
               arrayList1.add(moduleBase);
            }

            i++;
         }
      }
   }

   public static boolean methodA() {
      List list = ModuleRegistry.getModules();
      int i = 0;

      while (true) {
         int j = i;
         int k = list.size();
         if (j >= k) {
            return true;
         }

         int l = i;
         ModuleBase moduleBase = (ModuleBase)list.get(l);
         if (!(moduleBase instanceof ArrayList) && moduleBase.isEnabled()) {
            Map map = mapJ;
            String string = moduleBase.getName();
            Float float1 = 0.0F;
            float f = map.getOrDefault(string, float1);
            if (f != 0.0F) {
               Map map1 = mapD;
               String string1 = moduleBase.getName();
               Float float2 = 0.0F;
               float g = map1.getOrDefault(string1, float2);
               if (Math.abs(f - g) > 0.05F) {
                  return false;
               }
            }
         }

         i++;
      }
   }

   private static float methodA(String string, boolean bl) {
      Map map = mapJ;
      Float float1 = 0.0F;
      float f = map.getOrDefault(string, float1);
      if (bl) {
         float g = f;
         Map map1 = mapK;
         Float float2 = 0.0F;
         float h = map1.getOrDefault(string, float2) + 4.0F;
         f = g + h;
      }

      return f;
   }

   private static String methodA(String string) {
      int j = string.length();
      int i = Math.min(2, j);
      return "[" + string.substring(0, i).toUpperCase() + "]";
   }

   private static boolean methodA(java.util.ArrayList<ModuleBase> arrayList, String string) {
      int i = 0;

      while (true) {
         int j = i;
         int k = arrayList.size();
         if (j >= k) {
            return false;
         }

         int l = i;
         if (((ModuleBase)arrayList.get(l)).getName().equals(string)) {
            return true;
         }

         i++;
      }
   }

   private static void methodA(Map<String, Float> map, float f) {
      Set set = map.keySet();
      String[] strings1 = new String[0];
      String[] strings = set.toArray(strings1);
      int j = strings.length;

      for (int i = 0; i < j; i++) {
         int k = i;
         String string = strings[k];
         float g = (Float)map.get(string);
         float l = 1.0F - f;
         float h = g * l;
         if (h < 0.5F) {
            map.remove(string);
            mapJ.remove(string);
            mapK.remove(string);
         } else {
            Float float1 = h;
            map.put(string, float1);
         }
      }
   }

   private static float methodA(float f, float g, float h) {
      float i;
      if (f < g) {
         i = g;
      } else {
         i = Math.min(f, h);
      }

      return i;
   }

   private static int methodA(int i, int j, float f) {
      float g = methodA(f, 0.0F, 1.0F);
      int k = i >> 16 & 0xFF;
      int l = i >> 8 & 0xFF;
      int m = i & 0xFF;
      int n = j >> 16 & 0xFF;
      int o = j >> 8 & 0xFF;
      int p = j & 0xFF;
      float h = k;
      float w = (n - k) * g;
      int q = Math.round(h + w);
      float r = l;
      float x = (o - l) * g;
      int s = Math.round(r + x);
      float t = m;
      float y = (p - m) * g;
      int u = Math.round(t + y);
      int v = q << 16;
      int z = s << 8;
      return v | z | u;
   }

   private static void methodA(NVGColor nVGColor, int i, float f) {
      float g = (i >> 16 & 0xFF) / 255.0F;
      float h = (i >> 8 & 0xFF) / 255.0F;
      float j = (i & 0xFF) / 255.0F;
      NanoVG.nvgRGBAf(g, h, j, f, nVGColor);
   }

   static {
      HashMap hashMap = new HashMap();
      mapD = hashMap;
      HashMap hashMap1 = new HashMap();
      mapE = hashMap1;
      HashMap hashMap2 = new HashMap();
      mapF = hashMap2;
      HashMap hashMap3 = new HashMap();
      mapG = hashMap3;
      HashSet hashSet = new HashSet();
      setH = hashSet;
      HashMap hashMap4 = new HashMap();
      mapJ = hashMap4;
      HashMap hashMap5 = new HashMap();
      mapK = hashMap5;
      LinkedList linkedList = new LinkedList();
      linkedListM = linkedList;
   }
}
