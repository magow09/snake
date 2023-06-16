/**
 * Snake
 *
 * @author Marc Oliveras <admin@oligalma.com> 
 * @link http://www.oligalma.com
 * @copyright 2021 Oligalma
 * @license GPL License v3
 */

package snake.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Frame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel = new MainPanel(this);
	private ControlsPanel controlsPanel = new ControlsPanel(mainPanel);

	public Frame()
	{
		getContentPane().add(controlsPanel, BorderLayout.SOUTH);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		pack();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2,
				(d.height - getSize().height) / 2);
		setTitle("Snake");
		setResizable(false);	
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public ControlsPanel getControlsPanel() {
		return controlsPanel;
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}
}
