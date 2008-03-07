/*
 * Created on 5-Jun-2006
 */
package ca.neo.math;

import java.io.Serializable;

/**
 * <p>Finds coefficients on a set of functions so that their linear combination approximates 
 * a target Function. In other words, finds a_i so that sum(a_i * f_i(x)) roughly equals 
 * t(x) in some sense, where f_i are the functions to be combined and t is a target function,  
 * both over some range of the variable x.</p> 
 * 
 * <p>Can be used to find decoding vectors and synaptic weights.</p> 
 * 
 * @author Bryan Tripp
 */
public interface LinearApproximator extends Serializable, Cloneable {

	/**
	 * Note: more information is needed than the arguments provide (for example 
	 * the functions that are to be combined to estimate the target). These other 
	 * data are object properties. This enables re-use of calculations based on these 
	 * data, for estimating multiple functions. 
	 * 
	 * @param target Function to approximate
	 * @return coefficients on component functions which result in an approximation of the 
	 * 		target
	 */
	public float[] findCoefficients(Function target);
	
	public LinearApproximator clone() throws CloneNotSupportedException;
	
}
