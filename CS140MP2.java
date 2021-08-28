import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class CS140MP2 {
	public static final String inputFilename = "WarStrategy.txt";
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {		
		final MarioWindow mp2 = new MarioWindow();
		SharedVars sv = new SharedVars();
		Scheduler scheduler = getScheduler(sv);
		Visualizer visualizer = new Visualizer(sv);
		
		mp2.setTitle("Battle Scheduler");        
		mp2.add(visualizer);

		(new Thread() {
			public void run() {
				mp2.startGame();
			}
		}).start();
		scheduler.start();
	}

	public static Scheduler getScheduler(SharedVars sv) {
		Scheduler scheduler = null;
		char role, name;
		
		System.out.println("It has begun your excellency. Here are the phases of the battle:");
		System.out.println("A. Attack\nB. Siege\nC. Defend\nD. Parley");
		System.out.print("I would like to lead choice: ");
		role = in.next().toUpperCase().charAt(0);

		switch (role) {
			case 'A': sv.role = "attack"; break;
			case 'B': sv.role = "siege"; break;
			case 'C': sv.role = "defend"; break;
			case 'D': sv.role = "parley";
		}

		System.out.println("What type of scheduling would you like to use, your excellency?");
		System.out.println("A. FCFS\nB. SJF\nC. Priority\nD. Round Robin (TQ 4, CS 2)");
		System.out.print("I would like to use scheduler: ");
		name = in.next().toUpperCase().charAt(0);
		
		switch (name) {
			case 'A': scheduler = new SchedulerFCFS(inputFilename, sv); break;
			case 'B': scheduler = new SchedulerSJF(inputFilename, sv); break;
			case 'C': scheduler = new SchedulerPriority(inputFilename, sv); break;
			case 'D': scheduler = new SchedulerRoundRobin(inputFilename, sv); 
		}

		return scheduler;
	}
}