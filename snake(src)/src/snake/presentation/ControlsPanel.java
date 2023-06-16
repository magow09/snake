/**
 * Snake
 *
 * @author Marc Oliveras <admin@oligalma.com> 
 * @link http://www.oligalma.com
 * @copyright 2021 Oligalma
 * @license GPL License v3
 */

package snake.presentation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JButton startButton = new JButton();
	private JSlider blocksSlider = new JSlider();
	private JSlider speedSlider = new JSlider();
	private JSlider sizeSlider = new JSlider();
	private JLabel scoreLabel = new JLabel();
	private JButton helpButton = new JButton();
	private MainPanel panel;
	
	public ControlsPanel(final MainPanel panel)
	{
		this.panel = panel;
		FlowLayout f = new FlowLayout();
		f.setHgap(10);
		this.setLayout(f);
		this.setBorder(new BevelBorder(1));
		
		startButton.setPreferredSize(new Dimension(85, 40));
		startButton.setText("START");
		startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(panel.isTitle())
				{
					panel.setTitle(false);
					panel.init();
				}
				else if(panel.isGameOver())
					panel.init();
			}
		});
		
		speedSlider.setPreferredSize(new Dimension(60, 40));
		speedSlider.setMinimum(1);
		speedSlider.setMaximum(10);
		speedSlider.setMajorTickSpacing(2);
		speedSlider.setToolTipText("Speed");
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setValue(panel.getSpeed());
		speedSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				panel.setSpeed(speedSlider.getValue());
				startButton.requestFocus();				
			}	
		});

		blocksSlider.setPreferredSize(new Dimension(80, 40));
		blocksSlider.setMinimum(0);
		blocksSlider.setMaximum(60);
		blocksSlider.setMajorTickSpacing(20);
		blocksSlider.setToolTipText("Blocks");
		blocksSlider.setPaintTicks(true);
		blocksSlider.setPaintLabels(true);
		blocksSlider.setValue(panel.getBlocksNumber());
		blocksSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				panel.setBlocksNumber(blocksSlider.getValue());
				startButton.requestFocus();
			}	
		});

		sizeSlider.setPreferredSize(new Dimension(50, 40));
		sizeSlider.setMinimum(1);
		sizeSlider.setMaximum(3);
		sizeSlider.setMajorTickSpacing(1);
		sizeSlider.setToolTipText("Board size");
		sizeSlider.setPaintTicks(true);
		sizeSlider.setPaintLabels(true);
		
		if(panel.getSquareSize() == 5)
			sizeSlider.setValue(1);
		else if(panel.getSquareSize() == 10)
			sizeSlider.setValue(2);
		else if(panel.getSquareSize() == 20)
			sizeSlider.setValue(3);
		else
			throw new IllegalArgumentException("Square size not allowed.");
				
		sizeSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int value;
				if(sizeSlider.getValue() == 1)
					value = 5;
				else if(sizeSlider.getValue() == 2)
					value = 10;
				else
					value = 20;
				
				panel.setSquareSize(value);
				startButton.requestFocus();
			}	
		});
		
		scoreLabel.setPreferredSize(new Dimension(70, 40));
		scoreLabel.setText("000");
		scoreLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
		
		helpButton.setPreferredSize(new Dimension(55, 40));
		helpButton.setText("???");
		helpButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			    JOptionPane.showMessageDialog(panel,"Snake 1.0\n\nby Marc Oliveras, " + Calendar.getInstance().get(Calendar.YEAR) + ". All rights reserved.\n\nadmin@oligalma.com\n\nhttp://www.oligalma.com","About...",JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		this.add(startButton);
		this.add(speedSlider);
		this.add(blocksSlider);
		this.add(sizeSlider);
		this.add(scoreLabel);
		this.add(helpButton);
	}
	
	public JLabel getScoreLabel() {
		return scoreLabel;
	}

	public void disableControls()
	{
		startButton.setEnabled(false);
		blocksSlider.setEnabled(false);
		speedSlider.setEnabled(false);
		sizeSlider.setEnabled(false);
	}
	
	public void enableControls()
	{
		startButton.setEnabled(true);
		blocksSlider.setEnabled(true);
		speedSlider.setEnabled(true);
		sizeSlider.setEnabled(true);
	}
}
