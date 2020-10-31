#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#  ThreadSafeQueue.py
#  
#  Copyright 2020 kangshan <kangshan123@yeah.net>
#  
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or
#  (at your option) any later version.
#  
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#  
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
#  MA 02110-1301, USA.
#  
#  


def main(args):
    return 0

if __name__ == '__main__':
    print("new a queue")
    queue = ThreadSafeQueue(max_size=100)    
    print("new a queue")
    


# Thread safe queue

import threading
import time

class ThreadSafeException(Exception):
	pass

class ThreadSafeQueue(object):
	def __init__(self, max_size=0):
		self.max_size = max_size
		self.queue = []
		self.lock = threading.Lock()
		self.condition = threading.Condition()
		
	def size(self):
		self.lock.require()
		size = len(self.queue)
		self.lock.release()
		return size
		
	def push(self, value):
		if(self.max_size != 0 and self.size() > self.max_size):
			return ThreadSafeException()
		self.lock.acquire()
		self.queue.append(value)
		self.lock.release()
		self.condition.acquire()
		self.condition.notify()
		self.condition.release()
		pass
		
	def pop(self, block=False, timeout=0):
		if(self.size() == 0):
			if(block):
				self.condition.acquire()
				self.condition.wait(timeout)
			else:
				return None
		if(self.size() == 0):
			return None
		self.lock.acquire()
		item = self.queue.pop()
		self.lock.release()
		return item

	
