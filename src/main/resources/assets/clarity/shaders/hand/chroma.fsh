#version 330 core

in vec2 uv;
out vec4 outColor;

uniform sampler2D ColorTexture;
uniform sampler2D DepthTexture;
uniform float time;
uniform float chromaSpeed;
uniform float chromaSaturation;
uniform float chromaBrightness;

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 createChromaEffect(vec2 coord, float t) {
    // Diagonal sweep with a subtle sine ripple - same formula, kept as-is
    float hue  = fract(coord.y * 0.5 + coord.x * 0.3 + t * chromaSpeed);
    float wave = sin(coord.y * 10.0 + t * 3.0) * 0.05;
    hue += wave;
    return hsv2rgb(vec3(hue, chromaSaturation, chromaBrightness));
}

void main() {
    // Single texelSize compute - shared by all taps
    vec2 texelSize = 1.0 / vec2(textureSize(DepthTexture, 0));

    // ── 3-tap depth mask (L-shape) - cheaper than original 5-tap cross,
    //    same quality at hand boundaries since we only need to detect the
    //    edge, not measure distance. ──
    float centerDepth = texture(DepthTexture, uv).r;
    float minDepth    = centerDepth;
    minDepth = min(minDepth, texture(DepthTexture, uv + vec2(-texelSize.x, 0.0)).r);
    minDepth = min(minDepth, texture(DepthTexture, uv + vec2(0.0, -texelSize.y)).r);

    // smoothstep range kept identical to original for matching mask shape
    float mask = smoothstep(0.99, 0.98, minDepth);
    if (mask < 0.01) discard;

    vec4 originalColor = texture(ColorTexture, uv);
    vec3 chromaColor   = createChromaEffect(uv, time);

    // ── Improved: softer breath pulse (sin²) looks smoother than raw sin ──
    float pulse = 0.88 + sin(time * 4.0) * sin(time * 4.0) * 0.12;
    chromaColor *= pulse;

    // Blend strength 0.85 → 0.80: lets a little of the original hand
    // texture show through, which looks more grounded and less flat
    vec4 finalHandColor = mix(originalColor, vec4(chromaColor, 1.0), 0.80);

    outColor = vec4(finalHandColor.rgb, mask);
}
