//File:         [ArithmeticCompiler.java]
//Created:      [09/15/2015]
//Last Changed: $Date: 09/15/2015 $
//Author:       <A HREF="mailto:[ybai11@jhu.edu]">[Yu Bai]</A>
//
package mathcompiler;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;


public class ArithmeticCompiler{ //throws IOException??
	/**
	[The master engine for compiling arithmetic expressions into assembly language instructions. 
	It handles postfix, prefix and infix expressions, with the ability to automatically detect the
	type of the input expressions.]
	[Usage: java -jar XXX]
	@see [stk]
	@see [instructions]
	@see [counterTmpVar]
	@see [whatType]
    @see [ArithmeticCompiler#run]
    @see [ArithmeticCompiler#reset]
    @see [ArithmeticCompiler#expressionToInstructions]
    @see [ArithmeticCompiler#compile]
    @see [ArithmeticCompiler#getInstructions]
    @see [ArithmeticCompiler#readExpression]
 	**/
	private MyStack stk;
	private MyQueue instructions;
	private int counterTmpVar;
	private int orgType=MyUtils.INVALID;	//type of the original expression (prefix, postfix, infix or invalid)

	public ArithmeticCompiler(){
		stk=new MyStack();
		instructions=new MyQueue();
		counterTmpVar=0;
	}
	
	/**
	 * clean up the stack, the instruction queue and temporary variable counter.
	 * all elements in the stack and queue are de-referenced such that the garbage collection can happen.
	 */
	public void reset(){
		stk.popAll();
		instructions.removeAll();
		counterTmpVar=0;	
	}
	/**
	 * convert an expression string to instruction lists (stored by a MyQueue data structure).
	 * The input operands are assumed to be single-letter.
	 */
	public void expressionToInstructions(PseudoString expression){
		int exprlength=expression.getLength();
		int idx=-1, myincreament=1;
		int whattype=MyUtils.findExpressionType(expression);
		orgType=whattype;
		if(whattype==MyUtils.INFIX){
			whattype=MyUtils.POSTFIX;
			expression=MyUtils.infix2Postfix(expression);
			if(expression==null){
				System.out.println("WARNING: the infix expression cannot be converted to a valid postfix one.");
				whattype=MyUtils.INVALID;
			}
		}
		if(whattype==MyUtils.PREFIX){
			idx=exprlength;
			myincreament=-1;
		}
		if(whattype==MyUtils.INVALID){	
			System.out.println("WARNING: invalid expression, no instruction is generated.");
			return;
		}
		if(expression.getLength()==1){	//expression is a single-letter operand 
			System.out.println("WARNING: single-operand expression, no instruction is needed.");
			instructions.add(expression);
			return;
		}
		while (expression.hasNext(idx, whattype)){
			idx=idx+myincreament;
			char currentChar=expression.getCharAt(idx);
			if(MyUtils.isValidOperator(currentChar)){ //if the current character is an operator
				PseudoString opn1=null;
				PseudoString opn2=null;
				if(!stk.empty()){
					opn1=stk.pop();
				}else{
					System.out.println("WARNING: invalid expression due to lack of operand(s).");
					reset();
					break;		//leave instructions empty for invalid expression
				}
				if(!stk.empty()){
					opn2=stk.pop();
				}else{
					System.out.println("WARNING: invalid expression due to lack of operand(s).");
					reset();
					break;		//leave instructions empty for invalid expression
				}
				//swap operands as in postfix expression the second popped operand is the left operand 
				if(whattype==MyUtils.POSTFIX){
					PseudoString tmp=opn1;
					opn1=opn2;
					opn2=tmp;
					tmp=null;  //clean up tmp
				}
				//compile the current tuple of two operands and one operator 
				PseudoString tmpvar=compile(opn1,opn2,currentChar);
				
				//push in new operand unless the expression string finishes
				if(expression.hasNext(idx, whattype)){
					stk.push(tmpvar);
				}
			}else{
				PseudoString newopn=new PseudoString(currentChar);
				if(MyUtils.isValidOperand(newopn)){
					stk.push(newopn);
				}else{
					//System.out.println("WARNING: invalid operator or operand.");
					reset();
					break;
				}
			}//end else
		}//end while
		if(!stk.empty()){
			reset();
			System.out.println("WARNING: invalid expression due to lack of operator(s).");
		}
	}
	
	/**
	 * Given the tuple of two operands and 1 operator, generate a triplet of the assembly commands.
	 * That is,load the first operand, perform arithmetic operation with the 2nd operand, store the result to a new temporary variable.
	 * Operands are of PseudoString type such that their names can have either single or multiple characters.
	 * The operator is a char type variable.
	 * @param opn1: 
	 * @param opn2
	 * @param opt
	 */
	public PseudoString compile(PseudoString opn1, PseudoString opn2, char opt){
		//load the 1st operand to the register.
		PseudoString loadcommand=new PseudoString(new char[]{'L', 'D', ' '});
		loadcommand.concatChars(opn1.getChars());

		//arithmetic operation of the 2nd operand to the register, command varies by the type of operator
		PseudoString mathcommand;
		switch(opt){		
		case '+':	mathcommand=new PseudoString(new char[]{'A', 'D', ' '});
					break;
		case '-':	mathcommand=new PseudoString(new char[]{'S', 'B', ' '});
					break;
		case '*':	mathcommand=new PseudoString(new char[]{'M', 'L', ' '});
					break;
		case '/':	mathcommand=new PseudoString(new char[]{'D', 'V', ' '});
					break;
		default :   //mathcommand=new PseudoString(new char[]{'E','R','R','O','R'});
					System.out.println("WARNING: invalid operator. Only +,-,*,/ are allowed");
					reset();
					return null;
					//System.exit(0);
		}
		mathcommand.concatChars(opn2.getChars());
		
		//store the results to variable 
		counterTmpVar++;
		PseudoString storecommand=new PseudoString(new char[]{'S', 'T', ' '});
		PseudoString newTmpVar=new PseudoString(new char[]{'T','E','M','P'});
		newTmpVar.concatChars(new PseudoString(counterTmpVar).getChars());
		storecommand.concatChars(newTmpVar.getChars());
		
		//accumulate these commands to the list of instructions.
		instructions.add(loadcommand);
		instructions.add(mathcommand);
		instructions.add(storecommand);
		return newTmpVar;
	}

