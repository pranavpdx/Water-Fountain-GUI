//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    The class creates a GUI water fountain
// Course:   CS 300 Spring 2022
//
// Author:   Pranav Sharma
// Email:    pnsharma@wisc.edu
// Lecturer: Mouna Kacem 
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons:         n/a
// Online Sources:  n/a
//
///////////////////////////////////////////////////////////////////////////////
import java.io.File;
import java.util.Random;
import processing.core.PImage;

public class Fountain {

  // private static fields that are used in the class
  private static Random randGen;
  private static PImage fountainImage;
  private static int positionX;
  private static int positionY;
  private static Droplet[] droplets;
  private static int startColor;
  private static int endColor;

  /**
   * Program starts here, method runs the Utility.runApplication() method, which calls setup() and
   * draw()
   * 
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    Utility.runApplication();
  }

  /**
   * Method runs testing methods and initializes static fields
   */
  public static void setup() {
    System.out.println(testUpdateDroplet());
    System.out.println(testRemoveOldDroplets());
    randGen = new Random();
    positionX = Utility.width() / 2;
    positionY = Utility.height() / 2;
    fountainImage = Utility.loadImage("images" + File.separator + "fountain.png");
    droplets = new Droplet[800];
    startColor = Utility.color(23, 141, 235);
    endColor = Utility.color(23, 200, 255);

  }

  /**
   * Method draws all the contents of the window that pops up, always being run
   */
  public static void draw() {
    Utility.background(Utility.color(253, 245, 230));
    Utility.fill(Utility.color(23, 141, 235));
    Utility.image(fountainImage, positionX, positionY);
    createNewDroplets(10);
    // iterates over all droplets to update them, which are drawn in their updated positions
    for (int i = 0; i < droplets.length; i++) {
      if (droplets[i] != null) {
        updateDroplet(i);
      }
    }
    removeOldDroplets(80);
  }

  /**
   * Updates the position, age, and Y velocity of a specific after drawing a circle to represent the
   * droplet
   * 
   * @param index The index of the droplet being updates in the droplets array
   */
  public static void updateDroplet(int index) {
    // draws the droplet
    Utility.fill(droplets[index].getColor(), droplets[index].getTransparency());
    Utility.circle(droplets[index].getPositionX(), droplets[index].getPositionY(),
        droplets[index].getSize());
    // updates the droplet
    droplets[index].setVelocityY(droplets[index].getVelocityY() + 0.3f);
    droplets[index].setPositionX(droplets[index].getPositionX() + droplets[index].getVelocityX());
    droplets[index].setPositionY(droplets[index].getPositionY() + droplets[index].getVelocityY());
    droplets[index].setAge(droplets[index].getAge() + 1);
  }

  /**
   * Iterates over the droplets array and adds a certain number of droplets (specified by the
   * caller) in null positions in the array
   * 
   * @param numberOfDroplets The number of droplets to be added
   */
  public static void createNewDroplets(int numberOfDroplets) {
    int count = 0;
    // iterates over all the droplets until a null position is found
    for (int i = 0; i < droplets.length; i++) {
      if (droplets[i] == null) {
        // once found, creates a new droplet at that position if count is under numberOfDroplets
        if (count < numberOfDroplets) {
          droplets[i] = new Droplet(positionX + randGen.nextFloat() * 6 - 3,
              positionY + randGen.nextFloat() * 6 - 3, randGen.nextFloat() * 7 + 4,
              Utility.lerpColor(startColor, endColor, randGen.nextFloat()));
          droplets[i].setVelocityX(randGen.nextFloat() * 2 - 1);
          droplets[i].setVelocityY(randGen.nextFloat() * 5 - 10);
          droplets[i].setAge(randGen.nextInt(41));
          droplets[i].setTransparency(randGen.nextInt(96) + 32);
          count++;
        } else {
          // if count exceeds number of droplets to be added, methods ends
          return;
        }
      }
    }
    return;
  }

  /**
   * Methods removes any droplets that exceed a max age
   * 
   * @param maxAge The max age a droplet can be before being removed
   */
  public static void removeOldDroplets(int maxAge) {
    for (int i = 0; i < droplets.length; i++) {
      if (droplets[i] != null && droplets[i].getAge() > maxAge) {
        droplets[i] = null;
      }
    }
  }

  /**
   * Callback method is called whenever the mouse is pressed, changes positionX and positionY to
   * where mouse is
   */
  public static void mousePressed() {
    int xpos = Utility.mouseX();
    int ypos = Utility.mouseY();
    positionX = xpos;
    positionY = ypos;
  }

  /**
   * Callback method is called whenever a key is pressed, saves a screenshot if the s key is clicked
   * 
   * @param keyClicked The key that is clicked
   */
  public static void keyPressed(char keyClicked) {
    if (keyClicked == 's' || keyClicked == 'S') {
      Utility.save("screenshot.png");
    }
  }

  /**
   * This tester initializes the droplets array to hold at least three droplets. Creates a single
   * droplet at position (3,3) with velocity (-1,-2). Then checks whether calling updateDroplet() on
   * this droplets index correctly results in changing its position to (2.0, 1.3).
   *
   * @return true when no defect is found, and false otherwise
   */
  private static boolean testUpdateDroplet() {
    droplets = new Droplet[] {new Droplet(), new Droplet(), new Droplet(), null, null};
    droplets[0].setPositionX(3);
    droplets[0].setPositionY(3);
    droplets[0].setVelocityX(-1);
    droplets[0].setVelocityY(-2);
    updateDroplet(0);
    // checks if updated properly
    if (!(Math.abs(droplets[0].getPositionX() - 2.0) < 0.001)
        && !(Math.abs(droplets[0].getPositionY() - 1.3) < 0.001)) {
      System.out.println(
          "Error: droplet position did not correctly update when updateDroplets() method is called");
      return false;
    }
    return true;
  }

  /**
   * This tester initializes the droplets array to hold at least three droplets. Calls
   * removeOldDroplets(6) on an array with three droplets (two of which have ages over six and
   * another that does not). Then checks whether the old droplets were removed and the young droplet
   * was left alone.
   *
   * @return true when no defect is found, and false otherwise
   */
  private static boolean testRemoveOldDroplets() {
    droplets = new Droplet[] {new Droplet(), new Droplet(), new Droplet(), null, null};
    droplets[0].setAge(7);
    droplets[1].setAge(9);
    droplets[2].setAge(3);
    removeOldDroplets(6);
    // checks to see if droplets were correctly removed
    if (droplets[0] == null && droplets[1] == null && droplets[2] != null) {
      return true;
    } else {
      System.out.println(

          "Error: droplets that are above the max age were not removed when removeOldDroplets() "
              + "is called");
      return false;
    }

  }
}
