package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import petadoptionapp.FontUtils;

public class Contact {
    private MainFrame mainFrame;
    private JPanel contactPanel;
    
    public Contact(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public JPanel createPage() {
        contactPanel = new JPanel(new GridBagLayout());
        contactPanel.setOpaque(true);
        contactPanel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel();
        label.setFont(FontUtils.Arial(24f));
        label.setForeground(Color.decode("#060644"));
        contactPanel.add(label);
        
        return contactPanel;
    }
}

class ContactDialog extends JDialog {
    private Image backgroundImage;
    private Image secondImage;
    private boolean showingSecondImage = false;
    private boolean showClickMeText = true;
    private JPanel contentPanel;
    private int clickMeTextYOffset = 90; // Adjust this value to move the "Click Me!" text up/down

    public ContactDialog(Frame owner, String contactNumber1, String contactNumber2, String contactEmail) {
        super(owner, "Contact Us", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setSize(500, 500);

        try {
            URL firstImageUrl = getClass().getResource("/Contact1.png");
            if (firstImageUrl != null) {
                backgroundImage = new ImageIcon(firstImageUrl).getImage();
            }
            
            URL secondImageUrl = getClass().getResource("/Contact2.png");
            if (secondImageUrl != null) {
                secondImage = new ImageIcon(secondImageUrl).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout()) {
        	@Override
        	protected void paintComponent(Graphics g) {
        	    super.paintComponent(g);
        	    Graphics2D g2 = (Graphics2D) g;
        	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        	    Image currentImage = showingSecondImage ? secondImage : backgroundImage;
        	    if (currentImage != null) {
        	        g2.drawImage(currentImage, 0, 0, getWidth(), getHeight(), this);
        	    }

        	    // Draw "Click Me!" text at the top if it should be visible
        	    if (showClickMeText) {
        	        g2.setFont(FontUtils.Arial(34f));
        	        g2.setColor(Color.decode("#b88917"));

        	        String clickText = "Click Me!";
        	        FontMetrics fm = g2.getFontMetrics();
        	        int textWidth = fm.stringWidth(clickText);
        	        int x = (getWidth() - textWidth) / 2;
        	        int y = clickMeTextYOffset; // Use the offset variable here

        	        // Draw the text without any background
        	        g2.setColor(Color.decode("#b88917"));
        	        g2.drawString(clickText, x, y);
        	    }

        	    int arc = 20;
        	    g2.setColor(new Color(0, 0, 0, 0));
        	    g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        	    g2.setColor(Color.BLACK);
        	    g2.setStroke(new BasicStroke(1));
        	    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        	}
        };
        mainPanel.setBorder(new EmptyBorder(15, 30, 50, 30));
        
        JButton closeButton = createCloseButton();
        closeButton.addActionListener(e -> dispose());
        
        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButtonPanel.setOpaque(false);
        closeButtonPanel.add(closeButton);
        mainPanel.add(closeButtonPanel, BorderLayout.NORTH);

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isDescendingFrom(e.getComponent(), closeButtonPanel)) {
                    // Toggle the "Click Me!" text visibility based on current state
                    if (showingSecondImage) {
                        // If we're showing contact info and user clicks, go back to envelope and show "Click Me!" again
                        showClickMeText = true;
                    } else {
                        // If we're showing the envelope with "Click Me!" and user clicks, hide the text and show contact info
                        showClickMeText = false;
                    }
                    
                    showingSecondImage = !showingSecondImage;
                    toggleContactVisibility();
                    mainPanel.repaint();
                    
                    Timer timer = new Timer(20, null);
                    final float[] opacity = {0f};
                    timer.addActionListener(evt -> {
                        opacity[0] += 0.05f;
                        if (opacity[0] >= 1.0f) {
                            opacity[0] = 1.0f;
                            timer.stop();
                        }
                        mainPanel.repaint();
                    });
                    timer.start();
                }
            }
        });

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Get in Touch!");
        titleLabel.setFont(FontUtils.Arial(28f));
        titleLabel.setForeground(Color.decode("#b88917"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Contact details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.setBorder(new EmptyBorder(10, 30, 20, 30));
        detailsPanel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));

        // Phone numbers
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(FontUtils.Arial(24f));
        phoneLabel.setForeground(Color.decode("#060644"));
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel number1Label = new JLabel(contactNumber1);
        number1Label.setFont(FontUtils.Arial(24f));
        number1Label.setForeground(Color.BLACK);
        number1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel number2Label = new JLabel(contactNumber2);
        number2Label.setForeground(Color.BLACK);
        number2Label.setFont(FontUtils.Arial(24f));
        number2Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email
        JLabel emailStaticLabel = new JLabel("Email:");
        emailStaticLabel.setFont(FontUtils.Arial(24f));
        emailStaticLabel.setForeground(Color.decode("#060644"));
        emailStaticLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailStaticLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel emailLabel = new JLabel(contactEmail);
        emailLabel.setFont(FontUtils.Arial(24f));
        emailLabel.setForeground(Color.BLACK);
        emailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String url = "https://mail.google.com/mail/?view=cm&fs=1&to=" + contactEmail;
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:" + contactEmail));
                    } catch (Exception ex2) {
                        JOptionPane.showMessageDialog(ContactDialog.this, 
                            "Unable to open email client.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                emailLabel.setForeground(Color.decode("#b88917"));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                emailLabel.setForeground(Color.BLACK);
            }
        });

        detailsPanel.add(phoneLabel);
        detailsPanel.add(number1Label);
        detailsPanel.add(number2Label);
        detailsPanel.add(emailStaticLabel);
        detailsPanel.add(emailLabel);

        contentPanel.add(titleLabel);
        contentPanel.add(detailsPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        
        // Position the dialog - you can adjust the yOffset value
        int yOffset = 50; // Positive moves down, negative moves up
        setLocationRelativeTo(owner);
        Point currentLocation = getLocation();
        setLocation(currentLocation.x, currentLocation.y + yOffset);
        
        toggleContactVisibility();
    }

    private JButton createCloseButton() {
        JButton closeButton = new JButton("X");
        closeButton.setFont(FontUtils.Arial(60f));
        closeButton.setForeground(Color.decode("#060644"));
        closeButton.setBackground(new Color(255, 255, 255, 0)); // Fully transparent background
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(40, 40));

        // Hover effect - slight background on hover
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(240, 240, 240, 50)); // Very subtle hover effect
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(255, 255, 255, 0)); // Back to transparent
            }
        });

        // Custom painting for transparent background
        closeButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();

                // Only fill background if it has some opacity (for hover effect)
                Color bg = btn.getBackground();
                if (bg.getAlpha() > 0) {
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, width, height, 20, 20);
                }

                // No border drawing - completely transparent outline

                // Draw the X text
                g2.setColor(btn.getForeground());
                g2.setFont(btn.getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (width - fm.stringWidth("×")) / 2;
                int textY = ((height - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString("×", textX, textY);

                g2.dispose();
            }
        });

        return closeButton;
    }
    private void toggleContactVisibility() {
        contentPanel.setVisible(showingSecondImage);
    }
}