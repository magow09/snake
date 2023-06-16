/**
 * Snake
 *
 * @author Marc Oliveras <admin@oligalma.com> 
 * @link http://www.oligalma.com
 * @copyright 2021 Oligalma
 * @license GPL License v3
 */

package snake.domain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;

public class Snake extends Sprite
{
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	private LinkedList<Point> body = new LinkedList<Point>();
	private int direction; //  LEFT: 0 | RIGHT: 1 | UP: 2 | DOWN: 3
	
	public Snake(int x, int y, int width, int height, int direction)
	{
		Point p = new Point(x,y);
		body.add(p);
		
		this.width = width;
		this.height = height;
		this.direction = direction;
	}
	
	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	
	public int getDirection()
	{
		return direction;
	}
	
	public boolean collide(Sprite sprite) {
		Iterator<Point> it = body.iterator();
		int i = 0;
		while(it.hasNext())
		{
			Point point = it.next();
			if (point.getX() + width <= sprite.getX() || sprite.getX() + sprite.getWidth() <= point.getX()
					|| point.getY() + width <= sprite.getY()
					|| sprite.getY() + sprite.getHeight() <= point.getY())
				i++;
		}

		if(i == body.size())
			return false;
					
		return true;
	}
	
	public boolean collideItself() {
		Iterator<Point> it = body.iterator();
		int i = 0;
		while(it.hasNext())
		{
			Iterator<Point> it2 = body.iterator();
			Point point = it.next();
			
			while(it2.hasNext())	
			{
				Point point2 = it2.next();
				
				if(point.getX() != point2.getX() || point.getY() != point2.getY())
				{
					if (point.getX() + width <= point2.getX() || point2.getX() + width <= point.getX()
							|| point.getY() + width <= point2.getY()
							|| point2.getY() + height <= point.getY())
						i++;
				}
			}
		}

		if((((body.size() * body.size()) - body.size())) == i)
			return false;
		
		return true;
	}
	
	@Override
	public void paint(Graphics2D g2) {
		Iterator<Point> it = body.iterator();
		while(it.hasNext())
		{
			Point point = it.next();
			g2.setColor(Color.black);
			g2.fillRect(point.getX(), point.getY(), width, height);
		}
	}
	
	public void grow()
	{
		Point p =  new Point();
		Point last = body.getLast();

		if(direction == Snake.LEFT)
		{
			p.setLocation(last.getX() + width, last.getY());
		}
		else if(direction == Snake.RIGHT)
		{
			p.setLocation(last.getX() - width, last.getY());
		}
		else if(direction == Snake.UP)
		{
			p.setLocation(last.getX(), last.getY() + height);
		}
		else if(direction == Snake.DOWN)
		{
			p.setLocation(last.getX(), last.getY() - height);
		}
				
		body.addLast(p);		
	}
	
	public void move()
	{	
		if(body.size() > 0)
		{
			Point point = body.get(body.size() - 1);
			
			for(int i = body.size() - 2; i >= 0; i--)
			{
				Point point2 = body.get(i);

				point.setLocation(point2.getX(), point2.getY());
				
				point = point2;
			}
		}
		
		Point point = body.get(0);
		
		if (direction == Snake.LEFT)
		{
			point.setLocation(point.getX() - width, point.getY());
		}
		else if (direction == Snake.RIGHT)
		{
			point.setLocation(point.getX() + width, point.getY());
		}
		else if (direction == Snake.UP)
		{
			point.setLocation(point.getX(), point.getY() - width);
		}
		else if (direction == Snake.DOWN)
		{
			point.setLocation(point.getX(), point.getY() + width);
		}
	}
	
	public LinkedList<Point> getBody() {
		return body;
	}

	public void setBody(LinkedList<Point> body) {
		this.body = body;
	}
}
