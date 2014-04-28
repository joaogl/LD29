#version 330 compatibility

uniform sampler2D texture, extraTexture;
in vec2 texCoords;

uniform vec2 lightPosition;
uniform vec3 lightColor;
uniform float lightIntensity;

uniform float tileInUse;
uniform float extraLevel;

void main(){
	vec4 tex = texture2D(texture, texCoords);
	vec4 extra = texture2D(extraTexture, texCoords);
	
	float distance = length(lightPosition - gl_FragCoord.xy);
	float attenuation = 1.0 / distance;
	vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(lightColor * lightIntensity, 1.0);
	
	color = mix(tex, color, 0.2);
	color = mix(color, extra, extraLevel);
	
	float falloff = 4;
	falloff -= distance / 25.0f / lightIntensity;
	color /= (distance / (lightIntensity * falloff));	
	
	if (tileInUse == 3 && lightIntensity > 1.5f) color = color + vec4(0.025, 0.025, 0.025, 0.025);
	else if (tileInUse == 3) color = color + vec4(0.025 * lightIntensity, 0.025 * lightIntensity, 0.025 * lightIntensity, 0.025 * lightIntensity);
	gl_FragColor = color;
}