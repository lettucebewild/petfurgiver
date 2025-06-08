package petadoptionapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Inheritance - MainFrame extends JFrame to inherit GUI functionality
public class MainFrame extends JFrame {
   // Encapsulation - private fields to hide internal state
   private JPanel contentPanel;
   private CardLayout cardLayout;
   private JLabel infoLabel;
   private Timer typingTimer;
   private Timer animationTimer;
   private Timer circleAnimationTimer;
   private Map<String, MenuButton> menuButtons;
   private String currentPage = "Home";
   private boolean showingCircleAnimation = true;
   private boolean showFirstBackground = false;
   private boolean showSecondBackground = false;
   private int circleRadius = 0;
   private final int maxRadius = 2000;
   private JPanel backgroundPanel;
   private boolean initialAnimationShown = false;
   private boolean homepageEffectsCompleted = false;
   private boolean typingEffectsStarted = false;
   private boolean firstLineCompleted = false;
   private boolean secondLineCompleted = false;
   private boolean infoEffectCompleted = false;
   private JPanel topWrapper;
   private JPanel bottomWrapper;
   private JLabel line1;
   private JLabel line2;
   private About aboutPage;
   private FindPets findPetPage;
   
   // Abstraction - hides complex font loading logic behind simple method
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
       
       // Polymorphism - overriding paintComponent for custom drawing behavior
       backgroundPanel = new JPanel() {
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2d = (Graphics2D) g;
               g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
              
               if (showingCircleAnimation && !initialAnimationShown) {
                   g2d.setColor(Color.decode("#060644"));
                   g2d.fillRect(0, 0, getWidth(), getHeight());
                  
                   if (circleRadius > 0) {
                       int centerX = getWidth() / 2;
                       int centerY = getHeight() / 2;
                       Shape oldClip = g2d.getClip();
                      
                       g2d.setClip(new java.awt.geom.Ellipse2D.Double(
                           centerX - circleRadius,
                           centerY - circleRadius,
                           circleRadius * 2,
                           circleRadius * 2
                       ));
                      
                       if (!showFirstBackground) {
                           g2d.setColor(Color.WHITE);
                           g2d.fillRect(0, 0, getWidth(), getHeight());
                       } else {
                           String backgroundImage = showSecondBackground ? "/Homepage2.png" : "/Homepage1.png";
                           Image bgImage = new ImageIcon(getClass().getResource(backgroundImage)).getImage();
                           g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                       }
                       g2d.setClip(oldClip);
                   }
               } else {
                   Image bgImage = new ImageIcon(getClass().getResource("/Homepage2.png")).getImage();
                   g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
               }
           }
       };
       backgroundPanel.setLayout(new BorderLayout());
       
       topWrapper = new JPanel(new BorderLayout());
       topWrapper.setOpaque(false);
       topWrapper.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));
       topWrapper.setVisible(false);
       
       ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Logo.png"));
       Image scaledLogo = logoIcon.getImage().getScaledInstance(300, 80, Image.SCALE_SMOOTH);
       JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
       topWrapper.add(logoLabel, BorderLayout.WEST);
       
       // Polymorphism - overriding paint methods for custom button appearance
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
       
       // Abstraction - anonymous method hides complex event handling logic
       for (String item : menuItems) {
    	    MenuButton btn = new MenuButton(item, navFont);
    	    menuButtons.put(item, btn);
    	    btn.addActionListener(e -> {
    	        if (item.equals("Contact")) {
    	            showContactDialog();
    	            return;
    	        }

    	        stopAllAnimations();
    	        currentPage = item;
    	        updateActiveButton();
    	        cardLayout.show(contentPanel, item);

    	        if (item.equals("Home")) {
    	            showHomepageContent();
    	        } else if (item.equals("About")) {
    	            aboutPage.startTypingEffect();
    	            infoLabel.setVisible(false);
    	        } else if (item.equals("Donate")) {
    	            handleDonateButton();
    	        } else {
    	            infoLabel.setVisible(false);
    	        }
    	    });
    	    navBox.add(btn);
    	}
       
       updateActiveButton();
       
       JPanel navRightWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
       navRightWrapper.setOpaque(false);
       navRightWrapper.add(navBox);
       topWrapper.add(navRightWrapper, BorderLayout.CENTER);
       backgroundPanel.add(topWrapper, BorderLayout.NORTH);
       
       cardLayout = new CardLayout();
       contentPanel = new JPanel(cardLayout);
       contentPanel.setOpaque(false);
       contentPanel.setVisible(false);
       
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
       
       aboutPage = new About(this);
       findPetPage = new FindPets(this);
      
       contentPanel.add(homePanel, "Home");
       contentPanel.add(aboutPage.createPage(), "About");
       contentPanel.add(findPetPage.createPage(), "Find a Pet");
      
       backgroundPanel.add(contentPanel, BorderLayout.CENTER);
       
       bottomWrapper = new JPanel(new BorderLayout());
       bottomWrapper.setOpaque(false);
       bottomWrapper.setVisible(false);

       infoLabel = new JLabel("<html><div style='text-align: center;'>You may check our adoptable<br>furbabies at the Find a Pet section<br>if you wish to adopt one!</div></html>");
       infoLabel.setVisible(false);
       infoLabel.setFont(FontUtils.Arial(32f));
       infoLabel.setForeground(Color.BLACK);
       infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
       infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 50, 90));
       bottomWrapper.add(infoLabel, BorderLayout.EAST);
       
       JPanel footerPanel = new JPanel(new BorderLayout());
       footerPanel.setBackground(Color.decode("#060644"));
       JLabel footerLabel = new JLabel("2025 FurGivers © All Rights Reserved");
       footerLabel.setFont(FontUtils.Arial(20f));
       footerLabel.setForeground(Color.WHITE);
       footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
       footerPanel.add(footerLabel, BorderLayout.CENTER);
       bottomWrapper.add(footerPanel, BorderLayout.SOUTH);
       
       backgroundPanel.add(bottomWrapper, BorderLayout.SOUTH);
       setContentPane(backgroundPanel);
       setVisible(true);
       
       Timer startDelay = new Timer(500, e -> startCircleAnimation());
       startDelay.setRepeats(false);
       startDelay.start();
   }
   
   // Encapsulation - private method to manage internal animation state
   private void stopAllAnimations() {
       if (typingTimer != null && typingTimer.isRunning()) {
           typingTimer.stop();
       }
       if (animationTimer != null && animationTimer.isRunning()) {
           animationTimer.stop();
       }
       if (infoLabel != null) {
           infoLabel.setFont(FontUtils.Arial(32f));
       }
   }
   
   // Abstraction - hides complex donation handling behind simple method call
   private void handleDonateButton() {
       final String DONATE_URL = "https://docs.google.com/forms/d/e/1FAIpQLSda_jQUn0XWPIzr3Eli5bVkoWOW10H_VVsiQDWC-dWoHyiPMQ/viewform?usp=sharing&ouid=108321204493867680753";
       try {
           if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
               Desktop.getDesktop().browse(new URI(DONATE_URL));
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
   
   private void showHomepageContent() {
       if (!topWrapper.isVisible()) {
           topWrapper.setVisible(true);
       }
       if (!contentPanel.isVisible()) {
           contentPanel.setVisible(true);
       }
       if (!bottomWrapper.isVisible()) {
           bottomWrapper.setVisible(true);
       }
      
       if (homepageEffectsCompleted || typingEffectsStarted) {
           line1.setText("Hello and");
           line2.setText("Welcome!");
           infoLabel.setVisible(true);
           infoLabel.setFont(FontUtils.Arial(32f));
       } else {
           line1.setText(" ");
           line2.setText(" ");
           infoLabel.setVisible(false);
       }
       backgroundPanel.repaint();
   }
   
   private void resumeHomepageEffects() {
       if (!topWrapper.isVisible()) {
           topWrapper.setVisible(true);
       }
       if (!contentPanel.isVisible()) {
           contentPanel.setVisible(true);
       }
       if (!bottomWrapper.isVisible()) {
           bottomWrapper.setVisible(true);
       }
       
       if (!typingEffectsStarted) {
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
           String currentText = line1.getText().trim();
           String targetText = "Hello and";
           if (currentText.length() < targetText.length()) {
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
           String currentText = line2.getText().trim();
           String targetText = "Welcome!";
           if (currentText.length() < targetText.length()) {
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
           if (!infoLabel.isVisible()) {
               infoLabel.setVisible(true);
               animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                   infoEffectCompleted = true;
                   homepageEffectsCompleted = true;
               });
           } else {
               animatePopEffect(infoLabel, 32f, 46f, 10, 50, () -> {
                   infoEffectCompleted = true;
                   homepageEffectsCompleted = true;
               });
           }
       } else {
           line1.setText("Hello and");
           line2.setText("Welcome!");
           infoLabel.setVisible(true);
           infoLabel.setFont(FontUtils.Arial(32f));
           homepageEffectsCompleted = true;
       }
       backgroundPanel.repaint();
   }
   
   private void startCircleAnimation() {
       circleAnimationTimer = new Timer(16, null);
       final int animationDuration = 55000;
       final long startTime = System.currentTimeMillis();
      
       circleAnimationTimer.addActionListener(e -> {
           long elapsed = System.currentTimeMillis() - startTime;
           float progress = Math.min(1.0f, (float) elapsed / animationDuration);
           float easedProgress = 1 - (float) Math.pow(1 - progress, 4);
           circleRadius = (int) (maxRadius * easedProgress);
          
           if (progress >= 0.12f && !topWrapper.isVisible()) {
               topWrapper.setVisible(true);
               contentPanel.setVisible(true);
               bottomWrapper.setVisible(true);
               backgroundPanel.repaint();
           }
          
           if (progress >= 0.14f && !showFirstBackground && elapsed >= 2000) {
               showFirstBackground = true;
               backgroundPanel.repaint();
           }
          
           if (progress >= 0.16f && !showSecondBackground && elapsed >= 2000) {
               showSecondBackground = true;
               backgroundPanel.repaint();
              
               Timer typingDelayTimer = new Timer(2000, evt -> {
                   if (currentPage.equals("Home") && !typingEffectsStarted) {
                       typingEffectsStarted = true;
                       startTypingEffect(line1, "Hello and", () -> {
                           firstLineCompleted = true;
                           startTypingEffect(line2, "Welcome!", () -> {
                               secondLineCompleted = true;
                               Timer delayTimer = new Timer(1500, evt2 -> {
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
               initialAnimationShown = true;
               homepageEffectsCompleted = true;
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
           button.setActive(pageName.equals(currentPage) && !pageName.equals("Contact"));
       }
   }
   
   // Abstraction - hides complex typing animation logic behind simple method interface
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
   
   // Abstraction - complex animation logic hidden behind simple method call
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
           if (!currentPage.equals("Home")) {
               animationTimer.stop();
               label.setFont(baseFont.deriveFont(baseSize));
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
   
   // Polymorphism - method overloading with different parameter sets
   private void animatePopEffect(JLabel label, float baseSize, float peakSize, int steps, int delay) {
       animatePopEffect(label, baseSize, peakSize, steps, delay, null);
   }
   
   public static void main(String[] args) {
       SwingUtilities.invokeLater(MainFrame::new);
   }
}

// Inheritance - MenuButton extends JButton to inherit button functionality
class MenuButton extends JButton {
    // Encapsulation - private fields to hide button's internal state
    private boolean hovered = false;
    private boolean active = false;
    private String labelText;
    private Font customFont;
    private Color normalColor = Color.BLACK;
    private Color hoverColor = Color.decode("#b88917");
    private Color activeColor = Color.decode("#060644");
    private Color activeBackgroundColor = Color.WHITE;
    private int animationProgress = 0;
    private int animationDirection = 1;
    private Timer animationTimer;
    private final int ANIMATION_DURATION = 15;
    private final int ANIMATION_STEPS = 10;
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
        
        // Abstraction - complex mouse handling hidden behind simple event methods
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
    
    // Encapsulation - controlled access to internal active state
    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            
            if (active) {
                currentRect = calculateActiveRect();
                animationDirection = 1;
            } else {
                animationDirection = -1;
            }
            
            animationTimer.start();
            
            if (active) {
                setForeground(activeColor);
            } else {
                setForeground(hovered ? hoverColor : normalColor);
            }
        }
    }
    
    // Abstraction - hides rectangle calculation complexity
    private Rectangle calculateActiveRect() {
        int padding = 0;
        int rectWidth = getWidth() - (padding * 2);
        int rectHeight = getHeight() - (padding * 2);
        int rectX = padding;
        int rectY = padding;
        return new Rectangle(rectX, rectY, rectWidth, rectHeight);
    }
    
    // Polymorphism - overriding paintComponent for custom button appearance
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (active || animationProgress > 0) {
            int padding = 0;
            int rectWidth = getWidth() - (padding * 2);
            int rectHeight = getHeight() - (padding * 2);
            int rectX = padding;
            int rectY = padding;
            int cornerRadius = 45;
            
            if (animationProgress > 0 && animationProgress < ANIMATION_STEPS) {
                float progress = (float)animationProgress / ANIMATION_STEPS;
                rectWidth = (int)(rectWidth * progress);
                rectX = padding + (getWidth() - padding * 2 - rectWidth) / 2;
            }
            
            g2.setColor(activeBackgroundColor);
            g2.fillRoundRect(rectX, rectY, rectWidth, rectHeight, cornerRadius, cornerRadius);
           
            g2.setColor(new Color(220, 220, 220)); 
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(rectX, rectY, rectWidth, rectHeight, cornerRadius, cornerRadius);
        }
        
        super.paintComponent(g);
    }
}