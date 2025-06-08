package petadoptionapp;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainFrame frame = new MainFrame();
			frame.setExtendedState(MainFrame.MAXIMIZED_BOTH); // Maximize the window
			frame.setUndecorated(true); // Set to true if you want no title bar
			frame.setVisible(true);
		});
	}
}
