package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.awt.Desktop;
import javax.swing.plaf.basic.BasicButtonUI;

// Inheritance - extends JDialog to reuse dialog functionality
public class PetDetails extends JDialog {
    // Encapsulation - private field to hide internal data
    private Image backgroundImage;

    // Encapsulation - public constructor controls object creation
    public PetDetails(JFrame parent, Pet pet) {
        super(parent, pet.getName(), true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setSize(1100, 650);
        setLocationRelativeTo(parent);

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/PetDetails.png")).getImage();
        } catch (Exception e) {
            System.out.println("Background image not found, using default background");
            backgroundImage = null;
        }

        // Polymorphism - anonymous class overrides paintComponent method
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 25;
                int width = getWidth();
                int height = getHeight();

                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, width, height, this);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                }

                g2.setColor(Color.decode("#060644"));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closePanel.setOpaque(false);

        JButton closeButton = createCloseButton();
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(450, 450));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);

        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(pet.getImagePath()));
            Image scaledImage = originalIcon.getImage().getScaledInstance(330, 330, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagePanel.add(imageLabel);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Image not available", SwingConstants.CENTER);
            errorLabel.setFont(FontUtils.Arial(24f));
            imagePanel.add(errorLabel);
        }

        leftPanel.add(imagePanel);

        // Polymorphism - overriding paintComponent and getPreferredSize methods
        JPanel greetingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.decode("#060644"));

                int panelWidth = getWidth();

                Font italicFont = FontUtils.Arial(48f).deriveFont(Font.ITALIC);
                g2.setFont(italicFont);
                FontMetrics italicFm = g2.getFontMetrics();
                String helloText = "Hello! I'm";
                int helloWidth = italicFm.stringWidth(helloText);
                int helloX = (panelWidth - helloWidth) / 2;
                int helloY = italicFm.getAscent() + 5;
                g2.drawString(helloText, helloX, helloY);

                Font boldFont = FontUtils.Arial(48f).deriveFont(Font.BOLD);
                g2.setFont(boldFont);
                FontMetrics boldFm = g2.getFontMetrics();
                String nameText = pet.getName();
                int nameWidth = boldFm.stringWidth(nameText);
                int nameX = (panelWidth - nameWidth) / 2;
                int nameY = helloY + boldFm.getHeight() + 5;
                g2.drawString(nameText, nameX, nameY);

                int underlineY = nameY + 3;
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(nameX, underlineY, nameX + nameWidth, underlineY);
            }

            @Override
            public Dimension getPreferredSize() {
                FontMetrics fm = getFontMetrics(FontUtils.Arial(48f));
                int height = (fm.getHeight() * 2) + 50;
                return new Dimension(350, height);
            }
        };
        greetingPanel.setOpaque(false);
        greetingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(greetingPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(700, 500));

        Font customFont = FontUtils.Arial(23f);
        
        String consolidatedDescription = pet.getDescription();

        // Polymorphism - anonymous class overrides paintComponent method
        JPanel textBoxPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 255, 255, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2.setColor(new Color(255, 255, 255, 0));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        textBoxPanel.setOpaque(false);
        textBoxPanel.setPreferredSize(new Dimension(680, 400));

        // Polymorphism - anonymous class overrides paintComponent method
        JTextPane descriptionPane = new JTextPane() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        descriptionPane.setContentType("text/html");
        descriptionPane.setFont(customFont); 
        descriptionPane.setText(consolidatedDescription);
        descriptionPane.setEditable(false);
        descriptionPane.setOpaque(false);

        textBoxPanel.add(descriptionPane, BorderLayout.CENTER);

        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(textBoxPanel);

        JPanel buttonContainer = createButtonContainer(pet);
        rightPanel.add(buttonContainer);

        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.add(closePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Abstraction - hides complex button creation details
    private JPanel createButtonContainer(Pet pet) {
        JPanel mainContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 38));
        mainContainer.setOpaque(false);
        mainContainer.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel sponsorContainer = createActionButton("Sponsor Me!",
            "https://docs.google.com/forms/d/e/1FAIpQLSda_jQUn0XWPIzr3Eli5bVkoWOW10H_VVsiQDWC-dWoHyiPMQ/viewform?usp=sharing&ouid=108321204493867680753",
            Color.decode("#e8e8e8"), Color.decode("#060644"));

        JPanel adoptContainer = createActionButton("Adopt Me!",
            "https://docs.google.com/forms/d/e/1FAIpQLSfid0_HZ6eX5P7FRYnQ2NTmOALPW6lwjK5EKyP-n515s8tbMQ/viewform?usp=sharing&ouid=108321204493867680753",
            Color.decode("#060644"), Color.WHITE);

        mainContainer.add(sponsorContainer);
        mainContainer.add(adoptContainer);

        return mainContainer;
    }

    // Abstraction - simplifies button creation by hiding complex implementation
    private JPanel createActionButton(String text, String url, Color bgColor, Color textColor) {
        // Polymorphism - anonymous class overrides multiple methods
        JPanel container = new JPanel(new BorderLayout()) {
            private boolean isHovered = false;
            private boolean isPressed = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color fillColor;
                if (isPressed || isHovered) {
                    fillColor = Color.decode("#231f1d");
                } else {
                    fillColor = bgColor;
                }

                g2.setColor(fillColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(6, 6, 68));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(190, 50);
            }

            // Encapsulation - controlled access to internal state
            public void setHovered(boolean hovered) {
                this.isHovered = hovered;
                repaint();
            }

            public void setPressed(boolean pressed) {
                this.isPressed = pressed;
                repaint();
            }
        };

        container.setOpaque(false);
        container.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(FontUtils.Arial(30f));
        textLabel.setForeground(textColor);

        container.add(textLabel, BorderLayout.CENTER);

        // Polymorphism - overriding MouseAdapter methods
        container.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                try {
                    java.lang.reflect.Method setHovered = container.getClass().getMethod("setHovered", boolean.class);
                    setHovered.invoke(container, true);
                    textLabel.setForeground(Color.WHITE);
                } catch (Exception e) {
                    container.repaint();
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                try {
                    java.lang.reflect.Method setHovered = container.getClass().getMethod("setHovered", boolean.class);
                    java.lang.reflect.Method setPressed = container.getClass().getMethod("setPressed", boolean.class);
                    setHovered.invoke(container, false);
                    setPressed.invoke(container, false);
                    textLabel.setForeground(textColor);
                } catch (Exception e) {
                    container.repaint();
                }
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                try {
                    java.lang.reflect.Method setPressed = container.getClass().getMethod("setPressed", boolean.class);
                    setPressed.invoke(container, true);
                    textLabel.setForeground(Color.WHITE);
                } catch (Exception e) {
                    container.repaint();
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                try {
                    java.lang.reflect.Method setPressed = container.getClass().getMethod("setPressed", boolean.class);
                    setPressed.invoke(container, false);
                } catch (Exception e) {
                    container.repaint();
                }
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openURL(url);
            }
        });

        return container;
    }

    // Abstraction - hides complex close button creation
    private JButton createCloseButton() {
        JButton closeButton = new JButton("X");
        closeButton.setFont(FontUtils.Arial(60f));
        closeButton.setForeground(Color.decode("#060644"));
        closeButton.setBackground(new Color(255, 255, 255, 200));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(40, 40));

        // Polymorphism - overriding MouseAdapter methods
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(240, 240, 240, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(255, 255, 255, 200));
            }
        });

        // Inheritance - extending BasicButtonUI to customize appearance
        closeButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                int width = btn.getWidth();
                int height = btn.getHeight();
                int arc = 20;

                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                g2.setColor(Color.decode("#060644"));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

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

    // Abstraction - hides URL opening complexity from caller
    private void openURL(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                JOptionPane.showMessageDialog(this, "Cannot open browser. Please visit:\n" + url,
                    "Browser Not Supported", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening link: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}