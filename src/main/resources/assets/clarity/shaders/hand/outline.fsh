#version 330 core

in  vec2 uv;
out vec4 outColor;

uniform sampler2D ColorTexture;
uniform sampler2D DepthTexture;

uniform vec3  outlineColor;
uniform float outlineThickness;

uniform bool  tintEnabled;
uniform vec3  tintColor;
uniform float tintAmount;

void main() {
    float centerDepth = texture(DepthTexture, uv).r;
    bool  isHand      = centerDepth < 0.99;

    // Hand interior - tint pass-through
    if (isHand) {
        vec4 origColor = texture(ColorTexture, uv);
        if (tintEnabled) {
            outColor = vec4(mix(origColor.rgb, tintColor, tintAmount), origColor.a);
        } else {
            outColor = origColor;
        }
        return;
    }

    vec2  texel = 1.0 / vec2(textureSize(DepthTexture, 0));
    float r     = outlineThickness;

    // ── 16-angle sampling (was 64 × 2 = 128 taps → now 16 × 3 = 48 taps max) ──
    // Uses 3 radii (0.33r, 0.66r, 1.0r) for anti-aliased soft edge instead
    // of a hard binary hit. Provides smoother outline at much lower cost.
    const int   N      = 16;
    const float TWO_PI = 6.28318530718;

    float nearestHit = r + 1.0; // > r means no hit yet
    bool  hit        = false;

    for (int i = 0; i < N; i++) {
        float angle = (float(i) + 0.5) / float(N) * TWO_PI;
        vec2  dir   = vec2(cos(angle), sin(angle)) * texel;

        // Sample 3 radii along this ray - innermost hit wins for soft edge
        for (int ri = 1; ri <= 3; ri++) {
            float sample_r = r * float(ri) / 3.0;
            if (texture(DepthTexture, uv + dir * sample_r).r < 0.99) {
                nearestHit = min(nearestHit, sample_r);
                hit = true;
                break; // inner loop early exit
            }
        }
        if (hit && nearestHit < 0.4 * r) break; // very near hit - skip remaining angles
    }

    if (!hit) discard;

    // ── Soft anti-aliased edge: fade from full opacity at distance 0 to
    //    zero at outlineThickness. smoothstep gives a clean gradient fringe.
    float alpha = smoothstep(r, 0.0, nearestHit);

    outColor = vec4(outlineColor, clamp(alpha, 0.0, 1.0));
}
