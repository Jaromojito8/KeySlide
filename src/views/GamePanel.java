package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.logging.Level;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import utilities.GameLog;

/**
 * GamePanel
 * @author heshamsalman
 *
 */
public class GamePanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	DirectionPanel directionPanel;
	Clip clip;
	Window window;
	private JPanel timePanel;
	private JProgressBar timeBar;
	private int timePosition = 0;
	private int score = 0;
	Timer timer;
	JButton scoreLabel;

	public GamePanel(final Window window) {
		this.window = window;
		setupGui();
	}

	private void setupGui() {
		setSize(1280, 720);
		setFocusable(true);
		setLayout(new BorderLayout());
		timePanel = new JPanel();
		timePanel.setBackground(Color.WHITE);
		timeBar = new JProgressBar();
		timeBar.setPreferredSize(new Dimension(1280, 50));
	    timeBar.setMaximum(maxTime());
	    timeBar.setMinimum(0);
	    timer = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    timeBar.setMaximum(maxTime());
				timeBar.setValue(timePosition);
				timePosition += 20;
				if (timeBar.getValue() >= maxTime()) {
					gameOver();
					window.switchToGameOver();
				}
			}
	    });
	    scoreLabel = new JButton("Score: " + score);
	    scoreLabel.setFont(new Font("Arial", Font.PLAIN, 40));
	    scoreLabel.setContentAreaFilled(false);
	    scoreLabel.setFocusPainted(false);
		scoreLabel.setBorder(BorderFactory.createEmptyBorder());
	    timePanel.add(timeBar);
	    add(scoreLabel, BorderLayout.NORTH);
	    add(timePanel, BorderLayout.SOUTH);
	    directionPanel = new DirectionPanel();
		directionPanel.setBackground(Color.WHITE);
	    addKeyListener(this);
	    add(directionPanel, BorderLayout.CENTER);
	}

	public void start() {
		doPlay(new File("Assets/Audio/Chiptune.wav"));
		score = 0;
		timePosition = 0;
		timer.start();
	}

	private void doPlay(final File url) {
		GameLog.log.entering(getClass().getName(), "doPlay");
	    try {
	        stopPlay();
	        AudioInputStream inputStream = AudioSystem
	                .getAudioInputStream(url);
			GameLog.log.log(Level.INFO, "Playing Audio");
	        clip = AudioSystem.getClip();
	        clip.open(inputStream);
	        clip.start();
	    } catch (Exception e) {
	        stopPlay();
	        System.err.println(e.getMessage());
	    }
		GameLog.log.exiting(getClass().getName(), "doPlay");
	}

	private void stopPlay() {
		GameLog.log.entering(getClass().getName(), "stopPlay");
	    if (clip != null) {
	        clip.stop();
	        clip.close();
			GameLog.log.log(Level.INFO, "Stopping Audio");
	        clip = null;
	    }
		GameLog.log.exiting(getClass().getName(), "doPlay");
	}


	public void gameOver() {
		directionPanel.getInstructionController().nextInstruction();
		GameLog.log.entering(getClass().getName(), "gameOver");
		stopPlay();
		timePosition = 0;
		timer.stop();
		timeBar.setValue(timePosition);
		window.switchToGameOver();
		GameLog.log.exiting(getClass().getName(), "gameOver");
	}

	public void restartTimer() {
		GameLog.log.entering(getClass().getName(), "restartTimer");
		timePosition = 0;
		timeBar.setValue(timePosition);
		timer.restart();
		GameLog.log.exiting(getClass().getName(), "restartTimer");
	}


	private int maxTime() {
		int time = 1500 - (30 * score);
		if (time < 500) {
			return 500;
		} else {
			return time;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		GameLog.log.entering(getClass().getName(), "keyPressed");
		if (e.getKeyCode() == KeyEvent.VK_F24) {

		}
		else if(e.getKeyCode() == directionPanel.getInternalKey()){
	    	directionPanel.updateDirection();
	    	restartTimer();
	    	score+=1;
	    	scoreLabel.setText("Score: " + score);
	    }
	    else {
	    	gameOver();
	    }
		GameLog.log.exiting(getClass().getName(), "keyPressed");
	}

	public int getScore() {
		return score;
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	/**
	 * Required if we add a key listener to this class.
	 */
	@Override
	public void addNotify() {
		super.addNotify();
		requestFocus();
	}
}
