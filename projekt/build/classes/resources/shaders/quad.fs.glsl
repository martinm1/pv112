#version 330

uniform float t;
uniform sampler2D sceneTex;
uniform sampler2D sceneTex2;

in vec2 vTexCoord;
out vec4 fragColor;

void main() {
    vec2 texCoord = vTexCoord;
    vec2 myvec = vec2(texCoord.x, 10.0*texCoord.y);

    if(vTexCoord.y > 0.1) fragColor = texture2D(sceneTex, texCoord);
    else fragColor = texture2D(sceneTex2, myvec);
}
