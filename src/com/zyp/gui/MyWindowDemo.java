package com.zyp.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyp.entity.Student;
import com.zyp.utils.DbUtils;

public class MyWindowDemo {

	/**
	 * f:主体GUI
	 * tf:输入文本框
	 * but:执行按钮
	 * ta:查询结果文本域
	 * d:会话  
	 * okbut:错误信息提示后确定按钮
	 * lab：对象是一个可在容器中放置文本的组件。一个标签只显示一行只读文本。文本可由应用程序更改，但是用户不能直接对其进行编辑。
	 */
	private Frame f;
	private TextField tf;
	private Button but;
	private TextArea ta;

	private Dialog d;
	private Button okbut;
	private Label lab;

	//学生类集合
	static List<Student> students = new ArrayList<Student>();

	public MyWindowDemo() {
		//初始化
		init();
	}

	public void init() {
		// 操作窗体布局
		f = new Frame("作业π");
		f.setBounds(300, 100, 600, 500);
		f.setLayout(new FlowLayout());

		tf = new TextField("输入文件夹路径",60);

		but = new Button("Action");

		ta = new TextArea(25, 70);

		f.add(tf);
		f.add(but);
		f.add(ta);

		// 操作窗体布局结束

		// 提示信息操作
		d = new Dialog(f, "提示信息", true);
		d.setSize(300, 200);
		d.setLocationRelativeTo(null);// 窗体居中
		d.setLayout(new FlowLayout());
		lab = new Label();
		okbut = new Button("Enter!");

		d.add(lab);
		d.add(okbut);
		// 提示信息操作结束

		//事件
		myEvent();

		//显示窗体
		f.setVisible(true);

	}

	private void myEvent() {
		d.addWindowListener(new WindowAdapter() {
			//错误信息会话Gui关闭
			public void windowClosing(WindowEvent e) {
				d.setVisible(false);
			}
		});
		okbut.addActionListener(new ActionListener() {
			//okbutton  确定后关闭
			public void actionPerformed(ActionEvent e) {
				d.setVisible(false);
			}
		});
		
		f.addWindowListener(new WindowAdapter() {
			//程序关闭事件
			public void windowClosing(WindowEvent e) {
				String stop="F:\\项目\\文件计数器\\stopmysql.bat";
				  Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
			        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
			        try {
			            ps = rt.exec(stop);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
			            ps.waitFor();  //等待子进程完成再往下执行。
			        } catch (IOException e1) {
			            e1.printStackTrace();
			        } catch (InterruptedException e1) {
			            // TODO Auto-generated catch block
			            e1.printStackTrace();
			        }
			        int i = ps.exitValue();  //接收执行完毕的返回值
			        if (i == 0) {
			        	System.exit(0);
			        } else {
			            System.out.println("执行失败.");
			        }
			        ps.destroy();  //销毁子进程
			        ps = null;
				
			}
		});

		tf.addActionListener(new ActionListener() {
			//查询执行事件  按enter可执行
			public void actionPerformed(ActionEvent e) {
				showDir();
			}
		});

		but.addActionListener(new ActionListener() {
			//查询执行事件  点击执行按钮可执行
			public void actionPerformed(ActionEvent e) {
				showDir();
			}
		});
	}

	//查询方法
	private void showDir() {
		ta.setText("");
		String dirPath = tf.getText();
		File dir = new File(dirPath);
		
		if (dir.exists() && dir.isDirectory()) {
			String[] files = dir.list();
			int a = files.length;
			ta.append("本文件夹共:" + String.valueOf(a) + "文件(非文件夹)如下:\r\n");
			for (String string : files) {
				ta.append("	"+string + "\r\n");
			}

			//开始判断班级作业提交情况
			int studnetcount = 0;
			for (int i = 0; i < files.length; i++) {
				// 集合迭代器
				Iterator it = students.iterator();
				while (it.hasNext()) {
					Student stu = (Student) it.next();
					if (files[i].contains(stu.getS_name())) {
						stu.setS_flag(true);
					}
				}
			}
			//结束判断班级作业提交情况
			
			for (Student student : students) {
				if(student.isS_flag()){
					studnetcount++;
				}
			}
			
			ta.append("*********************************************************************************\r\n");
			//显示提交人次
			ta.append("1606031班共:" + String.valueOf(students.size()) + "名学生\r\n");
			ta.append("该文件夹共:" + String.valueOf(studnetcount) + "人次交作业\r\n");
			
			//显示未提交学生信息
			int notcount = students.size() - studnetcount;
			ta.append("本次作业未交人数：" + String.valueOf(notcount) + "\r\n");
			for (Student student : students) {
				if(!student.isS_flag()){
					ta.append("	" + student.getS_id() + student.getS_name() + "\r\n");
				}
			}

		} else {
			String info = "您输入的路径://" + dirPath + "://有误请重新输入！";
			lab.setText(info);
			d.setVisible(true);
		}
	}

	public static void sql() {
		DbUtils db = new DbUtils();
		Connection conn = null;
		try {
			// 链接数据库
			conn = db.getConnection();
			// 拿到执行sql的Statement对象
			Statement stmt = conn.createStatement();
			// 执行sql
			String sql = "select * from student";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Student s = new Student();
				s.setS_id(rs.getString("s_id").trim());
				s.setS_name(rs.getString("s_name").trim());
				s.setS_flag(rs.getBoolean("s_flag"));
				students.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void startMysql(){
		try {
			Runtime.getRuntime().exec("startmysql.bat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		
		//打开数据库
		

		String start="F:\\项目\\文件计数器\\startmysql.bat";
		  Runtime rt = Runtime.getRuntime(); //Runtime.getRuntime()返回当前应用程序的Runtime对象
	        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
	        try {
	            ps = rt.exec(start);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
	            ps.waitFor();  //等待子进程完成再往下执行。
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        int i = ps.exitValue();  //接收执行完毕的返回值
	        if (i == 0) {
	        	try {
					Thread.sleep(1800);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	sql();
				new MyWindowDemo();
	        } else {
	            System.out.println("执行失败.");
	        }
	        ps.destroy();  //销毁子进程
	        ps = null;
	}

}
