package window;
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

 /*
  * 准备做动态进度条，还未完成
  */
@SuppressWarnings("serial")
public class Progress extends JFrame implements Runnable {
 
	// 定义加载窗口大小
	public static final int LOAD_WIDTH = 455;
	public static final int LOAD_HEIGHT = 295;
	// 获取屏幕窗口大小
	public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	// 定义进度条组件
	public JProgressBar progressbar;
	// 定义标签组件
 
	// 构造函数
	public Progress() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
    	setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(800, 450, 350, 60);
        setTitle("生成课表中....");
        getContentPane().setLayout(null);
		// 创建进度条
		progressbar = new JProgressBar();
		// 显示当前进度值信息
		progressbar.setStringPainted(true);
		// 设置进度条边框不显示
		progressbar.setBorderPainted(false);
		// 设置进度条的前景色
		progressbar.setForeground(new Color(0, 210, 40));
		// 设置进度条的背景色
		progressbar.setBackground(new Color(188, 190, 194));
		progressbar.setBounds(0,0,350,60);
		// 添加组件
		 getContentPane().add(progressbar);

 
	}
 
	public static void main(String[] args) {
		Progress t = new Progress();
		new Thread(t).start();
	}
 
	@Override
	public void run() {
		int i;
		for (i = 0; i < 100; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			progressbar.setValue(i);
		}
		if(i==100)
		JOptionPane.showMessageDialog(this, "加载完成");
		
		dispose();
 
	}
 
}

