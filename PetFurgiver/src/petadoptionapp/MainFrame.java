package petadoptionapp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import petadoptionapp.FontUtils;

public class MainFrame extends JFrame {
   private JPanel contentPanel;
   private CardLayout cardLayout;
   private JLabel infoLabel;
   private Timer typingTimer;
   private Timer animationTimer;
   private Timer circleAnimationTimer;
   private Map<String, MenuButton> menuButtons;
   private String currentPage = "Home";
   private boolean isTypingEffectRunning = false;
   private boolean isPopEffectRunning = false;
   private Runnable currentTypingCallback = null;
   private boolean showingCircleAnimation = true;
   private boolean showFirstBackground = false;
   private boolean showSecondBackground = false;
   private int circleRadius = 0;
   private final int maxRadius = 2000; // Large enough to cover the entire screen
   private JPanel backgroundPanel;
  
   // Track if initial animation has been shown
   private boolean initialAnimationShown = false;
   
   // NEW: Track different states of homepage effects
   private boolean homepageEffectsCompleted = false;
   private boolean typingEffectsStarted = false;
   private boolean firstLineCompleted = false;
   private boolean secondLineCompleted = false;
   private boolean infoEffectCompleted = false;
  
   // UI Components that need to be hidden/shown
   private JPanel topWrapper;
   private JPanel bottomWrapper;
  
   // Homepage labels that need to be accessed from multiple methods
   private JLabel line1;
   private JLabel line2;
   
   // FIXED: Make page instances class fields so they can be accessed from navigation handlers
   private About aboutPage;
   private FindPets findPetPage;
   
   public Font loadCustomFont(String fontFileName, float size, int style) {
       try {
           java.io.InputStream is = getClass().getResourceAsStream("/" + fontFileName);
           if (is == null) {
               System.err.println("Font file not found: " + fontFileName);
               return new Font("SansSerif", style, (int) size);
           }
           Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
           return baseFont.deriveFont(style, size);
       } catch (Exception e) {
           e.printStackTrace();
           return new Font("SansSerif", style, (int) size);
       }
   }
   
   public MainFrame() {
       setTitle("FurGivers");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setExtendedState(JFrame.MAXIMIZED_BOTH);
       setResizable(true);
       setLocationRelativeTo(null);
       menuButtons = new HashMap<>();
       
       // Create the main background panel with custom painting for the circle animation
       backgroundPanel = new JPanel() {
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2d = (Graphics2D) g;
               g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              
               if (showingCircleAnimation && !initialAnimationShown) {
                   // Fill entire background with the solid color #060644
                   g2d.setColor(Color.decode("#060644"));
                   g2d.fillRect(0, 0, getWidth(), getHeight());
                  
                   if (circleRadius > 0) {
                       // Create a clipping circle
                       int centerX = getWidth() / 2;
                       int centerY = getHeight() / 2;
                      
                       // Save the current clip
                       Shape oldClip = g2d.getClip();
                      
                       // Create circular clipping area
                       g2d.setClip(new java.awt.geom.Ellipse2D.Double(
                           centerX - circleRadius,
                           centerY - circleRadius,
                           circleRadius * 2,
                           circleRadius * 2
                       ));
                      
                       if (!showFirstBackground) {
                           // Show white inside the circle initially (no background image)
                           g2d.setColor(Color.WHITE);
                           g2d.fillRect(0, 0, getWidth(), getHeight());
                       } else {
                           // Draw the appropriate background image within the circle
                           String backgroundImage = showSecondBackground ? "/Homepage2.png" : "/Homepage1.png";
                           Image bgImage = new ImageIcon(getClass().getResource(backgroundImage)).getImage();
                           g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                       }
                      
                       // Restore the clip
                       g2d.setClip(oldClip);
                   }
               } else {
                   // Normal homepage background when animation is complete or when returning to homepage
                   Image bgImage = new ImageIcon(getClass().getResource("/Homepage2.png")).getImage();
                   g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
               }
           }
       };
       backgroundPanel.setLayout(new BorderLayout());
       
