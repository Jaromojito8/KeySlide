package views.game;

import java.awt.Font;
import javax.swing.JLabel;

/**
 * Label for the up direction.
 * @author heshamsalman
 *
 */
public class UpLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public UpLabel() {
		super();
		setText("<");
		setFont(new Font("Arial", Font.PLAIN, 512));
		setUI(new VerticalLabelUI(true));
	}
}
