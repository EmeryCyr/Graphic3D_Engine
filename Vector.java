public class Vector {
	public double x;
	public double y;
	public double z;
	
	public Vector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vector(Vector v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	// Add two vectors
	public Vector add(Vector v) {
		return new Vector(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	// Subtract two vectors
	public Vector subtract(Vector v) {
		return new Vector(this.x - v.x, this.y - v.y, this.z - v.z);
	}
	
	// Calculate the dot product of two vectors
	public double dotProduct(Vector v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}
	
	// Calculate the cross product of this vector and another vector
	public Vector crossProduct(Vector v) {
		double crossX = this.y * v.z - this.z * v.y;
		double crossY = this.z * v.x - this.x * v.z;
		double crossZ = this.x * v.y - this.y * v.x;
		return new Vector(crossX, crossY, crossZ);
	}
	
	// Calculate the magnitude (length) of this vector
	public double magnitude() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	public Vector multiplyMatrix(Matrix m4x4){
		Vector out = new Vector();
		out.x = this.x * m4x4.getValue(0,0) + this.y * m4x4.getValue(1,0) + this.z * m4x4.getValue(2,0) + m4x4.getValue(3,0);
		out.y = this.x * m4x4.getValue(0,1) + this.y * m4x4.getValue(1,1) + this.z * m4x4.getValue(2,1) + m4x4.getValue(3,1);
		out.z = this.x * m4x4.getValue(0,2) + this.y * m4x4.getValue(1,2) + this.z * m4x4.getValue(2,2) + m4x4.getValue(3,2);
		double w = this.x * m4x4.getValue(0,3) + this.y * m4x4.getValue(1,3) + this.z * m4x4.getValue(2,3) + m4x4.getValue(3,3);
		
		if (w != 0.0){
			out.x /= w; out.y /= w; out.z /= w;
		}
		return out;
	}
	
	public Vector getNormal() {
		double magnitude = Math.sqrt(x * x + y * y + z * z);
		return new Vector(x / magnitude, y / magnitude, z / magnitude);
	}
	
	public String toString(){
		return "{" + this.x + " ," + this.y + " ," + this.z + "}";
	}
}

