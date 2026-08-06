package com.mycompany.currencyconverter.gui;

import com.mycompany.currencyconverter.api.ExchangeRateAPI;
import com.mycompany.currencyconverter.db.DatabaseHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class MainFrame extends JFrame {

    private JComboBox<String> fromCurrencyBox;
    private JComboBox<String> toCurrencyBox;
    private JTextField amountField;
    private JLabel resultLabel;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    private javax.swing.Timer loadingTimer;
    private int loadingFrame = 0;

    private final String[] currencies = {
            "AUD", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD",
            "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK",
            "NZD", "PHP", "PLN", "RON", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"
    };

    private final Color COLOR_CANVAS_DARK = new Color(0, 0, 0);
    private final Color COLOR_CARD_DARK = new Color(24, 24, 24);
    private final Color COLOR_PRIMARY = new Color(0, 112, 209);
    private final Color COLOR_PRIMARY_PRESSED = new Color(0, 100, 183);
    private final Color COLOR_ON_DARK = new Color(255, 255, 255);
    private final Color COLOR_MUTE_DARK = new Color(107, 107, 107);

    private Font displayFont = new Font("Segoe UI", Font.PLAIN, 28);
    private Font bodyFont = new Font("Segoe UI", Font.PLAIN, 18);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);

    public MainFrame() {
        setTitle("Currency Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_CANVAS_DARK);

        initUI();
        loadHistory();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(24, 24));
        mainPanel.setBorder(new EmptyBorder(32, 32, 32, 32));
        mainPanel.setBackground(COLOR_CANVAS_DARK);

        JLabel titleLabel = new JLabel("Currency Converter");
        titleLabel.setFont(displayFont);
        titleLabel.setForeground(COLOR_ON_DARK);
        titleLabel.setBorder(new EmptyBorder(0, 0, 24, 0));

        RoundedPanel inputPanel = new RoundedPanel(8, COLOR_CARD_DARK);
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel amountLbl = new JLabel("Amount");
        amountLbl.setFont(bodyFont);
        amountLbl.setForeground(COLOR_ON_DARK);
        inputPanel.add(amountLbl, gbc);

        gbc.gridx = 1;
        amountField = createCustomTextField();
        inputPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel fromLbl = new JLabel("From Currency");
        fromLbl.setFont(bodyFont);
        fromLbl.setForeground(COLOR_ON_DARK);
        inputPanel.add(fromLbl, gbc);

        gbc.gridx = 1;
        fromCurrencyBox = createCustomComboBox(currencies);
        fromCurrencyBox.setSelectedItem("USD");
        inputPanel.add(fromCurrencyBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        IconButton swapButton = new IconButton();
        inputPanel.add(swapButton, gbc);
        
        gbc.gridheight = 1;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel toLbl = new JLabel("To Currency");
        toLbl.setFont(bodyFont);
        toLbl.setForeground(COLOR_ON_DARK);
        inputPanel.add(toLbl, gbc);

        gbc.gridx = 1;
        toCurrencyBox = createCustomComboBox(currencies);
        toCurrencyBox.setSelectedItem("IDR");
        inputPanel.add(toCurrencyBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        JPanel actionPanel = new JPanel(new BorderLayout(16, 0));
        actionPanel.setOpaque(false);

        RoundButton convertButton = new RoundButton("Convert");
        resultLabel = new JLabel(" ");
        resultLabel.setFont(displayFont);
        resultLabel.setForeground(COLOR_ON_DARK);
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        actionPanel.add(convertButton, BorderLayout.WEST);
        
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        resultPanel.setOpaque(false);
        resultPanel.add(resultLabel);
        
        CopyButton copyButton = new CopyButton();
        resultPanel.add(copyButton);
        
        actionPanel.add(resultPanel, BorderLayout.CENTER);

        gbc.insets = new Insets(24, 8, 8, 8);
        inputPanel.add(actionPanel, gbc);

        RoundedPanel tableCard = new RoundedPanel(8, COLOR_CARD_DARK);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel tableHeaderPanel = new JPanel(new BorderLayout());
        tableHeaderPanel.setOpaque(false);
        tableHeaderPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        JLabel historyTitle = new JLabel("Conversion History");
        historyTitle.setFont(bodyFont);
        historyTitle.setForeground(COLOR_ON_DARK);
        tableHeaderPanel.add(historyTitle, BorderLayout.WEST);
        
        RoundButton clearButton = new RoundButton("Clear History");
        clearButton.setPreferredSize(new Dimension(140, 32));
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearButton.setBackground(new Color(200, 50, 50));
        
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(new Color(220, 70, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(new Color(200, 50, 50));
            }
        });
        tableHeaderPanel.add(clearButton, BorderLayout.EAST);
        
        tableCard.add(tableHeaderPanel, BorderLayout.NORTH);

        String[] columns = { "From", "To", "Amount", "Result", "Date" };
        tableModel = new DefaultTableModel(columns, 0);
        historyTable = new JTable(tableModel);
        customizeTable(historyTable);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(COLOR_CARD_DARK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 24));
        centerWrapper.setOpaque(false);
        centerWrapper.add(inputPanel, BorderLayout.NORTH);
        centerWrapper.add(tableCard, BorderLayout.CENTER);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        add(mainPanel);

        loadingTimer = new javax.swing.Timer(300, e -> {
            String[] frames = { "Converting.", "Converting..", "Converting..." };
            resultLabel.setText(frames[loadingFrame % frames.length]);
            loadingFrame++;
        });

        convertButton.addActionListener(e -> performConversion());
        amountField.addActionListener(e -> performConversion());
        
        swapButton.addActionListener(e -> {
            int fromIdx = fromCurrencyBox.getSelectedIndex();
            int toIdx = toCurrencyBox.getSelectedIndex();
            fromCurrencyBox.setSelectedIndex(toIdx);
            toCurrencyBox.setSelectedIndex(fromIdx);
            if (!amountField.getText().trim().isEmpty()) {
                performConversion();
            }
        });
        
        copyButton.addActionListener(e -> {
            String text = resultLabel.getText();
            if (text != null && !text.trim().isEmpty() && !text.equals("...") && !text.startsWith("Error") && !text.equals("Copied!")) {
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new java.awt.datatransfer.StringSelection(text), null);
                
                resultLabel.setText("Copied!");
                resultLabel.setForeground(COLOR_PRIMARY);
                javax.swing.Timer t = new javax.swing.Timer(1500, evt -> {
                    resultLabel.setText(text);
                    resultLabel.setForeground(COLOR_ON_DARK);
                });
                t.setRepeats(false);
                t.start();
            }
        });
        
        clearButton.addActionListener(e -> {
            boolean confirm = showThemedConfirmDialog("Are you sure you want to clear all history?", "Clear History");
            if (confirm) {
                DatabaseHelper.clearHistory();
                loadHistory();
            }
        });
    }

    private JTextField createCustomTextField() {
        JTextField tf = new JTextField();
        tf.setFont(bodyFont);
        tf.setBackground(COLOR_CANVAS_DARK);
        tf.setForeground(COLOR_ON_DARK);
        tf.setCaretColor(COLOR_ON_DARK);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_MUTE_DARK, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        return tf;
    }

    private JComboBox<String> createCustomComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(bodyFont);
        cb.setBackground(COLOR_CANVAS_DARK);
        cb.setForeground(COLOR_ON_DARK);
        cb.setRenderer(new FlagComboBoxRenderer());
        return cb;
    }

    private void customizeTable(JTable table) {
        table.setBackground(COLOR_CARD_DARK);
        table.setForeground(COLOR_ON_DARK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(32);
        table.setGridColor(COLOR_CANVAS_DARK);

        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_CANVAS_DARK);
        header.setForeground(COLOR_ON_DARK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void performConversion() {
        try {
            double amount = parseAmount(amountField.getText());
            String from = (String) fromCurrencyBox.getSelectedItem();
            String to = (String) toCurrencyBox.getSelectedItem();

            loadingFrame = 0;
            loadingTimer.start();

            SwingWorker<Double, Void> worker = new SwingWorker<Double, Void>() {
                @Override
                protected Double doInBackground() throws Exception {
                    return ExchangeRateAPI.getExchangeRate(from, to);
                }

                @Override
                protected void done() {
                    loadingTimer.stop();
                    try {
                        double rate = get();
                        double result = amount * rate;
                        java.text.NumberFormat format = java.text.NumberFormat
                                .getCurrencyInstance(getLocaleForCurrency(to));
                        resultLabel.setText(format.format(result));
                        DatabaseHelper.saveHistory(from, to, amount, result);
                        loadHistory();
                    } catch (Exception ex) {
                        resultLabel.setText("Error");
                        showThemedMessageDialog("Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException ex) {
            loadingTimer.stop();
            showThemedMessageDialog("Please enter a valid amount.");
        }
    }

    private void showThemedMessageDialog(String message) {
        JDialog dialog = new JDialog(this, "Message", true);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(COLOR_MUTE_DARK, 1));
        dialog.getContentPane().setBackground(COLOR_CARD_DARK);
        
        JPanel panel = new JPanel(new BorderLayout(16, 24));
        panel.setBorder(new EmptyBorder(24, 32, 24, 32));
        panel.setOpaque(false);
        
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(bodyFont);
        msgLabel.setForeground(COLOR_ON_DARK);
        panel.add(msgLabel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.setOpaque(false);
        
        RoundButton okBtn = new RoundButton("OK");
        okBtn.setPreferredSize(new Dimension(80, 32));
        okBtn.addActionListener(ev -> dialog.dispose());
        
        btnPanel.add(okBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private boolean showThemedConfirmDialog(String message, String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(COLOR_MUTE_DARK, 1));
        dialog.getContentPane().setBackground(COLOR_CARD_DARK);
        
        JPanel panel = new JPanel(new BorderLayout(16, 24));
        panel.setBorder(new EmptyBorder(24, 32, 24, 32));
        panel.setOpaque(false);
        
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(bodyFont);
        msgLabel.setForeground(COLOR_ON_DARK);
        panel.add(msgLabel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setOpaque(false);
        
        RoundButton yesBtn = new RoundButton("Yes");
        yesBtn.setPreferredSize(new Dimension(80, 32));
        yesBtn.setBackground(new Color(200, 50, 50)); 
        
        RoundButton noBtn = new RoundButton("No");
        noBtn.setPreferredSize(new Dimension(80, 32));
        noBtn.setBackground(COLOR_MUTE_DARK); 
        
        final boolean[] result = {false};
        
        yesBtn.addActionListener(ev -> {
            result[0] = true;
            dialog.dispose();
        });
        noBtn.addActionListener(ev -> {
            result[0] = false;
            dialog.dispose();
        });
        
        btnPanel.add(noBtn);
        btnPanel.add(yesBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        return result[0];
    }

    private double parseAmount(String text) throws NumberFormatException {
        text = text.trim();

        if (text.contains(".") && text.contains(",")) {
            if (text.lastIndexOf(',') > text.lastIndexOf('.')) {
                text = text.replace(".", "").replace(",", ".");
            } else {
                text = text.replace(",", "");
            }
        }
        else if (text.contains(",")) {
            text = text.replace(",", ".");
        }
        else if (text.contains(".")) {
            String[] parts = text.split("\\.");
            if (parts.length > 2 || (parts.length == 2 && parts[1].length() == 3)) {
                text = text.replace(".", "");
            }
        }

        return Double.parseDouble(text);
    }

    private java.util.Locale getLocaleForCurrency(String currencyCode) {
        switch (currencyCode) {
            case "AUD":
                return new java.util.Locale("en", "AU");
            case "BRL":
                return new java.util.Locale("pt", "BR");
            case "CAD":
                return java.util.Locale.CANADA;
            case "CHF":
                return new java.util.Locale("de", "CH");
            case "CNY":
                return java.util.Locale.CHINA;
            case "CZK":
                return new java.util.Locale("cs", "CZ");
            case "DKK":
                return new java.util.Locale("da", "DK");
            case "EUR":
                return java.util.Locale.GERMANY;
            case "GBP":
                return java.util.Locale.UK;
            case "HKD":
                return new java.util.Locale("en", "HK");
            case "HUF":
                return new java.util.Locale("hu", "HU");
            case "IDR":
                return new java.util.Locale("id", "ID");
            case "ILS":
                return new java.util.Locale("he", "IL");
            case "INR":
                return new java.util.Locale("en", "IN");
            case "ISK":
                return new java.util.Locale("is", "IS");
            case "JPY":
                return java.util.Locale.JAPAN;
            case "KRW":
                return java.util.Locale.KOREA;
            case "MXN":
                return new java.util.Locale("es", "MX");
            case "MYR":
                return new java.util.Locale("ms", "MY");
            case "NOK":
                return new java.util.Locale("no", "NO");
            case "NZD":
                return new java.util.Locale("en", "NZ");
            case "PHP":
                return new java.util.Locale("en", "PH");
            case "PLN":
                return new java.util.Locale("pl", "PL");
            case "RON":
                return new java.util.Locale("ro", "RO");
            case "SEK":
                return new java.util.Locale("sv", "SE");
            case "SGD":
                return new java.util.Locale("en", "SG");
            case "THB":
                return new java.util.Locale("th", "TH");
            case "TRY":
                return new java.util.Locale("tr", "TR");
            case "USD":
                return java.util.Locale.US;
            case "ZAR":
                return new java.util.Locale("en", "ZA");
            default:
                return java.util.Locale.US;
        }
    }

    private void loadHistory() {
        tableModel.setRowCount(0);
        List<String[]> history = DatabaseHelper.getHistory();
        for (String[] row : history) {
            try {
                String from = row[0];
                String to = row[1];
                double amt = Double.parseDouble(row[2]);
                double res = Double.parseDouble(row[3]);

                java.text.NumberFormat formatFrom = java.text.NumberFormat
                        .getCurrencyInstance(getLocaleForCurrency(from));
                java.text.NumberFormat formatTo = java.text.NumberFormat.getCurrencyInstance(getLocaleForCurrency(to));

                row[2] = formatFrom.format(amt);
                row[3] = formatTo.format(res);
            } catch (Exception e) {
            }
            tableModel.addRow(row);
        }
    }

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundButton extends JButton {
        public RoundButton(String text) {
            super(text);
            setFont(buttonFont);
            setForeground(COLOR_ON_DARK);
            setBackground(COLOR_PRIMARY);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(160, 48));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(COLOR_PRIMARY_PRESSED);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(COLOR_PRIMARY);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isArmed()) {
                g2.setColor(COLOR_PRIMARY_PRESSED.darker());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

            FontMetrics metrics = g2.getFontMetrics(getFont());
            int x = (getWidth() - metrics.stringWidth(getText())) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    class IconButton extends RoundButton {
        public IconButton() {
            super("");
            setPreferredSize(new Dimension(48, 48));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            
            g2.drawLine(cx - 6, cy - 8, cx - 6, cy + 8);
            g2.drawLine(cx - 10, cy + 4, cx - 6, cy + 8);
            g2.drawLine(cx - 2, cy + 4, cx - 6, cy + 8);
            
            g2.drawLine(cx + 6, cy - 8, cx + 6, cy + 8);
            g2.drawLine(cx + 2, cy - 4, cx + 6, cy - 8);
            g2.drawLine(cx + 10, cy - 4, cx + 6, cy - 8);
            g2.dispose();
        }
    }

    class CopyButton extends RoundButton {
        public CopyButton() {
            super("");
            setPreferredSize(new Dimension(48, 48));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            
            g2.drawRoundRect(cx - 2, cy - 2, 10, 10, 2, 2);
            g2.drawRoundRect(cx - 8, cy - 8, 10, 10, 2, 2);
            g2.dispose();
        }
    }

    class FlagComboBoxRenderer extends DefaultListCellRenderer {
        private final java.util.Map<String, ImageIcon> iconCache = new java.util.HashMap<>();

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null) {
                String code = value.toString();
                label.setText(code);

                label.setHorizontalTextPosition(SwingConstants.LEFT);
                label.setIconTextGap(10);

                ImageIcon icon = iconCache.get(code);
                if (icon == null && !iconCache.containsKey(code)) {
                    try {
                        String country = getCountryCode(code);
                        java.net.URL url = new java.net.URL("https://flagcdn.com/w20/" + country + ".png");
                        icon = new ImageIcon(javax.imageio.ImageIO.read(url));
                        iconCache.put(code, icon);
                    } catch (Exception e) {
                        iconCache.put(code, null);
                    }
                }
                label.setIcon(icon);
                label.setBorder(new EmptyBorder(4, 8, 4, 8));

                if (isSelected) {
                    label.setBackground(COLOR_PRIMARY);
                    label.setForeground(COLOR_ON_DARK);
                } else {
                    label.setBackground(COLOR_CANVAS_DARK);
                    label.setForeground(COLOR_ON_DARK);
                }
            }
            return label;
        }

        private String getCountryCode(String currency) {
            switch (currency) {
                case "AUD":
                    return "au";
                case "BRL":
                    return "br";
                case "CAD":
                    return "ca";
                case "CHF":
                    return "ch";
                case "CNY":
                    return "cn";
                case "CZK":
                    return "cz";
                case "DKK":
                    return "dk";
                case "EUR":
                    return "eu";
                case "GBP":
                    return "gb";
                case "HKD":
                    return "hk";
                case "HUF":
                    return "hu";
                case "IDR":
                    return "id";
                case "ILS":
                    return "il";
                case "INR":
                    return "in";
                case "ISK":
                    return "is";
                case "JPY":
                    return "jp";
                case "KRW":
                    return "kr";
                case "MXN":
                    return "mx";
                case "MYR":
                    return "my";
                case "NOK":
                    return "no";
                case "NZD":
                    return "nz";
                case "PHP":
                    return "ph";
                case "PLN":
                    return "pl";
                case "RON":
                    return "ro";
                case "SEK":
                    return "se";
                case "SGD":
                    return "sg";
                case "THB":
                    return "th";
                case "TRY":
                    return "tr";
                case "USD":
                    return "us";
                case "ZAR":
                    return "za";
                default:
                    return "us";
            }
        }
    }
}
