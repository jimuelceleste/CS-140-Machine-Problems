public class Task {
	public String name;
	public int duration;

	Task(String name, int duration) {
		this.name = name;
		this.duration = duration;
	}

	public boolean isDone() {
		return (duration == 0);
	}

	@Override
	public String toString() {
		return (name + " " + Integer.toString(duration));
	}
}