class Node:
	def __init__(self):
		self.next = None

class Queue:
	def __init__(self):
		self.head = Node()
		# define a head

	def enqueue(self, data):
		rover = self.head
		while rover.next != None:
			rover = rover.next
		rover.next = data

	def dequeue(self):
		temp = self.head
		self.head = self.head.next
		return temp

	def get(self, data):
		temp = None
		back = self.head
		rover = self.head
		while rover != None:
			if rover is data:
				temp = rover
				back.next = rover.next
			back = rover
			rover = rover.next
		return temp