package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.geom.RoundRectangle2D;
import java.util.Collections;
import java.awt.AlphaComposite;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class FindPets extends JPanel {
    private JFrame ownerFrame;
    private JPanel petsGridPanel;
    private ArrayList<Pet> fullPetList;
    private String currentPetTypeFilter = "All";
    private String currentGenderFilter = "All";
    private JPanel typeFilterButton;
    private JPanel genderFilterButton;
    private JLabel typeFilterLabel;
    private JLabel genderFilterLabel;
    
    public JPanel createPage() {
        return this; 
    }

    public FindPets(JFrame ownerFrame) {
        this.ownerFrame = ownerFrame;
        initializeUI();
        initializePetData();
    }
    
    // Simplified custom scrollbar UI that's always visible but styled
    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
        	this.thumbColor = new Color(6, 6, 68, 200);
            this.trackColor = new Color(255, 255, 255, 200);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4, thumbBounds.height, 4, 4);
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            String backgroundImagePath = "/FindPets.png";
            Image bgImage = new ImageIcon(getClass().getResource(backgroundImagePath)).getImage();
            Graphics2D g2d = (Graphics2D) g;
            
            // Get component and image dimensions
            int componentWidth = getWidth();
            int componentHeight = getHeight();
            int imageWidth = bgImage.getWidth(this);
            int imageHeight = bgImage.getHeight(this);
            
            // Calculate scaling factor to fill entire background (may crop edges)
            double scaleX = (double) componentWidth / imageWidth;
            double scaleY = (double) componentHeight / imageHeight;
            double scale = Math.max(scaleX, scaleY); // Use larger scale to fill entire area
            
            // Calculate new dimensions
            int scaledWidth = (int) (imageWidth * scale);
            int scaledHeight = (int) (imageHeight * scale);
            
            // Center the image
            int x = (componentWidth - scaledWidth) / 2;
            int y = (componentHeight - scaledHeight) / 2;
            
            // Draw the scaled image
            g2d.drawImage(bgImage, x, y, scaledWidth, scaledHeight, this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setOpaque(false);
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        // Top Section: Title and Filters
        JPanel topContainerPanel = new JPanel();
        topContainerPanel.setLayout(new BoxLayout(topContainerPanel, BoxLayout.Y_AXIS));
        topContainerPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("<html><div style='text-align: center; line-height: 1.1;'>Our<br>Adoptable<br>FurBabies</div></html>");
        titleLabel.setFont(FontUtils.Arial(80f));
        titleLabel.setForeground(Color.decode("#060644"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(12, 50, 20, 50));

        // "Choose a:" text
        JLabel chooseLabel = new JLabel("Choose a:");
        chooseLabel.setFont(FontUtils.Arial(30f));
        chooseLabel.setForeground(Color.BLACK);
        chooseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Filter Panel
        JPanel filterPanel = createFilterPanel();

        topContainerPanel.add(titleLabel);
        topContainerPanel.add(chooseLabel);
        topContainerPanel.add(filterPanel);

        // Pet Grid Panel
        petsGridPanel = new JPanel(new GridLayout(0, 3, 50, 50));
        petsGridPanel.setOpaque(false);
        petsGridPanel.setBorder(new EmptyBorder(30, 150, 30, 150));

        // Add both top container and pets grid to main content panel
        mainContentPanel.add(topContainerPanel);
        mainContentPanel.add(petsGridPanel);

        // Create a standard JScrollPane with custom styling
        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);
        
        // Apply custom scrollbar UI
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                // Remove the fillRect call to avoid filling with any color
                // If you want transparency, do not paint anything here
            }
        };
        filterPanel.setOpaque(false); // Ensure the panel itself is non-opaque
        filterPanel.setBorder(new EmptyBorder(0, 10, 30, 10));
        filterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Type Filter Button
        typeFilterButton = createRoundedFilterPanel("Type");
        typeFilterLabel = (JLabel) typeFilterButton.getComponent(0);
        // Initialize the label with current filter
        updateTypeFilterLabel();
        typeFilterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showTypeDropdown();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                typeFilterLabel.setForeground(Color.decode("#b88917"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                typeFilterLabel.setForeground(Color.BLACK);
                // Don't call updateTypeFilterLabel() here - just reset the color
            }
        });
        filterPanel.add(typeFilterButton);

        // Gender Filter Button
        genderFilterButton = createRoundedFilterPanel("Gender");
        genderFilterLabel = (JLabel) genderFilterButton.getComponent(0);
        // Initialize the label with current filter
        updateGenderFilterLabel();
        genderFilterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showGenderDropdown();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                genderFilterLabel.setForeground(Color.decode("#b88917"));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                genderFilterLabel.setForeground(Color.BLACK);
                // Don't call updateGenderFilterLabel() here - just reset the color
            }
        });
        filterPanel.add(genderFilterButton);

        return filterPanel;
    }
    
    private JPanel createRoundedFilterPanel(String text) {
        JPanel navBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(239, 239, 218, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(6, 6, 68));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);
            }
        };
        
        navBox.setLayout(new BorderLayout());
        navBox.setOpaque(false);
        navBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navBox.setPreferredSize(new Dimension(190, 45));
        
        JLabel label = new JLabel(text);
        label.setFont(FontUtils.Arial(28f));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        navBox.add(label, BorderLayout.CENTER);
        
        return navBox;
    }

    private void showTypeDropdown() {
        String[] options = {"All", "Cat", "Dog"};
        JPopupMenu popup = createDropdownPopup(options, currentPetTypeFilter, selectedOption -> {
            currentPetTypeFilter = selectedOption;
            updateTypeFilterLabel();
            updatePetDisplay();
        });
        
        popup.show(typeFilterButton, 0, typeFilterButton.getHeight());
    }

    private void showGenderDropdown() {
        String[] options = {"All", "Male", "Female"};
        JPopupMenu popup = createDropdownPopup(options, currentGenderFilter, selectedOption -> {
            currentGenderFilter = selectedOption;
            updateGenderFilterLabel();
            updatePetDisplay();
        });
        
        popup.show(genderFilterButton, 0, genderFilterButton.getHeight());
    }

    private void updateTypeFilterLabel() {
        typeFilterLabel.setText("<html>Type: <span style='color: #b88917;'>" + currentPetTypeFilter + "</span></html>");
        typeFilterLabel.setForeground(Color.BLACK);
    }

    private void updateGenderFilterLabel() {
        genderFilterLabel.setText("<html>Gender: <span style='color: #b88917;'>" + currentGenderFilter + "</span></html>");
        genderFilterLabel.setForeground(Color.BLACK);
    }

    private JPopupMenu createDropdownPopup(String[] options, String currentSelection, DropdownSelectionListener listener) {
        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        for (String option : options) {
            JMenuItem menuItem = new JMenuItem(option);
            menuItem.setFont(FontUtils.Arial(24f));
            menuItem.setForeground(Color.BLACK);
            menuItem.setBackground(Color.WHITE);
            
            if (option.equals(currentSelection)) {
                menuItem.setBackground(Color.decode("#E8F4FD"));
            }
            
            menuItem.addActionListener(e -> {
                listener.onSelectionChanged(option);
                popup.setVisible(false);
            });
            
            popup.add(menuItem);
        }
        
        return popup;
    }

    private interface DropdownSelectionListener {
        void onSelectionChanged(String selectedOption);
    }

    private void initializePetData() {
        fullPetList = PetDataManager.getInstance().getAllPets();
        shufflePetList();
        updatePetDisplay();
    }
    
    private void shufflePetList() {
        Collections.shuffle(fullPetList);
    }

    private void updatePetDisplay() {
        petsGridPanel.removeAll();

        // Create a temporary list to hold filtered pets
        ArrayList<Pet> filteredPets = new ArrayList<>();
        
        for (Pet pet : fullPetList) {
            boolean matchesType = (currentPetTypeFilter.equals("All") ||
                                 (currentPetTypeFilter.equals("Cat") && pet instanceof Cat) ||
                                 (currentPetTypeFilter.equals("Dog") && pet instanceof Dog));

            boolean matchesGender = (currentGenderFilter.equals("All") ||
                                  pet.getGender().equalsIgnoreCase(currentGenderFilter));

            if (matchesType && matchesGender) {
                filteredPets.add(pet);
            }
        }

        // Shuffle just the filtered pets before displaying
        Collections.shuffle(filteredPets);
        
        // Add the shuffled, filtered pets to the panel
        for (Pet pet : filteredPets) {
            JPanel petCard = createPetCard(pet);
            petsGridPanel.add(petCard);
        }

        petsGridPanel.revalidate();
        petsGridPanel.repaint();
    }

    private JPanel createPetCard(Pet pet) {
        return new PetCardPanel(pet);
    }

    private class PetCardPanel extends JPanel {
        private float scale = 1.0f;
        private Timer scaleTimer;
        private final int ANIMATION_STEPS = 5;
        private final int ANIMATION_DELAY = 20;
        private Pet pet;
        private boolean isHovered = false;

        public PetCardPanel(Pet pet) {
            this.pet = pet;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setBorder(new EmptyBorder(15, 15, 15, 15));
            setPreferredSize(new Dimension(250, 350));
            setMaximumSize(new Dimension(250, 350));
            setMinimumSize(new Dimension(250, 350));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Pet Image
            JLabel petImageLabel = new JLabel();
            try {
                ImageIcon originalIcon = new ImageIcon(getClass().getResource(pet.getImagePath()));
                Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                petImageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (NullPointerException ex) {
                petImageLabel.setText("<html><center>Image Error:<br>" + pet.getName() + "</center></html>");
                petImageLabel.setFont(FontUtils.Arial(12f));
                petImageLabel.setBackground(Color.LIGHT_GRAY);
            }
            petImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            petImageLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

            // Pet Name
            JLabel petNameLabel = new JLabel(pet.getName());
            petNameLabel.setFont(FontUtils.Arial(40f));
            petNameLabel.setForeground(Color.decode("#060644"));
            petNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            petNameLabel.setBorder(new EmptyBorder(10, 0, 5, 0));

            add(petImageLabel);
            add(petNameLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    PetDetails petDetails = new PetDetails(ownerFrame, pet);
                    petDetails.setVisible(true);
                }

                @Override
                public void mouseEntered(MouseEvent evt) {
                    isHovered = true;
                    startScaleAnimation(1.05f);
                    petNameLabel.setForeground(Color.decode("#b88917"));
                    repaint(); // Ensure the panel is repainted
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    isHovered = false;
                    startScaleAnimation(1.0f);
                    petNameLabel.setForeground(Color.decode("#060644"));
                    repaint(); // Ensure the panel is repainted
                }
            });
        }

        private void startScaleAnimation(float targetScale) {
            if (scaleTimer != null && scaleTimer.isRunning()) {
                scaleTimer.stop();
            }

            float startScale = scale;
            float deltaScale = (targetScale - startScale) / ANIMATION_STEPS;

            scaleTimer = new Timer(ANIMATION_DELAY, e -> {
                int step = (Integer) ((Timer) e.getSource()).getDelay();
                scale = startScale + deltaScale * step;

                if (step >= ANIMATION_STEPS) {
                    scale = targetScale;
                    ((Timer) e.getSource()).stop();
                }

                repaint();
            });

            scaleTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            g2.translate(cx, cy);
            g2.scale(scale, scale);
            g2.translate(-cx, -cy);

            int arc = 15;
            int width = getWidth();
            int height = getHeight();

            g2.setColor(Color.decode("#060644"));
            g2.fill(new RoundRectangle2D.Double(2, 2, width - 2, height - 2, arc, arc));

            g2.setColor(Color.decode("#e6e6e4"));
            g2.fillRoundRect(0, 0, width, height, arc, arc);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 15;
            int width = getWidth();
            int height = getHeight();

            // Set border color to #060644 and change thickness on hover
            g2.setColor(Color.decode("#060644"));
            g2.setStroke(new BasicStroke(isHovered ? 3 : 1)); // Thicker border on hover
            g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

            g2.dispose();
        }
    }
}