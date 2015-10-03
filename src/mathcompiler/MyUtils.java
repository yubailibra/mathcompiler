//File:         [MyUtils.java]
//Created:      [09/15/2015]
//Last Changed: $Date: 09/15/2015 $
//Author:       <A HREF="mailto:[ybai11@jhu.edu]">[Yu Bai]</A>
//
package mathcompiler;

public class MyUtils {
	/**
	[This class provides a collection of useful static methods for manipulation of PseudoString instances,
	and conversions among different types of expressions etc.]
   	@see [MyUtils#findExpressionType]
    @see [MyUtils#infix2Postfix]
    @see [MyUtils#checkPrecedence]
   	@see [MyUtils#cloneChars]
    @see [MyUtils#int2Chars]
    @see [MyUtils#digit2Char]
    @see [MyUtils#cloneChars]
	@see [MyUtils#int2Char]
    @see [MyUtils#isValidOperator]
    @see [MyUtils#isValidOperand]
    @see [MyUtils#isOperand]
	**/

	/**definition of expression types**/
	public static final int POSTFIX=1;
	public static final int PREFIX=2;
	public static final int INFIX=3;
	public static final int INCORRECT=-1;	//an incorrect expression that doesn't belong to any expression type even before the evaluation

	/**definition of validation codes for expressions**/
	public static final int VALID=0;
	public static final int BAD_OPERAND=1;	
	public static final int BAD_OPERATOR=2;
	public static final int BAD_INFIX=3;	
	public static final int INCORRECT_EXPRESSION=4;	
	public static final int MISSING_OPERAND=5;
	public static final int MISSING_OPERATOR=6;
	public static final int SINGLE_OPERAND=7;
	
	
	/**
	 * identity the expression type as one of the POSTFIX, PREFIX or INFIX
	 * @param expression
	 */
	public static int findExpressionType(PseudoString expression){
		if(expression.getLength()<1){
			System.out.println("WARNING: empty expression. no type can be identified");
			return INCORRECT;
		}
		char first=expression.getCharAt(0);
		char last=expression.getCharAt(expression.getLength()-1);
		if(!isOperand(new PseudoString(first)) && isOperand(new PseudoString(last))){
			return PREFIX;
		}else if(isOperand(new PseudoString(first)) && !isOperand(new PseudoString(last))){
			return POSTFIX;
		}else if((isOperand(new PseudoString(first)) || first=='(') && (isOperand(new PseudoString(last))||last==')')){
			return INFIX;
		}else{		//the expression is simply wrong as it cannot be identified as any of the pre-, post- or infix type
			return INCORRECT;
		}
	}

	/**
	 * Convert infix expression to postfix, assuming all operands & operators are single-letter.
	 * Code is adopted from the example shown in p115 in A&T text book.
	 * @param infix
	 * @return postfix
	 */
	public static PseudoString infix2Postfix(PseudoString infix){
		MyStack opstk=new MyStack();
		char[] poststr=new char[infix.getLength()]; //the length of a postfix will not exceed that of an infix 
		char inChar;			//a character in the infix 
		char topChar=' ';			//a character at the top of the stack, default is white space
		boolean wasEmpty=true;  //save stack status before each actual pop 
		int posIn=0;			//index for the character in infix 
		int posOut=0;	//index for the character in the poststr
	
		//read in infix string from left to right
		for(posIn=0;posIn<infix.getLength();posIn++){
			inChar = infix.getCharAt(posIn);		 
			if(isValidOperand(new PseudoString(inChar))){ //if the character is an operand, save it to poststr starting from the lowest index 
				poststr[posOut++]=inChar;
			}else if(isValidOperator(inChar)){				//if inChar is operator, compare it with the top element in opstk if any
				if(!opstk.empty()){
					wasEmpty=false;
					topChar = opstk.pop().getCharAt(0);	//only the 1st letter is needed assuming single-letter operand
					if (topChar==')'){
						System.out.println("WARNING: ) is an invalid operator to occur in the operator stack.");
						return null;
					}
				}else{
					wasEmpty=true;
				}
				//As long as opstk is not empty and inChar has a lower precedence than the top element in opstk (aka topChar), 
				//output topChar to the poststr array and repeat on the next element in opstk. 
				//Note opstk must pop to present topChar for precedence comparison, 
				//however we need its status (empty or not) before the actual pop together with the 
				//precedence to do conditional test for the WHILE loop. Hence, wasEmpty is used. 
				//The method checkPrecedence is defined below.
				while (!wasEmpty && checkPrecedence(topChar, inChar)){
					poststr[posOut++]=topChar;
					if(!opstk.empty()){
						wasEmpty=false;
						topChar=opstk.pop().getCharAt(0); //only the 1st letter is needed assuming single-letter operand
					}else{
						wasEmpty =true;
					}
				}				
				
				//if opstk, before its last pop, was not empty after above WHILE loop, 
				//it means checkPrecedence(topChar, inChar) ==false, the last popped top element 
				//no longer has precedence over inChar, thus the element shall be restored back to opstk 
				if(!wasEmpty){
					opstk.push(new PseudoString(topChar));
				}
				//after removing all elements with higher precedence than inChar from opstk, 
				//inChar is pushed into opstk except when it is “)”. In that case, an “(” is expected at 
				//the top of opstk and shall be removed & discarded.
				if(wasEmpty || inChar != ')'){
					opstk.push(new PseudoString(inChar));
				}else{
					topChar=opstk.pop().getCharAt(0);	//discard “(”
				}
			}else{
				System.out.println("WARNING: invalid operator or operand in the input infix expression.");
				return null;
			}//end if
		}//end for
		
		//finish the infix string, output everything, if any, from opstk to poststr.
		while(!opstk.empty()){
			poststr[posOut++] = opstk.pop().getCharAt(0);
		}
		if(posOut<1){
		//	System.out.println("WARNING: the input infix expression cannot be converted to valid postfix.");
			return null;
		}else{
			return new PseudoString(MyUtils.cloneChars(poststr, 0, posOut-1));
		}
	}

/**
 * compare precedence of two operators.
 * return true if op1 has higher precedence when op1 appears to the left of op2 in an infix expression. 
 * When calling checkPrecedence, op1 represents an operator popped from a stack and thus is encountered 
 * earlier than the operator op2 
 */
	public static boolean checkPrecedence(char op1, char op2){
		if(op1== ')'){		// “)” in not a legit operator that can occur in a operator stack. op1 must be validated before invoking checkPrecedence.
				System.out.println("WARNING: right parenthesis should never be pushed into the stack");
				return true;  //return true to prevent op2 being pushed in the stack. Actual error handling is done by validating op1.
		}else if(op1== '('){     //“(” has lower precedence than any operator comes after it (op2) 
			return false;
		}else if (op2== '('){    //“(” has higher precedence than any non-“)” op1 stacked before it
			return false;
		}else if(op2== ')'){    // “)” has lower precedence than any non-“(” op1 stacked before it
			return true;	   //this definition will trigger evaluation of the content encapsulated by    the parenthesis
		}else{			       // neither op1 nor op2 is parenthesis
			if(op1==op2){
				return true;
			}else{
			return true;// if op1 has higher precedence than op2 by regular rule, false otherwise
			}
		}
	}

