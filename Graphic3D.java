import java.util.*;
import java.io.*;

/*
 * This program is my atempt at making a 3D graphics engine that runs in
 * the windows terminal using characters printed to the screen as pixels.
 * The density of the pixels in each character determines its "color".
 * 
 * As of now the program can produce a monocrome image of a simple 3D object.
 * The user can define and change the position and rotation of the object. 
 * 
 */

public class Graphic3D{
	
	// This array of characters represents a number of light levels. I plan to expand this to many more characters.
	public static char[] color = 	{'.', ':', ';', '+', '*', 'H', '#', 'M'};
	
	public static void main(String[] args)throws FileNotFoundException{
		
		char[][] screen = new char[100][250];
		double[][] depth = new double[screen.length][screen[0].length];
		
		double dis = 50.0;
		
		//create new entity
		Entity teapot = new  Entity(makeMesh("Objects/Teapot.obj"), new Vector(0,15,dis) ,0 ,0 ,0);
		
		//set the direction of the light and the camera
		//camera is currently meaningless 
		Vector camera = new Vector(0,0,0);
		Vector light = new Vector(1,-1,0);
		
		//remove carot (aka cursor). It is very noisy if you leave it.
		System.out.print("\033[?25l");
		
		long frames = 0;
		long FPS = 0;
		long fTime = System.currentTimeMillis();
		
		double angle = 0.0;
		
		System.out.println("WARNING: This program may potentially trigger seizures for people with photosensitive epilepsy. Viewer discretion is advised.");
		wait(5000);
		clearScreen();
		
		// Im sure there is a better way than to use a while true here
		while (true){
			
			frames++;
			
			long start = System.currentTimeMillis();
			
			initalizeScreen(screen, depth, '-');
			
			drawEntity(screen, depth, teapot, camera, light);
			teapot.rotateDeg(0.0,0.1,0.04);
			
			
			angle += 0.03;
			if (angle > Math.PI / 2.0) angle = -1.0 * (Math.PI / 2.0 );
			teapot.position.x = dis * Math.sin(angle);
			//System.out.println(teapot.position.x);
			teapot.position.z = dis * Math.cos(angle);
			//System.out.println(teapot.position.z);
			
			teapot.position.y += 0.5;
			if (teapot.position.y > screen.length) teapot.position.y = -5.0;
			
			
			//System.out.println(teapot.position);
			//System.out.println(teapot.getMesh());
			printScreen(screen);
			
			long end = System.currentTimeMillis();
			long elapsed = end - start;
			
			if (System.currentTimeMillis() - fTime > 1000){
				FPS = frames;
				frames = 0;
				fTime = System.currentTimeMillis();
			}
			System .out.println("FPS: " + FPS);
			
			System.out.println("Frame time: " + elapsed + " ms");
			if (elapsed < 50) wait(200 - (int)elapsed);
			else wait(200);
			clearScreen();
		}
	}
	
