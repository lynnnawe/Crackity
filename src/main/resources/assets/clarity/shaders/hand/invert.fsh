#version 330 core

in vec2 uv;
out vec4 outColor;

uniform sampler2D ColorTexture;
uniform sampler2D DepthTexture;
uniform float time;
uniform vec2 resolution;

uniform bool  hideHand;
uniform float invertIntensity;
uniform bool  invertHue;
uniform bool  invertSaturation;
uniform bool  invertBrightness;
uniform float edgeGlow;
uniform vec3  glowColor;

vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    // texelSize shared by all depth taps
    vec2 texelSize = 1.0 / vec2(textureSize(DepthTexture, 0));

    // 3-tap depth mask - same quality as 5-tap for edge detection, -2 taps
    float centerDepth = texture(DepthTexture, uv).r;
    float minDepth    = centerDepth;
    minDepth = min(minDepth, texture(DepthTexture, uv + vec2(-texelSize.x, 0.0)).r);
    minDepth = min(minDepth, texture(DepthTexture, uv + vec2(0.0, -texelSize.y)).r);

    float mask = smoothstep(0.99, 0.98, minDepth);
    if (mask < 0.01) discard;

    vec4 originalColor = texture(ColorTexture, uv);

    vec3 finalColor;

    if (centerDepth < 0.99) {
        vec3 baseColor = originalColor.rgb;

        if (invertHue || invertSaturation || invertBrightness) {
            vec3 hsv = rgb2hsv(baseColor);
            if (invertHue)        hsv.x = fract(hsv.x + 0.5);
            if (invertSaturation) hsv.y = 1.0 - hsv.y;
            if (invertBrightness) hsv.z = 1.0 - hsv.z;
            finalColor = mix(baseColor, hsv2rgb(hsv), invertIntensity);
        } else {
            finalColor = mix(baseColor, vec3(1.0) - baseColor, invertIntensity);
        }
    } else {
        finalColor = originalColor.rgb;
    }

    // ── Edge glow: 8 diagonal+cardinal taps instead of 4 cardinal only ──
    // More taps means the glow corners are filled in - better visual quality.
    // Cost: +4 texture fetches per edge pixel (background pixels are discarded).
    if (edgeGlow > 0.01 && centerDepth < 0.99) {
        float edgeStrength = 0.0;
        float step = texelSize.x; // assume square pixels; texelSize.y ≈ texelSize.x

        // Cardinal
        if (texture(DepthTexture, uv + vec2( texelSize.x, 0.0)).r > 0.99) edgeStrength += 1.0;
        if (texture(DepthTexture, uv + vec2(-texelSize.x, 0.0)).r > 0.99) edgeStrength += 1.0;
        if (texture(DepthTexture, uv + vec2(0.0,  texelSize.y)).r > 0.99) edgeStrength += 1.0;
        if (texture(DepthTexture, uv + vec2(0.0, -texelSize.y)).r > 0.99) edgeStrength += 1.0;
        // Diagonal (weighted 0.5 so total still normalises to [0, 1])
        float diag = 0.7071 * texelSize.x; // 1/√2
        if (texture(DepthTexture, uv + vec2( diag,  diag)).r > 0.99) edgeStrength += 0.5;
        if (texture(DepthTexture, uv + vec2(-diag,  diag)).r > 0.99) edgeStrength += 0.5;
        if (texture(DepthTexture, uv + vec2( diag, -diag)).r > 0.99) edgeStrength += 0.5;
        if (texture(DepthTexture, uv + vec2(-diag, -diag)).r > 0.99) edgeStrength += 0.5;

        edgeStrength = clamp(edgeStrength / 6.0, 0.0, 1.0);

        // Smoother pulse: sin² gives 0→1→0 without negative dip
        float t     = time * 3.0;
        float pulse = 0.75 + sin(t) * sin(t) * 0.25;
        finalColor += glowColor * edgeStrength * edgeGlow * pulse;
    }

    outColor = vec4(finalColor, mask);
}
