/**
 * Snake
 *
 * @author Marc Oliveras <admin@oligalma.com> 
 * @link http://www.oligalma.com
 * @copyright 2021 Oligalma
 * @license GPL License v3
 */

package snake.domain;

public abstract class Sprite implements Drawable {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean collide(Sprite sprite) {
		if (x + width < sprite.getX() || sprite.getX() + sprite.getWidth() < x
				|| y + width < sprite.getY()
				|| sprite.getY() + sprite.getHeight() < y)
			return false;
		
		return true;
	}
}
