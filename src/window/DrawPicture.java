package window;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import GA.WholeSolution;

public class DrawPicture extends JFrame{
	WholeSolution ws;
	public DrawPicture(WholeSolution ws,int type){
		this.ws=ws;
		CategoryDataset datasetMemory=createDataset(type);
        createChart(datasetMemory,type);
	}

	public  CategoryDataset createDataset(int type) {
		   DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
		   String series="学习效率曲线";   //曲线名称
			
			if(type==0){
				series="学习效率曲线";
			}else if(type==1){
				series="前后八周课时曲线";
			}else if(type==2){
				series="每日课时曲线";
			}
			int size=ws.historyDay.size();
			System.out.println(ws.historyDay.size());
		   Double [][]array3 = new Double[size][3];
		   DecimalFormat df = new DecimalFormat(".00");
		  
		   for(int i=0;i<3;i++) {
			   for(int j=0;j<size;j++) {
				   switch(i) {
				   case 0:
					   array3[j][i]=Double.valueOf(df.format(ws.historyPoint.get(j)));
					   break;
				   case 1:
					   array3[j][i]=Double.valueOf(df.format(ws.historyHalfTerm.get(j)));
					   break;
				   case 2:
					   array3[j][i]=Double.valueOf(df.format(ws.historyDay.get(j)));
					   
					   break;
				   default:
						   break;
				   }
			   }
		   }
		   
		   for(int i=0;i<size;i++){
	          dataset.addValue(Double.valueOf(array3[i][type]), series,String.valueOf(i+1));  //参数分别是纵轴值、曲线名称、横轴值
		   }
	               
	       return dataset;
	}
	
	public void createChart(CategoryDataset dataset,int type) {

		
		JFreeChart chart = ChartFactory.createLineChart("学习效率曲线", "迭代轮数",
				"最高分", dataset, PlotOrientation.VERTICAL, true, true,
				true);
		if(type==0){
		    chart = ChartFactory.createLineChart("学习效率曲线", "迭代轮数",
				"最高分", dataset, PlotOrientation.VERTICAL, true, true,
				true);
		}else if(type==1){
			chart = ChartFactory.createLineChart("前后八周曲线", "迭代轮数",
					"最高分", dataset, PlotOrientation.VERTICAL, true, true,
					true);
		}else if(type==2){
			chart = ChartFactory.createLineChart("每日学时曲线", "迭代轮数",
					"最高分", dataset, PlotOrientation.VERTICAL, true, true,
					true);
		}
		
 
		CategoryPlot cp = chart.getCategoryPlot();
		cp.setBackgroundPaint(ChartColor.WHITE); // 背景色设置
		cp.setRangeGridlinePaint(ChartColor.GRAY); // 网格线色设置
		cp.setDomainGridlinePaint(ChartColor.BLACK);
		cp.setNoDataMessage("没有数据");
		// 数据轴属性部分
		NumberAxis rangeAxis = (NumberAxis) cp.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true); // 自动生成
		rangeAxis.setUpperMargin(0.20);
		rangeAxis.setLabelAngle(Math.PI / 2.0);
		rangeAxis.setAutoRange(false);
 
		// 数据渲染部分 主要是对折线做操作
		CategoryPlot plot = chart.getCategoryPlot();
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		
		renderer.setBaseItemLabelsVisible(false);// 设置曲线是否显示数据点
		// 设置曲线显示各数据点的值
		renderer.setSeriesPaint(0, Color.black); // 设置折线的颜色
 
		renderer.setBaseShapesFilled(true);
 
		renderer.setBaseItemLabelsVisible(false);
 
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
 
		ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
 
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
 
		
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 15));
		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		CategoryAxis domainAxis = plot.getDomainAxis();
		/**
		 * x 轴座标文字
		 */
		domainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 11));
		/**
		 * x轴标题文字
		 */
		domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		/**
		 * y轴上的文字
		 */
		numberaxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 12));
		/**
		 * y坐标
		 */
		numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
 

		JPanel jPanel = new ChartPanel(chart);
		JFrame frame = new JFrame("演化图");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(jPanel);
		frame.setBounds(800, 450, 800, 600);
		frame.setVisible(true);
 
	} 
	
}
