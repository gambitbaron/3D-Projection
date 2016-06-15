public class Vector{
	/**
	 * 3-space vector
	 */
	double x, y, z;
	Vector(){
		x = y = z = 0;
	}
	Vector(double x0, double y0, double z0){
		x = x0;
		y = y0;
		z = z0;
	}

	public double magnitude(){
		return Math.sqrt(VectorMath.dot(this, this));
	}
}