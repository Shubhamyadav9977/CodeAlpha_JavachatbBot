import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class JavaChatbotGUI extends JFrame {
    private JTextPane chatPane;
    private JTextField inputField;
    private JButton sendBtn, addQAButton, clearBtn;

    private final List<Intent> intents = new ArrayList<>();

    public JavaChatbotGUI() {
        setTitle("🤖 Chatbot");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initDefaultIntents();
        initUI();
    }

    private void initDefaultIntents() {
        intents.add(new Intent("greeting",
                Arrays.asList("hello", "hi", "hey"),
                Arrays.asList("Hello 👋 How can I help?", "Hi there! 🚀", "Hey! What’s up?")));

        intents.add(new Intent("goodbye",
                Arrays.asList("bye", "goodbye"),
                Arrays.asList("Goodbye! 👋", "Take care!", "See you soon!")));

        intents.add(new Intent("ai",
                Arrays.asList("what is ai", "define ai", "explain ai"),
                Arrays.asList("AI 🤖 means Artificial Intelligence: teaching machines to think and learn.",
                              "AI enables computers to make smart decisions like humans.")));

        intents.add(new Intent("thanks",
                Arrays.asList("thanks", "thank you"),
                Arrays.asList("You're welcome 🙌", "No problem!", "Glad I could help!")));
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(25, 25, 25));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chatPane.setBackground(new Color(35, 35, 35));
        chatPane.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(chatPane);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(new Color(25, 25, 25));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBackground(new Color(45, 45, 45));
        inputField.setForeground(Color.WHITE);

        sendBtn = styledButton("💬 Send", new Color(70, 130, 180));
        addQAButton = styledButton("➕ Teach Me", new Color(46, 139, 87));
        clearBtn = styledButton("🧹 Clear", new Color(128, 0, 128));

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 6, 6));
        btnPanel.setOpaque(false);
        btnPanel.add(sendBtn);
        btnPanel.add(addQAButton);
        btnPanel.add(clearBtn);

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(btnPanel, BorderLayout.EAST);

        main.add(scroll, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        add(main);

        sendBtn.addActionListener(e -> processUserInput());
        inputField.addActionListener(e -> processUserInput());
        addQAButton.addActionListener(e -> showAddQADialog());
        clearBtn.addActionListener(e -> chatPane.setText(""));

        appendBot("🤖 Hello! I’m your AI Chatbot. Ask me something!");
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    private void appendUser(String msg) {
        chatPane.setText(chatPane.getText() + "🧑 You: " + msg + "\n");
    }

    private void appendBot(String msg) {
        chatPane.setText(chatPane.getText() + "🤖 Bot: " + msg + "\n");
    }

    private void processUserInput() {
        String user = inputField.getText().trim();
        if (user.isEmpty()) return;
        appendUser(user);
        inputField.setText("");
        String response = generateResponse(user);
        appendBot(response);
    }

    private String generateResponse(String userMessage) {
        String normalized = normalize(userMessage);

        // Rule-based matching
        for (Intent it : intents) {
            for (String phrase : it.phrases) {
                if (normalized.contains(phrase)) {
                    return it.randomResponse();
                }
            }
        }

        // AI fallback (simulated here)
        return aiLikeResponse(userMessage);
    }

    private String aiLikeResponse(String msg) {
        // In real-world, integrate OpenAI API here
        // For demo, return a smart-ish reply
        return "Hmm 🤔 interesting! You asked about: \"" + msg + "\". I’ll try to improve my knowledge soon!";
    }

    private String normalize(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").trim();
    }

    private void showAddQADialog() {
        JTextField question = new JTextField();
        JTextField answer = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Trigger phrase (e.g., what is java):"));
        panel.add(question);
        panel.add(new JLabel("Answer:"));
        panel.add(answer);

        int res = JOptionPane.showConfirmDialog(this, panel, "Teach Me", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            if (!question.getText().isEmpty() && !answer.getText().isEmpty()) {
                intents.add(new Intent("custom",
                        Arrays.asList(normalize(question.getText())),
                        Arrays.asList(answer.getText())));
                appendBot("Thanks 🙌 I learned a new answer!");
            }
        }
    }

    static class Intent {
        String name;
        List<String> phrases;
        List<String> responses;

        Intent(String name, List<String> phrases, List<String> responses) {
            this.name = name;
            this.phrases = phrases;
            this.responses = responses;
        }

        String randomResponse() {
            Random r = new Random();
            return responses.get(r.nextInt(responses.size()));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JavaChatbotGUI().setVisible(true));
    }
}
