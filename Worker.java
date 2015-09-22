public class Worker extends Thread
{
	public static enum WorkerState
	{
		CREATED,
		STARTED,
		STATE1,
		STATE2,
		FINISHED,
	}

	private volatile WorkerState state;

	public Worker()
	{
		this.state=WorkerState.CREATED;
	}

	@Override
	public void run()
	{
		try
		{
			this.state=WorkerState.STARTED;
			Thread.sleep((int)(Math.random()*10000+5000));
			this.state=WorkerState.STATE1;
			Thread.sleep((int)(Math.random()*6000+12000));
			this.state=WorkerState.STATE2;
			Thread.sleep((int)(Math.random()*12000+2000));
			this.state=WorkerState.FINISHED;
		}
		catch(InterruptedException ex)
		{
		}
	}

	public WorkerState getWorkerState()
	{
		return this.state;
	}

	public static void main(String... args)
	{
		final java.util.List<Worker> list=new java.util.LinkedList<Worker>();
		for(int i=0;i<100;i++)
		{
			list.add(new Worker());
		}
		for(Thread t:list)
		{
			t.start();
		}
		Thread mon=new Thread()
		{
			@Override
			public void run()
			{
				boolean finished=false;
				for(int i=0;!finished;i++)
				{
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException ex)
					{
					}
					java.util.Map<WorkerState,Integer> map=new java.util.HashMap<WorkerState,Integer>();
					for(WorkerState s:WorkerState.values())
					{
						map.put(s,0);
					}
					for(Worker t:list)
					{
						WorkerState s=t.getWorkerState();
						map.put(s,map.get(s)+1);
					}
					System.out.printf("#%03d ",i);
					for(WorkerState s:WorkerState.values())
					{
						System.out.printf("%s: %03d ",s,map.get(s));
					}
					System.out.println();
					if(map.get(WorkerState.FINISHED)==list.size())
					{
						finished=true;
					}
				}
				System.out.println("all threads done");
			}
		};
		mon.start();
	}
}

