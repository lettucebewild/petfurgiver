package petadoptionapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Encapsulation - Class bundles data and methods together, hiding internal details
public class About {
    // Encapsulation - Private fields hide implementation details from outside classes
    private MainFrame mainFrame;
    private Timer typingTimer;
    private JLabel textLabel;
    private JLabel imageLabel;
    private JLabel aboutUsLabel;
    private String[] sentences;
    private int currentSentenceIndex = 0;
    private StringBuilder currentText = new StringBuilder();
    private boolean isFirstTime = true;

    // Encapsulation - Constructor controls how object is initialized
    public About(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    // Abstraction - Public method hides complex panel creation logic
    public JPanel createPage() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 0));

        // Polymorphism - JLabel can display different content types (image, text)
        imageLabel = new JLabel();
        try {
            ImageIcon aboutIcon = new ImageIcon(getClass().getResource("/About.png"));
            if (aboutIcon.getIconWidth() > 0) {
                Image scaledImage = aboutIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setPreferredSize(new Dimension(350, 350));
                imageLabel.setBackground(Color.LIGHT_GRAY);
                imageLabel.setOpaque(true);
                imageLabel.setText("About Image");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception e) {
            imageLabel.setPreferredSize(new Dimension(350, 350));
            imageLabel.setBackground(Color.LIGHT_GRAY);
            imageLabel.setOpaque(true);
            imageLabel.setText("About Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setVisible(false);
        leftPanel.add(imageLabel);

        leftPanel.add(Box.createVerticalStrut(20));

        aboutUsLabel = new JLabel("About Us");
        aboutUsLabel.setFont(FontUtils.Arial(50f));
        aboutUsLabel.setForeground(Color.decode("#060644"));
        aboutUsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutUsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aboutUsLabel.setVisible(false);
        leftPanel.add(aboutUsLabel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));

        String aboutText = "At <font color='#060644'><em>FurGivers</em></font>, we believe every animal deserves a second chance. " +
                           "We connect kind-hearted individuals with lovable cats and dogs from our local shelter, " +
                           "helping them find warm, forever homes. " +
                           "Whether you're looking to adopt or donate, we are here to guide you. " +
                           "Adopt through our form to begin the screening process, or support by donating via a separate form. " +
                           "FurGivers is here to make the process easier, more meaningful, and filled with heart. " +
                           "<font color='#060644'>Together, we can change lives. <em>One paw at a time.</em></font>";

        textLabel = new JLabel();
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        textLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        prepareSentencesArray(aboutText);
        updateTextDisplay();
        
        rightPanel.add(textLabel, BorderLayout.CENTER);

        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    // Abstraction - Hides complex text preparation logic
    private void prepareSentencesArray(String text) {
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

    // Abstraction - Public method hides complex animation sequence
    public void startTypingEffect() {
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }
        
        currentSentenceIndex = 0;
        currentText = new StringBuilder();
        textLabel.setText("");
        textLabel.repaint();
        
        if (isFirstTime) {
            isFirstTime = false;
            
            // Polymorphism - Timer objects handle different animation stages
            Timer imageRevealTimer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    imageLabel.setVisible(true);
                    imageLabel.repaint();
                    ((Timer)e.getSource()).stop();
                    
                    Timer textRevealTimer = new Timer(1500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            aboutUsLabel.setVisible(true);
                            aboutUsLabel.repaint();
                            ((Timer)e.getSource()).stop();
                            
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
            startTextTyping();
        }
    }
    
    // Abstraction - Hides element reveal complexity
    private void revealElements() {
        imageLabel.setVisible(true);
        aboutUsLabel.setVisible(true);
        
        imageLabel.repaint();
        aboutUsLabel.repaint();
        
        createFadeInEffect();
    }
    
    // Abstraction - Encapsulates fade animation logic
    private void createFadeInEffect() {
        final float[] alpha = {0.0f};
        Timer fadeTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] += 0.1f;
                if (alpha[0] >= 1.0f) {
                    alpha[0] = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                
                imageLabel.repaint();
                aboutUsLabel.repaint();
            }
        });
        fadeTimer.start();
    }
    
    // Abstraction - Hides sentence-by-sentence typing logic
    private void startTextTyping() {
        // Polymorphism - ActionListener interface implemented differently for typing
        typingTimer = new Timer(1000, new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentSentenceIndex < sentences.length) {
                    String currentSentence = sentences[currentSentenceIndex];
                    
                    if (currentSentence.contains("One paw at a time.")) {
                        typingTimer.stop();
                        startWordByWordTyping(currentSentence);
                        return;
                    }
                    
                    if (currentText.length() > 0) {
                        currentText.append(" ");
                    }
                    currentText.append(currentSentence);
                    currentSentenceIndex++;
                    updateTextDisplay();
                } else {
                    typingTimer.stop();
                }
            }
        });
        
        typingTimer.start();
    }
    
    // Abstraction - Hides word-by-word typing complexity
    private void startWordByWordTyping(String sentence) {
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
        
        // Polymorphism - Different Timer behavior for word-by-word typing
        Timer wordTimer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wordIndex[0] < cleanWords.length) {
                    wordText.append(cleanWords[wordIndex[0]]);
                    
                    if (wordIndex[0] + 1 < cleanWords.length && 
                        !cleanWords[wordIndex[0] + 1].startsWith("<") && 
                        !cleanWords[wordIndex[0]].endsWith(">")) {
                        wordText.append(" ");
                    }
                    
                    wordIndex[0]++;
                    
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
                    ((Timer)e.getSource()).stop();
                    
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

    // Abstraction - Hides text formatting details
    private String formatTextWithBreaks(String text) {
        text = text.replace("guide you.", "guide you.<br><br>");
        text = text.replace("separate form.", "separate form.<br><br>");
        text = text.replace("with heart.", "with heart.<br>");
        return text;
    }

    // Abstraction - Hides HTML text display complexity
    private void updateTextDisplay() {
        String currentDisplayText = currentText.toString();
        currentDisplayText = formatTextWithBreaks(currentDisplayText);
        
        String htmlText = "<html><div style='text-align: left; font-family: Arial Nova Cond, Arial, sans-serif; font-size: 24px; " +
                         "line-height: 1.4; font-weight: normal; color: black;'>" +
                         currentDisplayText +
                         "</div></html>";
        
        textLabel.setText(htmlText);
        textLabel.repaint();
    }

    // Abstraction - Public method hides timer cleanup details
    public void cleanup() {
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }
    }
}