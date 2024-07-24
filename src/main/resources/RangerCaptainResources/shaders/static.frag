//https://www.shadertoy.com/view/3d3fR7

varying vec4 v_color;
varying vec2 v_texCoords;
//varying vec4 v_pos;
//varying vec4 v_apos;

uniform sampler2D u_texture;
uniform float u_scale;//settings dot scale
uniform vec2 u_screenSize;//width, height
uniform vec2 u_mouse;
uniform float x_time;

float noise(vec2 pos, float evolve) {

    // Loop the evolution (over a very long period of time).
    float e = fract((evolve*0.01));

    // Coordinates
    float cx  = pos.x*e;
    float cy  = pos.y*e;

    // Generate a "random" black or white value
    return fract(23.0*fract(2.0/fract(fract(cx*2.4/cy*23.0+pow(abs(cy/22.4),3.3))*fract(cx*evolve/pow(abs(cy),0.050)))));
}

void main() {
    vec2 uv = gl_FragCoord.xy / u_screenSize;
    uv.x += (sin((uv.y + (x_time * 0.05)) * 10.0) * 0.02) + (sin((uv.y + (x_time * 0.02)) * 32.0) * 0.01);
    vec4 texColor = texture(u_texture, uv);
    vec3 staticColor = vec3(noise(gl_FragCoord.xy, x_time));
    gl_FragColor = vec4(staticColor, texColor.w);
}