	/**
	 * For now this method can read very simple obj files that only contain
	 * verticies and triangular faces. I'm working on support for quads.
	 * 
	 * @param string of the file path. In my case I store my object files in the folder Objects.
	 * @throws FNFE if the File path does not reach a file
	 * @throws IAE if the file is null.
	 */
	public static Mesh makeMesh(String file)throws FileNotFoundException{
		if (file == null) throw new IllegalArgumentException("File must exist");
		File objFile  = new File(file);
		Scanner scanLine = new Scanner(objFile);
		
		//keeping track of the number of verticies is important in obj files
		//here I am checking the file to see how many verticies are in the file.
		//this is a very ineficient way of doing this.
		int numOfVerts = 0;
		while (scanLine.hasNext()){
			String scan = scanLine.nextLine();
			if (scan.length() > 0){
				if (scan.charAt(0) == 'v') numOfVerts++;
			}
		}
		
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
	
	/**
	 * This method sets all elements in a 2D char array to the space character.
	 * 
	 * @param screen - a 2D character array that represents a screen
	 * @throws illegal argument exception if the array is null.
	 * 
	 */
	public static void initalizeScreen(char[][] screen, double[][]depth){
		if (screen == null) throw new IllegalArgumentException("char Array" + screen + "was null");
		// Initialize the screen with blank spaces
		for(int i = 0; i < screen.length; i++) {
			for(int j = 0; j < screen[i].length; j++) {
					screen[i][j] = ' ';
					depth[i][j] = 10000.0;
			}
		}
	}
	
	/**
	 * This method sets all elements in a 2D char array to the space character.
	 * Also adds a border around the outside of the array using the provided
	 * character.
	 * 
	 * @param screen - a 2D character array that represents a screen
	 * @param c - a character that the outer bounds will be set to
	 * @throws illegal argument exception if the array is null.
	 * 
	 */
	public static void initalizeScreen(char[][] screen, double[][]depth, char c){
		if (screen == null) throw new IllegalArgumentException("char Array" + screen + "was null!");
		// Initialize the screen with blank spaces
		for(int i = 0; i < screen.length; i++) {
			for(int j = 0; j < screen[i].length; j++) {
				if(i == screen.length - 1 || i == 0 || j == screen[i].length - 1 || j == 0){
					screen[i][j] = c;
					depth[i][j] = 10000.0;
				}else{
					screen[i][j] = ' ';
					depth[i][j] = 10000.0;
				}
			}
		}
	}
	
	/**
	 * This method makes the program sleep for a desired amount of milliseconds
	 * @param milliseconds - The ammout of milliseconds that you want the program to sleep for.
	 * @throws Illegl argument exception - if the number of milliseconds is negitive.
	 */
	public static void wait(int milliseconds) {
		if (milliseconds < 0) throw new IllegalArgumentException("Quantity cannot be negitive");
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			// Handle the exception according to your program's requirements
			System.out.println("Sleep was interrupted");
		}
	}
	
	/**
	 * 
	 * This method is used to determine the light value of a triangle based
	 * on the dot product between its normal vector and the normal vector of the light source.
	 * 
	 * the theoritical max should be 1 and theoretical minimum shoud be -1
	 * for the purposes of the light value.
	 * 
	 * this method really just breaks the range between the minimum and maximum values up into a number of parts based
	 * on the size of the provided array and finds where the provided value falls in that range. Then it returns an apropriate character
	 * from the array.
	 * 
	 * Im sure there is a better way of doing this.
	 * 
	 * @param char[]color - the array being used to test the value
	 * @param double value - the value that is being tested.
	 * @param double min - the minimum value that value could be.
	 * @param double max - the maximum value that value could be.
	 * @throws IAE if the array is null.
	 * 
	 */
	public static char getColor(char[]color, double value, double min, double max){
		if (color == null) throw new IllegalArgumentException("array " + color + " was null");
		if (value < min) return color[0];
		if (value > max) return color[color.length - 1];
		double range = max - min;
		int i = 0;
		while(min + 0.01 <= value){
			min += range/(double) color.length;
			i++;
			if (i == color.length - 1) break;
		}
		return color[i];
	}
	
	/**
	 * 
	 * @param screen 2D charater array that represents pixels on a screen
	 * @throws IAE if the array is null.
	 * 
	 */
	public static void printScreen(char[][] screen){
		if (screen == null) throw new IllegalArgumentException("Array must exist.");
		// print the screen
		for(char[] row : screen) {
			String str = "";
			for(char cell : row) {
				str += cell;
			}
			System.out.println(str);
		}
	}
	
	/**
	 * 
	 * Clears all characters in the windows terminal.
	 * I dont know how it works and I cant remember where I found it.
	 * 
	 * 
	 */
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
	
