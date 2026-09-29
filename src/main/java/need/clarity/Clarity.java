package need.clarity;

import need.clarity.auth.HardwareAccessCheck;
import need.clarity.config.ClarityConfig;
import need.clarity.config.ServerProfiles;
import need.clarity.modules.ModuleRegistry;
import need.clarity.modules.combat.AimAssist;
import need.clarity.modules.combat.AntiBot;
import need.clarity.modules.combat.AutoAnchor;
import need.clarity.modules.combat.AutoCrystal;
import need.clarity.modules.combat.AutoHitCrystal;
import need.clarity.modules.combat.AutoLunge;
import need.clarity.modules.combat.AutoTrap;
import need.clarity.modules.combat.Backtrack;
import need.clarity.modules.combat.HitSwap;
import need.clarity.modules.combat.Piercing;
import need.clarity.modules.combat.Reach;
import need.clarity.modules.combat.ShieldBreaker;
import need.clarity.modules.combat.SprintReset;
import need.clarity.modules.combat.Triggerbot;
import need.clarity.modules.exploit.CollisionExploit;
import need.clarity.modules.movement.AntiHunger;
import need.clarity.modules.movement.AntiLevitation;
import need.clarity.modules.movement.AntiPowderSnow;
import need.clarity.modules.movement.AntiPush;
import need.clarity.modules.movement.ElytraJump;
import need.clarity.modules.movement.Fly;
import need.clarity.modules.movement.InventoryWalk;
import need.clarity.modules.movement.MoveFix;
import need.clarity.modules.movement.NoFall;
import need.clarity.modules.movement.NoJumpDelay;
import need.clarity.modules.movement.ReverseStep;
import need.clarity.modules.movement.SnapTap;
import need.clarity.modules.movement.Speed;
import need.clarity.modules.movement.Sprint;
import need.clarity.modules.movement.Step;
import need.clarity.modules.movement.WindCharge;
import need.clarity.modules.utility.AutoArmour;
import need.clarity.modules.utility.AutoTool;
import need.clarity.modules.utility.ChestStealer;
import need.clarity.modules.utility.ClientSpoofer;
import need.clarity.modules.utility.CrystalOptimizer;
import need.clarity.modules.utility.Disabler;
import need.clarity.modules.utility.FastPlace;
import need.clarity.modules.utility.GhostHand;
import need.clarity.modules.utility.HoverTotem;
import need.clarity.modules.utility.InventoryCleaner;
import need.clarity.modules.utility.MLG;
import need.clarity.modules.utility.NameProtect;
import need.clarity.modules.utility.NoBreakDelay;
import need.clarity.modules.utility.Overtick;
import need.clarity.modules.utility.RightClicker;
import need.clarity.modules.utility.SilentTotem;
import need.clarity.modules.utility.Targets;
import need.clarity.modules.utility.XCarry;
import need.clarity.modules.visual.Ambience;
import need.clarity.modules.visual.Animations;
import need.clarity.modules.visual.ArrayList;
import need.clarity.modules.visual.CameraTweaks;
import need.clarity.modules.visual.Capes;
import need.clarity.modules.visual.ChinaHat;
import need.clarity.modules.visual.EnchantGlint;
import need.clarity.modules.visual.Freecam;
import need.clarity.modules.visual.Fullbright;
import need.clarity.modules.visual.HandShaders;
import need.clarity.modules.visual.HealthTags;
import need.clarity.modules.visual.HitParticles;
import need.clarity.modules.visual.HitSounds;
import need.clarity.modules.visual.NoRender;
import need.clarity.modules.visual.PlayerESP;
import need.clarity.modules.visual.PlayerTrails;
import need.clarity.modules.visual.Search;
import need.clarity.modules.visual.ServerRotations;
import need.clarity.modules.visual.Tracers;
import need.clarity.modules.visual.ViewModel;
import need.clarity.render.ArrayListOverlayRenderer;
import need.clarity.server.ClarityServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clarity implements ModInitializer {
   public static final Logger LOGGER = LoggerFactory.getLogger("clarity");
   private static boolean flagB = false;

   public void onInitialize() {
      HardwareAccessCheck.checkAccess();
      ClarityConfig.load();
      if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
         ClarityServer.stop();
         ClarityServer.start();
      }

      new Triggerbot();
      new AimAssist();
      new ShieldBreaker();
      new SprintReset();
      new Piercing();
      new Reach();
      new Backtrack();
      new AntiBot();
      new HitSwap();
      new AutoCrystal();
      new AutoHitCrystal();
      new AutoAnchor();
      new AutoLunge();
      new AutoTrap();
      new ServerRotations();
      new Fullbright();
      new Animations();
      new Search();
      new Tracers();
      new Freecam();
      new HealthTags();
      new Capes();
      new NoRender();
      new CameraTweaks();
      new ViewModel();
      new ChinaHat();
      new HandShaders();
      new PlayerESP();
      new HitParticles();
      new HitSounds();
      new PlayerTrails();
      new EnchantGlint();
      new ArrayList();
      new Ambience();
      new MoveFix();
      new WindCharge();
      new Sprint();
      new Fly();
      new ElytraJump();
      new Speed();
      new InventoryWalk();
      new AntiPowderSnow();
      new AntiHunger();
      new AntiLevitation();
      new NoFall();
      new AntiPush();
      new Step();
      new ReverseStep();
      new CollisionExploit();
      new Targets();
      new RightClicker();
      new MLG();
      new AutoTool();
      new NoBreakDelay();
      new FastPlace();
      new XCarry();
      new GhostHand();
      new NameProtect();
      new AutoArmour();
      new ChestStealer();
      new InventoryCleaner();
      new HoverTotem();
      new SilentTotem();
      new Overtick();
      new SnapTap();
      new NoJumpDelay();
      new ClientSpoofer();
      new CrystalOptimizer();
      new Disabler();
      if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
         ModuleRegistry.syncFromConfig();
      }

      if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
         ModuleRegistry.registerTickEvent();
         ServerProfiles.methodA();
      }

      if (!flagB) {
         flagB = true;
         if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)minecraft -> {
               ClarityConfig.flushSync();
               ClarityServer.stop();
               ArrayListOverlayRenderer.methodA();
            });
         }

         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ClarityConfig.flushSync();
            ClarityServer.stop();
            ArrayListOverlayRenderer.methodA();
         }, "clarity-shutdown"));
      }
   }

   public static Identifier methodA(String string) {
      return Identifier.fromNamespaceAndPath("clarity", string);
   }
}
