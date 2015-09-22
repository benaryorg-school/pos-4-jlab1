public class Stack<E>
{
	private E[] arr;
	private int tos;
	
	@SuppressWarnings("unchecked")
	public Stack(int len)
	{
		this.arr=(E[])new Object[len];
		this.tos=0;
	}

	public void push(E item)
	{
		if(tos>=arr.length)
		{
			throw new FullException();
		}
		this.arr[this.tos++]=item;
	}

	public E pop()
	{
		if(tos<=0)
		{
			throw new EmptyException();
		}
		E var=this.arr[--this.tos];
		this.arr[this.tos]=null;
		return var;
	}

	public String toString()
	{
		return java.util.Arrays.toString(this.arr);
	}

	public static void main(String... args)
	{
		Stack<String> stack=new Stack<String>(2);
		new Consumer(stack).start();
		new Producer(stack).start();
	}
}

class FullException extends RuntimeException
{
	public static final long serialVersionUID=0L;
}

class EmptyException extends RuntimeException
{
	public static final long serialVersionUID=0L;
}

class Consumer extends Thread
{
	private Stack<String> stack;
	
	public Consumer(Stack<String> stack)
	{
		this.stack=stack;
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				System.out.println("Consumer: sleep");
				Thread.sleep((int)(Math.random()*1000)+1000);
			}
			catch(InterruptedException ex)
			{
			}
			System.out.println("Consumer: synchronized");
			synchronized(this.stack)
			{
				try
				{
					this.stack.pop();
					System.out.println("Consumer: pop");
					System.out.println("Stack: "+this.stack);
					this.stack.notifyAll();
				}
				catch(EmptyException e1)
				{
					try
					{
						System.out.println("Consumer: wait");
						this.stack.wait();
					}
					catch(InterruptedException e2)
					{
					}
				}
			}
		}
	}
}

class Producer extends Thread
{
	private Stack<String> stack;
	
	public Producer(Stack<String> stack)
	{
		this.stack=stack;
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				System.out.println("Producer: sleep");
				Thread.sleep((int)(Math.random()*1000)+1000);
			}
			catch(InterruptedException ex)
			{
			}
			System.out.println("Producer: synchronized");
			synchronized(this.stack)
			{
				try
				{
					this.stack.push("("+Math.random()+")");
					System.out.println("Producer: push");
					System.out.println("Stack: "+this.stack);
					this.stack.notifyAll();
				}
				catch(FullException e1)
				{
					try
					{
						System.out.println("Producer: wait");
						this.stack.wait();
					}
					catch(InterruptedException e2)
					{
					}
				}
			}
		}
	}
}