	/**
	 * 
	 * This method works in conjunction with the drawLine method to draw a provided triangle
	 * onto the provided character array using the provided character.
	 * 
	 * This was inteded to be used to draw a wireframe mesh of 3D objects
	 * but could be used in 2D graphics as well.
	 * 
	 * @param screen a 2D character array representing a screen of pixels.
	 * @param tri a triangle object to be drawn onto the screen.
	 * @param c a character to use as the pixel to draw the lones of the triangle.
	 * @throws IAE if the array is null.
	 * @throws IAE if triangle is null.
	 * 
	 */
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
	
	/**
	 * This method draws an entity to the screen by projecting all of the entities triangles onto
	 * a plane. then the new projected triangles are drawn to the screen.
	 * 
	 * @param draw 
	 * @param 
	 * @param
	 * @param 
	 * @param 
	 * 
	 */
	public static void drawEntity(char[][]screen,double[][]depth , Entity entity, Vector camera, Vector light){
		
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
		
		Mesh mesh = entity.getMesh();
		for(Triangle tri : mesh.tris){
			
			char c = ' ';
			Vector normal = new Vector(tri.normal());
			
			if(((normal.x * tri.tri[0].x - camera.x) +
				(normal.y * tri.tri[0].y - camera.y) +
				(normal.z * tri.tri[0].z - camera.z)) > 0) continue;
			
			//System.out.println(Math.round((normal.dotProduct(light))*1000.0)/1000.0);
			//wait(20);
			
			c = getColor(color,normal.dotProduct(light),-1,1);
			
			Triangle triProjected = new Triangle();
			
			// Project triangles from 3D --> 2D
			triProjected.tri[0] = tri.tri[0].multiplyMatrix(matProj);
			triProjected.tri[1] = tri.tri[1].multiplyMatrix(matProj);
			triProjected.tri[2] = tri.tri[2].multiplyMatrix(matProj);
			
			// move into view
			triProjected.tri[0].x += 1.0; triProjected.tri[0].y += 1.0;
			triProjected.tri[1].x += 1.0; triProjected.tri[1].y += 1.0;
			triProjected.tri[2].x += 1.0; triProjected.tri[2].y += 1.0;
			
			//scale
			//x
			triProjected.tri[0].x *= 0.5 * (double)screen[0].length;
			triProjected.tri[1].x *= 0.5 * (double)screen[0].length;
			triProjected.tri[2].x *= 0.5 * (double)screen[0].length;
			
			//y half size because letters are tall
			triProjected.tri[0].y *= 0.25 * (double)screen.length;
			triProjected.tri[1].y *= 0.25 * (double)screen.length;
			triProjected.tri[2].y *= 0.25 * (double)screen.length;
			
			double dep = average(tri.tri[0].z,tri.tri[1].z,tri.tri[2].z);
			if (dep > zNear){
				List<Triangle> tris = new ArrayList<>();
				
				clipTriangle(screen, triProjected, tris);
				
				// Draw filled in triangle to screen
				for (Triangle t: tris){
					fillTriangle(screen,
								depth,
								dep,
								t,
								c);
				}
				
				// Rasterize triangle (wireframe)
				/*
				drawTriangle(screen,
					triProjected,
					'.');
				*/
			}
		}
	}
	
