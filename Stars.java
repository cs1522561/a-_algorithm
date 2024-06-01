import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.net.CookieHandler;
import java.util.List;
import java.util.concurrent.*;

public class Stars extends JPanel {
  //Padding around the edge of the jpanel
  private static int padding = 50;
  //the colour to draw the points as
  private Color pointColor = Color.red;
  //the size of the point
  private int pointWidth = 5;
  //array to store the coords of the points
  private static Double[][] coords;
  //list of ints that are the indexes of the final path
  private static List < Integer > finalPath;
  //the diameter of the range circle
  private static int d;
  //the start index
  private static int _start;
  //the end index
  private static int _end;
  //the width of the jpanel
  private static int paintWidth = 700;
  //the height of the jpanel
  private static int paintHeight = 700;

  //stars constructor
  public Stars(Double[][] coords) {
    Stars.coords = coords;
  }

  //draws on the jpanel
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphics = (Graphics2D) g;
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    //creates a list of the points changing the x and y values so that they are displayed on the jpanel nicely
    List < Point2D > graphPoints = new ArrayList < > ();
    for (int i = 0; i < coords.length; i++) {
      int x = (int)((coords[i][0] / 100 * (paintWidth - 2 * padding)) + padding);
      int y = (int)(paintHeight - ((coords[i][1] / 100 * (paintHeight - 2 * padding)) + padding));
      graphPoints.add(new Point2D.Double(x, y));
    }

    // draw white background
    graphics.setColor(Color.WHITE);
    graphics.fillRect(padding, padding, paintWidth - (2 * padding), paintHeight - 2 * padding);
    graphics.setColor(Color.BLACK);

    //adds the axis labels to the x and y axes
    ArrayList < Integer > axisScale = new ArrayList < > (Arrays.asList(0, 20, 40, 60, 80, 100));
    for (int i = 0; i < axisScale.size(); i++) {
      int x0 = padding;
      int y0 = paintHeight - (i * (paintHeight - padding * 2) / (axisScale.size() - 1) + padding);
      String yLabel = axisScale.get(i) + "";
      FontMetrics metrics = graphics.getFontMetrics();
      int labelWidth = metrics.stringWidth(yLabel);
      graphics.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
    }
    for (int i = 0; i < axisScale.size(); i++) {
      int x0 = i * (paintWidth - padding * 2) / (axisScale.size() - 1) + padding;
      int y0 = paintHeight - padding;
      String xLabel = axisScale.get(i) + "";
      FontMetrics metrics = graphics.getFontMetrics();
      int labelWidth = metrics.stringWidth(xLabel);
      graphics.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
    }

    // draw a black line around the white box
    graphics.drawLine(padding, paintHeight - padding, padding, padding);
    graphics.drawLine(padding, paintHeight - padding, paintWidth - padding, paintHeight - padding);
    graphics.drawLine(paintWidth - padding, paintHeight - padding, paintWidth - padding, padding);
    graphics.drawLine(paintWidth - padding, padding, padding, padding);

    graphics.setColor(pointColor);
    //draw each of the points on the screen
    for (int i = 0; i < graphPoints.size(); i++) {
      double x = graphPoints.get(i).getX() - (double) pointWidth / 2;
      double y = graphPoints.get(i).getY() - (double) pointWidth / 2;
      int ovalW = pointWidth;
      int ovalH = pointWidth;
      double rangeWidth = (double)(paintWidth / (double)(100 / d));
      double rangeHeight = (double)(paintHeight / (double)(100 / d));
      FontMetrics metrics = graphics.getFontMetrics();
      //draw the point
      graphics.fill(new Ellipse2D.Double(x, y, ovalW, ovalH));
      String indexLabel = i + "";
      //draw the index of the point underneath the point
      graphics.drawString(indexLabel, (int) x / 1, (int) y / 1 + metrics.getHeight() + 3);
      //draw the radius circles around the start and end points
      graphics.draw(new Ellipse2D.Double(graphPoints.get(_start).getX() - (double) rangeWidth / 2, graphPoints.get(_start).getY() - (double) rangeHeight / 2, rangeWidth, rangeHeight));
      graphics.draw(new Ellipse2D.Double(graphPoints.get(_end).getX() - (double) rangeWidth / 2, graphPoints.get(_end).getY() - (double) rangeHeight / 2, rangeWidth, rangeHeight));
    }

    //Check if the algorithm found a path
    if (finalPath != null) {
      //draw lines between each of the nodes in the path
      for (int i = 1; i < finalPath.size(); i++) {
        graphics.draw(new Line2D.Double(graphPoints.get(finalPath.get(i - 1)).getX(), graphPoints.get(finalPath.get(i - 1)).getY(), graphPoints.get(finalPath.get(i)).getX(), graphPoints.get(finalPath.get(i)).getY()));
      }
    }

