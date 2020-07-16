package window;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Font;
import java.awt.Color;



import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.UIManager;

import org.apache.poi.EncryptedDocumentException;

import DAO.StoreSolution;
import DAO.Impl.InitImpl;
import DAO.Impl.StoreSolutionImpl;
import GA.ClassSolution;
import GA.WholeSolution;
import tools.ExcelReader;
import tools.ExcelWriter;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ExeWindow{

	public JFrame frame;
	public static ExcelReader er;
	public static WholeSolution ws;
	private JMenuItem menuItem_halfterm;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws EncryptedDocumentException 
	 */
	public static void main(String[] args) throws EncryptedDocumentException, IOException {

	
		//ExcelWriter.outputAllCourse(ws.best);
		//ExcelWriter.outputSolution(ws.best);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExcelReader.loadArrangeInfo();
					ExcelReader.loadClassroom();
					ExcelReader.loadTeacher();
					ExcelReader.loadCourse();
					ws=new WholeSolution();
					ExeWindow window = new ExeWindow();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ExeWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setForeground(new Color(176, 224, 230));
		frame.setFont(new Font("Axure Handwriting", Font.BOLD, 12));
		frame.setTitle("\u9009\u8BFE\u7CFB\u7EDF1.0");
		frame.setBounds(700, 400, 450,300 );
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_main = new JPanel();
		frame.getContentPane().add(panel_main, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("\u6B22\u8FCE\u4F7F\u7528\u9009\u8BFE\u7CFB\u7EDF1.0");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(UIManager.getFont("FileChooser.listFont"));

		timeProgress progressBar = new timeProgress();
		progressBar.setAutoscrolls(true);
		progressBar.setStringPainted(true);
		
		progressBar.setBackground(new Color(0, 255, 255));
		
		JButton button_start = new JButton("\u5F00\u59CB\u6392\u8BFE");

		button_start.setForeground(new Color(0, 0, 0));
		button_start.setBackground(new Color(175, 238, 238));
		button_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				progressBar.setValue(0);
				progressBar.setVisible(true);
				ws.start();
				ExcelWriter.outputSolution(ws.best,ExcelReader.savePath);
				ExcelWriter.outputAllCourse(ws.best,ExcelReader.savePath_course);
				File file=new File(ExcelReader.savePath);
				StoreSolutionImpl ss=new StoreSolutionImpl();
				ss.saveSolution(ws.best);
				try {
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(button_start,"加载完成");
			}
			
		});
		
		
		ws.tp=progressBar;
		JButton button_examine = new JButton("\u67E5\u770B\u5F53\u524D\u914D\u7F6E\u6587\u4EF6");
		button_examine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				new ConfigFrame().setVisible(true);
				
			}
			
		});
		button_examine.setBackground(new Color(175, 238, 238));
		GroupLayout gl_panel_main = new GroupLayout(panel_main);
		gl_panel_main.setHorizontalGroup(
			gl_panel_main.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_main.createSequentialGroup()
					.addContainerGap(57, Short.MAX_VALUE)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
					.addGap(49))
				.addGroup(Alignment.LEADING, gl_panel_main.createSequentialGroup()
					.addGap(120)
					.addGroup(gl_panel_main.createParallelGroup(Alignment.LEADING)
						.addComponent(button_start, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
						.addComponent(button_examine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
					.addGap(120))
		);
		gl_panel_main.setVerticalGroup(
			gl_panel_main.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_main.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(button_examine, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(29)
					.addComponent(button_start)
					.addContainerGap(34, Short.MAX_VALUE))
		);
		
		panel_main.setLayout(gl_panel_main);
		
		JPanel panel_progress = new JPanel();
		frame.getContentPane().add(panel_progress, BorderLayout.SOUTH);
		
		panel_progress.add(progressBar);
		
		
		
		JPanel panel_left = new JPanel();
		frame.getContentPane().add(panel_left, BorderLayout.WEST);
		
		JPanel panel_right = new JPanel();
		frame.getContentPane().add(panel_right, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu_begin = new JMenu("\u5F00\u59CB");
		menuBar.add(mnNewMenu_begin);
		
		JMenuItem menuItem_start = new JMenuItem("\u5F00\u59CB\u6392\u8BFE");
		mnNewMenu_begin.add(menuItem_start);
		menuItem_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				ws.start();
				ExcelWriter.outputSolution(ws.best,ExcelReader.savePath);
				ExcelWriter.outputAllCourse(ws.best,ExcelReader.savePath_course);
				File file=new File(ExcelReader.savePath);
				StoreSolutionImpl ss=new StoreSolutionImpl();
				ss.saveSolution(ws.best);
				try {
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(button_start,"加载完成");
			}
			
		});
		
		JMenuItem menuItem_examine = new JMenuItem("\u67E5\u770B\u5F53\u524D\u5DF2\u914D\u7F6E\u7684\u4FE1\u606F");
		mnNewMenu_begin.add(menuItem_examine);
		menuItem_examine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				new ConfigFrame().setVisible(true);
			}
			
		});
		
		JMenuItem menuItem_history = new JMenuItem("\u67E5\u770B\u5386\u53F2\u8BFE\u8868");
		mnNewMenu_begin.add(menuItem_history);
		menuItem_history.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
					StoreSolutionImpl ss=new StoreSolutionImpl();
					ClassSolution cs=ss.OutputSolution(1);
					ClassSolution ctemp=cs;
				for(int i=2;i<=ExcelReader.classNum;i++) {
					ClassSolution cc=ss.OutputSolution(i);
					ctemp.next=cc;
					ctemp=ctemp.next;
				}
				ExcelWriter.outputSolution(cs,ExcelReader.savePath_history);
				//ExcelWriter.outputAllCourse(cs,ExcelReader.savePath_courseHistory);
				File file=new File(ExcelReader.savePath_history);
				try {
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(button_start,"加载完成");
			}
			
			
		});
		
		JMenuItem menuItem_exit = new JMenuItem("\u9000\u51FA");
		mnNewMenu_begin.add(menuItem_exit);
		menuItem_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				frame.dispose();
			}
			
		});
		
		JMenu mnNewMenu_param = new JMenu("\u8BBE\u7F6E\u53C2\u6570");
		mnNewMenu_param.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		menuBar.add(mnNewMenu_param);
		
		JMenuItem menuItem_classNum = new JMenuItem("\u8BBE\u7F6E\u73ED\u7EA7\u4E2A\u6570");
		mnNewMenu_param.add(menuItem_classNum);
		menuItem_classNum.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				new ParamSet(menuItem_classNum);
				
			}
			
		});
		
		JMenuItem menuItem_classPeople = new JMenuItem("\u8BBE\u7F6E\u73ED\u7EA7\u4EBA\u6570");
		mnNewMenu_param.add(menuItem_classPeople);
		menuItem_classPeople.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new ParamSet(menuItem_classPeople);
				
			}
		});
		
		JMenuItem menuItem_mutate = new JMenuItem("\u8BBE\u7F6E\u53D8\u5F02\u6982\u7387");
		mnNewMenu_param.add(menuItem_mutate);
		menuItem_mutate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new ParamSet(menuItem_mutate);
					
			}
		});
		
		JMenuItem menuItem_crossover = new JMenuItem("\u8BBE\u7F6E\u4EA4\u53C9\u6982\u7387");
		mnNewMenu_param.add(menuItem_crossover);
		menuItem_crossover.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new ParamSet(menuItem_crossover);
				
			}
		});
		
		JMenuItem menuItem_dayaverage = new JMenuItem("\u5F00\u542F\u6BCF\u5929\u8BFE\u65F6\u4F18\u5316");
		mnNewMenu_param.add(menuItem_dayaverage);
		menuItem_dayaverage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new ModelOn(menuItem_dayaverage);

			}
			
		});
		
		menuItem_halfterm = new JMenuItem("\u5F00\u542F\u524D\u540E\u516B\u5468\u8BFE\u65F6\u4F18\u5316");
		mnNewMenu_param.add(menuItem_halfterm);
		menuItem_halfterm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new ModelOn(menuItem_halfterm);
				
			}
		});
		JMenuItem menuItem_effectivity = new JMenuItem("\u5F00\u542F\u5B66\u5206\u4F18\u5316");
		mnNewMenu_param.add(menuItem_effectivity);
		menuItem_effectivity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new ModelOn(menuItem_effectivity);
			}
		});
		
		JMenu menu_import = new JMenu("\u5BFC\u5165\u6587\u4EF6");
		menuBar.add(menu_import);
		
		JMenuItem menuItem_courseinfo = new JMenuItem("\u5BFC\u5165\u8BFE\u7A0B\u4FE1\u606F");
		menu_import.add(menuItem_courseinfo);
		menuItem_courseinfo.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				new ImportInfo(menuItem_courseinfo);
			}
		});
		
		
		JMenuItem menuItem_arrange = new JMenuItem("\u5BFC\u5165\u5B66\u5206\u9700\u6C42\u4FE1\u606F");
		menu_import.add(menuItem_arrange);
		menuItem_arrange.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				new ImportInfo(menuItem_arrange);
				
			}
		});
		JMenuItem menuItem_teacher = new JMenuItem("\u5BFC\u5165\u6559\u5E08\u4FE1\u606F");
		menu_import.add(menuItem_teacher);
		menuItem_teacher.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				new ImportInfo(menuItem_teacher);
				
			}
		});
		
		JMenuItem menuItem_classroom = new JMenuItem("\u5BFC\u5165\u6559\u5BA4\u4FE1\u606F");
		menu_import.add(menuItem_classroom);
		menuItem_classroom.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				new ImportInfo(menuItem_classroom);
				
			}
		});
		
		JMenu mnNewMenu_look = new JMenu("\u67E5\u770B\u6F14\u5316\u56FE");
		mnNewMenu_look.setActionCommand("\u67E5\u770B\u6F14\u5316\u56FE");
		menuBar.add(mnNewMenu_look);
		
		JMenuItem menuItem = new JMenuItem("\u6548\u7387\u5F97\u5206\u6F14\u5316\u8FC7\u7A0B");
		mnNewMenu_look.add(menuItem);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new DrawPicture(ws,0);
			}
			
		});
		
		JMenuItem menuItem_1 = new JMenuItem("\u524D\u540E\u516B\u5468\u8BFE\u65F6\u6F14\u5316\u8FC7\u7A0B");
		mnNewMenu_look.add(menuItem_1);
		menuItem_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new DrawPicture(ws,1);
			}
			
		});
		
		JMenuItem menuItem_2 = new JMenuItem("\u6BCF\u65E5\u5B66\u65F6\u5E73\u5747\u6F14\u5316\u8FC7\u7A0B");
		mnNewMenu_look.add(menuItem_2);
		menuItem_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				new DrawPicture(ws,2);
			}
			
		});
		
		JMenu menu = new JMenu("\u67E5\u770B\u6700\u7EC8\u5206\u914D");
		menuBar.add(menu);
		
		JMenuItem menuItem_3 = new JMenuItem("\u67E5\u770B\u8BFE\u7A0B\u4FE1\u606F");
		menu.add(menuItem_3);
		menuItem_3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					File file=new File(ExcelReader.savePath_course);
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					JOptionPane.showMessageDialog(menuItem_3, "文件不存在");
					e.printStackTrace();
				}
			}
			
		});
		
		JMenu menu_help = new JMenu("\u5E2E\u52A9");
		menuBar.add(menu_help);
		
		JMenuItem menuItem_aboutSystem = new JMenuItem("\u5173\u4E8E\u9009\u8BFE\u7CFB\u7EDF1.0");
		menu_help.add(menuItem_aboutSystem);
		menuItem_aboutSystem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					File file=new File(ExcelReader.aboutSystem);
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					JOptionPane.showMessageDialog(menuItem_aboutSystem, "文件不存在");
					e.printStackTrace();
				}
			}
			
		});
		
		JMenuItem menuItem_aboutImport = new JMenuItem("\u5173\u4E8E\u6240\u9700\u6587\u4EF6\u7684\u8BF4\u660E");
		menu_help.add(menuItem_aboutImport);
		menuItem_aboutImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					File file=new File(ExcelReader.input_info_format);
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					JOptionPane.showMessageDialog(menuItem_aboutImport, "文件不存在");
					e.printStackTrace();
				}
			}
			
		});
		
		JMenuItem menuItem_aboutParam = new JMenuItem("\u5173\u4E8E\u53C2\u6570\u7684\u8BBE\u5B9A");
		menu_help.add(menuItem_aboutParam);
		menuItem_aboutParam.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					File file=new File(ExcelReader.about_params);
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					JOptionPane.showMessageDialog(menuItem_aboutParam, "文件不存在");
					e.printStackTrace();
				}
			}
			
		});
		
		JMenuItem menuItem_aboutFailure = new JMenuItem("\u5173\u4E8E\u8FD0\u884C\u5931\u8D25\u7684\u8BF4\u660E");
		menu_help.add(menuItem_aboutFailure);
		
		menuItem_aboutFailure.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				try {
					File file=new File(ExcelReader.aboutFailure);
					java.awt.Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					JOptionPane.showMessageDialog(menuItem_aboutFailure, "文件不存在");
					e.printStackTrace();
				}
			}
			
		});
	}


}
