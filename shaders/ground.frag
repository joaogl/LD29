#version 330 compatibility

uniform sampler2D texture;
in vec2 texCoords;

uniform vec2 lightPosition[10];
uniform vec3 lightColor[10];
uniform float lightIntensity[10];

void main(){
	vec4 color = vec4(1.0, 1.0, 1.0, 1.0);
	vec4 tex = texture2D(texture, texCoords);
	color = tex;
	int num = 0;
	float alpha = 1.0;
	for (int i = 0; i < 10; i++) {
		vec2 pos = lightPosition[i];
		if (pos.x == 0 && pos.y == 0) continue;
		num++;
		vec3 col = lightColor[i];
		float ints = lightIntensity[i];
	
		float distance = length(pos - gl_FragCoord.xy);
		float attenuation = 1.0 / distance;
		
		float falloff = 40f;
		falloff -= distance / 25.0f / ints;
		
		color *= vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4((col / distance * 15) * ints, 1.0) + 0.01;
		
		color /= (distance / (ints * falloff));
	}		

	color *= 300.0 * (num - 1) + 1;	
	if (tex.x == 1.0 && tex.y == 0.0 && tex.z == 1.0){
		alpha = 0.0;
	}	
	gl_FragColor = vec4(color.xyz, alpha);
}