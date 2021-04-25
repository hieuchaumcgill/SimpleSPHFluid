package mfp.object;

import java.util.List;

import javax.vecmath.Color3f;

public class WallGroup extends ParticleGroup{
	List<Wall> walls;
	
	public WallGroup(Color3f color) {
		this.color = color;
	}
}
