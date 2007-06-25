/*
 * Created on 1-Jun-2006
 */
package ca.neo.util;

import org.apache.log4j.Logger;

import ca.neo.math.PDF;

/**
 * "Matrix Utilities". Utility methods related to matrices and vectors of floats.
 * 
 * TODO: test
 * 
 * @author Bryan Tripp
 */
public class MU {
	
	private static Logger ourLogger = Logger.getLogger(MU.class);

	/**
	 * @param matrix An array of arrays that is expected to be in matrix form
	 * @return True if all "rows" (ie array elements) have the same length
	 */
	public static boolean isMatrix(float[][] matrix) {
		boolean is = true;
		
		int dim = matrix[0].length;
		for (int i = 1; i < matrix.length && is; i++) {
			if (matrix[i].length != dim) {
				is = false;
			}
		}
		
		return is;
	}
	
	/**
	 * @param matrix Any matrix
	 * @return An identical but independent copy of the given matrix
	 */
	public static float[][] clone(float[][] matrix) {
		float[][] result = new float[matrix.length][];
		
		for (int i = 0; i < matrix.length; i++) {
			result[i] = new float[matrix[i].length];
			System.arraycopy(matrix[i], 0, result[i], 0, matrix[i].length);
		}
		
		return result;
	}
	
	/**
	 * @param vector Vector to copy from
	 * @param start Index in vector from which to start copying
	 * @param interval Interval separating copied entries in source vector (ie skip over interval-1 entries)
	 * @param end Index in vector at which copying ends
	 * @return Values copied from source vector
	 */
	public static float[] copy(float[] vector, int start, int interval, int end) {
		float[] result = null;
		
		if (interval == 1) {
			result = new float[end-start+1];
			System.arraycopy(vector, start, result, 0, result.length);
		} else {
			result = new float[Math.round((float) (end-start+1) / (float) interval)];
			int i = 0;
			for (int j = start; j < end; j=j+interval) {
				result[i++] = vector[j];
			}
			if (i < result.length-1) {
				float[] trim = new float[i+1];
				System.arraycopy(result, 0, trim, 0, trim.length);
				result = trim;
			}
		}
		
		return result;
	}
	
	/**
	 * @param X Any vector
	 * @param a Any scalar
	 * @return aX (each element of the vector multiplied by the scalar)
	 */
	public static float[] prod(float[] X, float a) {
		float[] result = new float[X.length];
		for (int i = 0; i < X.length; i++) {
			result[i] = X[i] * a;
		}
		return result;
	}
	
	/**
	 * @param X Any vector
	 * @param Y Any vector of the same length as X
	 * @return X'Y 
	 */
	public static float prod(float[] X, float[] Y) {
		if (X.length != Y.length) {
			throw new IllegalArgumentException("Vectors must have same length");
		}		
		
		float result = 0f;
		for (int i = 0; i < X.length; i++) {
			result += X[i] * Y[i];
		}
		
		return result;
	}
	
