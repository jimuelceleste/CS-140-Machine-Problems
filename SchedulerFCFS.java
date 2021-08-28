public class SchedulerFCFS extends Scheduler {
	SchedulerFCFS(String inputFilename, SharedVars sv) {
		super(inputFilename);	
		this.sv = sv;
		this.sv.schedulerName = "FCFS";
		role = this.sv.role;
	}

	@Override
	public void nextRunning() {
		if (running == null) {
			for (City city : ready) {
				if (city.task.name.equals(role)) {
					// Remarks
					remarks += "Emperor chose " + city.name + ". ";
					running = city;
					ready.remove(ready.indexOf(running));
					break;
				}
			}
		}
	}
}