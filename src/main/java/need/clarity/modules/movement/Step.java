package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class Step extends ModuleBase {
   public static Step instance;
   private static final Identifier identifierB = Identifier.fromNamespaceAndPath("clarity", "step_height");

   public Step() {
      super("Step", "Movement");
      instance = this;
   }

   @Override
   public void onEnable() {
      this.methodA();
   }

   @Override
   public void onTick() {
      this.methodA();
   }

   @Override
   public void onDisable() {
      this.methodB();
   }

   private void methodA() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         LocalPlayer localPlayer = minecraft.player;
         Holder holder = Attributes.STEP_HEIGHT;
         AttributeInstance attributeInstance = localPlayer.getAttribute(holder);
         if (attributeInstance != null) {
            Float float1 = 1.0F;
            float f = this.getSetting("Step Height", float1);
            float h = Math.min(3.0F, f);
            float g = Math.max(0.6F, h);
            Identifier identifier1 = identifierB;
            attributeInstance.removeModifier(identifier1);
            Identifier identifier = identifierB;
            double d = g - 0.6F;
            Operation operation = Operation.ADD_VALUE;
            AttributeModifier attributeModifier = new AttributeModifier(identifier, d, operation);
            attributeInstance.addPermanentModifier(attributeModifier);
         }
      }
   }

   private void methodB() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         LocalPlayer localPlayer = minecraft.player;
         Holder holder = Attributes.STEP_HEIGHT;
         AttributeInstance attributeInstance = localPlayer.getAttribute(holder);
         if (attributeInstance != null) {
            Identifier identifier = identifierB;
            attributeInstance.removeModifier(identifier);
         }
      }
   }
}
