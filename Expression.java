package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
	//My helper methods
	private static String clean(String expr) {
		expr = expr.replace(" ", "");
		expr = expr.replace("\t", "");
		return expr;
 
	}
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
    	//use the variable name and tokenizer
    	//A + bi[a+c]
    	//A is a token
    	//+ is a token
    	//bi token
    	//[token
    	//a token
    	
    	//clean expression of spaces
    	String clean = clean(expr);
    	System.out.println("Clean:" + clean);
    	StringTokenizer st = new StringTokenizer(clean, delims, true);
    	  
    	while (st.hasMoreTokens()) {
    		//test if token
    		String unknown ="";
    		if(st.hasMoreTokens()) {
	    		unknown = st.nextToken();
	    		System.out.println("unknown:" + unknown);
    		}    	
    		//if stuck on token
    		if (unknown.equals("+")||
    				unknown.equals("-")||
    				unknown.equals("*")||
    				unknown.equals("/")||
    				unknown.equals("[")||
    				unknown.equals("]")||
    				unknown.equals("(")|| 
    				unknown.equals(")")){
    				System.out.println(unknown);
    			continue;
    		}

    		//test if it is a number
    		if(Character.isDigit(unknown.charAt(0))== true ) {
    			//it is a number
    			System.out.println("number"); 
    			continue;
    		}
    		//test if it is an array
    		if(st.hasMoreTokens()) {
	    		if(st.nextToken().equals("[")) {
	    			Array arr = new Array(unknown);
	    			int arri = arrays.indexOf(arr);
	    			//check if array already exists in list
	    			 if (arri == -1) {
	    				 arrays.add(arr);
	    				 System.out.println("Array " + unknown + " added");
	    				 continue;
	    			 }    
	    			 continue;
	    		}
    		}
    		//if it's not a number then it must be a variable but check if variable exists
            Variable varTotest = new Variable(unknown);
            int varNum = vars.indexOf(varTotest);
            if (varNum == -1) {
            	//make the variable
        		vars.add(varTotest);
        		System.out.println("Variable " + unknown + " added");
            }

    	}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	//BASE
    	//one thing in it
    	
    		expr = clean(expr);
	    	if(expr.length()== 1) {
	    		//is it a variable
	    		if(Character.isDigit(expr.charAt(0))== false) {
	    			return getVariableValue(expr, vars);
	    			
	    		}
	    		//I got me a number
	    		else {
	    			return Character.getNumericValue(expr.charAt(0));    			
	    		}		
	    	}
	    	
	    	//DO I ONLY HAVE NUMBERS!!!!
	    	if(onlyNumbers(expr) == true) {
	    		return(numFromString(expr));		
	    	}
	    	//Do I have just an Array with a number
	    	if(onlyASimpleArray(expr) == true) {
	    		StringTokenizer st = new StringTokenizer(expr, delims, true);
	    		//I should only have a number on my third thing
	    		String arrayName = st.nextToken();
	    		String bracket = st.nextToken();
	    		String strNumber = st.nextToken();
	    		float actualNum = numFromString(strNumber);
	    		System.out.println("Array value " + getArrayValue (arrayName,actualNum,arrays));
	    		return getArrayValue (arrayName,actualNum,arrays);
	    		
	    	}
	    	
	    	//NOT BASE
	    	
	    		
	    		
	    	StringTokenizer st = new StringTokenizer(expr, delims, false);
	    	String currentIssue = null;
	    	Stack <Float> Numbers = new Stack<>();
	    	Stack <Character> Operators = new Stack<>();
    		char firstThing = ' ';
    		String possible = null;

	    	while(expr.equals("") == false) {
	    		if(st.hasMoreTokens()==true) {
		    		currentIssue = st.nextToken();
		    		firstThing = expr.charAt(0);
	    		}
	    		else {
	    			currentIssue = possible;
		    		firstThing = expr.charAt(0);

	    		}
    			System.out.println("exp " + expr);
    			System.out.println("currentIssue " + currentIssue);
	    		if(firstThing == '+') {
	    			Operators.push('+');
	    			expr=expr.substring(1);
		    		firstThing = expr.charAt(0);
	    		}
	    		else if(firstThing == '-') {
	    			Operators.push('-'); 
	    			expr=expr.substring(1);
		    		firstThing = expr.charAt(0);

	    		}
	    		else if(firstThing == '/') {
	    			Operators.push('/');
	    			expr=expr.substring(1);
		    		firstThing = expr.charAt(0);

	    		}
	    		else if(firstThing == '*') {
	    			Operators.push('*');
	    			System.out.println("U pushed *");
	    			expr=expr.substring(1);
		    		firstThing = expr.charAt(0);

	    		}
	    		 if(firstThing == '(') {
	    			
	    			String toEval = findSubExpression( expr, 0, true, false);
	    			float result = evaluate(toEval, vars, arrays);
	    			expr = expr.substring(toEval.length() + 2);
	    			
	    			if(expr.equals("")== false) {
	    				System.out.println("EXPR " + expr);
	    			
	    				firstThing = expr.charAt(0);
	    			}    		
	    			
	    			if(Operators.isEmpty()== false) {	    				
	    				if(Operators.peek()== '/') {
		    				float prev = Numbers.pop();
		    				result = prev / result;
		    				Operators.pop();
		    				Operators.push('+');

	    				}
	    				else if (Operators.peek()== '*'){
	    					float prev = Numbers.pop();
		    				result = prev * result;
		    				Operators.pop();
		    				Operators.push('+');

	    				}
	    				
	    				else if (Operators.peek()== '-') {
		    				result =  (-1) * result;
		    				Operators.pop();
		    				Operators.push('+');

	    					
	    				}
	    			}
	    			
	    			System.out.println("ultimate " + result);
	    			Numbers.push(result);
	    			   
	    			System.out.println("toEval" + toEval);
	    			StringTokenizer newSt = new StringTokenizer(toEval, delims);
	    			String lll = null;
	    			while(newSt.hasMoreTokens()==true && st.hasMoreTokens()== true) {
	    				lll = st.nextToken();
	    				System.out.println("LLL " + lll);
	    				newSt.nextToken();
	    				System.out.println("LLL " + lll);

	    			}
	    			if(st.hasMoreTokens()== false) {
	    				possible = lll;
	    			}
	    			continue;
	    			
	    		}
	    
	    		
	    		//if it is a number
	    		
	    		if(onlyNumbers(currentIssue) == true) {
	    			float result = 0;
	    			result = (numFromString(currentIssue));	
	    			expr = expr.substring(currentIssue.length());
	    			
	    			
	    			if(Operators.isEmpty()== false) {	    				
	    				if(Operators.peek()== '/') {
		    				float prev = Numbers.pop();
		    				result = prev / result;
		    				Operators.pop();
		    				Operators.push('+');



	    				}
	    				else if (Operators.peek()== '*'){
	    					float prev = Numbers.pop();
		    				result = prev * result;
		    				Operators.pop();
		    				Operators.push('+');

	    				}
	    				else if (Operators.peek()== '-') {
		    				result =  (-1) * result;
		    				Operators.pop();
		    				Operators.push('+');

	    					
	    				}
	    			}
	    			System.out.println("pushed " + result);
	    			Numbers.push(result);
	    			continue;

	    			
	    		}
	    	
	    		//if it is a var
	    		else if(expr.length() == 1 || expr.charAt(currentIssue.length()) != '[') {
	    			float result = getVariableValue(currentIssue, vars);
	    			expr = expr.substring(currentIssue.length());
    			
	    			if(Operators.isEmpty()== false) {	    				
	    				if(Operators.peek()== '/') {
		    				float prev = Numbers.pop();
		    				result = prev / result;
		    				Operators.pop();
		    				Operators.push('+');



	    				}
	    				else if (Operators.peek()== '*'){
	    					float prev = Numbers.pop();
		    				result = prev * result;
		    				Operators.pop();
		    				Operators.push('+');

	    				}
	    				else if (Operators.peek()== '-') {
		    				result =  (-1) * result;
		    				Operators.pop();
		    				Operators.push('+');

	    					
	    				}
	    			}
	    			Numbers.push(result);

	    		}
	    		//if it is a array
	    		else {
	    			int begin = expr.indexOf("[");
	    			String toEval = findSubExpression( expr, begin, false, true);
	    			float result = evaluate(toEval, vars, arrays);
	    			
	    			expr = expr.substring(toEval.length() + 2 + currentIssue.length());
	    			result = getArrayValue(currentIssue,(int)result, arrays);
	    			if(Operators.isEmpty()== false) {	    				
	    				if(Operators.peek()== '/') {
		    				float prev = Numbers.pop();
		    				result = prev / result;
		    				Operators.pop();
		    				Operators.push('+');



	    				}
	    				else if (Operators.peek()== '*'){
	    					float prev = Numbers.pop();
		    				result = prev * result;
		    				Operators.pop();
		    				Operators.push('+');

	    				}
	    				else if (Operators.peek()== '-') {
		    				result =  (-1) * result;
		    				Operators.pop();
		    				Operators.push('+');

	    					
	    				}
	    			}

	    			Numbers.push(result);
	    			
	    			StringTokenizer newSt = new StringTokenizer(toEval, delims);
	    			while(newSt.hasMoreTokens()==true && st.hasMoreTokens()== true) {
	    				st.nextToken();
	    				newSt.nextToken();
	    			}
	    			
	    		}

	    		
	    		
	    		
	    		
	    	}
    		float answer = 0;
    		
    		while(Numbers.isEmpty()== false) {
    			float popped = Numbers.pop();
    			System.out.println("popped" + popped);
    			
    			answer = popped + answer;
    			System.out.println("answer " + answer);
    		}
    		
    		return answer;
	    	
	    	
	    	
	    	
	    	
	    	
	    	
    	
    	
    	
    }
    
    public static float Compute(String[] arrayExpr, ArrayList<Variable> vars) {
    	//scan for division, change variables to numbers, change subtraction
    	float answer = 0;
    	Stack <Float> Numbers = new Stack<>();
    	Stack <Character> Operators = new Stack<>();
    	Stack <Float> toAdd = new Stack<>();
    	for(int ii = 0; ii < arrayExpr.length; ii++) {
    		
    		
    		if(arrayExpr[ii].equals("/") ) {
    			arrayExpr[ii] = "*";
        		if(Character.isDigit(arrayExpr[ii+1].charAt(0))== false) {
        			//I have a variable that must be inverted
        			float variableToGet = getVariableValue(arrayExpr[ii+1], vars);
        		    variableToGet =  (float) (Math.pow(variableToGet, -1.0));	
        		    arrayExpr[ii+1] = variableToGet + "";
        		}
        	
    		}
    		else if(Character.isDigit(arrayExpr[ii+1].charAt(0))== false) {
    			
    		}
    	}
    	
    	
    	return answer;
    	
    }
    	
    public static float 
    Messevaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	expr = clean(expr);
    	
    	//one thing in it
    	if(expr.length()== 1) {
    		//is it a variable
    		if(Character.isDigit(expr.charAt(0))== false) {
    			return getVariableValue(expr, vars);
    			
    		}
    		//I got me a number
    		else {
    			return Character.getNumericValue(expr.charAt(0));    			
    		}		
    	}
    	
    	float currentNum = 0;
    	//DO I ONLY HAVE NUMBERS!!!!
    	if(onlyNumbers(expr) == true) {
    		return(numFromString(expr));		
    	}
    	//Do I have just an Array with a number
    	if(onlyASimpleArray(expr) == true) {
    		StringTokenizer st = new StringTokenizer(expr, delims, true);
    		//I should only have a number on my third thing
    		String arrayName = st.nextToken();
    		String bracket = st.nextToken();
    		String strNumber = st.nextToken();
    		float actualNum = numFromString(strNumber);
    		System.out.println("Array value " + getArrayValue (arrayName,actualNum,arrays));
    		return getArrayValue (arrayName,actualNum,arrays);
    		
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	//crud I still have a full expression
    	
    	StringTokenizer st = new StringTokenizer(expr, delims, true);
    	Stack <Character> Operators = new Stack<>();
    	Stack <Float> Numbers = new Stack<>();	 
    	String unknown = "";
    	String unknown2 = "";
    	boolean oneMore = true;
    	boolean parHandled = false;
    	boolean brackHandled = false;
    	int counter = 0;
    	
    	while (expr.equals("")== false ) {
    		
    		
    		//test if token
    		if(st.hasMoreTokens()) {
    			//if I have not done anything to unknown
    			if(unknown.equals("")&&unknown2.equals("")) {
    				unknown = st.nextToken();
    				counter++;
    				if(st.hasMoreTokens()) {
    					unknown2 = st.nextToken();
    				}
    				    			}
    			else {
    				unknown = unknown2;
    				unknown2 = st.nextToken();
    				counter++;
    			}
	    		
	    		System.out.println("unknown1: " + unknown + " unknown2: " + unknown2);
    		} 
    		else {
    			unknown = unknown2;
    			counter++;
    			oneMore = false;
    		}
    		
    		if(unknown.equals("]") || unknown.equals(")") ) {
    			counter++;
    			expr=expr.substring(1);
    			continue;
    		}
    		//if stuck on token
    		/*if (unknown.equals("+")||
    				unknown.equals("-")||
    				unknown.equals("*")||
    				unknown.equals("/")||
    				unknown.equals("[")||
    				unknown.equals("]")||
    				unknown.equals("(")|| 
    				unknown.equals(")")){
    				System.out.println(unknown);
    			continue;
    		}*/
    		//test for addition
    		if(unknown.equals("+")) {
    			Operators.push('+');
    			expr=expr.substring(1);
    			continue;
    		}
    		else if(unknown.equals("-")) {
    			Operators.push('-'); 
    			expr=expr.substring(1);
    			continue;
    		}
    		else if(unknown.equals("/")) {
    			Operators.push('/');
    			expr=expr.substring(1);
    			continue;
    		}
    		else if(unknown.equals("*")) {
    			Operators.push('*');
    			expr=expr.substring(1);
    			continue;
    			
    		}
    		
    		
    		
    		


    		//test if it is an array
    		if(st.hasMoreTokens()) {
	    		if(unknown2.equals("[")) {
	    			//evaluate subexpression
	    			int intFirstBrack = expr.indexOf("[");
	        		String subExpression = findSubExpression(expr,intFirstBrack, false, true);
	        		int subResult = (int)(evaluate(subExpression, vars, arrays));
	        		float arrayResult = getArrayValue(unknown, subResult, arrays);
	        		if(Numbers.isEmpty()==true) {
	        			Numbers.push(arrayResult);
	        		}
	        		else {
	        			currentNum = arrayResult;
	        			brackHandled = true;
	        		}
	        		System.out.println("I have been pushed " + arrayResult);
	        		while(st.hasMoreTokens() == true && st.nextToken()!="]"  ) {
	        			if(st.hasMoreTokens() == true) {
	        				unknown = unknown2;
	        				unknown2 = st.nextToken();
	        			}
	        		}
	        		
	        		System.out.println("I have been reached ");

	        		//it ends on ]
	    			
	    		}
    		}//end of array test
    		
    		//test if it is a paran
    		if(st.hasMoreTokens()) {
	    		if(unknown.equals("(")) {
	    			//System.out.println("I have a paranetheses");
	    			//evaluate subexpression
	    			int intFirstBrack = expr.indexOf("(");
	        		String subExpression = findSubExpression(expr,intFirstBrack, true, false);
	        		float result = evaluate(subExpression, vars, arrays);
	        		System.out.println("I have been pushed " + result);
	        		//test whether he ) is really the right one
	        		while(st.hasMoreTokens() == true && unknown2.equals(")")  ) {
	        			if(st.hasMoreTokens() == true) {
	        				unknown = unknown2;
	        				unknown2 = st.nextToken();
	        			}
	        		}
	        		
	        		unknown = unknown2;
	        		if(st.hasMoreTokens() == true) {
	        			unknown2 = st.nextToken();
	        		}
	        		else {
	        			
	        		}
	        		System.out.println(" HELP unknown " + unknown + "unknown2 " + unknown2);

	        		if(Numbers.isEmpty()==true) {
	        			Numbers.push(result);
	        			continue;
	        		}
	        		else {
	        			currentNum = result;
	        		}
	        		
	        		//System.out.println("I have been reached ");

	        		//it ends on )
	    			
	    		}
    		}//end of array test
    		
    		//test if it is a number
    		System.out.println("THIS IS WHAT I CHECK FOR NUM " + unknown);
    		if(Character.isDigit(unknown.charAt(0))== true && parHandled == false && brackHandled == false) {
    			System.out.println("I know I have a number");
    			currentNum = numFromString(unknown);
    		}
    		//I have a variable
    		else if (parHandled == false && brackHandled == false){
    			currentNum = getVariableValue(unknown,  vars);
    			
    		}
    		parHandled = false;
    		brackHandled = false;
    		//Operations
    		if(Operators.isEmpty()== false) {
	  			if(Operators.peek() == '/') {
					currentNum = (float) (Math.pow(currentNum, -1.0));
					Operators.pop();
					if(Numbers.isEmpty()==false) {
						float prev = Numbers.pop();
						currentNum = prev * currentNum;
					}
				}
	  			
	  			else if(Operators.peek() == '*') {
	  				if(Numbers.isEmpty()==false) {
	  					float prev = Numbers.pop();
						Operators.pop();
						currentNum = prev * currentNum;
	  				}
	  				
	  			}
	    			
	  			else if(Operators.peek() == '-') {
	    				currentNum = (-1) * currentNum;
	    				Operators.pop();
						if(Numbers.isEmpty()==false) {
							float prev = Numbers.pop();
							currentNum = prev + currentNum;
						}
	    		}
	    		else if(Operators.peek() == '+') {
	    			if(Numbers.isEmpty()==false) {
						float prev = Numbers.pop();
						Operators.pop();
						System.out.println("I am adding" + currentNum + " + " + prev);
						currentNum = prev + currentNum;
						System.out.println("answer" + currentNum);
					}
	    		}
    		}
			Numbers.push(currentNum);
			System.out.println("pushed" + currentNum);
			




    		
    		
    		
    		
    		
    		
    		
    		//by process of elimination I have a variable    		
    		
    		
    		
    		
    		//
    		
    		

    		

    		
    		
   	}//while
    
    	if(Numbers.isEmpty()==false && Operators.isEmpty()== true) {
    		System.out.println("final counter " + counter);
    		return Numbers.pop();
    	
    	}
    	System.out.println("ERROR");
    	return -1;
    	
    	
    	
    	
    	  	
    }
    
    
    
    public static String 
    recursivEvaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	
    	
    	return null;
    	
    }
    //find subexpression!!!
    
    private static String findSubExpression (String expr, int startIndex, boolean firstPar, boolean firstBrack) {
    	expr = clean(expr);
    	String target;
    	if(firstPar == true) {
    		target = ")";
    	}
    	else {
    		target = "]";
    	}
    	if(target.equals("]") ){
    		if(howManyBrackets(expr) > 1 && expr.lastIndexOf("]")!= expr.length()-1) {
    			int thisEndIndex = expr.indexOf("]");
    			System.out.println("String rr ");
    			return expr.substring(startIndex+1, thisEndIndex);
    			
    			
    			
    		}
    		else {
    			 return expr.substring(startIndex+1, expr.lastIndexOf("]"));
    		}
    		
    	}
    	
    	if(expr.indexOf(")+(")== -1 && expr.indexOf(")-(") == -1 && 
    			expr.indexOf(")*(") == -1 &&expr.indexOf(")/(") == -1) {
    		int endIndex = expr.lastIndexOf(target);
    		System.out.println("String "+  expr + "from " + startIndex +": " + expr.charAt(startIndex));//+
			//"to " + endIndex +": " + expr.charAt(endIndex));
    		return expr.substring(startIndex+1, endIndex);    		
    	}// messy case
    	
    	else {
    		int endIndex = -1;
    		if(expr.indexOf(")+(")!= -1) {
    			int thisIndex = expr.indexOf(")+(");
    			if(endIndex == -1 || thisIndex<endIndex) {
    				endIndex = thisIndex;
    			}
    			
    		}
    		 if(expr.indexOf(")-(")!= -1) {
    			int thisIndex = expr.indexOf(")-(");
    			if(endIndex == -1 || thisIndex<endIndex) {
    				endIndex = thisIndex;
    			}
    		}
    		 if(expr.indexOf(")*(")!= -1) {
    			int thisIndex = expr.indexOf(")*(");
    			if(endIndex == -1 || thisIndex<endIndex) {
    				endIndex = thisIndex;
    			}
    		}
    		if(expr.indexOf(")/(")!= -1) {
    			int thisIndex = expr.indexOf(")/(");
    			if(endIndex == -1 || thisIndex<endIndex) {
    				endIndex = thisIndex;
    			}
    		}
    		
    		return expr.substring(startIndex+1, endIndex);
    		
    	}
    }
    
    
    //have parantheses 
    
    //have parantheses 
	
	private static int howManyParantheses(String expr) {
		StringTokenizer everything = new StringTokenizer(expr, "(", true);
		StringTokenizer notEverything = new StringTokenizer(expr, "(", false);
		return everything.countTokens() - notEverything.countTokens();
		
	}

    
    private static int howManyBrackets(String expr) {
    	StringTokenizer everything = new StringTokenizer(expr, "[", true);
    	StringTokenizer notEverything = new StringTokenizer(expr, "[", false);
    	return everything.countTokens() - notEverything.countTokens();
    }
    
    //get value
    private static float getVariableValue(String var, ArrayList<Variable> vars) {     
        for(Variable indexVar : vars) {
        	if(indexVar.name.equals(var)) {
        		return indexVar.value;
        	}
        	
        }   
        return 0;      	
    }
    private static float getArrayValue(String arrayVar, float index,ArrayList<Array> arrays) {
    	int intIndex = (int) index;
    	for (Array indexArray : arrays) {
    		if(indexArray.name.equals(arrayVar)) {
        		return indexArray.values[intIndex];
        	}
    	}
    	return 0;
    }

    private static boolean onlyNumbers(String expr) {
    	boolean onlyNumbers= true;
    	for(int ii = 0; ii < expr.length(); ii++) {
    		if(Character.isDigit(expr.charAt(ii))== false) {
    			onlyNumbers = false;
    			break;
    		}	
    	}
    	return onlyNumbers;
    }
    
    private static boolean onlyASimpleArray(String expr) {
    	StringTokenizer st = new StringTokenizer(expr, delims, true);
    	if(st.countTokens()==4) {
    		  while (st.hasMoreTokens()) {
        		//test if token
        		String unknown ="";
        		if(st.hasMoreTokens()) {
    	    		unknown = st.nextToken();
    	    		//System.out.println("unknown:" + unknown);
        		}    	
        		//if stuck on token
        		if (unknown.equals("+")||
        				unknown.equals("-")||
        				unknown.equals("*")||
        				unknown.equals("/")||
        				unknown.equals("(")|| 
        				unknown.equals(")")){
        			return false;
        		}

          		//test if it is an array
        		if(st.hasMoreTokens()) {
    	    		if(st.nextToken().equals("[")) {
    	    			return true;
    	    		}
    	    	}
    		  }//while
    	}
    	return false;

    	
    }
    
    private static float numFromString (String expr) {
    	boolean onlyNumbers= true;
    	float answer = 0;
    	for(int ii = 0; ii < expr.length(); ii++) {
    		if(Character.isDigit(expr.charAt(ii))== false) {
    			onlyNumbers = false;
    			break;
    		}	
    		//start forming number
    		else {
    			float currentNum = Character.getNumericValue(expr.charAt(ii));
    			answer +=  currentNum * Math.pow(10, expr.length() - 1 - ii );
    		}
    	}
    	if(onlyNumbers == true) {
    		return answer;
    	}
    	return answer;
    }
}

