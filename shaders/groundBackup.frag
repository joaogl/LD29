#version 330 compatibility

uniform sampler2D texture waterTexure;
in vec2 texCoords;

uniform vec2 lightPosition;
uniform vec3 lightColor;
uniform float lightIntensity;

uniform float tileInUse;

void main(){
	vec4 tex = texture2D(texture, texCoords);
	vec4 color = vec4(1.0, 1.0, 1.0, 1.0);
	color = tex;
	float distance = length(lightPosition - gl_FragCoord.xy);
	float attenuation = 1.0 / distance;
	//vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(lightColor * lightIntensity, 1.0);
	//color = mix(tex, color, 0.2);
	//float falloff = 4;
	float falloff = 40;
	falloff -= distance / 25.0f / lightIntensity;
	
	color *= vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4((lightColor / distance * 15) * lightIntensity, 1.0) + 0.01;
	
	color /= (distance / (lightIntensity * falloff));	
	if (tileInUse == 3) color = color + vec4(0.025, 0.025, 0.025, 0.025);
	gl_FragColor = color;
}