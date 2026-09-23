import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CurrencyConverterGUI {

    // Replace this with your NEW API key
    private static final String API_KEY = "YOUR_API_KEY";

    private static JTextField amountField;
    private static JComboBox<String> fromBox;
    private static JComboBox<String> toBox;

    private static JLabel resultLabel;
    private static JLabel statusLabel;

    private static JTable historyTable;
    private static DefaultTableModel tableModel;

    private static final String[] currencies = {
            "INR", "USD", "EUR", "GBP", "JPY", "CAD", "AUD",
            "CHF", "CNY", "NZD", "ZAR", "SGD", "AED", "RUB",
            "SEK", "NOK", "BRL", "MXN", "KRW", "THB",
            "MYR", "IDR", "PHP", "EGP", "TRY", "SAR"
    };

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> createGUI());
    }

    private static void createGUI() {

        JFrame frame = new JFrame("Currency Converter");

        // Bigger window so all components are visible
        frame.setSize(750, 750);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                new EmptyBorder(20, 30, 20, 30)
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel("CURRENCY CONVERTER", SwingConstants.CENTER);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // =====================================================
        // CENTER PANEL
        // =====================================================

        JPanel centerPanel = new JPanel();

        centerPanel.setLayout(
                new BoxLayout(centerPanel, BoxLayout.Y_AXIS)
        );


        // =====================================================
        // AMOUNT SECTION
        // =====================================================

        JPanel amountPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JLabel amountLabel =
                new JLabel("Enter Amount:");

        amountLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        amountField = new JTextField();

        amountField.setPreferredSize(
                new Dimension(300, 40)
        );

        amountField.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        amountPanel.add(amountLabel);
        amountPanel.add(amountField);

        centerPanel.add(amountPanel);


        // =====================================================
        // CURRENCY SELECTION
        // =====================================================

        JPanel currencyPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 10)
        );


        // FROM
        JPanel fromPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        JLabel fromLabel =
                new JLabel("From", SwingConstants.CENTER);

        fromLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        fromBox = new JComboBox<>(currencies);

        fromBox.setPreferredSize(
                new Dimension(150, 35)
        );

        fromPanel.add(
                fromLabel,
                BorderLayout.NORTH
        );

        fromPanel.add(
                fromBox,
                BorderLayout.CENTER
        );


        // SWAP
        JButton swapButton =
                new JButton("⇄");

        swapButton.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        swapButton.setPreferredSize(
                new Dimension(60, 45)
        );


        // TO
        JPanel toPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        JLabel toLabel =
                new JLabel("To", SwingConstants.CENTER);

        toLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        toBox = new JComboBox<>(currencies);

        toBox.setPreferredSize(
                new Dimension(150, 35)
        );

        // Default values
        fromBox.setSelectedItem("INR");
        toBox.setSelectedItem("USD");

        toPanel.add(
                toLabel,
                BorderLayout.NORTH
        );

        toPanel.add(
                toBox,
                BorderLayout.CENTER
        );


        currencyPanel.add(fromPanel);
        currencyPanel.add(swapButton);
        currencyPanel.add(toPanel);

        centerPanel.add(currencyPanel);


        // =====================================================
        // CONVERT BUTTON
        // =====================================================

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        JButton convertButton =
                new JButton("CONVERT");

        convertButton.setPreferredSize(
                new Dimension(180, 45)
        );

        convertButton.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        buttonPanel.add(convertButton);

        centerPanel.add(buttonPanel);


        // =====================================================
        // RESULT
        // =====================================================

        resultLabel =
                new JLabel(
                        "Enter an amount and click Convert",
                        SwingConstants.CENTER
                );

        resultLabel.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        resultLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
                Box.createVerticalStrut(10)
        );

        centerPanel.add(resultLabel);


        // =====================================================
        // STATUS
        // =====================================================

        statusLabel =
                new JLabel(
                        "Ready",
                        SwingConstants.CENTER
                );

        statusLabel.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        statusLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        centerPanel.add(
                Box.createVerticalStrut(5)
        );

        centerPanel.add(statusLabel);


        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // HISTORY PANEL
        // =====================================================

        JPanel historyPanel =
                new JPanel(new BorderLayout(5, 5));


        JLabel historyLabel =
                new JLabel(
                        "CONVERSION HISTORY",
                        SwingConstants.CENTER
                );

        historyLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        historyPanel.add(
                historyLabel,
                BorderLayout.NORTH
        );


        // Table columns
        String[] columns = {
                "Amount",
                "From",
                "To",
                "Exchange Rate",
                "Converted Amount"
        };


        tableModel =
                new DefaultTableModel(columns, 0);


        historyTable =
                new JTable(tableModel);


        historyTable.setRowHeight(25);


        JScrollPane scrollPane =
                new JScrollPane(historyTable);


        // Give table a fixed height
        scrollPane.setPreferredSize(
                new Dimension(650, 150)
        );


        historyPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // Clear history button
        JButton clearButton =
                new JButton("Clear History");


        historyPanel.add(
                clearButton,
                BorderLayout.SOUTH
        );


        mainPanel.add(
                historyPanel,
                BorderLayout.SOUTH
        );


        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        convertButton.addActionListener(e -> {

            convertCurrency();

        });


        swapButton.addActionListener(e -> {

            String from =
                    fromBox.getSelectedItem().toString();

            String to =
                    toBox.getSelectedItem().toString();

            fromBox.setSelectedItem(to);

            toBox.setSelectedItem(from);

        });


        clearButton.addActionListener(e -> {

            tableModel.setRowCount(0);

            statusLabel.setText(
                    "Conversion history cleared."
            );

        });


        // Allow pressing ENTER to convert
        amountField.addActionListener(e -> {

            convertCurrency();

        });


        // Add everything to frame
        frame.add(mainPanel);

        frame.setVisible(true);
    }


    // =========================================================
    // CONVERSION
    // =========================================================

    private static void convertCurrency() {

        String amountText =
                amountField.getText().trim();


        // Empty input
        if (amountText.isEmpty()) {

            resultLabel.setText(
                    "Please enter an amount."
            );

            statusLabel.setText(
                    "Invalid input."
            );

            return;
        }


        double amount;


        // Convert text to number
        try {

            amount =
                    Double.parseDouble(amountText);

        } catch (NumberFormatException e) {

            resultLabel.setText(
                    "Please enter a valid number."
            );

            statusLabel.setText(
                    "Invalid input."
            );

            return;
        }


        // Check positive number
        if (amount <= 0) {

            resultLabel.setText(
                    "Amount must be greater than zero."
            );

            statusLabel.setText(
                    "Invalid amount."
            );

            return;
        }


        String from =
                fromBox.getSelectedItem().toString();

        String to =
                toBox.getSelectedItem().toString();


        try {

            statusLabel.setText(
                    "Fetching exchange rate..."
            );


            // Get exchange rate
            double rate =
                    getExchangeRate(from, to);


            // Calculate result
            double result =
                    amount * rate;


            // Display result
            resultLabel.setText(
                    String.format(
                            "%.2f %s = %.2f %s",
                            amount,
                            from,
                            result,
                            to
                    )
            );


            // Add to history
            addToHistory(
                    amount,
                    from,
                    to,
                    rate,
                    result
            );


        } catch (Exception e) {

            resultLabel.setText(
                    "Conversion failed."
            );

            statusLabel.setText(
                    "Unable to complete conversion."
            );
        }
    }


    // =========================================================
    // API CALL
    // =========================================================

    public static double getExchangeRate(
            String from,
            String to) {

        // Same currency
        if (from.equals(to)) {

            statusLabel.setText(
                    "Same currency selected."
            );

            return 1.0;
        }


        try {

            String urlString =
                    "https://v6.exchangerate-api.com/v6/"
                            + API_KEY
                            + "/pair/"
                            + from
                            + "/"
                            + to;


            URL url =
                    new URL(urlString);


            HttpURLConnection connection =
                    (HttpURLConnection)
                            url.openConnection();


            connection.setRequestMethod("GET");


            // Prevent application from waiting too long
            connection.setConnectTimeout(5000);

            connection.setReadTimeout(5000);


            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()
                            )
                    );


            StringBuilder response =
                    new StringBuilder();


            String line;


            while ((line = reader.readLine()) != null) {

                response.append(line);

            }


            reader.close();


            // Convert API response into JSON
            JSONObject json =
                    new JSONObject(
                            response.toString()
                    );


            String apiResult =
                    json.getString("result");


            if (apiResult.equals("success")) {

                double rate =
                        json.getDouble(
                                "conversion_rate"
                        );


                statusLabel.setText(
                        "✓ Rate fetched successfully from API."
                );


                return rate;

            } else {

                throw new Exception(
                        "API returned an error."
                );
            }


        } catch (Exception e) {

            System.out.println(
                    "API Error: " + e.getMessage()
            );


            // Use offline fallback
            statusLabel.setText(
                    "⚠ API unavailable. Using offline rate."
            );


            return getFallbackRate(
                    from,
                    to
            );
        }
    }


    // =========================================================
    // ADD HISTORY
    // =========================================================

    private static void addToHistory(
            double amount,
            String from,
            String to,
            double rate,
            double result) {


        tableModel.addRow(
                new Object[]{

                        String.format(
                                "%.2f",
                                amount
                        ),

                        from,

                        to,

                        String.format(
                                "%.4f",
                                rate
                        ),

                        String.format(
                                "%.2f",
                                result
                        )
                }
        );
    }


    // =========================================================
    // FALLBACK RATES
    // =========================================================

    public static double getFallbackRate(
            String from,
            String to) {


        if (from.equals(to)) {

            return 1.0;
        }


        switch (from + "-" + to) {

            case "USD-INR":
                return 83.2;

            case "INR-USD":
                return 1 / 83.2;

            case "EUR-INR":
                return 89.5;

            case "INR-EUR":
                return 1 / 89.5;

            case "GBP-INR":
                return 104.1;

            case "INR-GBP":
                return 1 / 104.1;

            case "USD-EUR":
                return 0.93;

            case "EUR-USD":
                return 1.08;

            case "USD-JPY":
                return 155.3;

            case "JPY-USD":
                return 1 / 155.3;

            case "INR-JPY":
                return 1.87;

            case "JPY-INR":
                return 1 / 1.87;

            case "USD-CAD":
                return 1.36;

            case "CAD-USD":
                return 1 / 1.36;

            case "USD-AUD":
                return 1.49;

            case "AUD-USD":
                return 1 / 1.49;

            case "USD-CHF":
                return 0.91;

            case "CHF-USD":
                return 1 / 0.91;

            case "USD-CNY":
                return 7.24;

            case "CNY-USD":
                return 1 / 7.24;

            case "USD-NZD":
                return 1.62;

            case "NZD-USD":
                return 1 / 1.62;

            case "USD-ZAR":
                return 18.5;

            case "ZAR-USD":
                return 1 / 18.5;

            case "USD-SGD":
                return 1.35;

            case "SGD-USD":
                return 1 / 1.35;

            case "USD-AED":
                return 3.67;

            case "AED-USD":
                return 1 / 3.67;

            case "USD-RUB":
                return 89.5;

            case "RUB-USD":
                return 1 / 89.5;

            case "USD-SEK":
                return 10.4;

            case "SEK-USD":
                return 1 / 10.4;

            case "USD-NOK":
                return 10.7;

            case "NOK-USD":
                return 1 / 10.7;

            case "INR-CAD":
                return 0.016;

            case "CAD-INR":
                return 1 / 0.016;

            case "INR-AUD":
                return 0.018;

            case "AUD-INR":
                return 1 / 0.018;

            case "INR-CHF":
                return 0.011;

            case "CHF-INR":
                return 1 / 0.011;

            case "INR-CNY":
                return 0.087;

            case "CNY-INR":
                return 1 / 0.087;

            case "INR-NZD":
                return 0.020;

            case "NZD-INR":
                return 1 / 0.020;

            case "INR-ZAR":
                return 0.045;

            case "ZAR-INR":
                return 1 / 0.045;

            case "INR-SGD":
                return 0.016;

            case "SGD-INR":
                return 1 / 0.016;

            case "INR-AED":
                return 0.044;

            case "AED-INR":
                return 1 / 0.044;

            case "INR-RUB":
                return 1.1;

            case "RUB-INR":
                return 1 / 1.1;

            case "INR-SEK":
                return 0.12;

            case "SEK-INR":
                return 1 / 0.12;

            case "INR-NOK":
                return 0.11;

            case "NOK-INR":
                return 1 / 0.11;

            case "USD-BRL":
                return 5.2;

            case "BRL-USD":
                return 1 / 5.2;

            case "USD-MXN":
                return 18.3;

            case "MXN-USD":
                return 1 / 18.3;

            case "USD-KRW":
                return 1370.5;

            case "KRW-USD":
                return 1 / 1370.5;

            case "USD-THB":
                return 36.2;

            case "THB-USD":
                return 1 / 36.2;

            case "USD-MYR":
                return 4.7;

            case "MYR-USD":
                return 1 / 4.7;

            case "USD-IDR":
                return 16000.0;

            case "IDR-USD":
                return 1 / 16000.0;

            case "USD-PHP":
                return 56.2;

            case "PHP-USD":
                return 1 / 56.2;

            case "USD-EGP":
                return 47.3;

            case "EGP-USD":
                return 1 / 47.3;

            case "USD-TRY":
                return 32.9;

            case "TRY-USD":
                return 1 / 32.9;

            case "USD-SAR":
                return 3.75;

            case "SAR-USD":
                return 1 / 3.75;

            default:

                // No known fallback rate
                throw new IllegalArgumentException(
                        "No offline rate available for "
                                + from + " to " + to
                );
        }
    }
}
