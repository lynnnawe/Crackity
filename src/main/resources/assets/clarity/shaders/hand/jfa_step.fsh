#version 330 core

in  vec2 uv;
out vec4 outColor;  // rg = nearest seed UV, b = unused, a = 1 if valid

uniform sampler2D JfaTexture;
uniform float     stepSize;    // offset in pixels
uniform vec2      texelSize;   // 1.0 / resolution
uniform float     glowRadius;  // discard seeds beyond this many pixels

void main() {
    vec2  bestSeed = vec2(0.0);
    float bestDist = 1e9;
    bool  found    = false;

    for (int dy = -1; dy <= 1; dy++) {
        for (int dx = -1; dx <= 1; dx++) {
            vec2 sampleUV = clamp(uv + vec2(float(dx), float(dy)) * stepSize * texelSize,
                                  vec2(0.0), vec2(1.0));
            vec4 s = texture(JfaTexture, sampleUV);
            if (s.a < 0.5) continue;  // neighbour has no valid seed

            // Distance in pixels from current pixel to candidate seed UV
            vec2  delta   = (uv - s.rg) / texelSize;
            float pixDist = length(delta);

            if (pixDist > glowRadius) continue;  // outside radius - don't propagate

            if (pixDist < bestDist) {
                bestDist = pixDist;
                bestSeed = s.rg;
                found    = true;
            }
        }
    }

    outColor = found ? vec4(bestSeed, 0.0, 1.0) : vec4(0.0, 0.0, 0.0, 0.0);
}
