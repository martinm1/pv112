#version 330

in vec4 position;
in vec3 normal;
in vec2 tex_coord;

out vec3 vNormal;
out vec3 vPosition;
out vec2 vTex_coord;

uniform mat4 MVP;
uniform mat3 N;
uniform mat4 model;

void main()
{
	vTex_coord = tex_coord;

	vPosition = vec3(model * position);
	vNormal = normalize(N * normal);
	gl_Position = MVP * position;
}
