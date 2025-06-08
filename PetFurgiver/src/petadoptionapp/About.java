package petadoptionapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class About {
    private MainFrame mainFrame;
    private Timer typingTimer;
    private JLabel textLabel;
    private JLabel imageLabel;
    private JLabel aboutUsLabel;
    private String[] sentences;
    private int currentSentenceIndex = 0;
    private StringBuilder currentText = new StringBuilder();
    private boolean isFirstTime = true;

    public About(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public JPanel createPage() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Create main content panel with horizontal layout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40)); // Add padding

        // Left panel for image and "About Us" text
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 0)); // Add left padding to move image to the right

        // Load and add the About.png image (smaller size) - Initially hidden
        imageLabel = new JLabel();
        try {
            ImageIcon aboutIcon = new ImageIcon(getClass().getResource("/About.png"));
            if (aboutIcon.getIconWidth() > 0) {
                Image scaledImage = aboutIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                // Fallback if image not found
                imageLabel.setPreferredSize(new Dimension(350, 350));
                imageLabel.setBackground(Color.LIGHT_GRAY);
                imageLabel.setOpaque(true);
                imageLabel.setText("About Image");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception e) {
            // Fallback if image not found
            imageLabel.setPreferredSize(new Dimension(350, 350));
            imageLabel.setBackground(Color.LIGHT_GRAY);
            imageLabel.setOpaque(true);
            imageLabel.setText("About Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setVisible(false); // Initially hidden
        leftPanel.add(imageLabel);

        // Add some spacing between image and text
        leftPanel.add(Box.createVerticalStrut(20));

        // Add "About Us" text below the image - Initially hidden
        aboutUsLabel = new JLabel("About Us");
        aboutUsLabel.setFont(FontUtils.Arial(50f));
        aboutUsLabel.setForeground(Color.decode("#060644"));
        aboutUsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutUsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutUsLabel.setVisible(false); // Initially hidden
        leftPanel.add(aboutUsLabel);

        // Right panel for the main text content
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0)); // Left margin to separate from image

        // Create text content with black color
        String aboutText = "At <font color='#060644'><em>FurGivers</em></font>, we believe every animal deserves a second chance. " +
                           "We connect kind-hearted individuals with lovable cats and dogs from our local shelter, " +
                           "helping them find warm, forever homes. " +
                           "Whether you're looking to adopt or donate, we are here to guide you. " +
                           "Adopt through our form to begin the screening process, or support by donating via a separate form. " +
                           "FurGivers is here to make the process easier, more meaningful, and filled with heart. " +
                           "<font color='#060644'>Together, we can change lives. <em>One paw at a time.</em></font>";

        // Initialize the text label
        textLabel = new JLabel();
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        textLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Split the text into sentences
        prepareSentencesArray(aboutText);
        
        // Start with empty text (don't start typing effect yet)
        updateTextDisplay();
        
        rightPanel.add(textLabel, BorderLayout.CENTER);

        // Add panels to main content panel
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private void prepareSentencesArray(String text) {
        // Define sentences manually to have better control
        sentences = new String[]{
            "At <font color='#060644'><em>FurGivers, </em></font> we believe every animal deserves a second chance.",
            "We connect kind-hearted individuals with lovable cats and dogs from our local shelter, helping them find warm, forever homes.",
            "Whether you're looking to adopt or donate, we are here to guide you.",
            "Adopt through our form to begin the screening process, or support by donating via a separate form.",
            "FurGivers is here to make the process easier, more meaningful, and filled with heart.",
            "<font color='#060644'>Together, we can change lives.",
            "<em>One paw at a time.</em></font>"
        };
    }

    public void startTypingEffect() {
        // Stop any running typing timer first
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }
        
        // Reset typing-related variables
        currentSentenceIndex = 0;
        currentText = new StringBuilder();
        textLabel.setText(""); // Clear existing text
        textLabel.repaint();
        
        if (isFirstTime) {
            // First time: do the full sequence with image and text reveals
            isFirstTime = false;
            
            // Stage 1: Show image first after 1.5 seconds
            Timer imageRevealTimer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show only the image
                    imageLabel.setVisible(true);
                    imageLabel.repaint();
                    ((Timer)e.getSource()).stop();
                    
                    // Stage 2: Show "About Us" text after another 1.5 seconds
                    Timer textRevealTimer = new Timer(1500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Show the "About Us" text
                            aboutUsLabel.setVisible(true);
                            aboutUsLabel.repaint();
                            ((Timer)e.getSource()).stop();
                            
                            // Stage 3: Start typing effect after another 1 second
                            Timer typingStartTimer = new Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    startTextTyping();
                                    ((Timer)e.getSource()).stop();
                                }
                            });
                            typingStartTimer.setRepeats(false);
                            typingStartTimer.start();
                        }
                    });
                    textRevealTimer.setRepeats(false);
                    textRevealTimer.start();
                }
            });
            imageRevealTimer.setRepeats(false);
            imageRevealTimer.start();
        } else {
            // Subsequent times: just restart the typing effect immediately
            startTextTyping();
        }
    }
    

    
    private void revealElements() {
        // Simple reveal by making elements visible
        imageLabel.setVisible(true);
        aboutUsLabel.setVisible(true);
        
        // Repaint to ensure visibility
        imageLabel.repaint();
        aboutUsLabel.repaint();
        
        // Optional: Add a subtle fade-in effect using a timer
        createFadeInEffect();
    }
    
    private void createFadeInEffect() {
        // Create a simple fade-in effect by adjusting component properties
        final float[] alpha = {0.0f};
        Timer fadeTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] += 0.1f;
                if (alpha[0] >= 1.0f) {
                    alpha[0] = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                
                // Apply alpha to components (this is a simplified approach)
                imageLabel.repaint();
                aboutUsLabel.repaint();
            }
        });
        fadeTimer.start();
    }
    
    private void startTextTyping() {
        // Create timer for sentence-by-sentence typing
        typingTimer = new Timer(1000, new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentSentenceIndex < sentences.length) {
                    String currentSentence = sentences[currentSentenceIndex];
                    
                    // Special handling for "One paw at a time."
                    if (currentSentence.contains("One paw at a time.")) {
                        typingTimer.stop(); // Stop main timer
                        startWordByWordTyping(currentSentence); // Start word-by-word for this sentence
                        return;
                    }
                    
                    // Add the sentence
                    if (currentText.length() > 0) {
                        currentText.append(" ");
                    }
                    currentText.append(currentSentence);
                    currentSentenceIndex++;
                    updateTextDisplay();
                } else {
                    // Stop the timer when all sentences are displayed
                    typingTimer.stop();
                }
            }
        });
        
        typingTimer.start();
    }
    
    private void startWordByWordTyping(String sentence) {
        // Split the sentence into words while preserving HTML tags
        String[] words = sentence.split("(?<=\\s)|(?=<)|(?<=>)");
        java.util.List<String> wordList = new java.util.ArrayList<>();
        for (String word : words) {
            if (!word.trim().isEmpty()) {
                wordList.add(word);
            }
        }
        String[] cleanWords = wordList.toArray(new String[0]);
        
        final int[] wordIndex = {0};
        final StringBuilder wordText = new StringBuilder();
        
        // Timer for word-by-word typing (faster for this special sentence)
        Timer wordTimer = new Timer(300, new ActionListener() { // 300ms between words
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wordIndex[0] < cleanWords.length) {
                    // Add the next word
                    wordText.append(cleanWords[wordIndex[0]]);
                    
                    // Add space if needed
                    if (wordIndex[0] + 1 < cleanWords.length && 
                        !cleanWords[wordIndex[0] + 1].startsWith("<") && 
                        !cleanWords[wordIndex[0]].endsWith(">")) {
                        wordText.append(" ");
                    }
                    
                    wordIndex[0]++;
                    
                    // Update display with current sentence + word-by-word text
                    StringBuilder tempText = new StringBuilder(currentText);
                    if (tempText.length() > 0) {
                        tempText.append(" ");
                    }
                    tempText.append(wordText.toString());
                    
                    String displayText = tempText.toString();
                    displayText = formatTextWithBreaks(displayText);
                    
                    String htmlText = "<html><div style='text-align: left; font-family: Arial Nova Cond, Arial, sans-serif; font-size: 24px; " +
                                     "line-height: 1.4; font-weight: normal; color: black;'>" +
                                     displayText +
                                     "</div></html>";
                    
                    textLabel.setText(htmlText);
                    textLabel.repaint();
                } else {
                    // Finished word-by-word typing
                    ((Timer)e.getSource()).stop();
                    
                    // Add the complete sentence to currentText
                    if (currentText.length() > 0) {
                        currentText.append(" ");
                    }
                    currentText.append(sentence);
                    currentSentenceIndex++;
                    updateTextDisplay();
                }
            }
        });
        
        wordTimer.start();
    }

    private String formatTextWithBreaks(String text) {
        // Add line breaks at appropriate points
        text = text.replace("guide you.", "guide you.<br><br>");
        text = text.replace("separate form.", "separate form.<br><br>");
        text = text.replace("with heart.", "with heart.<br>");
        return text;
    }

    private void updateTextDisplay() {
        String currentDisplayText = currentText.toString();
        currentDisplayText = formatTextWithBreaks(currentDisplayText);
        
        // Wrap in HTML with styling
        String htmlText = "<html><div style='text-align: left; font-family: Arial Nova Cond, Arial, sans-serif; font-size: 24px; " +
                         "line-height: 1.4; font-weight: normal; color: black;'>" +
                         currentDisplayText +
                         "</div></html>";
        
        textLabel.setText(htmlText);
        textLabel.repaint();
    }

    public void cleanup() {
        // Stop all timers if they're running
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }
        // Note: Individual timers are created as local variables and stopped individually
        // No need to track revealTimer anymore since we use multiple local timers
    }
}