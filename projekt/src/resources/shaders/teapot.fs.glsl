#version 330

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

uniform vec4 light3Position;

uniform vec3 light3AmbientColor;
uniform vec3 light3DiffuseColor;
uniform vec3 light3SpecularColor;

uniform vec3 light3Direction;
uniform float light3CutOff;


uniform vec3 materialAmbientColor;
uniform vec3 materialDiffuseColor;
uniform vec3 materialSpecularColor;
uniform float materialShininess;

uniform vec3 eyePosition;

uniform sampler2D my_color_tex;
uniform sampler2D my_alpha_tex;

void main()
{
        vec3 tex_color = texture(my_color_tex, vTex_coord).rgb;
	float tex_alpha = texture(my_alpha_tex, vTex_coord).a;

        //tenhle blok zajistuje pruhlednost, staci zakomentovat a konvice je nepruhledna
        //if(tex_alpha < 0.3){
        //    discard;
        //}
	
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



        /////////////////////////////////////////

        vec3 light3Final;
        
        vec3 light3;
        if(light3Position.w == 0.0) {
            light3 = normalize(light3Position.xyz);
        } else {
            light3 = normalize(light3Position.xyz - vPosition);
        }
        float theta = dot(light3, normalize(-light3Direction));

        if(theta > light3CutOff && length(light3Position.xyz - vPosition) < 4.0)
        {
            h = normalize(light3 + v);

            d = max(dot(n, light3), 0.0);
            s = pow(max(dot(n, h), 0.0), materialShininess);

            light3Final =  materialSpecularColor * light3SpecularColor * s +
                           materialDiffuseColor * tex_color * light3DiffuseColor *d +
                           materialAmbientColor * tex_color * light3AmbientColor;
        }
        else{
            light3Final =  materialAmbientColor * tex_color * light3AmbientColor;
        }


        fragColor = vec4(light3Final, 1.0);
}