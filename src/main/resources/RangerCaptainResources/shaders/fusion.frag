varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_scale;//settings dot scale
uniform vec2 u_screenSize;//width, height

uniform vec4 swap_from_0;
uniform vec4 swap_from_1;
uniform vec4 swap_from_2;
uniform vec4 swap_from_3;
uniform vec4 swap_from_4;
uniform vec4 swap_from_5;
uniform vec4 swap_from_6;
uniform vec4 swap_from_7;
uniform vec4 swap_from_8;
uniform vec4 swap_from_9;
uniform vec4 swap_from_10;
uniform vec4 swap_from_11;
uniform vec4 swap_from_12;
uniform vec4 swap_from_13;
uniform vec4 swap_from_14;

uniform vec4 swap_to_0;
uniform vec4 swap_to_1;
uniform vec4 swap_to_2;
uniform vec4 swap_to_3;
uniform vec4 swap_to_4;
uniform vec4 swap_to_5;
uniform vec4 swap_to_6;
uniform vec4 swap_to_7;
uniform vec4 swap_to_8;
uniform vec4 swap_to_9;
uniform vec4 swap_to_10;
uniform vec4 swap_to_11;
uniform vec4 swap_to_12;
uniform vec4 swap_to_13;
uniform vec4 swap_to_14;

vec4 swap(vec4 tex_color) {
    if (distance(tex_color, swap_from_0) < 1.0/256.0 && swap_to_0 != vec4(0)) return swap_to_0;
    if (distance(tex_color, swap_from_1) < 1.0/256.0 && swap_to_1 != vec4(0)) return swap_to_1;
    if (distance(tex_color, swap_from_2) < 1.0/256.0 && swap_to_2 != vec4(0)) return swap_to_2;
    if (distance(tex_color, swap_from_3) < 1.0/256.0 && swap_to_3 != vec4(0)) return swap_to_3;
    if (distance(tex_color, swap_from_4) < 1.0/256.0 && swap_to_4 != vec4(0)) return swap_to_4;
    if (distance(tex_color, swap_from_5) < 1.0/256.0 && swap_to_5 != vec4(0)) return swap_to_5;
    if (distance(tex_color, swap_from_6) < 1.0/256.0 && swap_to_6 != vec4(0)) return swap_to_6;
    if (distance(tex_color, swap_from_7) < 1.0/256.0 && swap_to_7 != vec4(0)) return swap_to_7;
    if (distance(tex_color, swap_from_8) < 1.0/256.0 && swap_to_8 != vec4(0)) return swap_to_8;
    if (distance(tex_color, swap_from_9) < 1.0/256.0 && swap_to_9 != vec4(0)) return swap_to_9;
    if (distance(tex_color, swap_from_10) < 1.0/256.0 && swap_to_10 != vec4(0)) return swap_to_10;
    if (distance(tex_color, swap_from_11) < 1.0/256.0 && swap_to_11 != vec4(0)) return swap_to_11;
    if (distance(tex_color, swap_from_12) < 1.0/256.0 && swap_to_12 != vec4(0)) return swap_to_12;
    if (distance(tex_color, swap_from_13) < 1.0/256.0 && swap_to_13 != vec4(0)) return swap_to_13;
    if (distance(tex_color, swap_from_14) < 1.0/256.0 && swap_to_14 != vec4(0)) return swap_to_14;
    return tex_color;
}

void main() {
    gl_FragColor = swap(v_color * texture2D(u_texture, v_texCoords));
}