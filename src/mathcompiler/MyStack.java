//File:         [MyStack.java]
//Created:      [09/15/2015]
//Last Changed: $Date: 09/15/2015 $
//Author:       <A HREF="mailto:[ybai11@jhu.edu]">[Yu Bai]</A>
//
package mathcompiler;

public class MyStack {
	/**
	[MyStack provides a stack data structure for PseudoString instances based on linked-list]
	[Basic functions pop, push, peek, empty are implemented]
	@see [size]
   	@see [top]
   	@see [MyStack#push]
   	@see [MyStack#pop]
   	@see [MyStack#popAll]
   	@see [MyStack#peek]
   	@see [MyStack#empty]
   	@see [MyStack#getSize]
	**/

	private PseudoStringNode top;
	private int size;
	public MyStack(){
		top=null;
		size=0;
	}	
	
	/**
	 * push a new element
	 * @param newString
	 * @return none
	 */
	public void push(PseudoString newString){
		PseudoStringNode temp=new PseudoStringNode();
		temp.myString=newString;
		temp.next=top;
		top=temp;
		size++;
	}

	/**
	 * pop the top element
	 * @param none
	 * @return item
	 */
	public PseudoString pop(){
		if(empty()){
			System.out.println("warning: stack is empty!");
			return null;
		}
		PseudoString item;
		PseudoStringNode temp=top;
		item=temp.myString;
		top=temp.next;
		temp.next=null;
		size--;
		return item;
	}


	/**
	 * remove all elements in the stack
	 * @param none
	 * @return none
	 */
	public void popAll(){
		PseudoStringNode temp;
		while(size>0){
			temp=top;
			top=temp.next;
			temp.next=null;
			size--;
		}
	}

	/**
	 * peek the top element
	 * @param none
	 * @return item
	 */
	public PseudoString peek(){
		if(empty()){
			System.out.println("warning: stack is empty!");
			return null;
		}
		return top.myString;
	}

	/**
	 * test if the stack is empty
	 * @param none
	 * @return true or false
	 */
	public boolean empty(){
		return (top==null);
	}

	/**
	 * get size of the current stack
	 * @param none
	 * @return size
	 */
	public int getSize(){
		return size;
	}


}
