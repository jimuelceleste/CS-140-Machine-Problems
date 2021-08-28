from queue_linkedlist import Queue

class Task:
	def __init__(self, name, time):
		self.name = name
		self.time = time
		self.next = None

class City:
	def __init__(self, name):
		self.name = name
		self.tasks = Queue()
		with open(name+'.txt', 'r') as file:
			for task in file.readlines():
				task = task.strip().split()
				time = int(task[-1])
				name = ''.join(task[:-1])
				self.tasks.enqueue(Task(name, time))
				print(self.name, name, time)
		self.task = self.tasks.dequeue()
		self.next = None

	def do_task(self):
		self.task.time -= 1
		if self.task.time == 0:
			self.task = self.task.next

	# def end_task(self):
	# 	self.tasks.