#version 330 compatibility

void main() {
	gl_Position = glModelViewProjectionMatrix * g_Vertex;
}