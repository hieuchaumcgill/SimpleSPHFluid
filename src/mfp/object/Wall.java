package mfp.object;

import javax.vecmath.Point2d;

public class Wall{
	Point2d startPoint;
	Point2d dimension;
	Point2d centerPoint;
	
	boolean isPad;
	
	public Wall(Point2d startPoint, Point2d dimension, Point2d centerPoint, boolean isPad) {
		this.startPoint = startPoint;
		this.centerPoint = centerPoint;
		this.dimension = dimension;
		this.isPad = isPad;
	}
}
