package gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import Controller.ActivityController;
import Model.Activity;

public class Gantt {
	
	protected JFrame frmGantt;
	private static TaskSeries s1 = new TaskSeries("Scheduled Activities");
	private static Map<Integer,Calendar> map = new LinkedHashMap<Integer,Calendar>();
	
	public Gantt() {
		frmGantt = new JFrame();
		frmGantt.setTitle("Gantt Chart");
		frmGantt.setBounds(100, 100, 800, 412);
		frmGantt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGantt.setResizable(false);
		
		final IntervalCategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

        // add the chart to a panel
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));
        frmGantt.setContentPane(chartPanel);
        
	}
	
	public static IntervalCategoryDataset createDataset() {
		
		
		//retrieve activity list
		List<Activity> list = new ArrayList<Activity>();
		ActivityController ac = new ActivityController();
		list = ac.getActByProjectId(MainWindow.selectedProject.getProject_id());

        for(Activity a : list){
        	//getPredecessors
			ActivityController ac1 = new ActivityController();
			ArrayList<Integer> list1 = new ArrayList<Integer>();
			List<Integer> list2 = ac1.getActPrecedence(a.getActivity_id());
			list1.addAll(list2);
			a.setPredecessors(list1);
        }
        
        Iterator<Activity> it = list.iterator();
        while(it.hasNext()){
        	Activity a = it.next();
        	if(a.getPredecessors().isEmpty()){
        		//start time
        		Date e = MainWindow.selectedProject.getStart_date();
        		Calendar f = Calendar.getInstance();
        		f.setTime(e);
        		
        		//end time
        		Date d = MainWindow.selectedProject.getStart_date();
        		
        		Calendar c = Calendar.getInstance();
        		c.setTime(d);
        		c.add(Calendar.DATE, a.getDuration());
        		
        		//add to graph
        		s1.add(new Task(a.getActivity_name(),
                        new SimpleTimePeriod(date(f.get(Calendar.DAY_OF_MONTH), f.get(Calendar.MONTH) + 1, f.get(Calendar.YEAR)),
                                             date(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)))));
        		
        		it.remove();
        		map.put(a.getActivity_id(), c);
        	}
        }
        
        
        addAct(list, map);

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        

        return collection;
    }
	
	private static void addAct(List<Activity> l, Map<Integer, Calendar> map){

		if(!l.isEmpty()){
			List<Integer> li = new ArrayList<Integer>();		
				
			for(Activity a : l){
				li.add(a.getActivity_id());
			}
			
			Iterator<Activity> it = l.iterator();
			while(it.hasNext()){
				Activity a = it.next();
				ArrayList<Integer> list1 = a.getPredecessors();
				Boolean add = true;
				for(Integer i : list1){
					if(li.contains(i)){
						add = false;
					}

				}
				if(add == true){	
					
					ArrayList<Calendar> lc = new ArrayList<Calendar>();
					for(Integer i : list1){
						if(map.containsKey(i)){
							lc.add(map.get(i));
						}
					}
					
					Calendar max = null;
					for(Calendar c : lc){
						if(max == null || c.after(max)){
							max = c;
						}
					}
					
			        Calendar c = Calendar.getInstance();
					c = (Calendar) max.clone();
			        
					Calendar f = Calendar.getInstance();
					f = (Calendar) max.clone();;
					f.add(Calendar.DATE, a.getDuration());
					
					s1.add(new Task(a.getActivity_name(),
	                        new SimpleTimePeriod(date(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)),
	                                             date(f.get(Calendar.DAY_OF_MONTH), f.get(Calendar.MONTH) + 1, f.get(Calendar.YEAR)))));
					
					
	        		it.remove();
	        		map.put(a.getActivity_id(), f);
				}
				
			}

			
			//recursion
			addAct(l,map);
		}
	}
	
	private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
	
	private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Gantt Chart for Project " + MainWindow.selectedProject.getProject_name(),  // chart title
            "Activity",          // domain axis label
            "Date",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );    

        return chart;    
    }
}