       topWrapper = new JPanel(new BorderLayout());
       topWrapper.setOpaque(false);
       topWrapper.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));
       topWrapper.setVisible(false); // Initially hidden
       
       ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Logo.png"));
       Image scaledLogo = logoIcon.getImage().getScaledInstance(300, 80, Image.SCALE_SMOOTH);
       JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
       topWrapper.add(logoLabel, BorderLayout.WEST);
       
       JPanel navBox = new JPanel() {
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D) g;
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(getBackground());
               g2.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);
           }
           @Override
           protected void paintBorder(Graphics g) {
               Graphics2D g2 = (Graphics2D) g;
               g2.setColor(new Color(6, 6, 68));
               g2.setStroke(new BasicStroke(2));
               g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 60, 60);
           }
       };
      
       navBox.setOpaque(false);
       navBox.setBackground(Color.decode("#e6e6e4"));
       navBox.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
       Font navFont = FontUtils.Arial(30f);
       String[] menuItems = { "Home", "About", "Find a Pet", "Donate", "Contact" };
       
       line1 = new JLabel(" ");
       line1.setFont(FontUtils.Arial(80f));
       line1.setForeground(Color.BLACK);
       line1.setAlignmentX(Component.CENTER_ALIGNMENT);
       
       line2 = new JLabel(" ");
       line2.setFont(FontUtils.Arial(80f));
       line2.setForeground(Color.BLACK);
       line2.setAlignmentX(Component.CENTER_ALIGNMENT);
       
       for (String item : menuItems) {
    	    MenuButton btn = new MenuButton(item, navFont);
    	    menuButtons.put(item, btn);
    	    btn.addActionListener(e -> {
    	        // Special handling for Contact button - show dialog without stopping animations
    	        if (item.equals("Contact")) {
    	            showContactDialog();
    	            // Don't change currentPage for Contact since it's just a dialog
    	            return;
    	        }

    	        // Stop all animations immediately when leaving homepage (for other buttons)
    	        stopAllAnimations();

    	        currentPage = item;
    	        updateActiveButton();
    	        cardLayout.show(contentPanel, item);

    	        if (item.equals("Home")) {
    	            // Show homepage content without effects if already completed
    	            showHomepageContent();
    	        } else if (item.equals("About")) {
    	            // FIXED: Start typing effect when navigating to About page
    	            aboutPage.startTypingEffect();
    	            // Hide the infoLabel for About page
    	            infoLabel.setVisible(false);
    	        } else if (item.equals("Donate")) {
    	            // Handle donate button separately
    	            handleDonateButton();
    	        } else {
    	            // Hide the infoLabel for other pages
    	            infoLabel.setVisible(false);
    	        }
    	    });
    	    navBox.add(btn);
    	}
       
       // Set initial active button
       updateActiveButton();
       
       JPanel navRightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
       navRightWrapper.setOpaque(false);
       navRightWrapper.add(navBox);
       topWrapper.add(navRightWrapper, BorderLayout.CENTER);
       backgroundPanel.add(topWrapper, BorderLayout.NORTH);
       
       cardLayout = new CardLayout();
       contentPanel = new JPanel(cardLayout);
       contentPanel.setOpaque(false);
       contentPanel.setVisible(false); // Initially hidden
       
       JPanel homePanel = new JPanel();
       homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
       homePanel.setOpaque(false);
       homePanel.add(Box.createVerticalStrut(58));
       
       JPanel line1Wrapper = new JPanel();
       line1Wrapper.setOpaque(false);
       line1Wrapper.setLayout(new BoxLayout(line1Wrapper, BoxLayout.X_AXIS));
       line1Wrapper.setBorder(BorderFactory.createEmptyBorder(20, 285, 0, 0));
       line1Wrapper.add(line1);
       homePanel.add(line1Wrapper);
       
       homePanel.add(Box.createVerticalStrut(5));
       
       JPanel line2Wrapper = new JPanel();
       line2Wrapper.setOpaque(false);
       line2Wrapper.setLayout(new BoxLayout(line2Wrapper, BoxLayout.X_AXIS));
       line2Wrapper.setBorder(BorderFactory.createEmptyBorder(0, 280, 0, 0));
       line2Wrapper.add(line2);
       homePanel.add(line2Wrapper);
       
       // FIXED: Create instances as class fields instead of local variables
       aboutPage = new About(this);
       findPetPage = new FindPets(this);
      
       // Add the pages to the content panel with their corresponding names
       contentPanel.add(homePanel, "Home");
       contentPanel.add(aboutPage.createPage(), "About");
       contentPanel.add(findPetPage.createPage(), "Find a Pet");
      
       backgroundPanel.add(contentPanel, BorderLayout.CENTER);
       
       bottomWrapper = new JPanel(new BorderLayout());
       bottomWrapper.setOpaque(false);
       bottomWrapper.setVisible(false); // Initially hidden

       infoLabel = new JLabel("<html><div style='text-align: center;'>You may check our adoptable<br>furbabies at the Find a Pet section<br>if you wish to adopt one!</div></html>");
       infoLabel.setVisible(false);
       infoLabel.setFont(FontUtils.Arial(32f));
       infoLabel.setForeground(Color.BLACK);
       infoLabel.setHorizontalAlignment(SwingConstants.CENTER); // This centers the label itself
       infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 50, 90));
       bottomWrapper.add(infoLabel, BorderLayout.EAST);
       
       JPanel footerPanel = new JPanel(new BorderLayout());
       footerPanel.setBackground(Color.decode("#060644"));
       JLabel footerLabel = new JLabel("2025 FurGivers © All Rights Reserved");
       footerLabel.setFont(FontUtils.Arial(20f));
       footerLabel.setForeground(Color.WHITE);
       footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
       // Add the label to the footer panel
       footerPanel.add(footerLabel, BorderLayout.CENTER);
       // Add the footer panel to the bottom wrapper
       bottomWrapper.add(footerPanel, BorderLayout.SOUTH);
       
       backgroundPanel.add(bottomWrapper, BorderLayout.SOUTH);
       setContentPane(backgroundPanel);
       setVisible(true);
       
       // Start the circle opening animation after a brief delay (ONLY on startup)
       Timer startDelay = new Timer(500, e -> startCircleAnimation());
       startDelay.setRepeats(false);
       startDelay.start();
   }
   
   // NEW: Method to stop all running animations
   private void stopAllAnimations() {
       if (typingTimer != null && typingTimer.isRunning()) {
           typingTimer.stop();
       }
       if (animationTimer != null && animationTimer.isRunning()) {
           animationTimer.stop();
       }
       // Reset font size to normal in case pop effect was running
       if (infoLabel != null) {
           infoLabel.setFont(FontUtils.Arial(32f));
       }
   }
   
   private void handleDonateButton() {
       final String DONATE_URL = "https://docs.google.com/forms/d/e/1FAIpQLSda_jQUn0XWPIzr3Eli5bVkoWOW10H_VVsiQDWC-dWoHyiPMQ/viewform?usp=sharing&ouid=108321204493867680753";
       try {
           if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
               Desktop.getDesktop().browse(new URI(DONATE_URL));
              
               // After opening the donation page, return to Home and show content without effects
               currentPage = "Home";
               updateActiveButton();
               cardLayout.show(contentPanel, "Home");
               showHomepageContent();
           } else {
               JOptionPane.showMessageDialog(this, "Cannot open browser. Please visit:\n" + DONATE_URL, "Browser Not Supported", JOptionPane.INFORMATION_MESSAGE);
           }
       } catch (Exception ex) {
           ex.printStackTrace();
           JOptionPane.showMessageDialog(this, "Error opening link: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
   }
   
   // MODIFIED: Method to show homepage content - show completed state if effects started
   private void showHomepageContent() {
       // Make sure all UI elements are visible
       if (!topWrapper.isVisible()) {
           topWrapper.setVisible(true);
       }
       if (!contentPanel.isVisible()) {
           contentPanel.setVisible(true);
       }
       if (!bottomWrapper.isVisible()) {
           bottomWrapper.setVisible(true);
       }
      
       // NEW: If effects have started or completed, show final state immediately
       if (homepageEffectsCompleted || typingEffectsStarted) {
           // Show final content immediately without any effects
           line1.setText("Hello and");
           line2.setText("Welcome!");
           infoLabel.setVisible(true);
           infoLabel.setFont(FontUtils.Arial(32f)); // Ensure normal font size
       } else {
           // Initial state - no effects started yet
           line1.setText(" ");
           line2.setText(" ");
           infoLabel.setVisible(false);
       }
      
       // Ensure the background is repainted
       backgroundPanel.repaint();
   }
   
   // NEW: Method to resume homepage effects where they left off
   private void resumeHomepageEffects() {
       // Make sure all UI elements are visible
       if (!topWrapper.isVisible()) {
           topWrapper.setVisible(true);
       }
       if (!contentPanel.isVisible()) {
           contentPanel.setVisible(true);
       }
       if (!bottomWrapper.isVisible()) {
           bottomWrapper.setVisible(true);
       }
       
       // Resume effects based on current state
       if (!typingEffectsStarted) {
           // Haven't started typing effects yet - start from beginning
           typingEffectsStarted = true;
           startTypingEffect(line1, "Hello and", () -> {
               firstLineCompleted = true;
               startTypingEffect(line2, "Welcome!", () -> {
                   secondLineCompleted = true;
                   Timer delayTimer = new Timer(1500, evt -> {
                       if (currentPage.equals("Home")) {
                           infoLabel.setVisible(true);
                           animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                               infoEffectCompleted = true;
                               homepageEffectsCompleted = true;
                           });
                       }
                   });
                   delayTimer.setRepeats(false);
                   delayTimer.start();
               });
           });
       } else if (!firstLineCompleted) {
           // First line was interrupted - continue from where it left off
           String currentText = line1.getText().trim();
           String targetText = "Hello and";
           if (currentText.length() < targetText.length()) {
               // Continue typing first line
               startTypingEffect(line1, targetText, () -> {
                   firstLineCompleted = true;
                   startTypingEffect(line2, "Welcome!", () -> {
                       secondLineCompleted = true;
                       Timer delayTimer = new Timer(1500, evt -> {
                           if (currentPage.equals("Home")) {
                               infoLabel.setVisible(true);
                               animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                                   infoEffectCompleted = true;
                                   homepageEffectsCompleted = true;
                               });
                           }
                       });
                       delayTimer.setRepeats(false);
                       delayTimer.start();
                   });
               });
           } else {
               firstLineCompleted = true;
               // Continue with second line if needed
               if (!secondLineCompleted) {
                   startTypingEffect(line2, "Welcome!", () -> {
                       secondLineCompleted = true;
                       Timer delayTimer = new Timer(1500, evt -> {
                           if (currentPage.equals("Home")) {
                               infoLabel.setVisible(true);
                               animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                                   infoEffectCompleted = true;
                                   homepageEffectsCompleted = true;
                               });
                           }
                       });
                       delayTimer.setRepeats(false);
                       delayTimer.start();
                   });
               }
           }
       } else if (!secondLineCompleted) {
           // Second line was interrupted - continue from where it left off
           String currentText = line2.getText().trim();
           String targetText = "Welcome!";
           if (currentText.length() < targetText.length()) {
               // Continue typing second line
               startTypingEffect(line2, targetText, () -> {
                   secondLineCompleted = true;
                   Timer delayTimer = new Timer(1500, evt -> {
                       if (currentPage.equals("Home")) {
                           infoLabel.setVisible(true);
                           animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                               infoEffectCompleted = true;
                               homepageEffectsCompleted = true;
                           });
                       }
                   });
                   delayTimer.setRepeats(false);
                   delayTimer.start();
               });
           } else {
               secondLineCompleted = true;
               // Continue with info effect if needed
               if (!infoEffectCompleted) {
                   Timer delayTimer = new Timer(1500, evt -> {
                       if (currentPage.equals("Home")) {
                           infoLabel.setVisible(true);
                           animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                               infoEffectCompleted = true;
                               homepageEffectsCompleted = true;
                           });
                       }
                   });
                   delayTimer.setRepeats(false);
                   delayTimer.start();
               }
           }
       } else if (!infoEffectCompleted) {
           // Info effect was interrupted - show and animate it
           if (!infoLabel.isVisible()) {
               infoLabel.setVisible(true);
               animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                   infoEffectCompleted = true;
                   homepageEffectsCompleted = true;
               });
           } else {
               // Info label is visible but effect might have been interrupted
               // Restart the pop effect
               animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                   infoEffectCompleted = true;
                   homepageEffectsCompleted = true;
               });
           }
       } else {
           // All effects completed - show final state
           line1.setText("Hello and");
           line2.setText("Welcome!");
           infoLabel.setVisible(true);
           infoLabel.setFont(FontUtils.Arial(32f));
           homepageEffectsCompleted = true;
       }
       
       // Ensure the background is repainted
       backgroundPanel.repaint();
   }
   
   private void startCircleAnimation() {
       circleAnimationTimer = new Timer(16, null); // ~60 FPS
       final int animationDuration = 55000;
       final long startTime = System.currentTimeMillis();
      
       circleAnimationTimer.addActionListener(e -> {
           long elapsed = System.currentTimeMillis() - startTime;
           float progress = Math.min(1.0f, (float) elapsed / animationDuration);
          
           // Use easeOutQuart for smooth deceleration
           float easedProgress = 1 - (float) Math.pow(1 - progress, 4);
          
           circleRadius = (int) (maxRadius * easedProgress);
          
           // Show UI elements when circle starts expanding (at 12% progress)
           if (progress >= 0.12f && !topWrapper.isVisible()) {
               topWrapper.setVisible(true);
               contentPanel.setVisible(true);
               bottomWrapper.setVisible(true);
               backgroundPanel.repaint();
           }
          
           // Show first background
           if (progress >= 0.14f && !showFirstBackground && elapsed >= 2000) {
               showFirstBackground = true;
               backgroundPanel.repaint();
           }
          
           // Switch to second background
           if (progress >= 0.16f && !showSecondBackground && elapsed >= 2000) {
               showSecondBackground = true;
               backgroundPanel.repaint();
              
               // Add 2 more seconds delay before starting typing effects
               Timer typingDelayTimer = new Timer(2000, evt -> {
                   // Only start effects if we're still on homepage and haven't started yet
                   if (currentPage.equals("Home") && !typingEffectsStarted) {
                       typingEffectsStarted = true;
                       startTypingEffect(line1, "Hello and", () -> {
                           firstLineCompleted = true;
                           startTypingEffect(line2, "Welcome!", () -> {
                               secondLineCompleted = true;
                               Timer delayTimer = new Timer(1500, evt2 -> {
                                   // Only show pop effect if we're still on homepage
                                   if (currentPage.equals("Home")) {
                                       infoLabel.setVisible(true);
                                       animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                                           infoEffectCompleted = true;
                                       });
                                   }
                               });
                               delayTimer.setRepeats(false);
                               delayTimer.start();
                           });
                       });
                   }
               });
               typingDelayTimer.setRepeats(false);
               typingDelayTimer.start();
           }
          
           backgroundPanel.repaint();
          
           if (progress >= 1.0f) {
               circleAnimationTimer.stop();
               showingCircleAnimation = false;
               initialAnimationShown = true; // Mark that initial animation has been shown
               homepageEffectsCompleted = true; // NEW: Mark that homepage effects are completed
               backgroundPanel.repaint();
           }
       });
      
       circleAnimationTimer.start();
   }
   
   private void showContactDialog() {
       ContactDialog dialog = new ContactDialog(
           this,
           "(123) 456-7890",
           "(098) 765-4321",
           "furgiverph@gmail.com"
       );
       dialog.addWindowListener(new WindowAdapter() {
           @Override
           public void windowOpened(WindowEvent e) {
               // When dialog opens, resume typing effects if we're on homepage
               if (currentPage.equals("Home")) {
                   resumeHomepageEffects();
               }
           } 
       });
       dialog.setVisible(true);
   }
   
   private void updateActiveButton() {
       for (Map.Entry<String, MenuButton> entry : menuButtons.entrySet()) {
           String pageName = entry.getKey();
           MenuButton button = entry.getValue();
           // Don't set Contact as active since it's a dialog, not a page
           button.setActive(pageName.equals(currentPage) && !pageName.equals("Contact"));
       }
   }
   
   // MODIFIED: Track completion and check if we're still on homepage before continuing effect
   private void startTypingEffect(JLabel label, String text, Runnable onComplete) {
       if (typingTimer != null) {
           typingTimer.stop();
       }
       typingTimer = new Timer(70, null);
       Random rand = new Random();
       final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
       typingTimer.addActionListener(new ActionListener() {
           int index = 0;
           int shuffleCount = 0;
           int shuffleLimit = 3;
           @Override
           public void actionPerformed(ActionEvent e) {
               // NEW: Stop effect if no longer on homepage
               if (!currentPage.equals("Home")) {
                   typingTimer.stop();
                   return;
               }
               
               if (index >= text.length()) {
                   typingTimer.stop();
                   if (onComplete != null) onComplete.run();
                   return;
               }
               if (shuffleCount < shuffleLimit) {
                   label.setText(label.getText().substring(0, index) + chars.charAt(rand.nextInt(chars.length())));
                   shuffleCount++;
               } else {
                   label.setText(label.getText().substring(0, index) + text.charAt(index));
                   index++;
                   shuffleCount = 0;
               }
           }
       });
       typingTimer.start();
   }
   
   // MODIFIED: Add completion callback and check if we're still on homepage before continuing effect
   private void animatePopEffect(JLabel label, float baseSize, float peakSize, int steps, int delay, Runnable onComplete) {
       if (animationTimer != null) {
           animationTimer.stop();
       }
       animationTimer = new Timer(delay, null);
       final int halfSteps = steps / 2;
       final float stepSize = (peakSize - baseSize) / halfSteps;
       final Font baseFont = label.getFont();
       final int[] currentStep = {0};
       animationTimer.addActionListener(e -> {
           // NEW: Stop effect if no longer on homepage
           if (!currentPage.equals("Home")) {
               animationTimer.stop();
               label.setFont(baseFont.deriveFont(baseSize)); // Reset to normal size
               return;
           }
           
           float newSize;
           if (currentStep[0] < halfSteps) {
               newSize = baseSize + (stepSize * currentStep[0]);
           } else {
               newSize = peakSize - (stepSize * (currentStep[0] - halfSteps));
           }
           label.setFont(baseFont.deriveFont(newSize));
           currentStep[0]++;
           if (currentStep[0] >= steps) {
               label.setFont(baseFont.deriveFont(baseSize));
               animationTimer.stop();
               if (onComplete != null) onComplete.run();
           }
       });
       animationTimer.start();
   }
   
   // Overloaded method for backward compatibility
   private void animatePopEffect(JLabel label, float baseSize, float peakSize, int steps, int delay) {
       animatePopEffect(label, baseSize, peakSize, steps, delay, null);
   }
   
   public static void main(String[] args) {
       SwingUtilities.invokeLater(MainFrame::new);
   }
}

