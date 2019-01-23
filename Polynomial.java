package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	private static void traverse (Node front) {
		Node ptr = front; //make pointer point to the first node of the LL
		while (ptr!= null) {
			System.out.print(ptr.term.coeff + "x^" + ptr.term.degree + " + ");
			ptr=ptr.next;
		}
		System.out.println("\\");
	}

	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if(poly1 == null && poly2 == null) {
			System.out.println("Error: Both polynomials are empty!");
			return null;
		}
		if(poly1 == null && poly2!=null) {
			return poly2;
			
		}
		if(poly2 == null && poly1!=null) {
			return poly1;
		}
		
		Node ptrPoly1 = poly1;
		Node ptrPoly2 = poly2;
		Node sum = null;
		while (ptrPoly1 != null && ptrPoly2 != null) {
			if(ptrPoly1.term.degree == ptrPoly2.term.degree) {
				if(ptrPoly1.term.coeff + ptrPoly2.term.coeff == 0 ) {
					ptrPoly1 = ptrPoly1.next;
					ptrPoly2 = ptrPoly2.next;
				}
				else{
				sum = addToBack(ptrPoly1.term.coeff + ptrPoly2.term.coeff, 
								ptrPoly1.term.degree, sum);
				ptrPoly1 = ptrPoly1.next;
				ptrPoly2 = ptrPoly2.next;
				}
			} else if(ptrPoly1.term.degree < ptrPoly2.term.degree) {
				sum = addToBack(ptrPoly1.term.coeff,ptrPoly1.term.degree,sum);
				ptrPoly1 = ptrPoly1.next;
			}else {  
				//ptrPol1 > ptrPolly 2 in degree
				sum = addToBack(ptrPoly2.term.coeff,ptrPoly2.term.degree,sum);
				ptrPoly2 = ptrPoly2.next;
			}

		}
		


		//one may still have left overs
		if(ptrPoly1 != null) {
			while(ptrPoly1 !=null) {
				sum = addToBack(ptrPoly1.term.coeff,ptrPoly1.term.degree,sum);
				ptrPoly1 = ptrPoly1.next;
				
			}
		}
		if(ptrPoly2 !=null) {
			while(ptrPoly2 !=null) {
				sum = addToBack(ptrPoly2.term.coeff,ptrPoly2.term.degree,sum);
				ptrPoly2 = ptrPoly2.next;
			}
		}
		

		
		return sum;
	}
	

	private static Node addToFront (float coeff, int degree, Node front) {
		Node node = new Node (coeff, degree, front);
		return node;
	}
	
	private static Node addToBack (float coeff, int degree, Node front ) {
		if(front == null) {
			//list is empty, we'll add the first item
			return addToFront( coeff, degree, front);
		}else {
			Node ptr = front;
			while(ptr.next != null) {
				ptr = ptr.next;
			}
			//ptr points to the last item in the LL
			Node node = new Node (coeff,degree, null);
			ptr.next = node; //make the last item point to the new last item (node)
			return front;
		}
	
		
	}
	
	
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if(poly1 == null || poly2 == null) {
			System.out.println("Error: one polynomial is null");
			return null;
		}
		Node answer = null;
		
		for(Node ptrPoly1 = poly1; ptrPoly1 != null; ptrPoly1 = ptrPoly1.next) {
			
			
			for(Node ptrPoly2 = poly2; ptrPoly2 != null; ptrPoly2 = ptrPoly2.next) {
				Node newTerm = null;
				newTerm = addToBack(ptrPoly1.term.coeff * ptrPoly2.term.coeff, 
						ptrPoly1.term.degree + ptrPoly2.term.degree, newTerm);
				answer = add(newTerm,answer);
				//System.out.println("polyToAdd");
				//traverse(polyToAdd);
			}
	
		}
		
		
		System.out.println("This is my linked list answer");
		traverse(answer);
		return answer;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float answer = 0;
		
		if(poly == null) {
			return answer ;
		}
		
		Node ptr = poly;
		while(ptr!=null) {
			answer += Math.pow(x, ptr.term.degree) * ptr.term.coeff;
			ptr = ptr.next;
		}
		
		
		return answer;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
