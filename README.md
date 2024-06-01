# Java Implementation of A* Algorithm
This was a university project for the paper COMPX301. We were tasked with creating an implementation of the A* pathfinding algorithm using Java. Provided to us were the csv test files containing the points we were to path find between. These files are included in the repo.

The A* algorithm navigates to find the shortest path from a starting point to a goal point. It does this by assigning cost to each node. Assuming starting at node 0, if node 1 is 50 units away from node 0, node 1 costs 50 to travel to. The algorithm works through these nodes resulting in the cheapest cost to get to the goal node. 

## Demonstration of the code

These are some screenshots of the working code.
![Screenshot 2024-06-01 184705](https://github.com/cs1522561/a-_algorithm/assets/91705168/23b78dfa-3676-4c20-9c5e-a2519e536aab)
![Screenshot 2024-06-01 184818](https://github.com/cs1522561/a-_algorithm/assets/91705168/fb7f01a2-d528-4841-88e0-528dc1bbd1c6)

## Running the code

The code needs to be compiled before it can be run. The complete process to run the program is as follows:

```
javac Stars.java
java Stars <file.csv> <start index> <end index> <radius of circle able to travel>
```

An example of this is:

```
javac Stars.java
java Stars spiral_v2.csv 75 101 15
```


