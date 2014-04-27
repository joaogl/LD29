#version 330 compatibility

layout(location = 0) in vec4 position;

out vec2 texCoords;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * position;
}