	public MyQueue getInstructions(){
		return instructions;
	}
	
	/**
	 * I/O: read expressions from the input file, bulk process, and write the corresponding instructions to the output file.  
	 */
	public void run(String infilename, String outfilename) throws IOException{
		try{
			FileReader freader=new FileReader(infilename);
			BufferedReader breader=new BufferedReader(freader);

			File output=new File(outfilename);
			if (!output.exists()) {
				output.createNewFile();
			}

			FileWriter fwriter=new FileWriter(outfilename);
			BufferedWriter bwriter=new BufferedWriter(fwriter);

			String expression;
	//		int linecount=0;
			String typeString="INVALID";
			while ((expression=breader.readLine())!=null){
				expression=expression.trim(); 	//remove flanking white spaces if any
				if(expression.length()>=1){ 	//skip blank lines
					System.out.println("... Start processing an expression "+expression+" ...");
					char[] expchars=readExpression(expression);
					expressionToInstructions(new PseudoString(expchars));

					String result;
					bwriter.write(expression+"\n");
					if(instructions.getSize()<1){
						result="expression "+expression+" is invalid. no instruction is generated.\n";
						bwriter.write(result);
					}else if(instructions.getSize()==1){
						result="expression "+expression+" is single-operand. no instruction is needed.\n";
						bwriter.write(result);
					}else{
						System.out.println("Succeed! "+expression+" is complied into assembly language instructions.");
						if(orgType==MyUtils.PREFIX){
							result="PREFIX expression "+expression+" is valid. Evaluation instructions are as follows:\n";
						}else if (orgType==MyUtils.POSTFIX){
							result="POSTFIX expression "+expression+" is valid. Evaluation instructions are as follows:\n";
						}else{
							result="INFIX expression "+expression+" is valid. Evaluation instructions of its POSTFIX equivalent are as follows:\n";
						}
						bwriter.write(result);
						int instructionSize=instructions.getSize();
						for(int i=0;i<instructionSize;i++){
							result=String.valueOf(instructions.remove().getChars());
							bwriter.write(result+"\n");
						}
					}//endif
					bwriter.write("\n\n");
					reset();
				}//endif
			}//end while
			breader.close();
			bwriter.close();
		
		}catch(IOException e){
			e.printStackTrace();
		}

	}
	

	
	/**
	 *I/O: convert an input expression string to a character array. 
	 *String operation is needed for I/O purpose.
	 * @param expression
	 * @return expchars
	 */
	public char[] readExpression(String expression){
		char[] expchars=new char[expression.length()];
		for(int i=0; i<expchars.length;i++){
			expchars[i]=expression.charAt(i);
		}
		return expchars;
	}

	/**
	 * I/O: read in all expression lines from an input file and output them in a String array.
	 * @param infile
	 * @return inputs
	 */
	/*public String[] readFile(String infile) throws IOException{
		int numLines=countLines(infile);
		String[] expressions=new String[numLines];

		FileReader fr=new FileReader(infile);
		BufferedReader breader=new BufferedReader(fr);
		
		for(int i=0;i<numLines;i++){
			expressions[i]=breader.readLine();
		}
		return expressions;
	}
	*/

	/**
	 * I/O: read through the input file and output the number of lines therein
	 * @param infile
	 * @return linecount
	 */
/*	public int countLines(String infile) throws IOException{
		FileReader fr=new FileReader(infile);
		BufferedReader breader=new BufferedReader(fr);
		
		String line;
		int linecount=0;
		while ((line=breader.readLine())!=null){
			linecount++;
		}
		breader.close();
		return linecount;
	}

	public void writeFile(String outfile) throws IOException{
		try{
			File output=new File(outfile);
			if (!output.exists()) {
				output.createNewFile();
			}

		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public String[] writeExpression(String expression) {
			String[] resultsPerExpression;
			
			if(instructions.getSize()<1){
				resultsPerExpression=new String[1];
				resultsPerExpression[0]="ERROR: Invalid expression. no instruction is generated.";
			}else{
				resultsPerExpression=new String[instructions.getSize()];
				for(int i=0;i<resultsPerExpression.length;i++){
					resultsPerExpression[i]=String.valueOf(instructions.remove().getChars());
				}
			}
			return resultsPerExpression;
	}
	
*/

	public static void main(String[] args) throws IOException{
		String infile=args[0];
		String outfile=args[1];
		
		ArithmeticCompiler arithmeticCompiler=new ArithmeticCompiler();
		
		try{
			arithmeticCompiler.run(infile, outfile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
