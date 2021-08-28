import java.util.List;
import java.util.ArrayList;

public class City {
	public String name;
	public int arrival;
	public List<Task> tasks;
	public Task task;

	City(String name, int arrival, List<Task> tasks) {
		this.name = name;
		this.arrival = arrival;
		this.tasks = tasks;
		task = this.tasks.get(0);
	}

	public void doTask() {
		task.duration -= 1;
		if (task.duration <= 0) {
			tasks.remove(0);
		}
	}

	public void nextTask() {
		if (!tasks.isEmpty()) {
			task = tasks.get(0);
		}
	}

	public boolean isDone() {
		return tasks.isEmpty();
	}

	public String getInfo() {
		if (task != null) {
			return name + " " + String.valueOf(task.duration);
		}
		
		return " ";
	}

	@Override
	public String toString() {
		if (task != null) {
			return name + " (" + task.name + ")";
		}
		
		return " ";
	}
}