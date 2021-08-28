// import java.util.List;
// import java.util.ArrayList;
// import java.util.Collections;
import java.util.Scanner;

public class Demo {
	public static void main(String[] args) {
		SharedVars sv = new SharedVars();
		Scheduler sched = new SchedulerFCFS("WarStrategy.txt", "attack", sv);
		sched.createSchedule();

		// List<Integer> numbers = new ArrayList<Integer>();
		// for(int i = 0; i < 10; i++) {
		// 	numbers.add(i);
		// }

		// System.out.println(numbers);
		// numbers.remove(4);
		// System.out.println(numbers);
		// for(int i = 3; i < 6; i++) {
		// 	numbers.remove(i);
		// 	System.out.print("Removed " + i + ": ");
		// 	System.out.println(numbers);
		// }

		//Dictionary Demo ----------------------------------------------
		// Map m1 = new HashMap();
		// m1.put(1, new Task("Parley", 10));
		// m1.put(2, new Task("Defend", 1));
		// m1.put(3, new Task("Attack", 5));
		// System.out.print(m1);
		// System.out.println();

		// Scheduler Class Demo ----------------------------------------
		// Scanner in = new Scanner(System.in);
		// Scheduler sched = new Scheduler("WarStrategy.txt");
		// String input = "";
		// City city;

		// while(!(input = in.nextLine()).equals("exit")) {
		// 	if(input.equals("A")) {
		// 		for(Object value : sched.cities) {
		// 			city = (City) value; // type casting
		// 			System.out.println(city);

		// 			for(Task task : city.tasks) {
		// 				System.out.println("\t" + task);
		// 			}
		// 		}

		// 	} else {
		// 		for(Object value : sched.cities) {
		// 			city = (City) value;
		// 			System.out.println(value);
		// 		}
		// 	}
		// }
	}
}