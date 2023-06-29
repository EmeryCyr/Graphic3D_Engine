import java.util.*;
import java.io.*;

public class Graphic3D{
	// This array of characters represents 16 light levels 
	public static char[] light = 	{'.', ',', ':', '-', '+', '=', ';', '^', '*', '%', '#', 'U', 'O', 'N', 'H', 'M'};
	
										// rows collumns
	public static char[][] screen = new char[100][150];
	// I recomend at least 100 tall for best quality the height scales the quality.
	public static double[][] depth = new double[screen.length][screen[0].length];
	
	public static void main(String[] args)throws FileNotFoundException{
		
		//wait(5000);
		
		// Projection Matrix
		double zNear = 0.1;
		double zFar = 1000.0;
		double fov = 45.0;
		double aspectRatio = (double)screen.length / (double)screen[0].length;
		double fovRad = 1.0 / Math.tan(fov * 0.5 / 180.0 * Math.PI);
		
		Matrix matProj = new Matrix(4,4);
		matProj.matrix[0][0] = aspectRatio * fovRad;
		matProj.matrix[1][1] = fovRad;
		matProj.matrix[2][2] = zFar / (zFar - zNear);
		matProj.matrix[3][2] = ((-1 * zFar) * zNear) / (zFar - zNear);
		matProj.matrix[2][3] = 1.0;
		matProj.matrix[3][3] = 0.0;
		
		Entity teapot = new  Entity(makeMesh("Teapot.txt"), new Vector(0,20,70) ,0 ,0 ,0);
		
		Vector camera = new Vector(0,0,1);
		Vector light = new Vector(0,-1,0);
		
		//remove carot (aka cursor). It is very noisy if you leave it.
		System.out.print("\033[?25l");
		while (true){
			
			initalizeScreen(screen);
			
			drawEntity(screen, teapot, camera, light, matProj);
			teapot.rotateDeg(0.0,0.2,0.1);
			//teapot.position.z -= 5;
			
			
			
			//System.out.println(teapot.position);
			//System.out.println(teapot.getMesh());
			printScreen(screen);
			wait(500);
			clearScreen();
		}
		
	}
	
	public static Mesh makeMesh(String file)throws FileNotFoundException{
		File objFile  = new File(file);
		Scanner scanLine = new Scanner(objFile);
		
		int numOfVerts = 0;
		while (scanLine.hasNext()){
			String scan = scanLine.nextLine();
			if (scan.length() > 0){
				if (scan.charAt(0) == 'v') numOfVerts++;
			}
		}
		
		//System.out.println(numOfVerts);
		//wait(1000);
		
		Vector[]verts = new Vector[numOfVerts + 1];
		Mesh mesh = new Mesh();
		scanLine = new Scanner(objFile);
		
		int times = 0;
		while (scanLine.hasNextLine()){
			String strLine = scanLine.nextLine();
			Scanner scan = new Scanner(strLine);
			
			if (strLine.length() > 0){
				String str = scan.next();
				if (str.equals("v")){
					times++;
					verts[times] = new Vector(scan.nextDouble(),scan.nextDouble(),scan.nextDouble());
					//System.out.println("I got here " + times + " times!");
				}else if (str.equals("f")){
					Triangle tri = new Triangle(verts[scan.nextInt()],verts[scan.nextInt()],verts[scan.nextInt()]);
					mesh.addTriangle(tri);
					//System.out.println(tri);
				}
			}
		}
		return mesh;
	}
	
