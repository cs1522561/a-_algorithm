# Java Implementation of A* AlgorithmThis was a university project for the paper COMPX301. We were tasked with creating an implementation of the A* pathfinding algorithm using Java. Provided to us were the csv test files containing the points we were to path find between. These files are included in the repo.

The A* algorithm navigates to find the shortest path from a starting point to a goal point. It does this by assigning cost to each node. Assuming starting at node 0, if node 1 is 50 units away from node 0, node 1 costs 50 to travel to. The algorithm works through these nodes resulting in the cheapest cost to get to the goal node. 

## Demonstration of the code

These are some screenshots of the working code.



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

e.g.
java Stars spiral_v2.csv 75 101 15
