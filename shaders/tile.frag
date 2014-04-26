#version 330 compatibility

uniform sampler2D texture;



void main(){
	gl_FragColor = vec4(gl_FragCoord.x / 960.0, gl_FragCoord / 540.0, 1.0, 1.0);
}