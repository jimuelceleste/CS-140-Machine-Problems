public class SchedulerPriority extends Scheduler {
	SchedulerPriority(String inputFilename, SharedVars sv) {
		super(inputFilename);
		this.sv = sv;
		this.sv.schedulerName = "Priority";
		role = this.sv.role;
	}

	@Override
	public void nextRunning() {
		if (running == null) {
			for (City city : ready) {
				if (city.task.name.equals(role)) {
					if (running == null) {
						running = city;
					} else if (hasHigherPriority(city, running)) {
						running = city;
					}
				}
			}

			if (running != null) {
				ready.remove(ready.indexOf(running));
				remarks += "Emperor chose " + running.name + ". ";
			}
		}
	}

	public boolean hasHigherPriority(City city1, City city2) {
		// Returns true if city1 has higher priority than city2
		// Criteria: alphabetical order
		boolean result = false;
		
		for (int i = 0; i < city1.name.length() && result == false; i++) {
			if (i == city2.name.length()) {
				result = true;
			} else if (city1.name.charAt(i) < city2.name.charAt(i)) {
				result = true;
			} else if (city1.name.charAt(i) > city2.name.charAt(i)) {
				break; 
			}
		}

		return result;
	}
}