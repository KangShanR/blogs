# coding: utf-8 for the utf-8 comment
#!/usr/bin/env python
# 
#
#  list_test.py
#  
#  Copyright 2019 kangshan <kangshan123@yeah.net>
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

# if __name__ == '__main__':
    #import sys
    #sys.exit(main(sys.argv))
    



# del list element 列表 list
names = []
# append the element to the list's nail
names.append('kang')
names.append('shan')
names.append('Jungle')
names.append(' ')
names.append('silence')
names.append('kfc')
names.append(' JFK')
# pop the element assigned
names.pop(1)
print(names)
# pop the last element (default)
names.pop()
print("default poped:")
print(names)
# delete by value
names.remove(names[1])
print(names)
# delete from root
del names[1]
print(names)

# sort
print('sort the names')
names.append('Kang')
names.sort()
print(names)
names.sort(reverse = True)
print(names)
# check the value assigned. result ： just the reference be copy to 
# new object
names2 = names
print(names2)
names.append('小付付')
print(names)
print(names2)
print(names2[len(names) - 1])

# foreach body
for name in names:
	print('hello,' + name.title() + '. How do you do ?')
