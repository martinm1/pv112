#version 330

const vec3 red = vec3(1.0, 0.0, 0.0);
const vec3 white = vec3(0.6, 0.6, 0.6); //vec3(1.0, 1.0, 1.0);//

out vec4 fragColor;

in vec3 vNormal;
in vec3 vPosition;
in vec2 vTex_coord;

uniform vec4 light1Position;

uniform vec3 light1AmbientColor;
uniform vec3 light1DiffuseColor;
uniform vec3 light1SpecularColor;

uniform vec4 light2Position;

uniform vec3 light2AmbientColor;
uniform vec3 light2DiffuseColor;
uniform vec3 light2SpecularColor;


uniform vec3 materialAmbientColor;
uniform vec3 materialDiffuseColor;
uniform vec3 materialSpecularColor;
uniform float materialShininess;

uniform vec3 eyePosition;

uniform sampler2D my_color_tex;
uniform sampler2D my_alpha_tex;

void main()
{

        if(vPosition.r > 0 && vPosition.b < 0)
        if(vTex_coord.r < 0.465 && vTex_coord.r > 0.215 && vTex_coord.g < 0.55 && vTex_coord.g > 0.45){
             discard;
        }
        if(vPosition.r < 0 && vPosition.b > 0)
        if(vTex_coord.r < 0.55 && vTex_coord.g < 0.835 && vTex_coord.g > 0.605){
             discard;
        }

        vec2 repeat = vec2(20.72, 10.86);
        float offset = -0.5 * mod(floor(repeat.x * vTex_coord.s), 2.0);
        float brick = smoothstep(0.4, 0.5, max(abs(fract(repeat.y * vTex_coord.t + offset) - 0.5), abs(fract(repeat.x * vTex_coord.s) - 0.5)));
        
        
        //mojemoje konec

        vec3 tex_color = mix(red, white, brick) + 0*texture(my_color_tex, vTex_coord).rgb; //texture(my_color_tex, vTex_coord).rgb;
	float tex_alpha = texture(my_alpha_tex, vTex_coord).a;

        //tenhle blok zajistuje pruhlednost, staci zakomentovat a konvice je nepruhledna
       // if(tex_alpha < 0.3){
       //     discard;
       // }
        
	
	vec3 n = normalize(vNormal);
	vec3 v = normalize(eyePosition - vPosition);

        if(!gl_FrontFacing){
            n = -n;
        }

	vec3 light1;
        if(light1Position.w == 0.0) {
            light1 = normalize(light1Position.xyz);
        } else {
            light1 = normalize(light1Position.xyz - vPosition);
        }

        vec3 h = normalize(light1 + v);

        float d = max(dot(n, light1), 0.0);
        float s = pow(max(dot(n, h), 0.0), materialShininess);

        vec3 light1Final =  materialSpecularColor * light1SpecularColor * s +
                       materialDiffuseColor * tex_color * light1DiffuseColor *d +
                       materialAmbientColor * tex_color * light1AmbientColor;
                       

        vec3 light2;
        if(light2Position.w == 0.0) {
            light2 = normalize(light2Position.xyz);
        } else {
            light2 = normalize(light2Position.xyz - vPosition);
        }

        h = normalize(light2 + v);

        d = max(dot(n, light2), 0.0);
        s = pow(max(dot(n, h), 0.0), materialShininess);

        vec3 light2Final =  materialSpecularColor * light2SpecularColor * s +
                       materialDiffuseColor * tex_color * light2DiffuseColor *d +
                       materialAmbientColor * tex_color * light2AmbientColor;



        fragColor = vec4(light1Final+light2Final, 1.0);
}