package mfp;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.GLAutoDrawable;

import mfp.object.Liquid;
import mfp.object.LiquidGroup;
import mfp.object.WallGroup;

public class ParticleSystem {

	//TODO: Add objects
	WallGroup walls;
	List<LiquidGroup> liquids;
	
	//Temp constant
	double h;    //Particle size
	//double dt;   //Time step
	//double rho0; // Reference density
	//double k;    // Bulk modulus
	//double mu;   // Viscosity
	//double g;    // Gravity
	
	
	Vector2d padding;
	Vector2d window;
	
	public ParticleSystem(Vector2d padding, Vector2d window) {
		//Initialize LiquidGroup
		liquids = new ArrayList<LiquidGroup>();
		liquids.add(new LiquidGroup(new Color3f(0,0,1),1,1000,1000,0.1,0.75));
		this.padding = padding;
		this.window = window;
		
		//Test LiquidGroup
		liquids.get(0).test();
	}
	
	public void advanceTime(double dt,double g) {
		//Compute density, for now we only do 1 liquid group
		liquids.get(0).compute_density(h);
		//Compute Force
		liquids.get(0).compute_accel(h, g);
		//Compute Euler
		liquids.get(0).sympletic_Euler(dt);
		//Compute Wall collision
		liquids.get(0).damp_reflect(padding, window);
	}

	public void display(GLAutoDrawable drawable) {
		//Draw all
		for(LiquidGroup l: liquids) 
			l.display(drawable);
	}

	public void spawnParticle(double x, double y) {
		double pushForce = 100;
		double brushSize = 2;
		for(LiquidGroup l: liquids) {
			l.spawnPush(x, y, pushForce, brushSize);
		}
		//for now, we only spawn particle for the first liquid
		for(int i = 0; i < 2; ++i) {
			for(int j = 0; j < 2; j++) {
				Liquid l = new Liquid(new Point2d(i,j));
				liquids.get(0).addParticle(l);
			}
		}
	}

	public void init() {
		for(LiquidGroup l: liquids) {
			l.init();
		}
	}
}
