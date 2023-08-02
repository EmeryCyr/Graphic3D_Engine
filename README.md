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

## Citation
* Based heavily on parts 22 - 25 of this [YouTube Playlist](https://www.youtube.com/playlist?list=PLrOv9FMX8xJE8NgepZR1etrsU63fDDGxO) by javidx9.
* [Euler angles](https://en.wikipedia.org/wiki/Euler_angles) are used to calculate the new vectors of the vertices of a rotated mesh.
* [How to read an object file in java.](https://examples.javacodegeeks.com/java-development/core-java/io/file/how-to-read-an-object-from-file-in-java/)
