package petadoptionapp;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainFrame frame = new MainFrame();
			frame.setExtendedState(MainFrame.MAXIMIZED_BOTH); 
			frame.setUndecorated(true); 
			frame.setVisible(true);
		});
	}
}
