#version 330 compatibility

uniform sampler2D texture;

in vec2 texCoords;

void main(){
	vec4 color = vec4(1.0, 0.0, 1.0, 1.0);
	vec4 tex = texture2D(texture, texCoords);
	gl_FragColor = tex;
}