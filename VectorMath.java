
public class VectorMath {
	/**
	 * Class providing static mathematical functions
	 * for use with Vector class objects
	 */
	public static Vector subtract(Vector a, Vector b){
		return new Vector(a.x-b.x, a.y-b.y, a.z-b.z);
	}
	public static Vector add(Vector a, Vector b){
		return new Vector(a.x+b.x, a.y+b.y, a.z+b.z);
	}
	public static Vector scale(Vector a, double multiple){
		return new Vector(multiple*a.x, multiple*a.y, multiple*a.z);
	}
	public static double angle(Vector a, Vector b){
		return Math.acos(dot(a, b) / (a.magnitude() * b.magnitude()));
	}
	public static Vector projAOntoB(Vector a, Vector b){
		return scale(b, (dot(a, b)) / dot(b, b));
	}
	public static double dot(Vector a, Vector b){
		return a.x*b.x + a.y*b.y + a.z*b.z;
	}
	public static void setVector(Vector a, Vector b, double distance){
		/**
		 * changes a location vector, corresponding to angle vector and distance 
		 * @param a location vector
		 * @param b angle vector
		 * @param distance magnitude of translation
		 */
		a.x = distance * Math.cos(b.y) * Math.cos(b.x); 
		a.y = distance * Math.sin(b.y);
		a.z = distance * Math.cos(b.y) * Math.sin(b.x);
	}
}
