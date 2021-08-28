import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Scheduler extends Thread {
	public final int DELAY = 1000; // 4000 ms 
	public List<City> cities = new ArrayList<City>();
	public List<City> ready = new ArrayList<City>();
	public List<City> waiting = new ArrayList<City>();
	public List<City> terminated = new ArrayList<City>();
	public List<City> arrived = new ArrayList<City>();
	public City running = null;
	public String remarks = "";
	public String role = "";
	public SharedVars sv;
	public int day;

	Scheduler(String filename) {
		try {
			Scanner scanner = new Scanner(new File(filename));
			Scanner innerScanner;
			List<Task> tasks;
			String line, name, task;
			String info[];
			int arrival, duration;

			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				info = line.split(" ");
				name = info[0];
				arrival = Integer.parseInt(info[1]);
				tasks = new ArrayList<Task>();
				innerScanner = new Scanner(new File(name+".txt"));
				
				while (innerScanner.hasNextLine()) {
					line = innerScanner.nextLine().trim();
					info = line.split(" ");
					task = info[0];
					duration = Integer.parseInt(info[1]);
					tasks.add(new Task(task, duration));
				}

				cities.add(new City(name, arrival, tasks));
			}

		} catch (Exception e) { 
			System.out.println("Error Parsing file: " + filename);
			e.printStackTrace(); 
		}	
	}

	@Override
	public void run() {
		City current;
		day = 1;
		while (hasNext()) {
			for (City city : cities) {
				if (city.arrival == day) {
					city.name = titleCase(city.name);
					ready.add(city);
					arrived.add(city);
				}
			}
			cities.removeAll(ready);
			
			nextRunning();
			nextWaiting();
			getRemarks();

			synchronized (sv) {
				reportProgress();
			}
			MarioWindow.delay(DELAY);

			doTasks();
			day++;
		}
	}

	public void nextRunning() {
		// Should be overriden on subclasses
	}

	public void nextWaiting() {
		// Sifts through the ready queue to collect all 'waiting' cities
		for (City city : ready) {
			if (!city.task.name.equals(role)) {
				waiting.add(city);
			}
		}
		ready.removeAll(waiting);
	}
	
	public void reportProgress() {
		// Prints current simulation state
		System.out.println("Day: " + day);
		System.out.println("Running: " + running);
		System.out.println("Ready: " + ready);
		System.out.println("Waiting: " + waiting);
		System.out.println("terminated: " + terminated + "\n");

		// Day and Ready
		sv.day = String.valueOf(day);
		sv.remarks = remarks;
		remarks = "";

		// Running
		if (running != null) {
			sv.running = running.getInfo();
		} else {
			sv.running = "null";
		}

		// Waiting
		sv.waiting_list = waiting;
		if (waiting.size() > 1) {
			sv.waiting = String.valueOf(waiting.size()) + " cities";
		} else if (waiting.size() == 1) {
			sv.waiting = "1 city";
		} else {
			sv.waiting = "0";
		}

		// Ready
		sv.ready_list = ready;
		if (ready.size() > 1) {
			sv.ready = String.valueOf(ready.size()) + " cities";
		} else if (ready.size() == 1) {
			sv.ready = "1 city";
		} else {
			sv.ready = "0";
		}
	}

	public void doTasks() {
		// Processes the current task of 'running' city and tasks of 'waiting' cities
		if (running != null) {
			running.doTask();
			
			if (running.isDone()) {
				terminated.add(running);
				running = null;
			} else if (running.task.isDone()) {
				running.nextTask();
				ready.add(running);
				running = null;
			}
		}
		
		for (City city : waiting) {
			city.doTask();	
			
			if (city.isDone()) {
				terminated.add(city);
			} else if (city.task.isDone()) {
				city.nextTask();
				ready.add(city);
			}
		}
		
		waiting.removeAll(terminated);
		waiting.removeAll(ready);
	}	

	// UTILITY FUNCTIONS
	public String titleCase(String str) {
		String result = new String();
		
		result += str.toUpperCase().charAt(0);
		for (int i = 1; i < str.length(); i++) {
			result += str.toLowerCase().charAt(i);
		}

		return result;
	}

	public void getRemarks() {
		// Arrived
		if (arrived.size() > 0) {
			remarks += "Battle for ";
		}
		for (int i = 0; i < arrived.size(); i++) {
			remarks += arrived.get(i).name;
			if (i != arrived.size()-1) {
				remarks += ", ";
			} else {
				if (arrived.size() > 1) {
					remarks += " begin. ";
				} else {
					remarks += " begins. ";
				}
			}
		}
		
		// Terminated
		for (int i = 0; i < terminated.size(); i++) {
			remarks += terminated.get(i).name;
			if (i != terminated.size()-1) {
				remarks += ", ";
			} else {
				if (terminated.size() > 1) {
					remarks += " fall. ";
				} else {
					remarks += " falls. ";
				}
			}
		}
		
		arrived.clear();
		terminated.clear();
	}

	public boolean hasNext() {
		// Returns true if simulation is not over yet
		return (running != null) | !waiting.isEmpty() | !ready.isEmpty() | !cities.isEmpty();
	}

}