	public static Mesh makeCube(){
		Mesh meshCube = new Mesh();
		
		// SOUTH
		meshCube.addTriangle(new Triangle(new Vector(-1.0, -1.0, -1.0),new Vector(-1.0, 1.0, -1.0),new Vector(1.0, 1.0, -1.0)));
		meshCube.addTriangle(new Triangle(new Vector(-1.0, -1.0, -1.0),new Vector(1.0, 1.0, -1.0),new Vector(1.0, -1.0, -1.0)));
		
		// EAST
		meshCube.addTriangle(new Triangle(new Vector(1.0, -1.0, -1.0),new Vector(1.0, 1.0, -1.0),new Vector(1.0, 1.0, 1.0)));
		meshCube.addTriangle(new Triangle(new Vector(1.0, -1.0, -1.0),new Vector(1.0, 1.0, 1.0),new Vector(1.0, -1.0, 1.0)));
		
		// NORTH
		meshCube.addTriangle(new Triangle(new Vector(1.0, -1.0, 1.0),new Vector(1.0, 1.0, 1.0),new Vector(-1.0, 1.0, 1.0)));
		meshCube.addTriangle(new Triangle(new Vector(1.0, -1.0, 1.0),new Vector(-1.0, 1.0, 1.0),new Vector(-1.0, -1.0, 1.0)));
		
		// WEST
		meshCube.addTriangle(new Triangle(new Vector(-1.0, -1.0, 1.0),new Vector(-1.0, 1.0, 1.0),new Vector(-1.0, 1.0, -1.0)));
		meshCube.addTriangle(new Triangle(new Vector(-1.0, -1.0, 1.0),new Vector(-1.0, 1.0, -1.0),new Vector(-1.0, -1.0, -1.0)));
		
		// TOP
		meshCube.addTriangle(new Triangle(new Vector(-1.0, 1.0, -1.0),new Vector(-1.0, 1.0, 1.0),new Vector(1.0, 1.0, 1.0)));
		meshCube.addTriangle(new Triangle(new Vector(-1.0, 1.0, -1.0),new Vector(1.0, 1.0, 1.0),new Vector(1.0, 1.0, -1.0)));
		
		// BOTTOM
		meshCube.addTriangle(new Triangle(new Vector(1.0, -1.0, 1.0),new Vector(-1.0, -1.0, 1.0),new Vector(-1.0, -1.0, -1.0)));
		meshCube.addTriangle(new Triangle(new Vector(1.0, -1.0, 1.0),new Vector(-1.0, -1.0, -1.0),new Vector(1.0, -1.0, -1.0)));
		return meshCube;
	}
	
	public static void initalizeScreen(char[][] screen){
		// Initialize the screen with blank spaces
		for(int i = 0; i < screen.length; i++) {
			for(int j = 0; j < screen[i].length; j++) {
				if(i == screen.length - 1 || i == 0 || j == screen[i].length - 1 || j == 0){
					screen[i][j] = '0';
					depth[i][j] = 10000.0;
				}else{
					screen[i][j] = ' ';
					depth[i][j] = 10000.0;
				}
			}
		}
	}
	
