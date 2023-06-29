public class Triangle{
	
	public Vector[]tri = new Vector[3];
	
	public Triangle(){
		tri[0] = new Vector(0,0,0);
		tri[1] = new Vector(0,0,0);
		tri[2] = new Vector(0,0,0);
	}
	
	public Triangle(Vector p1 ,Vector p2 ,Vector p3){
		tri[0] = p1;
		tri[1] = p2;
		tri[2] = p3;
	}
	
	public String toString(){
		return "{" + tri[0] + " ," + tri[1] + " ," + tri[2] + "}";
	}
	
	public Vector normal() {
		// Create two vectors representing two sides of the triangle
		Vector edge1 = new Vector(this.tri[1].x - this.tri[0].x, this.tri[1].y - this.tri[0].y, this.tri[1].z - this.tri[0].z);
		Vector edge2 = new Vector(this.tri[2].x - this.tri[0].x, this.tri[2].y - this.tri[0].y, this.tri[2].z - this.tri[0].z);
		
		// The normal vector is the cross product of the two edges
		return edge1.crossProduct(edge2).normalize();
	}

}
