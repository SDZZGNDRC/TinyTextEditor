import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Editor extends JFrame implements ActionListener, DocumentListener  {
    public JEditorPane textPane;
    private JMenuBar menuBar;
    private boolean text_changed_flag;
    private File file;
    public Editor(){
        super("TinyTextEditor");
        textPane = new JEditorPane(); // 编辑区域
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        textPane.getDocument().addDocumentListener(this);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // '文件'菜单栏
        JMenu menu_file = new JMenu("文件");
        menuBar.add(menu_file);
        JMenuItem menuItem_create = new JMenuItem("新建");
        menu_file.add(menuItem_create);
		JMenuItem menuItem_open = new JMenuItem("打开");
        menu_file.add(menuItem_open);
		JMenuItem menuItem_save = new JMenuItem("保存");
		menu_file.add(menuItem_save);
		JMenuItem menuItem_saveAs = new JMenuItem("保存为");
        menu_file.add(menuItem_saveAs);
        JMenuItem menuItem_quit = new JMenuItem("退出");
		menu_file.add(menuItem_quit);

        // '编辑'菜单栏
        JMenu menu_edit = new JMenu("编辑");
        menuBar.add(menu_edit);
		JMenuItem menuItem_cut = new JMenuItem("剪切");
		menuItem_cut.addActionListener(this);
		menuItem_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		menu_edit.add(menuItem_cut);
        JMenuItem menuItem_copy = new JMenuItem("复制");
		menuItem_copy.addActionListener(this);
		menuItem_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		menu_edit.add(menuItem_copy);
		JMenuItem menuItem_paste = new JMenuItem("粘贴");
		menuItem_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
		menuItem_paste.addActionListener(this);
		menu_edit.add(menuItem_paste);
        JMenuItem menuItem_find = new JMenuItem("查找");
		menuItem_find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		menuItem_find.addActionListener(this);
		menu_edit.add(menuItem_find);
        JMenuItem menuItem_selectAll = new JMenuItem("全选");
		menuItem_selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		menuItem_selectAll.addActionListener(this);
		menu_edit.add(menuItem_selectAll);

        // Initial
        text_changed_flag = false;

        setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String action = e.getActionCommand();
        if(action.equals("退出")){
            System.exit(0);
        }else if(action.equals("打开")){
            OpenFile();
        }else if(action.equals("保存")){
            SaveFile();
        }else if(action.equals("新建")){
            CreateFile();
        }else if(action.equals("保存为")){
            SaveAsFile();
        }else if(action.equals("剪切")){
            textPane.cut();
        }else if(action.equals("复制")){
            textPane.copy();
        }else if(action.equals("粘贴")){
            textPane.paste();
        }else if(action.equals("查找")){
            FindDialog find = new FindDialog(this, true);
			find.showDialog();
        }else if(action.equals("全选")){
            textPane.selectAll();
        }
    }
    private void OpenFile(){
        JFileChooser dialog = new JFileChooser(System.getProperty("user.home"));
		dialog.setMultiSelectionEnabled(false);
		try {
			int result = dialog.showOpenDialog(this);
			if (result != JFileChooser.APPROVE_OPTION){
				return;
            }
            if (text_changed_flag){
                SaveFile();
            }
            file = dialog.getSelectedFile();
            textPane.setText(readFile(file));
            text_changed_flag = false;
            setTitle("TinyTextEditor - " + file.getName());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		}
    }
    private void SaveFile(){
        if(!text_changed_flag){
            return;
        }
        if(file == null){
            SaveAsFile();
        }else{
            String content = textPane.getText();
            try (PrintWriter writer = new PrintWriter(file);){
                if (!file.canWrite())
                    throw new Exception("Cannot write file!");
                writer.write(content);
                text_changed_flag = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void CreateFile(){
        if(text_changed_flag){
            SaveFile();
        }
        file = null;
        textPane.setText("");
        text_changed_flag = false;
        setTitle("TinyTextEditor");
    }
    private void SaveAsFile(){
        JFileChooser chooseFile = new JFileChooser(System.getProperty("user.home"));
        chooseFile.setDialogTitle("保存为");
        int result = chooseFile.showSaveDialog(this);
        if(result != JFileChooser.APPROVE_OPTION){
            return;
        }
        file = chooseFile.getSelectedFile();
		try (PrintWriter writer = new PrintWriter(file);){
			writer.write(textPane.getText());
			text_changed_flag = false;
			setTitle("TinyTextEditor - " + file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

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

    // 从指定文件中读取所有内容
    private String readFile(File file) {
		StringBuilder result = new StringBuilder();
		try (	FileReader fr = new FileReader(file);		
				BufferedReader reader = new BufferedReader(fr);) {
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot read file !", "Error !", JOptionPane.ERROR_MESSAGE);
		}
		return result.toString();
	}
}
