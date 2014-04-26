#version 330 compatibility

uniform sampler2D texture;

in vec2 texCoords;

void main(){
	gl_FragColor = texture2D(texture, texCoords);
}