	/**
	 * clone a part of the source char array defined by the start and end indices;
	 * this method is designed to be static for instance-independent access
	 * @param startIdx, endIdx
	 * @return a subset of characters
	 */
	public static char[] cloneChars(char[] source, int startIdx, int endIdx){
		if(startIdx>endIdx || startIdx<0 || endIdx>source.length-1 ||source.length<1){
			System.out.println("WARNING: invalid indices or the source array");
			return null;
		}
		char[] clone=new char[endIdx-startIdx+1];
		for(int i=0; i<clone.length;i++){
			clone[i]=source[i+startIdx];
		}
		return clone;
	}
	
	/**
	 * convert an int type variable inNum to a character array
	 */
	public static char[] int2Chars(int inNum){
		int[] temp={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}; //10 digits is enough to hold int type data in java
		int idx=-1;
		
		while(inNum >0){ 	//store in revese order the digits of inNum into temp[]
			temp[++idx]=inNum % 10;
			inNum=inNum/10;
		}
		char[] results=new char[idx+1];
		for(int i=idx;i>=0;i--){
			results[idx-i]=digit2Char(temp[i]); //covert digits to chars and save them in proper order
		}
		return results;
	}

	/**
	 *handy function to convert 0-9 digit to "0"-"9" characters
	 */
	public static char digit2Char(int n){
		if(n<0 || n>9){
			System.out.println("WARNING: invalid input, n must be within 0-9.");
			return ' ';
		}
		return (char)(n+48);
	}
	
	/**
	 * test if a character is a valid (e.g. +,-,*,/) operator. 
	 */
	public static boolean isValidOperator(char c){
		switch(c){
		case '+': return true;
		case '-': return true;
		case '*': return true;
		case '/': return true;
		case ')': return true;
		case '(': return true;
		default:  	//return false when either the operator is invalid or it's an operand
			//if(c!='_' && (c <'0' || (c>'9' && c<'A') || (c>'Z' && c<'a') || c>'z') ){
			//	System.out.println("WARNING: invalid operator. Only +,-,*,/ are allowed.");
			//}
			return false;
		}
	}

	/**
	 * According to java variable naming rule, operands must start with Alphabet or underscore, followed by 
	 * any number of letters, digits or underscore. 
	 * We exclude dollar sign because we treat it as an exponentiation in this course
	 * @param opn
	 * @return true or false
	 */
	public static boolean isValidOperand(PseudoString opn){
		if(opn.getLength()<1){return false;}
		char letter=opn.getCharAt(0);
		if( letter!='_' && (letter<'A' || (letter>'Z' && letter<'a')||letter>'z') ){
		//	System.out.println("WARNING: "+letter+" is invalid in an operand.");
			return false;
		}
		//Lab1 only allows single-letter operands, thus the input opn always has a single character, 
		//the code below wont not get executed. I put it here for completeness.
		for(int i=1;i<opn.getLength();i++){
			letter=opn.getCharAt(i);
			if( letter!='_' && (letter <'0' || (letter>'9' && letter<'A') || (letter>'Z' && letter<'a') || letter>'z')){
		//		System.out.println("WARNING: "+letter+" is invalid in an operand.");
				return false;
			}
		}
		return true;
	}

	/**
	 * test if the input is composed of letters, digits or underscore. 
	 * We exclude dollar sign because we treat it as an exponentiation in this course.
	 * @param opn
	 * @return true or false
	 */
	public static boolean isOperand(PseudoString opn){
		if(opn.getLength()<1){return false;}
		char letter;
		for(int i=0;i<opn.getLength();i++){
			letter=opn.getCharAt(i);
			if( letter!='_' && (letter <'0' || (letter>'9' && letter<'A') || (letter>'Z' && letter<'a') || letter>'z')){
		//		System.out.println("WARNING: "+letter+" is invalid in an operand.");
				return false;
			}
		}
		return true;
	}
}
