public class Print {
	
	public static void print() {
		synchronized(Print.class){
			System.out.print("-");
			try { Thread.sleep(50); } 
			catch (InterruptedException exn) { }
			System.out.print("|");
		}		
	}   
	public static void main(String[] args) {
	Print p1=new Print();
    final int counts = 10_000_000;
    Thread t1 = new Thread(() -> {
      for (int i=0; i<counts; i++) 
		Print.print();
    });
    Thread t2 = new Thread(() -> {
      for (int i=0; i<counts; i++) 
		Print.print();
    });
    t1.start(); t2.start();
    try { t1.join(); t2.join(); }
    catch (InterruptedException exn) { 
      System.out.println("Some thread was interrupted");
    }
  }
}
