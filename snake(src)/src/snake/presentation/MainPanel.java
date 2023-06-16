/**
 * Snake
 *
 * @author Marc Oliveras <admin@oligalma.com> 
 * @link http://www.oligalma.com
 * @copyright 2021 Oligalma
 * @license GPL License v3
 */

package snake.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JPanel;
import snake.utils.Utils;
import snake.domain.Block;
import snake.domain.Food;
import snake.domain.Point;
import snake.domain.Snake;
import snake.domain.Sprite;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class MainPanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;
	private BufferedImage buffer;
	private Thread th;
	private Snake snake;
	private Food food;
	private LinkedList<Block> blocks;
	private boolean gameOver;
	private int iteration = 0;
	private Frame frame;
	private int blocksNumber = 20;
	private int speed = 5;
	private int squareSize = 10; // 5, 10 or 20
	private boolean title = true;
	private Image titleSnakeImage;
	private Clip crashSound;
	private Clip growSound;
	private boolean directionChanged;

	public MainPanel(Frame frame)
	{
		this.frame = frame;
		setSize(new Dimension(500, 400));
		setPreferredSize(new Dimension(500, 400));
		buffer = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);

		String key;
		KeyStroke keyStroke;
		AbstractAction leftAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(snake.getDirection() == Snake.UP || snake.getDirection() == Snake.DOWN)
				{
					snake.setDirection(Snake.LEFT);	
					snake.move();
					directionChanged = true;
				}			
			}};
		 key = "LEFT";
		 keyStroke = KeyStroke.getKeyStroke(key);
		 this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
		 this.getActionMap().put(key, leftAction);

		AbstractAction rightAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(snake.getDirection() == Snake.UP || snake.getDirection() == Snake.DOWN)
				{
					snake.setDirection(Snake.RIGHT);
					snake.move();
					directionChanged = true;
				}		
			}};
		 key = "RIGHT";
		 keyStroke = KeyStroke.getKeyStroke(key);
		 this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
		 this.getActionMap().put(key, rightAction);
		 
		AbstractAction upAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(snake.getDirection() == Snake.LEFT || snake.getDirection() == Snake.RIGHT)
				{
					snake.setDirection(Snake.UP);
					snake.move();
					directionChanged = true;
				}			
			}};
		 key = "UP";
		 keyStroke = KeyStroke.getKeyStroke(key);
		 this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
		 this.getActionMap().put(key, upAction);
				 
		AbstractAction downAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(snake.getDirection() == Snake.LEFT || snake.getDirection() == Snake.RIGHT)
				{
					snake.setDirection(Snake.DOWN);
					snake.move();
					directionChanged = true;
				}	
			}};
		 key = "DOWN";
		 keyStroke = KeyStroke.getKeyStroke(key);
		 this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
		 this.getActionMap().put(key, downAction);
		 
		AbstractAction enterAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(title)
				{
					title = false;
					init();
				}
				else if(gameOver)
					init();
			}};
		 key = "ENTER";
		 keyStroke = KeyStroke.getKeyStroke(key);
		 this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
		 this.getActionMap().put(key, enterAction);
		
		titleSnakeImage = Utils.getImage("snake.png");

		crashSound = Utils.getSound("crash.wav");
		growSound = Utils.getSound("blop.wav");

		repaint();
	}

	public void init()
	{
		gameOver = false;
		blocks = new LinkedList<Block>();
		
		for(int i = 1; i <= blocksNumber; i++)
		{
			while(true)
			{
				Block block = new Block();
				block.setX(Utils.random(0, (getWidth() - 1) / squareSize) * squareSize);
				block.setY(Utils.random(0, (getHeight() - 1) / squareSize) * squareSize);
				block.setWidth(squareSize);
				block.setHeight(squareSize);
				
				if(collideBlocks(block))
					continue;
				
				blocks.add(block);
				break;
			}
		}		
		
		while(true)
		{
			boolean snakeOK = true;
			
			snake = new Snake(Utils.random(0, (getWidth() - 1) / squareSize) * squareSize, 
					Utils.random(0, (getHeight() - 1) / squareSize) * squareSize, squareSize, squareSize,
					Utils.random(0, 3));

			Iterator<Block> it = blocks.iterator();
			while(it.hasNext())
			{
				Block block = it.next();
				if(snake.collide(block))
					snakeOK = false;
			}
			
			if(snakeOK)
				break;
		}
			
		while(true)
		{					
			food = new Food();
			food.setWidth(squareSize);
			food.setHeight(squareSize);
			food.setX(Utils.random(0, (getWidth() - 1) / squareSize) * squareSize);
			food.setY(Utils.random(0, (getHeight() - 1) / squareSize) * squareSize);

			if(!collideBlocks(food))
				break;
		}
		
		frame.getControlsPanel().getScoreLabel().setText(String.format("%03d", snake.getBody().size() - 1));
		frame.getControlsPanel().disableControls();
		
		th = new Thread(this);
		th.start();
	}
	

	public void paint(Graphics g)
	{	
		Graphics2D g2 = (Graphics2D) buffer.createGraphics();
		
		if(title)
		{
			// Paint background
			g2.setColor(Color.orange);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g2.drawImage(titleSnakeImage, 0, 0, this.getWidth(), this.getHeight(), this);
			g2.setColor(Color.black);
			g2.setFont(new Font("Verdana", Font.BOLD, 80));
			g2.drawString("SNAKE", 180, 270);
			g2.setFont(new Font("Verdana", Font.BOLD, 20));
			g2.drawString("by Marc Oliveras", 210, 300);
			g2.setFont(new Font("Verdana", Font.BOLD, 30));
			g2.drawString("Hit enter to start", 200, 350);			
		}
		else
		{
			// Paint background
			g2.setColor(Color.white);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			// Paint blocks
			Iterator<Block> it = blocks.iterator();
			while(it.hasNext())
			{
				Block block = it.next();
				block.paint(g2);
			}
			
			// Paint food
			if(iteration % 2 == 0)
				food.paint(g2);
			
			// Paint snake
			snake.paint(g2);
			
			// Paint game over
			if(gameOver)
			{
				g2.setColor(new Color(200,0,255));
				g2.setFont(new Font("Verdana", Font.BOLD, 70));
				g2.drawString("GAME OVER", 16, this.getHeight() / 2);	
				g2.setFont(new Font("Verdana", Font.BOLD, 30));
				g2.drawString("Hit enter to start", 120, this.getHeight() / 2 + 50);			
			}
		}
		g.drawImage(buffer, 0, 0, this);
	}
	
	@Override
	public void run()
	{		
		while(!gameOver)
		{
			iteration++;
			if(iteration == 10)
				iteration = 0;
			
			if(!directionChanged)
				snake.move();
			else
				directionChanged = false;

			Point point = snake.getBody().get(0);
			if(point.getX() < 0 || point.getX() + snake.getWidth() > this.getWidth() || 
					point.getY() < 0 || point.getY() + snake.getHeight() > this.getHeight())
			{
				Utils.playSound(crashSound);
				gameOver = true;
			}
			
			Iterator<Block> it = blocks.iterator();
			while(it.hasNext())
			{
				Block block = it.next();
				if(snake.collide(block))
				{
					Utils.playSound(crashSound);
					gameOver = true;
				}
			}
			
			if(snake.collide(food))
			{ 
				Utils.playSound(growSound);
				snake.grow();
				frame.getControlsPanel().getScoreLabel().setText(String.format("%03d", snake.getBody().size() - 1));
				
				while(true)
				{					
					food = new Food();
					food.setWidth(squareSize);
					food.setHeight(squareSize);
					food.setX(Utils.random(0, (getWidth() - 1) / squareSize) * squareSize);
					food.setY(Utils.random(0, (getHeight() - 1) / squareSize) * squareSize);
	
					if(!collideBlocks(food))
						break;
				}
			}
			else if(snake.collideItself())
			{
				Utils.playSound(crashSound);
				gameOver = true;
			}
			
			try {
				Thread.sleep(400 / speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			repaint();
		}
		
		frame.getControlsPanel().enableControls();
	}

	private boolean collideBlocks(Sprite sprite)
	{
		Iterator<Block> it = blocks.iterator();
		while(it.hasNext())
		{
			Block block = it.next();
			if(sprite.collide(block))
				return true;
		}
		
		return false;
	}
	
	public boolean isTitle() {
		return title;
	}

	public void setTitle(boolean title) {
		this.title = title;
	}
	
	public int getBlocksNumber() {
		return blocksNumber;
	}

	public void setBlocksNumber(int blocksNumber) {
		this.blocksNumber = blocksNumber;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getSquareSize() {
		return squareSize;
	}

	public void setSquareSize(int squareSize) {
		this.squareSize = squareSize;
	}
}
