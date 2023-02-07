import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;

public class TabPage extends JScrollPane implements ActionListener, DocumentListener {
    public JEditorPane textPane;
    private JTabbedPane tabPane;
    private TextLineNumber textLineNumber;
    private JPanel pnlTab; // Responsible for the rendering to the title.
    public JLabel lblTitle; // Title
    private JButton btnClose; // Close button
    public boolean text_changed_flag;
    public TabPage(JTabbedPane _tabPane, String _title, Font _font, String _FontName_LineNumber) {
        super();
        text_changed_flag = false;
        tabPane = _tabPane;
        textPane = new JEditorPane(); // 编辑区域
        this.setViewportView(textPane);
        textPane.setFont(_font);
        // JScrollPane jScrollPane = new JScrollPane(textPane);
        textLineNumber = new TextLineNumber(textPane);
        textLineNumber.setFont(new Font(_FontName_LineNumber, _font.getStyle(), _font.getSize()));
        this.setRowHeaderView(textLineNumber);
        // jScrollPane.setRowHeaderView(textLineNumber);
        // add(jScrollPane, BorderLayout.CENTER);
        textPane.getDocument().addDocumentListener(this);
        textPane.setText("");


        tabPane.addTab(_title, this);
        lblTitle = new JLabel(_title);
        lblTitle.setOpaque(false);
        btnClose = new JButton("x");
        btnClose.addActionListener(this);
        btnClose.setOpaque(false);
        pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        
        pnlTab.add(lblTitle, gbc);
        
        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);
        tabPane.setTabComponentAt(tabPane.indexOfTab(_title), pnlTab);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String action = e.getActionCommand();
        if(action.equals("x")){
            close();
            JButton source = (JButton)(e.getSource());
            source.removeActionListener(this);
        }
    }

    // DocumentListener
    @Override
    public void insertUpdate(DocumentEvent e) {
        text_changed_flag = true;
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        text_changed_flag = true;
    }
	@Override
	public void changedUpdate(DocumentEvent e) {
		text_changed_flag = true;
	}

    private void close(){
        int index = tabPane.indexOfTab(lblTitle.getText());
        if(index>=0){
            tabPane.removeTabAt(index);
        }
    }
    public void Select(){
        tabPane.setSelectedIndex(tabPane.indexOfTab(lblTitle.getText()));
    }
}
