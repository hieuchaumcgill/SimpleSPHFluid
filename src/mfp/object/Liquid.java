package mfp.object;

import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mfp.tools.glVertex2dScale;

public class Liquid {
	Point2d p;
	Vector2d v;
	Vector2d a;
	double rho;
	
	public Liquid(Point2d p) {
		this.p = p;
		this.v = new Vector2d(0,0);
		this.a = new Vector2d(0,0);
	}
	
	public Liquid(Point2d p, Vector2d v, Vector2d a) {
		this.p = p;
		this.v = v;
	}

	public void display(GLAutoDrawable drawable, Color3f c) {
		// TODO Auto-generated method stub
        GL2 gl = drawable.getGL().getGL2();
        gl.glColor4f( c.x, c.y, c.z, 1 );
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        double h = 1;
        glVertex2dScale.glVertex2d( p.x - h, p.y - h );
        glVertex2dScale.glVertex2d( p.x - h, p.y + h );
        glVertex2dScale.glVertex2d( p.x + h, p.y - h );
        glVertex2dScale.glVertex2d( p.x + h, p.y + h );
        gl.glEnd();
	}
}
