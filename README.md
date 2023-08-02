# Graphic3D_Engine
## WARNING: This program may potentially trigger seizures for people with photosensitive epilepsy. Viewer discretion is advised.
Summer Project 2023 A 3D Graphics engine that runs entirely in the command line and prints out an object file as an ASCII art style 3D render of the object.

## Classes
### Graphic3D
This is the main class that runs what I have of the program so far.
Inside the while loop, you can edit the properties of an entity using the methods located in the entity class.
#### Static variables
* color - Character array - This array lists all the usable characters I have found so far in order by visual density. So far, I only have eight characters.
#### Methods
* **makeMesh** - reads the provided obj file and outputs a Mesh object.
  * Parameter: file - String - The file path of the obj file.
  * Returns: Mesh object.
* **initalizeScreen** - sets all elements in a 2D character array to the space character.
  * Parameter: screen - 2D Character Array - The array being reset.
  * Optional Parameter: c - Character - the character the outer border will be set to.
  * Throws: IAE if the provided array is null.
* **wait** - waits for a provided number of milliseconds.
  
### Entity
This class object represents a 3D object with a position in space, a rotation, and a Mesh. A mesh is defined in the Mesh class. 
  
### Mesh
A mesh is a list of triangles and their respective coordinates in space.
  
### Triangle
A Triangle is three Vectors.
  
### Vector
A Vector is supposed to be a direction and a magnitude. The tools are provided to find those values, but I treat vectors like a point in my class.
  
### Matrix
A matrix is a 2D array of values that can be used in some complex math.
  
### Light, Artifact, Camera
These Classes are not implemented, but they are a thought that I had to try to make things make more sense. I would love to expand this project and make it more streamlined, but I have reached the edge of what I know how to do. I plan to revisit this after I have learned more about computer science and linear algebra.
  
## Citation
* Based heavily on parts 22 - 25 of this [YouTube Playlist](https://www.youtube.com/playlist?list=PLrOv9FMX8xJE8NgepZR1etrsU63fDDGxO) by javidx9.
* [Euler angles](https://en.wikipedia.org/wiki/Euler_angles) are used to calculate the new vectors of the vertices of a rotated mesh.
* [How to read an object file in java.](https://examples.javacodegeeks.com/java-development/core-java/io/file/how-to-read-an-object-from-file-in-java/)
