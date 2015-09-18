public class Stack<E> extends java.util.ArrayDeque<E>
{
	public static final long serialVersionUID=0L;
	private int len;
	
	public Stack(int len)
	{
		this.len=len;
	}

	@Override
	public void push(E item)
	{
		if(this.size()>=this.len)
		{
			throw new FullException();
		}
		super.push(item);
	}

	public static void main(String... args)
	{
		Stack<String> stack=new Stack<String>(1);
		new Consumer(stack).start();
		new Producer(stack).start();
	}
}

class FullException extends RuntimeException
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
				catch(java.util.NoSuchElementException e1)
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

