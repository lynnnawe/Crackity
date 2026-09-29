#version 330 core

in  vec2 uv;
out vec4 outColor;

uniform sampler2D ColorTexture;
uniform sampler2D DepthTexture;
uniform sampler2D JfaTexture;

uniform vec3  glowColor;
uniform float glowRadius;
uniform float glowIntensity;
uniform float glowFalloff;

uniform bool  tintEnabled;
uniform vec3  tintColor;
uniform float tintAmount;

uniform vec2 texelSize;  // 1.0 / resolution

void main() {
    float centerDepth = texture(DepthTexture, uv).r;

    // Hand interior - tint pass-through identical to original
    if (centerDepth < 0.99) {
        vec4 origColor = texture(ColorTexture, uv);
        if (tintEnabled) {
            outColor = vec4(mix(origColor.rgb, tintColor, tintAmount), origColor.a);
        } else {
            outColor = origColor;
        }
        return;
    }

    vec4 jfa = texture(JfaTexture, uv);
    if (jfa.a < 0.5) discard;  // no seed reached this pixel

    // Recompute pixel distance here in the composite pass - UV coords survive
    // RGBA8 quantization cleanly (0-1 range). Pixel distance does not.
    vec2  delta   = (uv - jfa.rg) / texelSize;
    float pixDist = length(delta);

    if (pixDist >= glowRadius) discard;

    // Identical gradient formula to original edgeglow.fsh
    float t     = clamp(pixDist / glowRadius, 0.0, 1.0);
    float alpha = glowIntensity * pow(1.0 - t, glowFalloff);

    outColor = vec4(glowColor, clamp(alpha, 0.0, 1.0));
}
