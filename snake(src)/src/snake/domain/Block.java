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

public class Block extends Sprite
{
	@Override
	public void paint(Graphics2D g2) {
		g2.setColor(Color.green);
		g2.fillRect(x, y, width, height);
	}
}
