package mathcompiler;

import java.util.Stack;

public class test {
	
	static Stack<Integer> A=new Stack<Integer>();

	public static void doubleValue(){
		if(!A.isEmpty()){
			int x=(Integer)A.pop().intValue();
			doubleValue();
			A.push(new Integer(2*x));
		}
	}

	static int rec(int n){
		if( n<100){
			System.out.println("this round n="+n);
			return(rec(3*n));
		}else{
			System.out.println("return value="+n);
			return n;
		}
	}

	static int iter(int n){
		int i=n;
		while(i<100){
			System.out.println("this round n="+i);
			i=3*i;
		}
			System.out.println("return value="+i);
			return i;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[] mychars=new char[0];
		System.out.println(mychars.length);
		
		char c= (char)(65+15);
		System.out.println(c);
		
		A.push(new Integer(1));
		A.push(new Integer(2));
		A.push(new Integer(3));
		A.push(new Integer(4));
		A.push(new Integer(5));
		A.push(new Integer(6));
		A.push(new Integer(7));

		doubleValue();
		while(!A.isEmpty()){
		System.out.println((Integer)A.pop().intValue());
		}
		System.out.println(-1);
		//65-90(A-Z); 97-122(a-z) 
		System.out.println((char)(122));
		char[] tmparr=new char[]{'S','T','\t','T','E','M','P','4'};
		String tmpstr=new String(tmparr);
		System.out.println(tmpstr);
		
		ArithmeticCompiler compiler=new ArithmeticCompiler();
		
//		char[]expression=new char[]{'A','B','C','*','+','D','E','-','/'};//(A+(B*C))/(D-E)
//		char[]expression=new char[]{'/','+','A','*','B','C','-','D','E'}; ///+A*BC-DE
//		char[]expression=new char[]{'A','B','C','+','$','C','B','A','-','+','*'}; // /+A*BC-DE
//		char[] expression=new char[]{'(','A','+','(','B','*','C',')',')','/','(','D','-','E',')'}; //(A+(B*C))/(D-E)
//		char[] expression=new char[]{'a'};
		char[] expression=new char[]{'A','+','(','B','*','C',')','/','D','^','E'}; //(A+(B*C))/(D-E)
		compiler.expressionToInstructions(new PseudoString(expression));
		
		System.out.println(String.valueOf(expression)+":");
		while(compiler.getInstructions().getSize()>0){
			PseudoString tmp=compiler.getInstructions().remove();
			System.out.println(String.valueOf(tmp.getChars()));
		}
		//todo: print out translated postfix or direct all console output to bufferwritter file too
		//System.setOut(outputFile(outfile);
		// (lack operand, excess operand),invalid operand (. - num ! # @), lower case valid opn, white spaces, invalud operators (%, & | > < : ?)
		// expression = 'A' single opn, 
		// '-' single operator cases
		// infix, prefix cases
		// running out of register cases (too long an expression)??

		
	}
	


}
