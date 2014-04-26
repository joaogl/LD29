#version 330 compatibility

layout()

void main() {
	gl_Position = glModelViewProjectionMatrix * g_Vertex;
}