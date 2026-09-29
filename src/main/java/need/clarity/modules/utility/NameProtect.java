package need.clarity.modules.utility;

import java.util.ArrayList;
import java.util.List;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;

public class NameProtect extends ModuleBase {
   private static NameProtect instance;

   public NameProtect() {
      super("Name Protect", "Utility");
      instance = this;
   }

   public static NameProtect getInstance() {
      return instance;
   }

   public String getSpoofNameSetting() {
      String string = this.getSetting("Spoof Name", "Player");
      return string != null && !string.isBlank() ? string : "Player";
   }

   private static String methodC() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         return minecraft.player.getGameProfile().name();
      } else {
         return minecraft.getUser() != null ? minecraft.getUser().getName() : null;
      }
   }

   public static String methodA(String string) {
      if (string == null) {
         return null;
      }

      String string2 = methodC();
      if (string2 != null && string.contains(string2)) {
         NameProtect nameProtect = instance;
         String string3;
         if (nameProtect != null && nameProtect.isEnabled()) {
            string3 = nameProtect.getSpoofNameSetting();
         } else {
            string3 = string2;
         }

         String string1 = string3;
         return string.replace(string2, string1);
      } else {
         return string;
      }
   }

   public static Component methodA(Component component) {
      if (component == null) {
         return null;
      }

      NameProtect nameProtect = instance;
      if (nameProtect != null && nameProtect.isEnabled()) {
         String string = methodC();
         if (string == null) {
            return component;
         }

         String string1 = nameProtect.getSpoofNameSetting();
         return methodA(component, string, string1);
      } else {
         return component;
      }
   }

   private static Component methodA(Component component, String string, String string1) {
      ComponentContents componentContents = component.getContents();
      List list = component.getSiblings();
      boolean bl = false;
      Object object = componentContents;
      if (componentContents instanceof LiteralContents) {
         String string2 = ((LiteralContents)componentContents).text();
         if (string2.contains(string)) {
            String string5 = string2.replace(string, string1);
            LiteralContents literalContents = new LiteralContents(string5);
            object = literalContents;
            bl = true;
         }
      } else if (componentContents instanceof TranslatableContents translatableContents) {
         Object[] objects = translatableContents.getArgs();
         Object[] objects4 = null;
         byte l = 0;

         while (true) {
            byte c = l;
            int j = objects.length;
            if (c >= j) {
               if (objects4 != null) {
                  String string6 = translatableContents.getKey();
                  String string9 = translatableContents.getFallback();
                  Object[] objects3 = objects4;
                  TranslatableContents translatableContents1 = new TranslatableContents(string6, string9, objects3);
                  object = translatableContents1;
               }
               break;
            }

            byte f = l;
            Object object1 = objects[f];
            if (object1 instanceof Component component4) {
               Component component5 = methodA(component4, string, string1);
               if (component5 != component4) {
                  if (objects4 == null) {
                     objects4 = (Object[])objects.clone();
                  }

                  Object[] objects1 = objects4;
                  byte g = l;
                  objects1[g] = component5;
                  bl = true;
               }
            } else if (object1 instanceof String string3 && string3.contains(string)) {
               if (objects4 == null) {
                  objects4 = (Object[])objects.clone();
               }

               Object[] objects2 = objects4;
               byte h = l;
               String string7 = string3.replace(string, string1);
               objects2[h] = string7;
               bl = true;
            }

            l += 1;
         }
      } else {
         String string4 = component.getString();
         if (string4 != null && string4.contains(string)) {
            String string8 = string4.replace(string, string1);
            LiteralContents literalContents1 = new LiteralContents(string8);
            object = literalContents1;
            bl = true;
            list = List.of();
         }
      }

      List list3 = list;
      if (!list.isEmpty()) {
         ArrayList arrayList = null;
         byte k = 0;

         while (true) {
            byte b = k;
            int i = list.size();
            if (b >= i) {
               if (arrayList != null) {
                  list3 = arrayList;
               }
               break;
            }

            List list1 = list;
            byte d = k;
            Component component1 = (Component)list1.get(d);
            Component component2 = methodA(component1, string, string1);
            if (component2 != component1) {
               if (arrayList == null) {
                  List list2 = list;
                  ArrayList arrayList1 = new ArrayList(list2);
                  arrayList = arrayList1;
               }

               ArrayList arrayList2 = arrayList;
               byte e = k;
               arrayList2.set(e, component2);
               bl = true;
            }

            k += 1;
         }
      }

      if (!bl) {
         return component;
      }

      MutableComponent mutableComponent = MutableComponent.create((ComponentContents)object);
      Style style = component.getStyle();
      MutableComponent mutableComponent1 = mutableComponent.withStyle(style);

      for (Component component3 : list3) {
         mutableComponent1.append(component3);
      }

      return mutableComponent1;
   }
}
