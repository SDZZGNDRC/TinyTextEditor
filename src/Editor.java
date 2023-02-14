import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.plaf.ColorUIResource;
import com.formdev.flatlaf.intellijthemes.*;

public class Editor extends JFrame implements ActionListener  {
    private JTabbedPane tab;
    private TextLineNumber textLineNumber;
    private JMenuBar menuBar;
    public  TabPage currentTabPage;
    private final Font defaultFont_Text = new Font("微软雅黑", Font.PLAIN, 18);;
    private Font currentFont_Text;
    private String FontName_LineNumber = "Sitka Display";
    private JFontChooser fontChooser = new JFontChooser();
    private String Current_Theme = "FlatSolarizedLightIJTheme";
    String[] Theme_choices = {
        "FlatArcDarkIJTheme",
        "FlatArcDarkOrangeIJTheme",
        "FlatCyanLightIJTheme",
        "FlatDarkPurpleIJTheme",
        "FlatGradiantoDeepOceanIJTheme",
        "FlatHighContrastIJTheme",
        "FlatOneDarkIJTheme",
        "FlatSolarizedLightIJTheme",
        "FlatMonokaiProIJTheme",
        "FlatGruvboxDarkSoftIJTheme",
    };
    public Editor(){
        super("TinyTextEditor");
        currentFont_Text = defaultFont_Text;
        tab = new JTabbedPane();
        add(tab, BorderLayout.CENTER);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // '文件'菜单栏
        JMenu menu_file = new JMenu("文件");
        menuBar.add(menu_file);
        JMenuItem menuItem_create = new JMenuItem("新建");
        menuItem_create.addActionListener(this);
		menuItem_create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        menu_file.add(menuItem_create);
		JMenuItem menuItem_open = new JMenuItem("打开");
        menuItem_open.addActionListener(this);
		menuItem_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        menu_file.add(menuItem_open);
		JMenuItem menuItem_save = new JMenuItem("保存");
        menuItem_save.addActionListener(this);
		menuItem_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		menu_file.add(menuItem_save);
		JMenuItem menuItem_saveAs = new JMenuItem("保存为");
        menuItem_saveAs.addActionListener(this);
        menu_file.add(menuItem_saveAs);
        JMenuItem menuItem_quit = new JMenuItem("退出");
        menuItem_quit.addActionListener(this);
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

        // '设置'菜单栏
        JMenu menu_settings = new JMenu("设置");
        menuBar.add(menu_settings);
		JMenuItem menuItem_SetFont = new JMenuItem("更改字体");
		menuItem_SetFont.addActionListener(this);
		menu_settings.add(menuItem_SetFont);
		JMenuItem menuItem_SetTheme = new JMenuItem("更改主题");
		menuItem_SetTheme.addActionListener(this);
		menu_settings.add(menuItem_SetTheme);

        // Initial
        NewTab();
        setTheme(Current_Theme);

        setSize(1000, 800);
        this.setLocationRelativeTo(this);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String action = e.getActionCommand();
        Switch2CurrentTabPage();
        if(action.equals("退出")){
            System.exit(0);
        }else if(action.equals("打开")){
            OpenFile();
        }else if(action.equals("保存")){
            SaveFile();
        }else if(action.equals("新建")){
            NewTab();
        }else if(action.equals("保存为")){
            SaveAsFile();
        }else if(action.equals("剪切")){
            currentTabPage.textPane.cut();
        }else if(action.equals("复制")){
            currentTabPage.textPane.copy();
        }else if(action.equals("粘贴")){
            currentTabPage.textPane.paste();
        }else if(action.equals("查找")){
            FindDialog find = new FindDialog(this, true);
			find.showDialog();
        }else if(action.equals("全选")){
            currentTabPage.textPane.selectAll();
        }else if(action.equals("更改字体")){
            int result = fontChooser.showDialog(this);
            if (result == JFontChooser.OK_OPTION)
            {
               Font font = fontChooser.getSelectedFont(); 
               currentTabPage.textPane.setFont(font);
               textLineNumber.setFont(new Font(FontName_LineNumber, font.getStyle(), font.getSize()));
            }
        }else if(action.equals("更改主题")){
            String input = (String) JOptionPane.showInputDialog(
                null, "Choose now...",
        "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, // Use
                Theme_choices, // Array of choices
                Current_Theme); // Initial choice
            setTheme(input);
            Current_Theme = input;
        }
    }
    private void OpenFile(){
        JFileChooser dialog = new JFileChooser(System.getProperty("user.home"));
		dialog.setMultiSelectionEnabled(false);
        File temp;
		try {
			int result = dialog.showOpenDialog(this);
			if (result != JFileChooser.APPROVE_OPTION){
				return;
            }
            if (currentTabPage.text_changed_flag){
                SaveFile();
            }
            temp = dialog.getSelectedFile();
            // currentTabPage.setFile(temp);
            currentTabPage.file_absolutePath = temp.getAbsolutePath();
            NewTab();
            currentTabPage.textPane.setText(readFile(temp));
            currentTabPage.setTitle(temp.getName());
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		}
    }
    private void SaveFile(){
        File file = new File(currentTabPage.file_absolutePath);
        if(!currentTabPage.text_changed_flag){
            return;
        }
        if(currentTabPage.file_absolutePath == ""){
            SaveAsFile();
        }else{
            String content = currentTabPage.textPane.getText();
            try (PrintWriter writer = new PrintWriter(file);){
                if (!file.canWrite())
                    throw new Exception("Cannot write currentTabPage.file!");
                writer.write(content);
                currentTabPage.text_changed_flag = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentTabPage.setTitle(file.getName());
        }
    }
    private void SaveAsFile(){
        File file;
        JFileChooser chooseFile = new JFileChooser(System.getProperty("user.home"));
        chooseFile.setDialogTitle("保存为");
        int result = chooseFile.showSaveDialog(this);
        if(result != JFileChooser.APPROVE_OPTION){
            return;
        }
        file = chooseFile.getSelectedFile();
		try (PrintWriter writer = new PrintWriter(file);){
			writer.write(currentTabPage.textPane.getText());
			currentTabPage.text_changed_flag = false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        currentTabPage.setTitle(file.getName());
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
			JOptionPane.showMessageDialog(null, "Cannot read currentTabPage.file !", "Error !", JOptionPane.ERROR_MESSAGE);
		}
		return result.toString();
	}
    // Create a new tabbed page in tabPane
    private void NewTab(){
        TabPage tabPage = new TabPage(tab, GetNewTitle(), currentFont_Text, FontName_LineNumber);
        tabPage.Select();
        Switch2CurrentTabPage();
    }
    private void Switch2CurrentTabPage(){
        currentTabPage = (TabPage)tab.getSelectedComponent();
    }
    private String GetNewTitle(){
        int totalTabs = tab.getTabCount();
        int index = 0;
        String result = "新建";
        for(int i = 0; i < totalTabs; i ++){
            TabPage tabPage = (TabPage)tab.getComponentAt(i);
            if(tabPage.lblTitle.getText().equals(result)){
                index += 1;
                result = "新建" + Integer.toString(index);
            }else{
                break;
            }
        }
        return result;
    }
    public void setTheme(String themeName){
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("TextPane.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        try {
            switch (themeName) {
                case "FlatArcDarkIJTheme":
                    UIManager.setLookAndFeel( new FlatArcDarkIJTheme() );
                    break;
                case "FlatArcDarkOrangeIJTheme":
                    UIManager.setLookAndFeel( new FlatArcDarkOrangeIJTheme() );
                    break;
                case "FlatCyanLightIJTheme":
                    UIManager.setLookAndFeel( new FlatCyanLightIJTheme() );
                    break;
                case "FlatDarkPurpleIJTheme":
                    UIManager.setLookAndFeel( new FlatDarkPurpleIJTheme() );
                    break;
                case "FlatGradiantoDeepOceanIJTheme":
                    UIManager.setLookAndFeel( new FlatGradiantoDeepOceanIJTheme() );
                    break;
                case "FlatHighContrastIJTheme":
                    UIManager.setLookAndFeel( new FlatHighContrastIJTheme() );
                    break;
                case "FlatOneDarkIJTheme":
                    UIManager.setLookAndFeel( new FlatOneDarkIJTheme() );
                    break;
                case "FlatSolarizedLightIJTheme":
                    UIManager.setLookAndFeel( new FlatSolarizedLightIJTheme() );
                    break;
                case "FlatMonokaiProIJTheme":
                    UIManager.setLookAndFeel( new FlatMonokaiProIJTheme() );
                    break;
                case "FlatGruvboxDarkSoftIJTheme":
                    UIManager.setLookAndFeel( new FlatGruvboxDarkSoftIJTheme() );
                    break;
                
                default:
                    break;
            }
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
}
