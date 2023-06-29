public class Matrix{
	
	public double[][]matrix;
	
	public Matrix(){
		matrix = new double[0][0];
	}
	
	public Matrix(int rows,int columns){
		matrix = new double[rows][columns];
	}
	
	public double getValue(int row, int column){
		return this.matrix[row][column];
	}
}