	/**
	 * @param A Any matrix 
	 * @param X Any vector with the same number of elements as there are columns in A
	 * @return AX
	 */
	public static float[] prod(float[][] A, float[] X) {
		assert isMatrix(A);
		
		if (A[0].length != X.length) {
			throw new IllegalArgumentException("Dimension mismatch: " + A[0].length + 
					" columns in matrix and " + X.length + " elements in vector");
		}
		
		float[] result = new float[A.length];
		
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < X.length; j++) {
				result[i] += A[i][j] * X[j];
			}
		}
		
		return result;
	}
	
	/**
	 * @param A Any m x n matrix 
	 * @param B Any n x p matrix 
	 * @return Product of matrices
	 */
	public static float[][] prod(float[][] A, float[][] B) {
		assert isMatrix(A);
		assert isMatrix(B);
		
		if (A[0].length != B.length) {
			throw new IllegalArgumentException("Dimension mismatch: " + A[0].length + 
					" columns in matrix A and " + B.length + " rows in matrix B");
		}
		
		float[][] result = new float[A.length][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[B[0].length];
			for (int j = 0; j < result[i].length; j++) {
				for (int k = 0; k < B.length; k++) {
					result[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		
		return result;
	}
	
	/**
	 * @param A Any matrix
	 * @param a Any scalar
	 * @return aA (each element of matrix multiplied by scalar)
	 */
	public static float[][] prod(float[][] A, float a) {
		assert isMatrix(A);
		
		float[][] result = new float[A.length][];
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[A[i].length];
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = A[i][j] * a;
			}
		}
		
		return result;
	}
	
	/**
	 * @param A Any m x n matrix 
	 * @param B Any m x n matrix
	 * @return The element-wise sum of the given matrices
	 */
	public static float[][] sum(float[][] A, float[][] B) {
		assert isMatrix(A);
		assert isMatrix(B);

		if (A[0].length != B[0].length) {
			throw new IllegalArgumentException("Dimension mismatch: " + A[0].length + 
					" columns in matrix A and " + B[0].length + " columns in matrix B");
		}
		
		if (A.length != B.length) {
			throw new IllegalArgumentException("Dimension mismatch: " + A.length + 
					" rows in matrix A and " + B.length + " rows in matrix B");
		}
		
		float[][] result = new float[A.length][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[A[0].length];
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = A[i][j] + B[i][j];
			}
		}
		
		return result;
	}
	
	/**
	 * @param A Any m x n matrix 
	 * @param B Any m x n matrix
	 * @return The element-wise difference of the given matrices (A-B)
	 */
	public static float[][] difference(float[][] A, float[][] B) {
		assert isMatrix(A);
		assert isMatrix(B);

		if (A[0].length != B[0].length) {
			throw new IllegalArgumentException("Dimension mismatch: " + A[0].length + 
					" columns in matrix A and " + B[0].length + " columns in matrix B");
		}
		
		if (A.length != B.length) {
			throw new IllegalArgumentException("Dimension mismatch: " + A.length + 
					" rows in matrix A and " + B.length + " rows in matrix B");
		}
		
		float[][] result = new float[A.length][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[A[0].length];
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = A[i][j] - B[i][j];
			}
		}
		
		return result;
	}
	
	/**
	 * @param X Any vector 
	 * @param Y Any vector same length as vector X
	 * @return X+Y (element-wise sum) 
	 */
	public static float[] sum(float[] X, float[] Y) {
		if (X.length != Y.length) {
			throw new IllegalArgumentException("Vectors must have same length");
		}
		
		float[] result = new float[X.length];
		for (int i = 0; i < X.length; i++) {
			result[i] = X[i] + Y[i];
		}
		
		return result;
	}

	/**
	 * @param X Any vector 
	 * @param Y Any vector same length as vector X
	 * @return X-Y (element-wise difference) 
	 */
	public static float[] difference(float[] X, float[] Y) {
		if (X.length != Y.length) {
			throw new IllegalArgumentException("Vectors must have same length");
		}
		
		float[] result = new float[X.length];
		for (int i = 0; i < X.length; i++) {
			result[i] = X[i] - Y[i];
		}
		
		return result;
	}

	/**
	 * @param matrix An array of float arrays (normally a matrix but can have rows of different length)
	 * @param rows Desired number of rows 
	 * @param cols Desired number of columns
	 * @return Matrix with requested numbers of rows and columns drawn from the given matrix, and padded 
	 * 		with zeros if there are not enough values in the original matrix
	 */
	public static float[][] shape(float[][] matrix, int rows, int cols) {
		float[][] result = new float[rows][];
		
		int fromRow = 0;
		int fromCol = -1;
		
		ourLogger.debug(matrix.length + " rows");
		ourLogger.debug(matrix[0].length + " cols");
		
		for (int i = 0; i < rows; i++) {
			result[i] = new float[cols];
			
			
			copyRow : for (int j = 0; j < cols; j++) {
				
				boolean atNextValue = false;
				while (!atNextValue) { //accounts for null or 0-length rows in original
					fromCol++;
					
					if (fromRow == matrix.length) {
						break copyRow;
					} else if (matrix[fromRow] == null || fromCol == matrix[fromRow].length) {
						fromRow = fromRow + 1;
						fromCol = -1;
					} else {
						atNextValue = true;
					} 
				}
				
				result[i][j] = matrix[fromRow][fromCol];
			}
		}
	
		return result;
	}
	
	/**
	 * @param matrix Any matrix
	 * @return The transpose of the matrix
	 */
	public static float[][] transpose(float[][] matrix) {		
		float[][] result = new float[0][];
		
		if (matrix.length > 0) {
			result = new float[matrix[0].length][];
		}
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[matrix.length];
			for (int j = 0; j < result[i].length; j++) {
				result[i][j] = matrix[j][i];
			}
		}
		return result;
	}
	
	/**
	 * @param entries A list of diagonal entries 
	 * @return A square diagonal matrix with given entries on the diagonal
	 */
	public static float[][] diag(float[] entries) {
		float[][] result = new float[entries.length][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[entries.length];
			result[i][i] = entries[i];
		}
		
		return result;
	}

	/**
	 * @param dimension # of rows/columns
	 * @return Identity matrix of specified dimension
	 */
	public static float[][] I(int dimension) {
		float[][] result = new float[dimension][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[dimension];
			result[i][i] = 1f;
		}
		
		return result;
	}

	/**
	 * @param rows Number of rows in the requested matrix 
	 * @param cols Number of columns in the requested matrix
	 * @return Matrix of zeroes with the given dimensions
	 */
	public static float[][] zero(int rows, int cols) {
		float[][] result = new float[rows][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[cols];
		}
		
		return result;
	}
	
	/**
	 * @param rows Number of rows in the requested matrix 
	 * @param cols Number of columns in the requested matrix
	 * @param value Value of each element
	 * @return Matrix with the given dimensions where each entry is the given value
	 */
	public static float[][] uniform(int rows, int cols, float value) {
		float[][] result = new float[rows][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[cols];
			for (int j = 0; j < cols; j++) {
				result[i][j] = value;
			}
		}
		
		return result;
	}
	
	/**
	 * @param rows Number of rows in the requested matrix 
	 * @param cols Number of columns in the requested matrix
	 * @param pdf One-dimensional PDF from which each element is drawn
	 * @return Matrix with the given dimensions where each entry is randomly drawn 
	 * 		from the given PDF
	 */
	public static float[][] random(int rows, int cols, PDF pdf) {
		float[][] result = new float[rows][];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = new float[cols];
			for (int j = 0; j < cols; j++) {
				result[i][j] = pdf.sample()[0];
			}
		}
		
		return result;		
	}

	/**
	 * @param matrix Any float matrix
	 * @return Duplicate with each element cast to double
	 */
	public static double[][] convert(float[][] matrix) {
		double[][] result = new double[matrix.length][];
		
		for (int i = 0; i < matrix.length; i++) {
			result[i] = new double[matrix[i].length];
			for (int j = 0; j < matrix[i].length; j++) {
				result[i][j] = (double) matrix[i][j];
			}
		}
		
		return result;
	}
	
	/**
	 * @param vector Any float vector
	 * @return Duplicate with each element cast to double
	 */
	public static double[] convert(float[] vector) {
		double[] result = new double[vector.length];
		
		for (int i = 0; i < vector.length; i++) {
			result[i] = (double) vector[i];
		}
		
		return result;
	}
	
	/**
	 * @param matrix Any double matrix
	 * @return Duplicate with each element cast to float 
	 */
	public static float[][] convert(double[][] matrix) {
		float[][] result = new float[matrix.length][];
		
		for (int i = 0; i < matrix.length; i++) {
			result[i] = new float[matrix[i].length];
			for (int j = 0; j < matrix[i].length; j++) {
				result[i][j] = (float) matrix[i][j];
			}
		}
		
		return result;		
	}
	
	/**
	 * @param vector Any double vector
	 * @return Duplicate with each element cast to float
	 */
	public static float[] convert(double[] vector) {
		float[] result = new float[vector.length];
		
		for (int i = 0; i < vector.length; i++) {
			result[i] = (float) vector[i];
		}
		
		return result;
	}
	
	/**
	 * @param vector Any vector
	 * @return Sum of elements
	 */
	public static float sum(float[] vector) {
		float result = 0f;
		
		for (int i = 0; i < vector.length; i++) {
			result += vector[i];
		}
		
		return result;
	}

	/**
	 * @param vector Any vector
	 * @return Mean of vector elements
	 */
	public static float mean(float[] vector) {
		float sum = 0f;
		
		for (int i = 0; i < vector.length; i++) {
			sum += vector[i];
		}
		
		return sum / (float) vector.length;
	}
	
	/**
	 * @param vector Any vector
	 * @param mean Value around which to take variance, eg MU.mean(vector) or some pre-defined value
	 * @return Bias-corrected variance of vector elements around the given values
	 */
	public static float variance(float[] vector, float mean) {
		float sum = 0f;		
		for (int i = 0; i < vector.length; i++) {
			float deviation = vector[i] - mean;
			sum += deviation*deviation;
		}
				
		return sum / ((float) vector.length - 1);
	}

	/**
	 * @param vector Any vector
	 * @return The vector normalized to 2-norm of 1
	 */
	public static float[] normalize(float[] vector) {
		float[] result = new float[vector.length];
		
		float sum = 0f;
		for (int i = 0; i < vector.length; i++) {
			sum += vector[i]*vector[i];
		}
		float norm = (float) Math.sqrt(sum);
		
		for (int i = 0; i < vector.length; i++) {
			result[i] = vector[i] / norm;
		}
		
		return result;
	}
	
	/**
	 * @param vector Any vector
	 * @param p Degree of p-norm (use -1 for infinity)
	 * @return The p-norm of the vector
	 */
	public static float pnorm(float[] vector, int p) {
		assert p != 0; //undefined
		
		float result = 0;
		
		if (p < 0) { //interpret as infinity-norm
			float max = 0;
			for (int i = 0; i < vector.length; i++) {
				if (Math.abs(vector[i]) > max) max = Math.abs(vector[i]); 
				if ( !(Math.abs(vector[i]) >= 0) ) max = Float.NaN;
			}
			result = max;
		} else {
			double sum = 0;
			for (int i = 0; i < vector.length; i++) {
				sum += Math.pow(Math.abs(vector[i]), (double) p);
			}
			result = (float) Math.pow(sum, 1.0 / (double) p);			
		}
		
		return result;
	}
	
	/**
	 * TODO: handle exponential notation
	 * 
	 * @param matrix Any matrix
	 * @param decimalPlaces number of decimal places to display for float values
	 * @return String representation of matrix with one row per line
	 */
	public static String toString(float[][] matrix, int decimalPlaces) {
		StringBuffer buf = new StringBuffer();
		
		float max = 0f;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (Math.abs(matrix[i][j]) > max) max = Math.abs(matrix[i][j]);
			}
		}
		String maxString = String.valueOf(max);
		int pre = maxString.length();
		if (maxString.indexOf(".") > 0) pre = maxString.indexOf(".");
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				buf.append(format(matrix[i][j], pre+2, decimalPlaces));
			}
			buf.append(System.getProperty("line.separator"));
		}
		
		return buf.toString();
	}
	
	//used by toString(...) 
	private static String format(float f, int pre, int post) {
		String s = String.valueOf(f);
		int index = s.indexOf(".");
		
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < pre-index; i++) {
			buf.append(" ");
		}		
		buf.append(s.substring(0, Math.min(s.length(), index + post + 1)));
		for (int i = 0; i < index + post + 1 - s.length(); i++) {
			buf.append(" ");
		}
		
		return buf.toString();
	}
	
	/**
	 * @param start Value of first element in vector
	 * @param increment Increment between adjacent elements
	 * @param end Value of last element in vector
	 * @return A vector with elements evenly incremented from <code>start</code> to <code>end</code> 
	 */
	public static float[] makeVector(float start, float increment, float end) {
		int len = 1 + (int) Math.round((end - start) / increment);
		
		float[] result = new float[len];
		for (int i = 0; i < len-1; i++) {
			result[i] = start + (float) i * increment; 
		}
		result[len-1] = end;
		return result;
	}
	
	/**
	 * A tool for growing vectors (similar to java.util.List). 
	 *    
	 * @author Bryan Tripp
	 */
	public static class VectorExpander {
		
		private static final int ourIncrement = 1000;
		
		private int myIndex;
		private float[] myValues;
		
		public VectorExpander() {
			myIndex = 0;
			myValues = new float[ourIncrement];
		}
		
		/**
		 * @param value New element to append
		 */
		public void add(float value) {
			if (myIndex == myValues.length) {
				float[] newValues = new float[myValues.length + ourIncrement];
				System.arraycopy(myValues, 0, newValues, 0, myValues.length);
				myValues = newValues;
			}
			
			myValues[myIndex++] = value;
		}
		
		/**
		 * @return Array of elements in order appended
		 */
		public float[] toArray() {
			float[] result = new float[myIndex];
			System.arraycopy(myValues, 0, result, 0, myIndex);
			return result;
		}
	}	
	
	/**
	 * A tool for growing matrices (similar to java.util.List). 
	 *    
	 * @author Bryan Tripp
	 */
	public static class MatrixExpander {
		
		private static final int ourIncrement = 1000;
		
		private int myIndex;
		private float[][] myValues;
		
		public MatrixExpander() {
			myIndex = 0;
			myValues = new float[ourIncrement][];
		}
		
		/**
		 * @param value New row to append
		 */
		public void add(float[] value) {
			if (myIndex == myValues.length) {
				float[][] newValues = new float[myValues.length + ourIncrement][];
				System.arraycopy(myValues, 0, newValues, 0, myValues.length);
				myValues = newValues;
			}
			
			myValues[myIndex++] = value;
		}

		/**
		 * @return Array of rows in order appended
		 */
		public float[][] toArray() {
			float[][] result = new float[myIndex][];
			System.arraycopy(myValues, 0, result, 0, myIndex);
			return result;
		}
	}	
	
}
