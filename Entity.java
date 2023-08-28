public class Entity extends Artifact{
	
	public Mesh mesh;
	
	// in degrees from 0 to 360.
	public double xDirection; 
	public double yDirection;
	public double zDirection;
	
	public Entity(){
		this.mesh = new Mesh();
		this.position = new Vector(0,0,0);
		this.velocity = new Vector(0,0,0);
		this.direction = new Vector(0,0,0);
		this.rotVelocity = new Vector(0,0,0);
		
		setMats(this.direction.x,this.direction.y,this.direction.z);
	}
	
	public Entity(Mesh mesh, Vector position, Vector velocity, Vector direction,Vector rotVelocity){
		this.mesh = mesh;
		this.position = position;
		this.velocity = velocity;
		this.rotVelocity = rotVelocity;
		
		this.direction = direction;
		
		setMats(this.direction.x,this.direction.y,this.direction.z);
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
}
