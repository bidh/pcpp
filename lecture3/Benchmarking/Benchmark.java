// PCPP week 3
// bidh@itu.dk
import java.util.function.IntToDoubleFunction;

public class Benchmark
{	
	public static void main(String[] args){
		SystemInfo();
		//Mark1();
		//Mark2();
		//Mark3();
		//Mark4();
		//Mark5();
		//Mark6("multiply",Benchmark::multiply);
		//Mark7("multiply",Benchmark::multiply);
		Mark7("pow", i -> Math.pow(10.0, 0.1 * (i & 0xFF)));
		Mark7("exp", i -> Math.exp(0.1 * (i & 0xFF)));
		Mark7("log", i -> Math.log(0.1 + 0.1 * (i & 0xFF)));
		Mark7("sin", i -> Math.sin(0.1 * (i & 0xFF)));
		Mark7("cos", i -> Math.cos(0.1 * (i & 0xFF)));
		Mark7("tan", i -> Math.tan(0.1 * (i & 0xFF)));
		Mark7("asin", i -> Math.asin(1.0/256.0 * (i & 0xFF)));
		Mark7("acos", i -> Math.acos(1.0/256.0 * (i & 0xFF)));
		Mark7("atan", i -> Math.atan(1.0/256.0 * (i & 0xFF)));
	}
	//Multiply
	private static double multiply(int i) {
		double x = 1.1 * (double)(i & 0xFF);
		return x * x * x * x * x * x * x * x * x * x
		* x * x * x * x * x * x * x * x * x * x;
	}
	//Mark1
	public static void Mark1(){
		Timer t = new Timer();
		Integer count = 100_000_000;
		for (int i=0; i<count; i++) {
			double dummy = multiply(i);
		}
		double time = t.check() * 1e9 / count;
		System.out.printf("%6.1f ns%n", time);
	}
	//Mark2
	public static double Mark2() {
		Timer t = new Timer();
		int count = 100_000_000;
		double dummy = 0.0;
		for (int i=0; i<count; i++)
			dummy += multiply(i);
		double time = t.check() * 1e9 / count;
		System.out.printf("%6.1f ns%n", time);
		return dummy;
	}
	//Mark3
	public static double Mark3() {
		int n = 10;
		int count = 100_000_000;
		double dummy = 0.0;
		for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += multiply(i);
			double time = t.check() * 1e9 / count;
			System.out.printf("%6.1f ns%n", time);
		}
		return dummy;
	}
	//Mark4
	public static double Mark4() {
	int n = 10;
	int count = 100_000_000;
	double dummy = 0.0;
	double st = 0.0, sst = 0.0;
	for (int j=0; j<n; j++) {
		Timer t = new Timer();
		for (int i=0; i<count; i++)
			dummy += multiply(i);
		double time = t.check() * 1e9 / count;
		st += time;
		sst += time * time;
	}
	double mean = st/n, sdev = Math.sqrt((sst - mean*mean*n)/(n-1));
	System.out.printf("%6.1f ns +/- %6.3f %n", mean, sdev);
	return dummy;
	}
	//Mark5
	public static double Mark5() {
		int n = 10, count = 1, totalCount = 0;
		double dummy = 0.0, runningTime = 0.0;
		do {
		count *= 2;
		double st = 0.0, sst = 0.0;
		for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += multiply(i);
			runningTime = t.check();
			double time = runningTime * 1e9 / count;
			st += time;
			sst += time * time;
			totalCount += count;
		}
		double mean = st/n, sdev = Math.sqrt((sst - mean*mean*n)/(n-1));
		System.out.printf("%6.1f ns +/- %8.2f %10d%n", mean, sdev, count);
		} while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
		return dummy / totalCount;
	}
	//Mark6
	public static double Mark6(String msg, IntToDoubleFunction f) {
		int n = 10, count = 1, totalCount = 0;
		double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
		do {
		count *= 2;
		st = sst = 0.0;
		for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += f.applyAsDouble(i);
			runningTime = t.check();
			double time = runningTime * 1e9 / count;
			st += time;
			sst += time * time;
			totalCount += count;
		}
		double mean = st/n, sdev = Math.sqrt((sst - mean*mean*n)/(n-1));
		System.out.printf("%-25s %15.1f %10.2f %10d%n", msg, mean, sdev, count);
		} while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
		return dummy / totalCount;
	}
	//Mark6
	public static double Mark7(String msg, IntToDoubleFunction f) {
		int n = 10, count = 1, totalCount = 0;
		double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
		do {
		count *= 2;
		st = sst = 0.0;
		for (int j=0; j<n; j++) {
			Timer t = new Timer();
			for (int i=0; i<count; i++)
				dummy += f.applyAsDouble(i);
			runningTime = t.check();
			double time = runningTime * 1e9 / count;
			st += time;
			sst += time * time;
			totalCount += count;
		}
		
		} while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
		double mean = st/n, sdev = Math.sqrt((sst - mean*mean*n)/(n-1));
		System.out.printf("%-25s %15.1f %10.2f %10d%n", msg, mean, sdev, count);
		return dummy / totalCount;
	}
	//SystemInfo
	public static void SystemInfo() {
		System.out.printf("# OS:   %s; %s; %s%n", 
						  System.getProperty("os.name"), 
						  System.getProperty("os.version"), 
						  System.getProperty("os.arch"));
		System.out.printf("# JVM:  %s; %s%n", 
						  System.getProperty("java.vendor"), 
						  System.getProperty("java.version"));
		// The processor identifier works only on MS Windows:
		System.out.printf("# CPU:  %s; %d \"cores\"%n", 
				  System.getenv("PROCESSOR_IDENTIFIER"),
				  Runtime.getRuntime().availableProcessors());
		java.util.Date now = new java.util.Date();
		System.out.printf("# Date: %s%n", 
		  new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(now));
	}
	//Timer
	static class Timer {
		private long start, spent = 0;
		public Timer() { play(); }
		public double check() { return (System.nanoTime()-start+spent)/1e9; }
		public void pause() { spent += System.nanoTime()-start; }
		public void play() { start = System.nanoTime(); }
	}  
}