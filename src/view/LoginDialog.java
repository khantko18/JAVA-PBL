package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Simple login dialog shown before the main POS application starts.
 * 
 * Username: admin
 * Password: 1234
 */
public class LoginDialog extends JDialog {

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private boolean succeeded = false;

    public LoginDialog(Frame parent) {
        super(parent, "Cafe POS Login", true);

        // Overall dialog styling
        getContentPane().setBackground(new Color(245, 247, 250));

        // Main container with border layout
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        content.setBackground(new Color(245, 247, 250));

        // Header panel (title + subtitle)
        JLabel titleLabel = new JLabel("Welcome to Cafe POS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(40, 40, 40));

        JLabel subtitleLabel = new JLabel("Please sign in to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(110, 110, 110));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subtitleLabel);

        // Form panel (username + password)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
        cs.insets = new Insets(5, 5, 5, 5);

        JLabel lbUsername = new JLabel("Username");
        lbUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        formPanel.add(lbUsername, cs);

        tfUsername = createTextField();
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        formPanel.add(tfUsername, cs);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        formPanel.add(lbPassword, cs);

        pfPassword = createPasswordField();
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        formPanel.add(pfPassword, cs);

        // Buttons panel
        JButton btnLogin = createPrimaryButton("Login");
        JButton btnCancel = createGhostButton("Cancel");

        btnLogin.addActionListener((ActionEvent e) -> onLogin());

        btnCancel.addActionListener((ActionEvent e) -> {
            succeeded = false;
            dispose();
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(new Color(245, 247, 250));
        buttonsPanel.add(btnCancel);
        buttonsPanel.add(btnLogin);

        content.add(headerPanel, BorderLayout.NORTH);
        content.add(formPanel, BorderLayout.CENTER);
        content.add(buttonsPanel, BorderLayout.SOUTH);

        getContentPane().add(content);

        pack();
        // Make the dialog popup larger so it feels more spacious
        Dimension size = getSize();
        size.width = Math.max(size.width, 520);
        size.height = Math.max(size.height, 320);
        setSize(size);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(18);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(33, 37, 41));
        field.setBackground(new Color(248, 249, 252));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 220)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(18);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(new Color(33, 37, 41));
        field.setBackground(new Color(248, 249, 252));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 220)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }

    private JButton createGhostButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(new Color(245, 247, 250));
        button.setForeground(new Color(80, 80, 80));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 204, 210)),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        return button;
    }

    private void onLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword());

        if ("admin".equals(username) && "1234".equals(password)) {
            succeeded = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login",
                    JOptionPane.ERROR_MESSAGE);
            // reset password field
            pfPassword.setText("");
            succeeded = false;
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}


