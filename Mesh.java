import java.util.*;

public class Mesh {
	List<Triangle> tris;
	
	public Mesh() {
		this.tris = new ArrayList<>();
	}
	
	public Mesh(Mesh mesh) {
		this.tris = mesh.tris;
	}
	
	public void addTriangle(Triangle triangle) {
		this.tris.add(triangle);
	}
	
	public List<Triangle> getTriangles() {
		return this.tris;
	}
	
	public String toString(){
		return tris.toString();
	}
}
