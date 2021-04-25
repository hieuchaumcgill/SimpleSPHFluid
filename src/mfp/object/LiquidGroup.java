package mfp.object;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.GLAutoDrawable;

public class LiquidGroup extends ParticleGroup{
	double m;
	double rho0; // Reference density
	double k;    // Bulk modulus
	double mu;   // Viscosity
	double wdamp;// Wall damping
	List<Liquid> liquids;
	
	public LiquidGroup(Color3f color, double m, double rho0, double k, double mu, double wdamp) {
		this.color = color;
		this.m = m;
		this.rho0 = rho0;
		this.k = k;
		this.mu = mu;
		this.wdamp = wdamp;
		liquids = new ArrayList<Liquid>();
	}
	
	public void compute_density(double h) {
		double h2 = h*h;
		double h8 = (h2*h2)*(h2*h2);
		double C = 4*m/(Math.PI*h8);
		
		for(Liquid l: liquids) {
			l.rho = 0;
		}
		
		for(int i = 0; i < liquids.size(); ++i) {
			liquids.get(i).rho += 4*m/(Math.PI*h2);
			for(int j = i+1; j < liquids.size(); ++j) {
				Vector2d dd = new Vector2d(liquids.get(j).p);
				dd.negate();
				dd.add(liquids.get(i).p);
				double r2 = dd.length();
				r2 = r2*r2;
				double z = h2 - r2;
				if(z > 0) {
					double rho_ij = C*z*z*z;
					liquids.get(i).rho += rho_ij;
					liquids.get(j).rho += rho_ij;
				}
			}
		}
	}
	
	public void compute_accel(double h,double g) {
		//apply gravity
		for(Liquid l: liquids) {
			l.a.set(l.a.x,l.a.y+g);
		}
		double h2 = h*h;
		double C0 = m/(Math.PI * (h2*h2));
		double Cp = 15*k;
		double Cv = -40*mu;
		
		for(int i = 0; i < liquids.size(); ++i) {
			double rho_i = liquids.get(0).rho;
			for(int j = i+1; j < liquids.size(); ++j) {
				Vector2d dd = new Vector2d(liquids.get(j).p);
				dd.negate();
				dd.add(liquids.get(i).p);
				double r = dd.length();
				double r2 = r*r;
				double z = h2 - r2;
				if(z > 0) {
					double rho_j = liquids.get(1).rho;
					double q = Math.sqrt(r2)/h;
					double u = 1-q;
					double w0 = C0* u/(rho_i);
					double wp = w0 * Cp*(rho_i+rho_j-2*rho0)*u/q;
					double wv = w0 * Cv;
					Vector2d dv = new Vector2d(liquids.get(j).v);
					dv.negate();
					dv.add(liquids.get(i).v);
					dd.scale(wp);
					dv.scale(wv);
					dd.add(dv);
					liquids.get(i).a.add(dv);
					liquids.get(j).a.add(dv);
				}
			}
		}
	}
	
	public void sympletic_Euler(double dt) {
		for(Liquid l: liquids) {
			Vector2d a = new Vector2d(l.a);
			a.scale(dt/2);
			l.v.add(a);
			l.p.add(l.v);
		}
	}
	
	public void damp_reflect(Vector2d padding , Vector2d window) {
		//We only do border wall right now
		for(Liquid l: liquids) {
			if(l.v.x != 0) {
				boolean collided = false;
				double barrier = 0;
				if(l.p.x < padding.x) {
					barrier = padding.x;
					collided = true;
				}
				else if(l.p.x > (window.x - padding.x)) {
					barrier = window.x - padding.x;
					collided = true;
				}
				
				if(collided) {
					double tbounce = (l.p.x-barrier)/l.v.x;
					Vector2d bounce = new Vector2d(-l.v.x*(1-wdamp)*tbounce,-l.v.y*(1-wdamp)*tbounce);
					l.p.add(bounce);
					l.p.set(2*barrier-l.p.x,l.p.y);
					l.v.set(-l.v.x*wdamp,l.v.y);
				}
			}
			if(l.v.y != 0) {
				boolean collided = false;
				double barrier = 0;
				if(l.p.y < padding.y) {
					barrier = padding.y;
					collided = true;
				}
				else if(l.p.y > (window.y - padding.y)) {
					barrier = window.y - padding.y;
					collided = true;
				}
				
				if(collided) {
					double tbounce = (l.p.y-barrier)/l.v.y;
					Vector2d bounce = new Vector2d(-l.v.x*(1-wdamp)*tbounce,-l.v.y*(1-wdamp)*tbounce);
					l.p.add(bounce);
					l.p.set(l.p.x,2*barrier-l.p.y);
					l.v.set(l.v.x,-l.v.y*wdamp);
				}
			}
		}
	}

	public void display(GLAutoDrawable drawable) {
		// Drawing all particle
		for(Liquid l: liquids)
			l.display(drawable,color);
		
	}

	public void test() {
		// TODO Auto-generated method stub
		for(int i = 250; i < 300; ++i) {
			for(int j = 250; j < 300; ++j) {
				Liquid l = new Liquid(new Point2d(i,j));
				liquids.add(l);
			}
			
		}
	}

	public void init() {
		for(Liquid l: liquids) {
			l.a.set(0,0);
		}
	}

	public void spawnPush(double x, double y, double pushForce, double brushSize) {
		for(Liquid l: liquids) {
			Vector2d dd = new Vector2d(l.p.x-x,l.p.y-y);
			double length = dd.length();
			if(length < brushSize) {
				dd.normalize();
				dd.scale(length*pushForce/brushSize);
				l.a.set(l.a.x+dd.x,l.a.y+dd.y);
			}
		}
		
	}

	public void addParticle(Liquid l) {
		liquids.add(l);
		
	}
	
}
