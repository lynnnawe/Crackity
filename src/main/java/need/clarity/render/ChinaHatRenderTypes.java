package need.clarity.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class ChinaHatRenderTypes {
   private static final DepthStencilState depthStencilStateC;
   private static final RenderPipeline renderPipelineD;
   private static final RenderPipeline renderPipelineE;
   public static final RenderType renderTypeA;
   public static final RenderType renderTypeB;

   static {
      CompareOp compareOp = CompareOp.LESS_THAN_OR_EQUAL;
      DepthStencilState depthStencilState = new DepthStencilState(compareOp, false);
      depthStencilStateC = depthStencilState;
      Builder builder = RenderPipeline.builder(new Snippet[0])
         .withLocation("clarity/china_hat_fill")
         .withVertexShader("core/position_color")
         .withFragmentShader("core/position_color");
      UniformType uniformType = UniformType.UNIFORM_BUFFER;
      Builder builder1 = builder.withUniform("DynamicTransforms", uniformType);
      BlendFunction blendFunction = BlendFunction.TRANSLUCENT;
      ColorTargetState colorTargetState = new ColorTargetState(blendFunction);
      Builder builder2 = builder1.withColorTargetState(colorTargetState);
      DepthStencilState depthStencilState1 = depthStencilStateC;
      Builder builder3 = builder2.withDepthStencilState(depthStencilState1).withCull(false);
      VertexFormat vertexFormat = DefaultVertexFormat.POSITION_COLOR;
      Mode mode = Mode.TRIANGLES;
      renderPipelineD = builder3.withVertexFormat(vertexFormat, mode).build();
      Builder builder4 = RenderPipeline.builder(new Snippet[0])
         .withLocation("clarity/china_hat_outline")
         .withVertexShader("core/position_color")
         .withFragmentShader("core/position_color");
      UniformType uniformType1 = UniformType.UNIFORM_BUFFER;
      Builder builder5 = builder4.withUniform("DynamicTransforms", uniformType1);
      BlendFunction blendFunction1 = BlendFunction.TRANSLUCENT;
      ColorTargetState colorTargetState1 = new ColorTargetState(blendFunction1);
      Builder builder6 = builder5.withColorTargetState(colorTargetState1);
      DepthStencilState depthStencilState2 = depthStencilStateC;
      Builder builder7 = builder6.withDepthStencilState(depthStencilState2).withCull(false);
      VertexFormat vertexFormat1 = DefaultVertexFormat.POSITION_COLOR;
      Mode mode1 = Mode.QUADS;
      renderPipelineE = builder7.withVertexFormat(vertexFormat1, mode1).build();
      RenderSetup renderSetup = RenderSetup.builder(renderPipelineD).sortOnUpload().bufferSize(8192).createRenderSetup();
      renderTypeA = RenderType.create("clarity:china_hat_fill", renderSetup);
      RenderSetup renderSetup1 = RenderSetup.builder(renderPipelineE).sortOnUpload().bufferSize(4096).createRenderSetup();
      renderTypeB = RenderType.create("clarity:china_hat_outline", renderSetup1);
   }
}
