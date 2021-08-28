from queue_linkedlist import Queue
from city import City

def next_run(running, ready, terminated, role):
	if running == None:
		return ready.dequeue()
	
	elif running.task.time == 0:
		terminated.enqueue(running)
		city = ready.head
		while city != None:
			if city.task.name == role:
				return ready.get(city)
			city = city.next
		return None

	return running


def next_wait(waiting, ready, terminated, role):
	city = waiting.head
	while city != None:
		if city.task.time == 0:
			if city.task.next == None:
				terminated.enqueue(waiting.get(city))
			else:
				city.task = city.task.next # MIGHT BE REMOVED SOON
				ready.enqueue(waiting.get(city))
			city = city.next
	city = ready.head
	while city != None:
		if city.task.name != role:
			waiting.enqueue(ready.get(city))
		city = city.next


def main():
	cities = {}
	with open('WarStrategy.txt', 'r') as file:
		for city in file.readlines():
			city = city.split()
			time = int(city[-1])
			city = City(''.join(city[:-1]).strip())
			cities[time] = city
	running = None
	ready = Queue()
	waiting = Queue()
	terminated = Queue()

	role = ''
	roles = {'A':'attack', 'B':'siege', 'C':'defend', 'D':'parley'}
	while role not in roles:
		print('It has begun, your excellency. Here are the phases of battle:')
		print('A. Attack\nB. Siege\nC. Defend\nD. Parley')
		role = input('I would like to lead choice: ').upper()
	role = roles[role]


	day = 1
	while True:
		if day in cities:
			ready.enqueue(cities[day])
			del cities[day]

		running = next_run(running, ready, terminated, role)
		waiting = next_wait(waiting, ready, terminated, role)

		print(running.name, running.task.name, running.task.time)
		day += 1


if __name__ == '__main__':
	main()