	public static void clipTriangle(char[][]screen, Triangle ori, List<Triangle> tris){
		
		// X section ---------------------------------------------------
		
		List<Vector> tri = new ArrayList<>();
		List<Triangle> trisc = new ArrayList<>();
		
		int outx1 = 0;
		int outx2 = 0;
		for(Vector v: ori.tri){
			if(v.x < 0) outx1++;
			else if(v.x > screen[0].length) outx2++;
			
			if(v.x < 0 || v.x > screen[0].length) tri.add(v);
			else tri.add(0,v);
		}
		
		if(outx1 == 0 && outx2 == 0){
			trisc.add(ori);
			
		}else if(outx1 == 1 && outx2 == 0){
			Vector p3 = tri.get(0);
			Vector p2 = tri.get(1);
			Vector p1 = tri.get(2);
			
			double ma = (p2.y - p1.y)/(p2.x - p1.x);
			double mb = (p3.y - p1.y)/(p3.x - p1.x);
			
			//System.out.println(ma + " " + mb);
			
			Vector a = new Vector(0, ma * (0 - p1.x) + p1.y, 0);
			Vector b = new Vector(0, mb * (0 - p1.x) + p1.y, 0);
			
			trisc.add(new Triangle(a, p2, b));
			trisc.add(new Triangle(b, p2, p3));
			
			/*
			System.out.println("p1: " + p1);
			System.out.println("p2: " + p2);
			System.out.println("p3: " + p3);
			System.out.println("A: " + a);
			System.out.println("B: " + b);
			System.out.println("-----------");
			wait(1000);
			*/
			
		}else if(outx1 == 2 && outx2 == 0){
			Vector p1 = tri.get(0);
			Vector p2 = tri.get(1);
			Vector p3 = tri.get(2);
			
			double ma = (p2.y - p1.y)/(p2.x - p1.x);
			double mb = (p3.y - p1.y)/(p3.x - p1.x);
			
			//System.out.println(ma + " " + mb);
			
			Vector a = new Vector(0, ma * (0 - p1.x) + p1.y, 0);
			Vector b = new Vector(0, mb * (0 - p1.x) + p1.y, 0);
			
			trisc.add(new Triangle(a, b, p1));
			
		}else if(outx1 == 3 && outx2 == 0){
			//do nothing
			
		}else if(outx1 == 0 && outx2 == 1){
			Vector p3 = tri.get(0);
			Vector p2 = tri.get(1);
			Vector p1 = tri.get(2);
			
			double x = (double) screen[0].length;
			double ma = (p2.y - p1.y)/(p2.x - p1.x);
			double mb = (p3.y - p1.y)/(p3.x - p1.x);
			
			Vector a = new Vector(x ,ma * (x - p1.x) + p1.y, 0);
			Vector b = new Vector(x ,mb * (x - p1.x) + p1.y, 0);
			
			trisc.add(new Triangle(a, p2, b));
			trisc.add(new Triangle(b, p2, p3));
			
		}else if(outx1 == 0 && outx2 == 2){
			Vector p1 = tri.get(0);
			Vector p2 = tri.get(1);
			Vector p3 = tri.get(2);
			
			double x = (double) screen[0].length;
			double ma = (p2.y - p1.y)/(p2.x - p1.x);
			double mb = (p3.y - p1.y)/(p3.x - p1.x);
			
			//System.out.println(ma + " " + mb);
			
			Vector a = new Vector(x, ma * (x - p1.x) + p1.y, 0);
			Vector b = new Vector(x, mb * (x - p1.x) + p1.y, 0);
			
			trisc.add(new Triangle(a, b, p1));
			
		}else if(outx1 == 0 && outx2 == 3){
			//do nothing
			
		}else{
			//do nothing
		}
		
		// Y section ---------------------------------------------------
		
		for(Triangle t: trisc){
			
			int outy1 = 0;
			int outy2 = 0;
			
			List<Vector> tri2 = new ArrayList<>();
			
			for(Vector v: t.tri){
				if(v.y < 0) outy1++;
				else if(v.y > screen.length) outy2++;
				
				if(v.y < 0 || v.y > screen.length) tri2.add(v);
				else tri2.add(0,v);
			}
			
			if(outy1 == 0 && outy2 == 0){
				tris.add(t);
				
			}else if(outy1 == 1 && outy2 == 0){
				Vector p3 = tri2.get(0);
				Vector p2 = tri2.get(1);
				Vector p1 = tri2.get(2);
				
				double ma = (p2.x - p1.x)/(p2.y - p1.y);
				double mb = (p3.x - p1.x)/(p3.y - p1.y);
				
				//System.out.println(ma + " " + mb);
				
				Vector a = new Vector(ma * (0 - p1.y) + p1.x, 0, 0);
				Vector b = new Vector(mb * (0 - p1.y) + p1.x, 0, 0);
				
				tris.add(new Triangle(a, p2, b));
				tris.add(new Triangle(b, p2, p3));
				
				/*
				System.out.println("p1: " + p1);
				System.out.println("p2: " + p2);
				System.out.println("p3: " + p3);
				System.out.println("A: " + a);
				System.out.println("B: " + b);
				System.out.println("-----------");
				wait(1000);
				*/
				
			}else if(outy1 == 2 && outy2 == 0){
				Vector p1 = tri2.get(0);
				Vector p2 = tri2.get(1);
				Vector p3 = tri2.get(2);
				
				double ma = (p2.x - p1.x)/(p2.y - p1.y);
				double mb = (p3.x - p1.x)/(p3.y - p1.y);
				
				//System.out.println(ma + " " + mb);
				
				Vector a = new Vector(ma * (0 - p1.y) + p1.x, 0, 0);
				Vector b = new Vector(mb * (0 - p1.y) + p1.x, 0, 0);
				
				tris.add(new Triangle(a, b, p1));
				
			}else if(outy1 == 3 && outy2 == 0){
				//do nothing
				
			}else if(outy1 == 0 && outy2 == 1){
				Vector p3 = tri2.get(0);
				Vector p2 = tri2.get(1);
				Vector p1 = tri2.get(2);
				
				double x = (double) screen.length;
				double ma = (p2.x - p1.x)/(p2.y - p1.y);
				double mb = (p3.x - p1.x)/(p3.y - p1.y);
				
				Vector a = new Vector(ma * (screen.length - p1.y) + p1.x, screen.length, 0);
				Vector b = new Vector(mb * (screen.length - p1.y) + p1.x, screen.length, 0);
				
				tris.add(new Triangle(a, p2, b));
				tris.add(new Triangle(b, p2, p3));
				
			}else if(outy1 == 0 && outy2 == 2){
				Vector p1 = tri2.get(0);
				Vector p2 = tri2.get(1);
				Vector p3 = tri2.get(2);
				
				double x = (double) screen.length;
				double ma = (p2.x - p1.x)/(p2.y - p1.y);
				double mb = (p3.x - p1.x)/(p3.y - p1.y);
				
				Vector a = new Vector(ma * (screen.length - p1.y) + p1.x, screen.length, 0);
				Vector b = new Vector(mb * (screen.length - p1.y) + p1.x, screen.length, 0);
				
				tris.add(new Triangle(a, b, p1));
				
			}else if(outy1 == 0 && outy2 == 3){
				//do nothing
				
			}else{
				//do nothing
			}
		}
	}
	
