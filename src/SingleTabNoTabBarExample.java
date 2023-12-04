import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SingleTabNoTabBarExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Excel-Like Ribbon Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom ribbon panel
        RibbonPanel ribbonPanel = new RibbonPanel();

        // Create a content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new JLabel("Content goes here"), BorderLayout.CENTER);

        // Add action listeners to the ribbon buttons
        ribbonPanel.addButton("Home", "Home", e -> contentPanel.setBackground(Color.RED));
        ribbonPanel.addButton("Insert", "Insert", e -> contentPanel.setBackground(Color.GREEN));
        ribbonPanel.addButton("Page Layout", "Page Layout", e -> contentPanel.setBackground(Color.BLUE));

        // Add the ribbon and content panels to the frame
        frame.add(ribbonPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}

class RibbonPanel extends JPanel {
    private ButtonGroup buttonGroup;

    public RibbonPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonGroup = new ButtonGroup();
    }

    public void addButton(String text, String actionCommand, ActionListener actionListener) {
        JToggleButton button = new JToggleButton(text);
        button.setActionCommand(actionCommand);
        button.addActionListener(actionListener);

        buttonGroup.add(button);
        add(button);
    }
}
