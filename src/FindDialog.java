import java.awt.GridLayout;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class FindDialog extends JDialog implements ActionListener, KeyListener {

	Editor parent;
	JLabel label;
	JTextField textField;
	JCheckBox caseSensitive;
	JButton find, close;
	boolean finishedFinding = true;
	Matcher matcher;

	public FindDialog(Editor parent, boolean modal) {
		super(parent, modal);
		this.parent = parent;
		getContentPane().addKeyListener(this);
		getContentPane().setFocusable(true);
		initComponents();
		setTitle("查找");
		setLocationRelativeTo(parent);
		pack();
	}

	public void showDialog() {
		setVisible(true);
	}

	private void initComponents() {
		setLayout(new GridLayout(3, 1));
		JPanel panel1 = new JPanel();
		label = new JLabel("查找 : ");
		panel1.add(label);
		textField = new JTextField(15);
		panel1.add(textField);
		label.setLabelFor(textField);
		add(panel1); // 输入框
		JPanel panel2 = new JPanel();
		caseSensitive = new JCheckBox("大小写敏感");
		panel2.add(caseSensitive);
		add(panel2);
		JPanel panel3 = new JPanel();
		find = new JButton("查找/下一个");
		close = new JButton("关闭");
		find.addActionListener(this);
		close.addActionListener(this);
		panel3.add(find);
		panel3.add(close);
		add(panel3);
		textField.addKeyListener(this);
		find.addKeyListener(this);
		close.addKeyListener(this);
		caseSensitive.addKeyListener(this);
	}

	private void find(String pattern) {
		if (!finishedFinding) {
			if (matcher.find()) {
				int selectionStart = matcher.start();
				int selectionEnd = matcher.end();
				parent.currentTabPage.textPane.moveCaretPosition(matcher.start());
				parent.currentTabPage.textPane.select(selectionStart, selectionEnd);
			} else {
				finishedFinding = true;
				JOptionPane.showMessageDialog(this, "You have reached the end of the file", "End of file",
						JOptionPane.INFORMATION_MESSAGE);
				// closeDialog();
			}
		} else {
			matcher = Pattern.compile(pattern).matcher(parent.currentTabPage.textPane.getText());
			finishedFinding = false;
			find(pattern);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("查找/下一个")) {
			String input = textField.getText();
			StringBuilder pattern = new StringBuilder();
			if (!caseSensitive.isSelected()) {
				pattern.append("(?i)");
			}
			pattern.append(input);
			find(pattern.toString());
		} else if (cmd.equals("关闭")) {
			closeDialog();
		}
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			closeDialog();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
