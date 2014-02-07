/*
 * File: Breakout.java
 * -------------------
 * Name:Urvashi Jouhari
 * Section Leader: Bryan Offutt
 * 
 * This file implements the game of Breakout-extended version.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;


import java.applet.*;
import java.awt.*;
import java.awt.event.*;


public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board
	 *  Should not be used directly (use getWidth()/getHeight() instead).
	 *  * */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
		(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/**Delay for the animation effect*/
	private static final int ANIMATION_DELAY = 10;

	private int turnsLeft = NTURNS;

	private GRect brick;

	private GRect paddle;

	private GOval ball;

	private double vx,vy;

	private int brickCounter=100;

	private RandomGenerator rgen=RandomGenerator.getInstance();

	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");


	public void init(){

		addMouseListeners();
		setBackground(Color.lightGray);
	}

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {

		displayWelcome();
		setupGame();
		playGame();
	}

	/**Displays a welcome screen for a few seconds before the game begins. */
	private void displayWelcome(){

		GLabel welcome= new GLabel ("*WELCOME TO BREAKOUT*", getWidth()/2, getHeight()/2);
		welcome.setFont("Helvetica-20");
		welcome.setColor(Color.BLUE);
		double x = welcome.getWidth()/2;
		double y = welcome.getHeight()/2;
		welcome.setLocation(getWidth()/2-x, getHeight()/2-y);
		add(welcome);
		pause(2000);
		remove(welcome);
	}

	/**Defines the method for creating the components of the game*/
	private void setupGame(){

		createBrickRows();
		createPaddle();
		createBall();
	}

	/**Defines the method for playing the game.
	 * Velocities for the ball are defined. 
	 * While the game still has bricks left and the player still has turns remaining,
	 * the ball loops in the moveBall() method.
	 * If the player has lost all turns, the game is declared over.
	 * If the player hasn't exhausted all turns and all bricks have been removed, 
	 * the player is declared winner. */
	private void playGame(){

		waitForClick();
		setBallVelocities();

		while (brickCounter>0 && turnsLeft>0){

			moveBall();
		}

		if (turnsLeft==0){

			displayScore();
			displayGameOver();
		}

		if (brickCounter==0 && turnsLeft>0){

			displayScore();
			displayGameWon();
		}
	}

	/**Defines the method for creating the rows of bricks.
	 * The initial x and y coordinates are calculated, then they are subsequently 
	 * calculated depending on the brick number and row number respectively.
	 * Depending on the rows the bricks belong to, their colors are assigned.
	 */
	private void createBrickRows(){

		for (int i=0; i< NBRICK_ROWS; i++){

			for(int j=0; j<NBRICKS_PER_ROW; j++){

				double x=getWidth()/2 - (NBRICKS_PER_ROW * BRICK_WIDTH)/2 - ((NBRICKS_PER_ROW-1) * BRICK_SEP)/2;
				double y=BRICK_Y_OFFSET ;

				x = x + (j*BRICK_WIDTH)+ (j*BRICK_SEP);
				y = y + (i*BRICK_HEIGHT) + (i* BRICK_SEP);

				brick = new GRect (x,y,BRICK_WIDTH,BRICK_HEIGHT);
				brick.setFilled(true);
				add(brick);


				if (i==0 || i==1){

					brick.setColor(Color.RED);
					brick.setFillColor(Color.RED);
				}

				if(i==2 || i==3){

					brick.setColor(Color.ORANGE);
					brick.setFillColor(Color.ORANGE);
				}

				if(i==4 || i==5){

					brick.setColor(Color.YELLOW);
					brick.setFillColor(Color.YELLOW);
				}

				if (i==6 || i==7){

					brick.setColor(Color.GREEN);
					brick.setFillColor(Color.GREEN);
				}

				if (i==8 || i==9){

					brick.setColor(Color.CYAN);
					brick.setFillColor(Color.CYAN);
				}
			}
		}
	}

	/** Defines the method for creating the paddle.
	 * The initial X coordinate of the paddle is the center of the screen.
	 * The y coordinate is calculated using the PADDLE OFFSET to place it at the bottom of the screen.
	 */
	private void createPaddle(){

		double x = getWidth()/2 - PADDLE_WIDTH/2;
		double y = getHeight()-(PADDLE_Y_OFFSET+PADDLE_HEIGHT);
		paddle = new GRect (x,y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.BLACK);
		paddle.setFillColor(Color.BLACK);
		add(paddle);
	}

	/**Every time the mouse is moved, the paddle X coordinate is updated.
	 * The "if-condition" ensures the paddle stays on the display screen
	 * */
	public void mouseMoved (MouseEvent e){	

		double changingX= e.getX();
		double y = getHeight()-(PADDLE_Y_OFFSET+PADDLE_HEIGHT);

		if ( changingX > 0 && changingX < getWidth() - PADDLE_WIDTH){
			paddle.setLocation(changingX, y);
		}
	}

	/**Defines the method for creating the ball
	 */
	private void createBall(){

		double x = getWidth()/2-BALL_RADIUS;
		double y = getHeight()/2-BALL_RADIUS;
		ball = new GOval (x,y,BALL_RADIUS*2, BALL_RADIUS*2);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		ball.setFillColor(Color.BLACK);
		add(ball);
	}

	/**Defines the method for setting the x and y velocities of the ball.
	 */
	private void setBallVelocities(){
		vy = 3.5;
		vx = rgen.nextDouble(1.0, 3.5);
		if (rgen.nextBoolean(0.5)){
			vx = -vx;
		}
	}

	/**Defines the method for moving the ball across the display window.
	 */
	private void moveBall(){

		pause(ANIMATION_DELAY);
		ball.move(vx, vy);

		double leftX = ball.getX();
		double rightX = ball.getX() + BALL_RADIUS*2;
		double topY = ball.getY();
		double bottomY = ball.getY() + BALL_RADIUS*2;

		/*If the ball hits the left end of the graphics window
		 * Or if it hits the right end of the graphics window,
		 * Reverse the X velocity.
		 */
		if (( leftX <= 0 && vx < 0 ) || (rightX >= (getWidth() - BALL_RADIUS*2) && vx > 0)) {
			vx = -vx;
		}

		/*If the ball hits the top end of the graphics window,
		 * Reverse the Y velocity.
		 */
		if (( topY <= 0 && vy < 0 )) {	
			vy = -vy;
		}

		/*If the ball hits the bottom end of the graphics window,
		 * Subtract and display the number of turns the player has left. 
		 */
		if (( bottomY >= (getHeight() - BALL_RADIUS*2))){

			vy=-vy;
			turnsLeft --;

			if (turnsLeft>1){

				pause (750);
				GLabel turnsLeft= new GLabel (+ this.turnsLeft + " Turns Left", getWidth()/2, getHeight()/2);
				turnsLeft.setColor(Color.RED);
				turnsLeft.setFont("Helvetica-45");
				double x = turnsLeft.getWidth()/2;
				double y = turnsLeft.getHeight()/2;
				turnsLeft.setLocation(getWidth()/2-x, getHeight()/2-y);
				add(turnsLeft);
				pause(750);
				remove(turnsLeft);
			}
			
			else if (turnsLeft==1){
				
				pause (750);
				GLabel turnsLeft= new GLabel (+ this.turnsLeft + " Turn Left", getWidth()/2, getHeight()/2);
				turnsLeft.setColor(Color.RED);
				turnsLeft.setFont("Helvetica-45");
				double x = turnsLeft.getWidth()/2;
				double y = turnsLeft.getHeight()/2;
				turnsLeft.setLocation(getWidth()/2-x, getHeight()/2-y);
				add(turnsLeft);
				pause(750);
				remove(turnsLeft);
			}
		}

		/*Assigns value to the variable collider*/
		GObject collider = getCollidingObject();

		/*If the object that the ball collided with is the paddle 
		 * and if the ball is on top of the paddle, reverse the y velocity.
		 */
		if(collider == paddle){

			if(vy>0){
				vy=-vy;
			}
			bounceClip.play();
			increaseBallVelocities();
		}

		/*If the object the ball collided with is the brick, remove it from the screen 
		 * Update the brick counter.
		 */
		else if (collider!=null){

			remove(collider);
			vy=-vy;
			brickCounter--;
			bounceClip.play();

		}
	}

	/**Checks the four corner points of the square in which the ball is inscribed
	 * Checks if the ball has collided with another object in the graphics window.
	 */
	private GObject getCollidingObject(){

		if (getElementAt (ball.getX(),ball.getY()) != null){
			return getElementAt(ball.getX(),ball.getY());
		}
		if (getElementAt ((ball.getX() + BALL_RADIUS*2),ball.getY()) != null){
			return getElementAt ((ball.getX() + BALL_RADIUS*2),ball.getY());
		}
		if (getElementAt (ball.getX(),(ball.getY()+ BALL_RADIUS*2)) != null){
			return getElementAt (ball.getX(),(ball.getY()+ BALL_RADIUS*2));
		}
		if (getElementAt ((ball.getX() + BALL_RADIUS*2),(ball.getY()+ BALL_RADIUS*2)) != null){
			return getElementAt ((ball.getX() + BALL_RADIUS*2),(ball.getY()+ BALL_RADIUS*2));
		}
		else{
			return null;
		}
	}

	/**Increases the x and y velocities gradually */
	public void increaseBallVelocities(){

		if (vx>0){
			vx += .002;
		}
		if (vx<0){
			vx += -.002;
		}
		if(vy>0){
			vy += .002;
		}
		if (vy<0){
			vy += -.002;
		}
	}

	/**Displays the score of the user, ten points for every brick removed. */
	private void displayScore(){

		int userScore= (100 - brickCounter)*10;
		GLabel score= new GLabel ("**Your new high score is: " + userScore + "**" , getWidth()/2, getHeight()/2);
		score.setColor(Color.BLUE);
		score.setFont("Helvetica-20");
		double x = score.getWidth()/2;
		double y = score.getHeight()/2;
		score.setLocation(getWidth()/2-x, getHeight() - y + (PADDLE_Y_OFFSET+PADDLE_HEIGHT)/5);
		add(score);	
	}

	/**If the user has exhausted all the turns without completing the game,
	 * A game over message is flashed in the center of the graphics window.
	 */
	private void displayGameOver(){

		remove(ball);
		GLabel gameOver= new GLabel ("GAME OVER", getWidth()/2, getHeight()/2);
		gameOver.setColor(Color.BLUE);
		gameOver.setFont("Helvetica-45");
		double x = gameOver.getWidth()/2;
		double y = gameOver.getHeight()/2;
		gameOver.setLocation(getWidth()/2-x, getHeight()/2-y);
		add(gameOver);	
	}

	/**If the user has completed the game successfully,
	 * A You have won message is displayed in the center of the window.
	 */
	private void displayGameWon(){

		remove(ball);
		GLabel gameWon= new GLabel ("CONGRATULATIONS!!! YOU HAVE WON!!!", getWidth()/2, getHeight()/2);
		gameWon.setFont("Helvetica-20");
		gameWon.setColor(Color.BLUE);
		double x = gameWon.getWidth()/2;
		double y = gameWon.getHeight()/2;
		gameWon.setLocation(getWidth()/2-x, getHeight()/2-y);
		add(gameWon);
	}

}



