package mfp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.gl2.GLUT;

import mfp.tools.glVertex2dScale;
import mintools.parameters.BooleanParameter;
import mintools.parameters.DoubleParameter;
import mintools.swing.VerticalFlowPanel;
import mintools.viewer.EasyViewer;
import mintools.viewer.Interactor;
import mintools.viewer.SceneGraphNode;

public class Main implements SceneGraphNode, Interactor{

	private ParticleSystem system;
	private EasyViewer ev;
	
	private Vector2d defaultWindowSize = new Vector2d(1000,1000);
	//private Vector2d scale = new Vector2d();
	private Vector2d defaultPadding = new Vector2d(200,200);
	private Color3f WallColor = new Color3f(0.3f,0.3f,0.3f);
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		system = new ParticleSystem(defaultPadding,defaultWindowSize);
		ev = new EasyViewer("MultipleFluidProject", this, new Dimension(1000,1000), new Dimension(640,480));
        ev.addInteractor(this);
        
	}

	@Override
	public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glEnable( GL.GL_BLEND );
        gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
        gl.glEnable( GL.GL_LINE_SMOOTH );
        gl.glEnable( GL2.GL_POINT_SMOOTH );
        gl.glDisable( GL2.GL_LIGHTING );
        gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		
	}
	//private double dt = 2;
	private boolean repeat = false;
	@Override
	public void display(GLAutoDrawable drawable) {
		
		Vector2d getWindowSize = new Vector2d(drawable.getSurfaceWidth(),drawable.getSurfaceHeight());
		Vector2d scale = new Vector2d(getWindowSize.x/defaultWindowSize.x, getWindowSize.y/defaultWindowSize.y);
		glVertex2dScale.setScale(scale.x, scale.y);
		
		system.init();
		if(clicked) {
			system.spawnParticle(setPoint.x/scale.x,setPoint.y/scale.y);
		}
		if(run.getValue()) {
			system.advanceTime(dt.getValue(),9.8);
		}
		
		
		// TODO Auto-generated method stub
        GL2 gl = drawable.getGL().getGL2();
        glVertex2dScale.setGL(gl);
        EasyViewer.beginOverlay(drawable);
        gl.glColor4d( 0,1,0,1 );
        gl.glLineWidth(1);    
        
        //draw BorderWall
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        gl.glColor4d(0,0,0,1);
        glVertex2dScale.glVertex2d(defaultPadding.x,defaultPadding.y);
        glVertex2dScale.glVertex2d(defaultWindowSize.x-defaultPadding.x,defaultPadding.y);
        glVertex2dScale.glVertex2d(defaultPadding.x,defaultWindowSize.y-defaultPadding.y);
        glVertex2dScale.glVertex2d(defaultWindowSize.x-defaultPadding.x,defaultWindowSize.y-defaultPadding.y);
        gl.glEnd();
        
        //Draw Fluid
        system.display(drawable);

        gl.glColor4f(0.8f,0.8f,0.8f,1);
        String text = "Hieu Chau Nguyen\n";
        
        EasyViewer.printTextLines(drawable,  text, 10, 10, 12, GLUT.BITMAP_HELVETICA_10);
        EasyViewer.endOverlay(drawable);
	}

	BooleanParameter run = new BooleanParameter("simulate",false);
	DoubleParameter dt = new DoubleParameter("time step",0.005,0.001,10);
	DoubleParameter wdamp = new DoubleParameter("Collision damp",0.75,0.1,1);
	DoubleParameter g = new DoubleParameter("gravity",9.8,0.1,100);

	boolean clicked = false;
	Point2d setPoint = new Point2d(0,0);
	@Override
	public JPanel getControls() {
		// TODO Auto-generated method stub
        VerticalFlowPanel vfp = new VerticalFlowPanel();
        vfp.add(run.getControls());
        vfp.add(dt.getSliderControls(true));
        vfp.add(wdamp.getSliderControls(true));
        vfp.add(g.getSliderControls(true));
        return vfp.getPanel();
	}

	@Override
	public void attach(Component component) {
		// TODO Auto-generated method stub
		component.addMouseMotionListener(new MouseMotionAdapter() {
			
		});
		
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				clicked = true;
				setPoint.set(e.getX(),e.getY());
			}
		});
	}
}