	/**
	 * 
	 * Method that thakes the average of three doubles
	 * The only use in this program at the moment is to take the average of the z values of each point of a triangle
	 * to determine the average depth of the triangle. This is a hacky way to get around the pixel depth issue until
	 * I can find a solution.
	 * @param num1,num2,num3 - Doubles that are being tested.
	 * 
	 */
	public static double average(double num1 ,double num2 ,double num3){
		return (num1 + num2 + num3) / 3.0;
	}
	
	/**
	 * This method draws a filled in 2D triangle to a character array.
	 * 
	 * @param char[][]screen - the screen that is being drawn to.
	 * @param double[][]depth - is the depth values of the screen character array.
	 * @param double - dep is the depth value
	 * @throws IAE if screen is null.
	 * @throws IAE if depth is null.
	 * @throws IAE if triangle is null.
	 * 
	 */
	public static void fillTriangle(char[][] screen, double[][] depth,double dep ,Triangle tri, char c){
		if (screen == null) throw new IllegalArgumentException("Array must exist"); 
		if (depth == null) throw new IllegalArgumentException("Array must exist");
		if (tri == null) throw new IllegalArgumentException("Triangle must exist");
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
	 * @throws IAE if triange is null.
	 * 
	 * these points are integers in this method because this program draws
	 * onto a character array these parameters can be changed to doubles and
	 * the code will still work.
	 * 
	 */
	public static boolean isInside(Triangle tri, int x, int y) {
		if (tri == null) throw new IllegalArgumentException("Triangle was null");
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
