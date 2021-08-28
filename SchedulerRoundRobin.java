public class SchedulerRoundRobin extends Scheduler {
	public final int TQ = 4;
	public final int CS = 2;
	public int tq = 0;
	public int cs = 0;
	public boolean in_transit = false;

	SchedulerRoundRobin(String inputFilename, SharedVars sv) {
		super(inputFilename);
		this.sv = sv;
		this.sv.schedulerName = "Round Robin";
		role = this.sv.role;
	}

	@Override
	public void nextRunning() {
		if (!in_transit) {
			if (running == null) {
				for (City city : ready) {
					if( city.task.name.equals(role)) {
						running = city;
						remarks += "Emperor chose " + city.name + ". ";
						ready.remove(ready.indexOf(running));
						break;
					}
				}
			}
		} else {
			// no context switch when there are no cities in ready
			if (running != null && ready.size() > 0) {
				remarks += running.name + "'s term ended. ";
				ready.add(running);
				running = null;
				in_transit = true;
				cs = 0;
			}
		}
	}

	@Override
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
		if (in_transit) {
			sv.running = "In transit";
		} else {
			if (running != null) {
				sv.running = running.getInfo();
			} else {
				sv.running = "null";
			}
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

	@Override
	public void doTasks() {
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

		if (!in_transit) {
			if (running != null) {
				running.doTask();		
				if (running.isDone()) {
					terminated.add(running);
					running = null;
					in_transit = true;
					cs = 0;
				} else if (running.task.isDone()) {
					running.nextTask();
					ready.add(running);
					running = null;
					in_transit = true;
					cs = 0;
				}
			}
			
			tq++;
			if (tq >= TQ && running != null && ready.size() > 0) {
				cs = 0;
				in_transit = true;
			}
		} else {
			cs++;
			if (cs >= CS) {
				tq = cs = 0;  
				in_transit = false;
			}
		}
	}
}