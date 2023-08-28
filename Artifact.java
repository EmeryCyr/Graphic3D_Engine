public class Artifact{
	
	public Vector direction;
	public Vector position;
	public Vector velocity;
	public Vector rotVelocity;
	
	public Matrix rotX = new Matrix(4,4);
	public Matrix rotY = new Matrix(4,4);
	public Matrix rotZ = new Matrix(4,4);
	
	public void setDirection(Vector direction){
		this.direction = direction;
		
		setMats(this.direction.x,this.direction.y,this.direction.z);
	}
	
	public void rotateDeg(Vector direction){
		this.direction = direction;
		
		setMats(this.direction.x,this.direction.y,this.direction.z);
	}
	
	public void updatePos(){
		this.position = this.position.add(this.velocity);
	}
	
	public void updateRot(){
		this.direction = this.direction.add(this.rotVelocity);
		setMats(this.direction.x,this.direction.y,this.direction.z);
	}
	
	protected void setMats(double xTheta,double yTheta, double zTheta){
		
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