class MenuButton extends JButton {
    private boolean hovered = false;
    private boolean active = false;
    private String labelText;
    private Font customFont;
    private Color normalColor = Color.BLACK;
    private Color hoverColor = Color.decode("#b88917");
    private Color activeColor = Color.decode("#060644"); // Active text is golden
    private Color activeBackgroundColor = Color.WHITE;
    
    // Animation properties
    private int animationProgress = 0;
    private int animationDirection = 1; // 1 for expanding, -1 for contracting
    private Timer animationTimer;
    private final int ANIMATION_DURATION = 15; // milliseconds between frames
    private final int ANIMATION_STEPS = 10; // total animation steps
    
    // Previous and current button states for animation
    private Rectangle previousRect = null;
    private Rectangle currentRect = null;
    
    public MenuButton(String text, Font font) {
        super(text);
        this.labelText = text;
        this.customFont = font;
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFont(font);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setForeground(normalColor);
        setOpaque(false);
        
        // Initialize animation timer
        animationTimer = new Timer(ANIMATION_DURATION, e -> {
            animationProgress += animationDirection;
            if (animationProgress >= ANIMATION_STEPS || animationProgress <= 0) {
                animationTimer.stop();
                if (animationProgress >= ANIMATION_STEPS) {
                    animationProgress = ANIMATION_STEPS;
                } else {
                    animationProgress = 0;
                }
            }
            repaint();
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                if (!active) {
                    setForeground(hoverColor);
                }
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                if (!active) {
                    setForeground(normalColor);
                }
                repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (!active) {
                    setForeground(hoverColor);
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!active && !hovered) {
                    setForeground(normalColor);
                }
            }
        });
    }
    
    public void setActive(boolean active) {
        if (this.active != active) {
            // Only start animation if state is changing
            this.active = active;
            
            if (active) {
                // Store current position for animation
                currentRect = calculateActiveRect();
                animationDirection = 1;
            } else {
                animationDirection = -1;
            }
            
            // Start animation
            animationTimer.start();
            
            // Update colors immediately
            if (active) {
                setForeground(activeColor);
            } else {
                setForeground(hovered ? hoverColor : normalColor);
            }
        }
    }
    
    private Rectangle calculateActiveRect() {
        int padding = 0;
        int rectWidth = getWidth() - (padding * 2);
        int rectHeight = getHeight() - (padding * 2);
        int rectX = padding;
        int rectY = padding;
        return new Rectangle(rectX, rectY, rectWidth, rectHeight);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the white rounded rectangle background for active button
        if (active || animationProgress > 0) {
            // Calculate rectangle dimensions with some padding
            int padding = 0;
            int rectWidth = getWidth() - (padding * 2);
            int rectHeight = getHeight() - (padding * 2);
            int rectX = padding;
            int rectY = padding;
            int cornerRadius = 45; // Rounded corners
            
            // Calculate animated width if in progress
            if (animationProgress > 0 && animationProgress < ANIMATION_STEPS) {
                float progress = (float)animationProgress / ANIMATION_STEPS;
                rectWidth = (int)(rectWidth * progress);
                
                // Center the rectangle during animation
                rectX = padding + (getWidth() - padding * 2 - rectWidth) / 2;
            }
            
            // Draw the white background
            g2.setColor(activeBackgroundColor);
            g2.fillRoundRect(rectX, rectY, rectWidth, rectHeight, cornerRadius, cornerRadius);
            
            // Optional: Add a subtle border to make it more defined
            g2.setColor(new Color(220, 220, 220)); // Light gray border
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(rectX, rectY, rectWidth, rectHeight, cornerRadius, cornerRadius);
        }
        
        // Draw the text
        super.paintComponent(g);
    }
}