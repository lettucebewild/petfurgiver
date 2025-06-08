package petadoptionapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.RoundRectangle2D;
import java.util.Collections;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;

// Inheritance - extends JPanel to inherit GUI functionality
public class FindPets extends JPanel {
    // Encapsulation - private fields hide internal data
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
    
    // Inheritance - extends BasicScrollBarUI to customize scrollbar appearance
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
            
            int componentWidth = getWidth();
            int componentHeight = getHeight();
            int imageWidth = bgImage.getWidth(this);
            int imageHeight = bgImage.getHeight(this);

            double scaleX = (double) componentWidth / imageWidth;
            double scaleY = (double) componentHeight / imageHeight;
            double scale = Math.max(scaleX, scaleY);
            
            int scaledWidth = (int) (imageWidth * scale);
            int scaledHeight = (int) (imageHeight * scale);
            
            int x = (componentWidth - scaledWidth) / 2;
            int y = (componentHeight - scaledHeight) / 2;
            
            g2d.drawImage(bgImage, x, y, scaledWidth, scaledHeight, this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Abstraction - hides complex UI setup behind simple method name
    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setOpaque(false);
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        JPanel topContainerPanel = new JPanel();
        topContainerPanel.setLayout(new BoxLayout(topContainerPanel, BoxLayout.Y_AXIS));
        topContainerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("<html><div style='text-align: center; line-height: 1.1;'>Our<br>Adoptable<br>FurBabies</div></html>");
        titleLabel.setFont(FontUtils.Arial(80f));
        titleLabel.setForeground(Color.decode("#060644"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(12, 50, 20, 50));

        JLabel chooseLabel = new JLabel("Choose a:");
        chooseLabel.setFont(FontUtils.Arial(30f));
        chooseLabel.setForeground(Color.BLACK);
        chooseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filterPanel = createFilterPanel();

        topContainerPanel.add(titleLabel);
        topContainerPanel.add(chooseLabel);
        topContainerPanel.add(filterPanel);

        petsGridPanel = new JPanel(new GridLayout(0, 3, 50, 50));
        petsGridPanel.setOpaque(false);
        petsGridPanel.setBorder(new EmptyBorder(30, 150, 30, 150));

        mainContentPanel.add(topContainerPanel);
        mainContentPanel.add(petsGridPanel);

        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(64);
        
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        
        add(scrollPane, BorderLayout.CENTER);
    }

    // Abstraction - simplifies filter panel creation
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
            }
        };
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(0, 10, 30, 10));
        filterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        typeFilterButton = createRoundedFilterPanel("Type");
        typeFilterLabel = (JLabel) typeFilterButton.getComponent(0);
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
            }
        });
        filterPanel.add(typeFilterButton);

        genderFilterButton = createRoundedFilterPanel("Gender");
        genderFilterLabel = (JLabel) genderFilterButton.getComponent(0);
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
            }
        });
        filterPanel.add(genderFilterButton);

        return filterPanel;
    }
    
    // Abstraction - creates styled button without exposing styling details
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

    // Abstraction - hides dropdown creation complexity
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

    // Abstraction - defines contract without implementation details
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

    // Polymorphism - uses instanceof to check object types at runtime
    private void updatePetDisplay() {
        petsGridPanel.removeAll();

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

        Collections.shuffle(filteredPets);
        
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

    // Inheritance - extends JPanel for custom pet card behavior
    private class PetCardPanel extends JPanel {
        // Encapsulation - private fields control animation state
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
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    isHovered = false;
                    startScaleAnimation(1.0f);
                    petNameLabel.setForeground(Color.decode("#060644"));
                    repaint();
                }
            });
        }

        // Encapsulation - private method controls animation details
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

            g2.setColor(Color.decode("#060644"));
            g2.setStroke(new BasicStroke(isHovered ? 3 : 1));
            g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

            g2.dispose();
        }
    }
}