public class Entity{
	
	public Mesh mesh;
	public Vector position;
	
	// in degrees from 0 to 360.
	public double xDirection; 
	public double yDirection;
	public double zDirection;
	
	public Matrix rotX = new Matrix(4,4);
	public Matrix rotY = new Matrix(4,4);
	public Matrix rotZ = new Matrix(4,4);
	
	public Entity(){
		this.mesh = new Mesh();
		this.position = new Vector(0,0,0);
		this.xDirection = 0.0;
		this.yDirection = 0.0;
		this.zDirection = 0.0;
		
		setMats(0,0,0);
	}
	
	public Entity(Mesh mesh, Vector position, double xTheta, double yTheta, double zTheta){
		this.mesh = mesh;
		this.position = position;
		this.xDirection = xTheta;
		this.yDirection = yTheta;
		this.zDirection = zTheta;
		
		setMats(this.xDirection,this.yDirection,this.zDirection);
	}
	
	public void rotateDeg(double xTheta, double yTheta, double zTheta){
		this.xDirection += xTheta;
		this.yDirection += yTheta;
		this.zDirection += zTheta;
		
		setMats(this.xDirection,this.yDirection,this.zDirection);
	}
	
	public void setDirection(double xTheta, double yTheta, double zTheta){
		this.xDirection = xTheta;
		this.yDirection = yTheta;
		this.zDirection = zTheta;
		
		setMats(this.xDirection,this.yDirection,this.zDirection);
	}
	
	public Mesh getMesh(){
		Mesh newMesh = new Mesh();
		for(Triangle tri : this.mesh.tris){
			
			Triangle triRotatedX = new Triangle();
			Triangle triRotatedY = new Triangle();
			Triangle triRotatedZ = new Triangle();
			
			// Rotate in X-Axis
			triRotatedX.tri[0] = tri.tri[0].multiplyMatrix(rotX);
			triRotatedX.tri[1] = tri.tri[1].multiplyMatrix(rotX);
			triRotatedX.tri[2] = tri.tri[2].multiplyMatrix(rotX);
			
			
			// Rotate in Y-Axis
			triRotatedY.tri[0] = triRotatedX.tri[0].multiplyMatrix(rotY);
			triRotatedY.tri[1] = triRotatedX.tri[1].multiplyMatrix(rotY);
			triRotatedY.tri[2] = triRotatedX.tri[2].multiplyMatrix(rotY);
			
			// Rotate in Z-Axis
			triRotatedZ.tri[0] = triRotatedY.tri[0].multiplyMatrix(rotZ);
			triRotatedZ.tri[1] = triRotatedY.tri[1].multiplyMatrix(rotZ);
			triRotatedZ.tri[2] = triRotatedY.tri[2].multiplyMatrix(rotZ);
			
			//System.out.println("Here!");
			
			// Offset into the screen
			triRotatedZ.tri[0].x += position.x;
			triRotatedZ.tri[1].x += position.x;
			triRotatedZ.tri[2].x += position.x;
			
			triRotatedZ.tri[0].y += position.y;
			triRotatedZ.tri[1].y += position.y;
			triRotatedZ.tri[2].y += position.y;
			
			triRotatedZ.tri[0].z += position.z;
			triRotatedZ.tri[1].z += position.z;
			triRotatedZ.tri[2].z += position.z;
			
			newMesh.addTriangle(triRotatedZ);
		}
		return newMesh;
	}
	
	private void setMats(double xTheta,double yTheta, double zTheta){
		
		// Rotation X (Clockwise)
		rotX.matrix[0][0] = 1;
		rotX.matrix[1][1] = Math.cos(xTheta);
		rotX.matrix[1][2] = Math.sin(xTheta);  // Change sign
		rotX.matrix[2][1] = -Math.sin(xTheta); // Change sign
		rotX.matrix[2][2] = Math.cos(xTheta);
		rotX.matrix[3][3] = 1;
		
		// rotation Y (Clockwise)
		rotY.matrix[0][0] = Math.cos(yTheta);
		rotY.matrix[0][2] = -Math.sin(yTheta); // Change sign
		rotY.matrix[1][1] = 1;
		rotY.matrix[2][0] = Math.sin(yTheta);  // Change sign
		rotY.matrix[2][2] = Math.cos(yTheta);
		rotY.matrix[3][3] = 1;
		
		// rotation Z (Clockwise)
		rotZ.matrix[0][0] = Math.cos(zTheta);
		rotZ.matrix[0][1] = Math.sin(zTheta);  // Change sign
		rotZ.matrix[1][0] = -Math.sin(zTheta); // Change sign
		rotZ.matrix[1][1] = Math.cos(zTheta);
		rotZ.matrix[2][2] = 1;
		rotZ.matrix[3][3] = 1;
	}
}
