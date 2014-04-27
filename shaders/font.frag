#version 330 compatibility

uniform sampler2D texture;
in vec2 texCoords;

void main() {
	vec4 color = texture2D(texture, texCoords);
	if (color == vec4(1.0, 0.0, 1.0, 1.0)) color = vec4(0.0, 0.0, 0.0, 0.0);
	gl_FragColor = color; 
}