#version 330

in vec2 position;

out vec2 vTexCoord;

void main() {
    vTexCoord = position * 0.5 + 0.5;
    gl_Position = vec4(position, 0.0, 1.0);
}
