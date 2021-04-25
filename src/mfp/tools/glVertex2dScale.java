package mfp.tools;

import javax.vecmath.Vector2d;

import com.jogamp.opengl.GL2;

public class glVertex2dScale {
	public static Vector2d scale;
	
	private static GL2 gl;
	public static void setScale(double x, double y) {
		if(scale == null)
			scale = new Vector2d(0,0);
		scale.set(x,y);
	}

	public static void setScale(Vector2d scale) {
		glVertex2dScale.scale = scale;
	}

	public static void setGL(GL2 gl) {
		glVertex2dScale.gl = gl;
	}
	
	public static void glVertex2d(double x, double y) {
		gl.glVertex2d(x*scale.x, y*scale.y);
	}
}
