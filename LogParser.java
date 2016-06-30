package blayzer.logparser;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class LogParser extends JFrame {

	private static final long serialVersionUID = 1L;
	private BufferedReader StringFromFile;
	private FileInputStream SelectedFile;
	private String filename;
	private String str;
        private StringBuilder result = new StringBuilder();
	
	JFileChooser fileopen = new JFileChooser();
	File file;
	
	public LogParser() {
	super("MachineGuard Log Parser");
	setBounds(450, 250, 400, 400);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setLayout(new GridLayout(3, 1));
	
	final JLabel label1 = new JLabel("Выберите лог-файл");
	label1.setHorizontalAlignment(SwingConstants.CENTER);
	add(label1);
	
    JButton button1 = new JButton("Выбрать лог-файл");
    add(button1);
    button1.addActionListener(action -> {
        	
            int ret = fileopen.showDialog(null, "Открыть файл");                
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = fileopen.getSelectedFile();
                try
                {
                	SelectedFile = new FileInputStream(new File(file.getPath()));
                	StringFromFile = new BufferedReader(new InputStreamReader(SelectedFile, "UTF-8"));
                }
                catch (IOException error)
                {
                	error.printStackTrace();
                }
                
                filename = file.getName();
                label1.setText("<html> Выбран файл: " + filename + "<br>" + "Нажмите: Преобразовать </html>");
            }
    });
    
    JButton button2 = new JButton("Преобразовать");
    add(button2);
    button2.addActionListener(action -> {
        	
        	try {
				while ((str = StringFromFile.readLine()) != null){
					if(str.contains("Injected new Forge block")){
						str = str.replaceAll(".+(material)","-");
						str = str.replaceAll("(\\s\\with.*)","" );
						
						if (!str.isEmpty()){
						result.append(str).append('\n');
						}
						try(FileWriter fw = new FileWriter(file.getPath() + ".mg.txt")) {
							fw.write(result.toString());
						}
						catch(IOException error){
				             
				            System.out.println(error.getMessage());
				        }
					}
					
				}
				label1.setText("<html> Выбран файл: " + filename + "<br>" + "Готово. Создан файл " + filename + ".mg.txt. </html>");
			} catch (IOException error) {
				error.printStackTrace();
			}
    });
    
	}
	
	public static void main(String[] args){
		LogParser app = new LogParser();
		app.setVisible(true);
	}

}
