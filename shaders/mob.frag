#version 330 compatibility

uniform sampler2D texture;
in vec2 texCoords;

uniform vec2 lightPosition;
uniform vec3 lightColor;
uniform float lightIntensity;

void main(){
	vec4 tex = texture2D(texture, texCoords);
	float distance = length(lightPosition - gl_FragCoord.xy);
	float attenuation = 1.0 / distance;
	vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(lightColor * lightIntensity, 1.0);
	color = mix(tex, color, 0.2);
	float falloff = 4;
	falloff -= distance / 25.0f / lightIntensity;
	color /= (distance / (lightIntensity * falloff));
	gl_FragColor = tex;
}