	public static void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			// Handle the exception according to your program's requirements
			System.out.println("Sleep was interrupted");
		}
	}
	
	public static char getColor(double value, double min, double max){
		double range = max - min;
		int i = 0;
		while(min + 0.01 <= value){
			min += range/(double) light.length;
			i++;
			if (i == light.length - 1) break;
		}
		return light[i];
	}
	
	public static void printScreen(char[][] screen){
		// print the screen
		for(char[] row : screen) {
			for(char cell : row) {
				System.out.print(cell);
			}
			System.out.println();
		}
	}
	
	public static void clearScreen() {  
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * The drawLine method uses the Bresenham Line Algorithm to draw lines in the screen char array
	 * or any char array really
	 * 
	 * @param screen a 2d screen array that represents the char array that the line will be drawn to.
	 * @param x1 is the x position in the array of point 1 where the line starts.
	 * @param y1 is the y position in the array of point 1 where the line starts.
	 * @param x2 is the x position in the array of point 2 where the line ends.
	 * @param y2 is the y position in the array of point 2 where the line ends.
	 * @param c is the character that will be drawn.
	 * 
	 * 
	 */
	public static void drawLine(char[][] screen, int x1, int y1, int x2, int y2, char c){
		int w = x2 - x1 ;
		int h = y2 - y1 ;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
		if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
		if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
		if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
		int longest = Math.abs(w) ;
		int shortest = Math.abs(h) ;
		if (!(longest>shortest)) {
			longest = Math.abs(h) ;
			shortest = Math.abs(w) ;
			if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
			dx2 = 0 ;            
		}
		int numerator = longest >> 1 ;
		for (int i=0;i<=longest;i++) {
			if(y1 >= 0 && x1 >= 0 && y1 < screen.length && x1 < screen[y1].length){
				screen[y1][x1] = c ;
			}
			numerator += shortest ;
			if (!(numerator<longest)) {
				numerator -= longest ;
				x1 += dx1 ;
				y1 += dy1 ;
			} else {
				x1 += dx2 ;
				y1 += dy2 ;
			}
		}
	}
	
	public static void drawTriangle(char[][] screen, Triangle tri, char c){
		drawLine(screen,
				(int) Math.round(tri.tri[0].x),
				(int) Math.round(tri.tri[0].y),
				(int) Math.round(tri.tri[1].x),
				(int) Math.round(tri.tri[1].y),
				c);
		drawLine(screen,
				(int) Math.round(tri.tri[1].x),
				(int) Math.round(tri.tri[1].y),
				(int) Math.round(tri.tri[2].x),
				(int) Math.round(tri.tri[2].y),
				c);
		drawLine(screen,
				(int) Math.round(tri.tri[2].x),
				(int) Math.round(tri.tri[2].y),
				(int) Math.round(tri.tri[0].x),
				(int) Math.round(tri.tri[0].y),
				c);
	}
	
	public static void drawEntity(char[][]screen, Entity entity, Vector camera, Vector light, Matrix matProj){
		Mesh mesh = entity.getMesh();
		for(Triangle tri : mesh.tris){
			
			char c = ' ';
			Vector normal = new Vector(tri.normal());
			
			if(((normal.x * tri.tri[0].x - camera.x) +
				(normal.y * tri.tri[0].y - camera.y) +
				(normal.z * tri.tri[0].z - camera.z)) > 0) continue;
			
			//System.out.println(Math.round((normal.dotProduct(light))*1000.0)/1000.0);
			//wait(20);
			
			c = getColor(normal.dotProduct(light),-1,1);
			
			Triangle triProjected = new Triangle();
			
			// Project triangles from 3D --> 2D
			triProjected.tri[0] = tri.tri[0].multiplyMatrix(matProj);
			triProjected.tri[1] = tri.tri[1].multiplyMatrix(matProj);
			triProjected.tri[2] = tri.tri[2].multiplyMatrix(matProj);
			
			// Scale into view
			triProjected.tri[0].x += 1.0; triProjected.tri[0].y += 1.0;
			triProjected.tri[1].x += 1.0; triProjected.tri[1].y += 1.0;
			triProjected.tri[2].x += 1.0; triProjected.tri[2].y += 1.0;
			triProjected.tri[0].x *= 0.5 * (double)screen[0].length;
			triProjected.tri[0].y *= 0.25 * (double)screen.length;
			triProjected.tri[1].x *= 0.5 * (double)screen[0].length;
			triProjected.tri[1].y *= 0.25 * (double)screen.length;
			triProjected.tri[2].x *= 0.5 * (double)screen[0].length;
			triProjected.tri[2].y *= 0.25 * (double)screen.length;
			
			double depth = average(tri.tri[0].z,tri.tri[1].z,tri.tri[2].z);
			
			fillTriangle(screen,
						depth,
						triProjected,
						c);
			
			
			// Rasterize triangle
			/*
			drawTriangle(screen,
				triProjected,
				'.');
			*/
			
		}
	}
	
	public static double average(double num1 ,double num2 ,double num3){
		return (num1 + num2 + num3) / 3.0;
	}
	
	/**
	 * This method draws a filled in triangle to a character array the 
	 * method requires a 
	 * 
	 */
	public static void fillTriangle(char[][] screen,double dep ,Triangle tri, char c){
		
		// find the bounding box of the triangle
		int minX = (int)Math.min(Math.min(tri.tri[0].x, tri.tri[1].x), tri.tri[2].x);
		int maxX = (int)Math.max(Math.max(tri.tri[0].x, tri.tri[1].x), tri.tri[2].x);
		int minY = (int)Math.min(Math.min(tri.tri[0].y, tri.tri[1].y), tri.tri[2].y);
		int maxY = (int)Math.max(Math.max(tri.tri[0].y, tri.tri[1].y), tri.tri[2].y);
		
		// check each point in the bounding box to see if it's inside the triangle
		for (int y = minY; y <= maxY; y++){
			for (int x = minX; x <= maxX; x++){
				if(isInside(tri,x,y)){
					//System.out.println(dep);
					if(x >= 0 && x < screen[0].length && y >= 0 && y < screen.length){
						if(dep < depth[y][x] && dep > 1.0){
							screen[y][x] = c;
							depth[y][x] = dep;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Checks a 2D point to se if it is inside of a provided triangle.
	 * 
	 * @param Triangle tri - the 2D triangle being tested on.
	 * @param int x - the x value of the point being tested.
	 * @param int y - the y value of the point bing tested.
	 * 
	 * these points are integers in this method because this program draws
	 * onto a character array these parameters can be changed to doubles and
	 * the code will still work.
	 * 
	 */
	public static boolean isInside(Triangle tri, int x, int y) {
		double x0 = tri.tri[0].x;
		double y0 = tri.tri[0].y;
		double x1 = tri.tri[1].x;
		double y1 = tri.tri[1].y;
		double x2 = tri.tri[2].x;
		double y2 = tri.tri[2].y;
		
		double area = 0.5 * (-y1 * x2 + y0 * (-x1 + x2) + x0 * (y1 - y2) + x1 * y2);
		double s = 1 / (2 * area) * (y0 * x2 - x0 * y2 + (y2 - y0) * x + (x0 - x2) * y);
		double t = 1 / (2 * area) * (x0 * y1 - y0 * x1 + (y0 - y1) * x + (x1 - x0) * y);
		
		return s >= 0 && t >= 0 && (1 - s - t) >= 0;
	}
}