    //draw the start node as orange
    graphics.setColor(Color.orange);
    graphics.fill(new Ellipse2D.Double(graphPoints.get(_start).getX() - (double) pointWidth / 2, graphPoints.get(_start).getY() - (double) pointWidth / 2, pointWidth, pointWidth));
    //draw the end node as pink
    graphics.setColor(Color.magenta);
    graphics.fill(new Ellipse2D.Double(graphPoints.get(_end).getX() - pointWidth / 2, graphPoints.get(_end).getY() - pointWidth / 2, pointWidth, pointWidth));
  }

  //calculate the euclidian distance between two points
  private double getDistance(Point2D a, Point2D b) {
    return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
  }

  //returns 0 < x < 1 if the point is inside the radius of an eclipse
  private double findPoints(double h, double k, double x, double y, double a, double b) {
    double p = (Math.pow((x - h), 2) / Math.pow(a, 2)) + (Math.pow((y - k), 2) / Math.pow(b, 2));
    return p;
  }

  //create the jpanel
  private void createAndShowGui(Double[][] coords) {
    MainPanel mainPanel = new MainPanel(coords);
    mainPanel.setPreferredSize(new Dimension(paintWidth, paintHeight));
    JFrame frame = new JFrame("A* Algorithm");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(mainPanel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  //checks if a point is inside of an ellipse
  private double checkInside(Point2D pointA, Point2D pointB) {
    double h = pointA.getX();
    double k = pointA.getY();
    double a = (double)(paintWidth / (double)(100 / d) / 2);
    double b = (double)(paintHeight / (double)(100 / d) / 2);
    double x = pointB.getX();
    double y = pointB.getY();
    double result = findPoints(h, k, x, y, a, b);
    return result;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        //makes sure the user inputs the correct number of arguments
        if (args.length != 4) {
          System.err.println("Correct usage: java Stars <filename> <start index> <end index> <distance>");
          return;
        }

        //read and save the csv file as a list of strings
        List < List < String >> records = new ArrayList < > ();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
          String line;
          while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            records.add(Arrays.asList(values));
          }
        } catch (Exception e) {
          System.err.println("died " + e);
        }
        //create a new stars object
        Stars s = new Stars(coords);

        //read and save the list of string coords and change them to doubles
        Double[][] coords = new Double[records.size()][2];
        for (int i = 0; i < records.size(); i++) {
          List < String > line = records.get(i);
          coords[i][0] = Double.parseDouble(line.get(0));
          coords[i][1] = Double.parseDouble(line.get(1));
        }
        //save the start index
        _start = Integer.parseInt(args[1]);
        //save the end index
        _end = Integer.parseInt(args[2]);
        //save the diameter of the range circle
        d = Integer.parseInt(args[3]);

        //check the start and end values are valid
        if (_start < 0 || _start >= coords.length) {
          System.err.println("Start value invalid");
          System.err.println("Valid range : 0 - " + (coords.length - 1));
          return;
        }
        if (_end < 0 || _end >= coords.length) {
          System.err.println("End value invalid");
          System.err.println("Valid range : 0 - " + (coords.length - 1));
          return;
        }

        //Convert the raw coordinates into the ones used by the jpanel to display them on the screen
        List < Point2D > graphPoints = new ArrayList < > ();
        for (int i = 0; i < coords.length; i++) {
          int x = (int)((coords[i][0] / 100 * (paintWidth - 2 * padding)) + padding);
          int y = (int)(paintHeight - ((coords[i][1] / 100 * (paintHeight - 2 * padding)) + padding));
          //int y = (int) ((getMaxScore() - coords[i][1]) * yScale + padding);
          graphPoints.add(new Point2D.Double(x, y));
        }

        //convert the list of coordinates into nodes
        List < Integer > path = s.createNodes(graphPoints);

        //save the final path so it can be drawn by the paintcomponent
        if (path != null) {
          finalPath = path;
        }
        System.err.println("Path: " + finalPath);

        //display the jpanel
        s.createAndShowGui(coords);
      }
    });
  }

  //backtracks through the parents of the end node to find the best path
  private List < Integer > getPath(Node end) {
    Node n = end;
    if (n == null) {
      return null;
    }

    List < Integer > idsOfNodes = new ArrayList < > ();
    while (n.parent != null) {
      idsOfNodes.add(n.id);
      n = n.parent;
    }
    idsOfNodes.add(n.id);
    Collections.reverse(idsOfNodes);
    return idsOfNodes;
  }

  //creates the nodes of the graph and each of their neighbour edges
  private List < Integer > createNodes(List < Point2D > graphPoints) {
    //create the start node
    Node start = new Node(getDistance(graphPoints.get(_start), graphPoints.get(_end)), _start, graphPoints.get(_start));
    //create the end node;
    Node end = new Node(0, _end, graphPoints.get(_end));
    //set the value of the path cost of the start to 0
    start.gCost = 0;

    //iterate through all of the points in the list and turn them into nodes
    List < Node > nodes = new ArrayList < > ();
    for (int i = 0; i < graphPoints.size(); i++) {
      if (i != _start && i != _end) {
        nodes.add(new Node(getDistance(graphPoints.get(i), graphPoints.get(_end)), i, graphPoints.get(i)));
      }
    }
    nodes.add(start);
    nodes.add(end);

    //for every node check every other node to see if they are a neighbour
    for (Node n: nodes) {
      for (Node m: nodes) {
        //if they are a neighbour, create a branch to the neighbour
        if (checkInside(n.points, m.points) < 1) {
          n.addBranch(getDistance(n.points, m.points), m);
        }
      }
    }
    //call the algorithm passing in the start and end nodes
    Node result = aStar(start, end);
    return getPath(result);
  }

  //actually run the a* algorithm
  private Node aStar(Node start, Node end) {
    //A list to store the open list and order it by size
    PriorityQueue < Node > openList = new PriorityQueue < > ();
    //a list to store the closed list and order it by size
    PriorityQueue < Node > closedList = new PriorityQueue < > ();

    //set the fcost of the start node
    start.fCost = start.gCost + start.calculateHCost(end);
    //add it to the open list
    openList.add(start);

    //while there are still paths to find
    while (openList.size() > 0) {
      //get the node that has the smallest fCost
      Node n = openList.peek();

      //if the node is the end node, return it
      if (n == end) {
        return n;
      }
      //go through each neighbour in n
      for (Node.Edge edge: n.neighbours) {
        Node m = edge.node;
        //calculate the cost to move to the neighbour
        double weightSum = n.gCost + edge.weight;
        //check if either of the lists doesnt contain the node 
        if (!openList.contains(m) && !closedList.contains(m)) {
          //set the parent to the previous node
          m.parent = n;
          //increase the gcost
          m.gCost = weightSum;
          //set the fcost as the sum
          m.fCost = m.gCost + m.calculateHCost(end);
          //add it to the open list
          openList.add(m);
        } else {
          //checks if the new path is shorter than an already existing one
          if (weightSum < m.gCost) {
            //set the parent
            m.parent = n;
            //increase the gcost
            m.gCost = weightSum;
            //set the f cost as the sum
            m.fCost = m.gCost + m.calculateHCost(end);
            //check if the node is in the closed list
            if (closedList.contains(m)) {
              //remove from the closed list
              closedList.remove(m);
              //add to the open list
              openList.add(m);
            }
          }
        }
      }
      //remove the node from the open list
      openList.remove(n);
      //add it to the closed list
      closedList.add(n);
    }
    //no path was found so return null
    return null;
  }

  //something to do with the jpanel
  static class MainPanel extends JPanel {
    public MainPanel(Double[][] coords) {
      setLayout(new BorderLayout());
      JPanel Stars = new Stars(coords);
      add(Stars, BorderLayout.CENTER);
    }
  }

  //Class to create nodes
  class Node implements Comparable < Node > {
    //the index of the node
    public int id;
    //the parent
    public Node parent = null;
    //list to store the neighbours
    public List < Edge > neighbours;
    //the corodinates of the node
    public Point2D points;
    //the fcost
    public double fCost;
    //the gcost
    public double gCost;
    //the hcost
    public double hCost;

    //constructor to create the node
    Node(double hCost, int index, Point2D points) {
      this.hCost = hCost;
      this.id = index;
      this.neighbours = new ArrayList < > ();
      this.points = points;
    }

    //override the compare method so the list of objects can be reversed
    @Override
    public int compareTo(Node n) {
      return Double.compare(this.fCost, n.fCost);
    }

    //a class representing the edge between nodes
    public static class Edge {
      //the distance between the nodes
      public double weight;
      //the node it came from
      public Node node;

      //constructor to create an edge
      Edge(double weight, Node node) {
        this.weight = weight;
        this.node = node;
      }
    }

    //method create a branch from a node
    public void addBranch(double weight, Node node) {
      Edge newEdge = new Edge(weight, node);
      neighbours.add(newEdge);
    }

    //return the hcost
    public double calculateHCost(Node target) {
      return hCost;
    }
  }

}