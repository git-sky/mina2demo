package cn.com.sky.mina2.simulator;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Device {

	private IoSession session;

	private ConnectFuture connFuture;

	private NioSocketConnector connector;

	private Logger logger = LoggerFactory.getLogger(Device.class);

	private Timer timer = null;

	JFrame frame;

	JLabel label1;

	JTextArea text0;

	JComboBox combo1;

	JLabel label2;

	JComboBox combo2;

	JButton button1;

	JLabel label3;

	JTextArea text1;

	JLabel label4;

	JTextArea text2;

	JButton button2;

	JCheckBox cbox;

	JTextArea text3;

	JButton button3;

	JButton button4;

	JLabel label5;
	JLabel label6;

	JTextArea text5;
	JTextArea text6;
	JTextArea text7;
	JTextArea text8;
	JTextArea text9;
	JTextArea text10;

	JComboBox combo7;
	JLabel label7;

	JComboBox combo8;
	JLabel label8;

	JComboBox combo9;

	JLabel label9;
	JTextArea text11;
	JTextArea text12;
	JTextArea text13;

	JLabel label11;

	JCheckBox cbox2;

	// 室内位置点
	JLabel label15;
	JComboBox combo15;

	// 时间
	JLabel label16;
	JComboBox combo16;

	// 模拟室内轨迹1
	JButton button5;

	// 模拟室内轨迹2
	JButton button6;

	// 模拟室外轨迹
	JButton button7;

	private static JTextArea text4;

	private double startX = 115.246963;

	private double startY = 39.058023;

	private double endX = 116.999140;

	private double endY = 40.488258;

	private int stage = 0;

	private double lng = startX;

	private double lat = startY;

	public void connect(String ip) throws Throwable {

		String HOST = "172.169.0.77";
		int PORT = 60004;

		String ips[] = ip.trim().split(":");
		if (ips.length >= 2) {
			HOST = ips[0];
			PORT = Integer.valueOf(ips[1]);
		}

		// String HOST = "219.142.87.104";
		// String HOST = "127.0.0.1";
		// String HOST = "211.100.96.3";
		// String HOST = "172.169.0.77";
		// String HOST = "218.245.5.231";
		// String HOST = "192.168.169.121";
		// int PORT = 60004;

		int CONNECT_TIMEOUT_MILLS = 30000;

		int TIMOUT_CHECK_INTERVAL = 15000;

		connector = new NioSocketConnector();

		// 连接超时设置
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT_MILLS);
		// 连接超时检查间隔
		connector.setConnectTimeoutCheckInterval(TIMOUT_CHECK_INTERVAL);

		DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();

		filterChain.addLast("codec",
				new ProtocolCodecFilter(new CodecFactory()));
		// 日志过滤器
		filterChain.addLast("logger", new LoggingFilter());

		TestHandler handler = new TestHandler();
		connector.setHandler(handler);

		while (true) {
			try {
				connFuture = connector
						.connect(new InetSocketAddress(HOST, PORT));
				connFuture.awaitUninterruptibly();
				session = connFuture.getSession();
				break;
			} catch (RuntimeIoException e) {
				Thread.sleep(30000);
			}
		}

	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 */
	public void sendMessage(Object msg) {
		if (session != null)
			session.write(msg);
		System.out.println(msg.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 设置Log4j
		DailyRollingFileAppender appender = new DailyRollingFileAppender();
		appender.setFile("device" + ".log");
		appender.setDatePattern("'.'yyyy-MM-dd");
		appender.setName("ServerAppender");

		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("%d{dd MMM yyyy HH:mm:ss,SSS} %-4r [%t] %-5p %c %x - %m%n");
		appender.setLayout(layout);
		appender.activateOptions();

		BasicConfigurator.configure(appender);

		final Device client = new Device();
		try {
			client.connect("218.245.5.231:60004");
			// client.connect("116.213.171.106:60004");
			client.show();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void displayMsg(String msg) {
		text4.setText(msg);
		System.out.println("已设置");
	}

	private void show() {
		frame = new JFrame("设备模拟器");
		Font f = new Font("la", 0, 18);
		label1 = new JLabel("设备:");
		label1.setFont(f);

		combo1 = new JComboBox();

		// 添加设备
		File inputfile = new File("sn.txt");
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(inputfile);
			br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				combo1.addItem(s);
			}
		} catch (Exception e2) {
			System.out.println("sn.txt不存在。。。。。");
		} finally {
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		combo1.addItem("012345678900002-OBD");
		combo1.addItem("012345678910002-OBD");
		combo1.setSelectedItem("012345678900002-OBD");

		label2 = new JLabel("消息类型：");
		label2.setFont(f);

		// 添加报警
		combo2 = new JComboBox();
		combo2.addItem("位置");
		combo2.addItem("进围栏报警");
		combo2.addItem("出围栏报警");
		combo2.addItem("超速报警");
		combo2.addItem("低电报警");
		combo2.addItem("SOS报警");
		combo2.addItem("拔出报警");
		combo2.addItem("移动报警");
		combo2.addItem("移动报警-T2设备");
		combo2.addItem("开锁报警-状态位切换");
		combo2.addItem("开箱报警-状态位切换");
		combo2.addItem("离开报警-状态位切换");
		combo2.addItem("跌落报警");
		combo2.addItem("没电关机报警");
		combo2.addItem("关机键关机报警");
		combo2.addItem("休眠关机报警");

		combo2.setSelectedItem("位置");
		label3 = new JLabel("经度：");
		label3.setFont(f);
		text1 = new JTextArea("116.500400", 1, 10);
		text1.setFont(f);
		text1.setLineWrap(false);
		text1.setColumns(10);
		label4 = new JLabel("纬度：");
		label4.setFont(f);
		text2 = new JTextArea("39.979700", 1, 10);
		text2.setFont(f);
		text2.setLineWrap(false);
		button1 = new JButton("send");
		button2 = new JButton("自动模拟");
		cbox = new JCheckBox();
		text3 = new JTextArea("text", 1, 25);
		text3.setFont(f);
		text3.setLineWrap(false);
		button3 = new JButton("发送文本");
		button3.setEnabled(false);

		button4 = new JButton("发送室内位置点");

		label5 = new JLabel("收到消息:");
		label5.setFont(f);

		label6 = new JLabel("发送消息:");
		label6.setFont(f);

		text4 = new JTextArea("收到...", 1, 30);
		text4.setFont(f);
		text4.setLineWrap(false);

		text5 = new JTextArea("46000010dd0ab1_153_0", 1, 20);
		text5.setFont(f);
		text5.setLineWrap(false);
		text6 = new JTextArea("46000010dd0020_149", 1, 20);
		text6.setFont(f);
		text6.setLineWrap(false);
		text7 = new JTextArea("46000010dd17b0_147", 1, 20);
		text7.setFont(f);
		text7.setLineWrap(false);
		text8 = new JTextArea("46000010dd7ffe_147", 1, 20);
		text8.setFont(f);
		text8.setLineWrap(false);
		text9 = new JTextArea("46000010dd0009_145", 1, 20);
		text9.setFont(f);
		text9.setLineWrap(false);
		text10 = new JTextArea("46000010dd0021_143", 1, 20);
		text10.setFont(f);
		text10.setLineWrap(false);

		label11 = new JLabel("室内信标数据：");
		label11.setFont(f);

		text11 = new JTextArea("|F0E00F13A064|30", 1, 40);
		text11.setFont(f);
		text11.setLineWrap(false);
		text12 = new JTextArea("|F0E00F0CA00A|28", 1, 40);
		text12.setFont(f);
		text12.setLineWrap(false);
		text13 = new JTextArea("|F0E00F0CA029|20", 1, 40);
		text13.setFont(f);
		text13.setLineWrap(false);

		label7 = new JLabel("连接服务器：");
		label7.setFont(f);

		combo7 = new JComboBox();
		combo7.addItem("172.169.0.77:60004");
		combo7.addItem("218.245.5.231:60004");

		combo7.setSelectedItem("218.245.5.231:60004");

		File ipfile = new File("ip.txt");
		FileReader fread;
		BufferedReader br2 = null;
		try {
			fread = new FileReader(ipfile);
			br2 = new BufferedReader(fread);
			String s;
			while ((s = br2.readLine()) != null) {
				combo7.addItem(s);
			}
		} catch (Exception e2) {
			System.out.println("ip.txt不存在。。。。。");
		} finally {
			try {
				br2.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		label8 = new JLabel("定位：");
		label8.setFont(f);

		combo8 = new JComboBox();
		combo8.addItem("A");
		combo8.addItem("V");
		combo8.setSelectedItem("A");

		label9 = new JLabel("定位类型:");
		label9.setFont(f);
		combo9 = new JComboBox();
		combo9.addItem("GPS");
		combo9.addItem("Indoor");
		combo9.setSelectedItem("GPS");

		label15 = new JLabel("室内位置点：");
		label15.setFont(f);
		combo15 = new JComboBox();

		File wfile = new File("weizhi.txt");
		FileReader wfread;
		BufferedReader br15 = null;
		try {
			wfread = new FileReader(wfile);
			br15 = new BufferedReader(wfread);
			String s;
			while ((s = br15.readLine()) != null) {
				combo15.addItem(s);
			}
		} catch (Exception e2) {
			System.out.println("weizhi.txt不存在。。。。。");
		} finally {
			try {
				br15.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		label16 = new JLabel("时间：");
		label16.setFont(f);
		combo16 = new JComboBox();
		File wfile16 = new File("time.txt");
		FileReader wfread16;
		BufferedReader br16 = null;
		try {
			wfread16 = new FileReader(wfile16);
			br16 = new BufferedReader(wfread16);
			String s;
			while ((s = br16.readLine()) != null) {
				combo16.addItem(s);
			}
		} catch (Exception e2) {
			System.out.println("time.txt不存在。。。。。");
		} finally {
			try {
				br16.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		button5 = new JButton("模拟室内轨迹1层");
		button6 = new JButton("模拟室内轨迹2层");
		button7 = new JButton("模拟gps轨迹");

		lng = Double.valueOf(text1.getText());
		lat = Double.valueOf(text2.getText());

		// 为一般按钮添加动作监听器
		final List<String> imei = new LinkedList<String>();
		final List<String> alarm = new LinkedList<String>();
		final List<String> ip = new LinkedList<String>();
		final List<String> pos = new LinkedList<String>();
		final List<String> ptype = new LinkedList<String>();// 定位类型
		final List<String> inpoint = new LinkedList<String>();// 室内位置点
		final List<String> uptime = new LinkedList<String>();// 上传时间

		imei.add("012345678900002-OBD");
		alarm.add("AUT");
		ip.add("218.245.5.231:60004");
		pos.add("A");
		ptype.add("GPS");
		inpoint.add("F0E00F13A064");
		uptime.add("0");

		combo1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					imei.clear();
					imei.add(e.getItem().toString());
					try {
						session.close(true);
						connect(ip.get(0));
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		combo2.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					alarm.clear();
					String s = e.getItem().toString();

					if (s.equals("进围栏报警")) {
						alarm.add("BNDIN");
					} else if (s.equals("出围栏报警")) {
						alarm.add("BNDOUT");
					} else if (s.equals("超速报警")) {
						alarm.add("SPDHI");
					} else if (s.equals("低电报警")) {
						alarm.add("LPD");
					} else if (s.equals("位置")) {
						alarm.add("AUT");
					} else if (s.equals("SOS报警")) {
						alarm.add("SOS");
					} else if (s.equals("拔出报警")) {
						alarm.add("EPD");
					} else if (s.equals("移动报警")) {
						alarm.add("VIB");
					} else if (s.equals("移动报警-T2设备")) {
						alarm.add("VIB_T2");
					} else if (s.equals("开锁报警-状态位切换")) {
						alarm.add("OPENLOCK");
					} else if (s.equals("开箱报警-状态位切换")) {
						alarm.add("OPENBOX");
					} else if (s.equals("离开报警-状态位切换")) {
						alarm.add("LEAVEOUT");
					} else if (s.equals("跌落报警")) {
						alarm.add("FALLOFF");
					} else if (s.equals("没电关机报警")) {
						alarm.add("SHUTNOP");
					} else if (s.equals("关机键关机报警")) {
						alarm.add("SHUTDOWN");
					} else if (s.equals("休眠关机报警")) {
						alarm.add("SHUTSLEEP");
					}
				}
			}
		});

		combo7.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ip.clear();
					ip.add(e.getItem().toString());
					try {
						session.close(true);
						connect(ip.get(0));
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		combo8.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					pos.clear();
					pos.add(e.getItem().toString());
				}
			}
		});

		combo9.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ptype.clear();
					ptype.add(e.getItem().toString());
				}
			}
		});

		combo15.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					inpoint.clear();
					inpoint.add(e.getItem().toString());
				}
			}
		});

		combo16.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					uptime.clear();
					uptime.add(e.getItem().toString());
				}
			}
		});

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				String snTxt = imei.get(0);
				String alarm_type = alarm.get(0);
				String pos_type = pos.get(0);
				String pt = ptype.get(0);

				String lngStr = "0";
				if (!text1.getText().equals(""))
					lngStr = text1.getText();
				String latStr = "0";
				String cell = "";
				if (!text2.getText().equals(""))
					latStr = text2.getText();
				// if (!text0.getText().equals(""))
				// snTxt = text0.getText();
				if (!text5.getText().equals(""))
					cell = text5.getText();
				if (!text6.getText().equals(""))
					cell += "," + text6.getText();
				if (!text7.getText().equals(""))
					cell += "," + text7.getText();
				if (!text8.getText().equals(""))
					cell += "," + text8.getText();
				if (!text9.getText().equals(""))
					cell += "," + text9.getText();
				if (!text10.getText().equals(""))
					cell += "," + text10.getText();
				double lng = 0;
				double lat = 0;
				try {
					lng = Double.parseDouble(lngStr);
					lat = Double.parseDouble(latStr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				String devType = snTxt.substring(16, snTxt.length());
				String sn = snTxt.substring(0, 15);

				String wifi = "@";
				if (!text11.getText().equals(""))
					wifi += text11.getText();
				if (!text12.getText().equals(""))
					wifi += "," + text12.getText();
				if (!text13.getText().equals(""))
					wifi += "," + text13.getText();

				if ("Indoor".equals(pt)) {
					cell += wifi;
				}

				int nDaysBefore = 0;

				if ("OPENLOCK".equals(alarm_type)) {
					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 16, nDaysBefore));
				} else if ("OPENBOX".equals(alarm_type)) {
					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 1, nDaysBefore));
				} else if ("LEAVEOUT".equals(alarm_type)) {
					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 4, nDaysBefore));
				} else if ("VIB_T2".equals(alarm_type)) {

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 1, nDaysBefore));

				} else {
					sendMessage(MessageUtils.getMessage(devType, sn,
							alarm_type, lng, lat, cell, pos_type, 1,
							nDaysBefore));
				}

				// System.out.println(MessageUtils.getMessage(devType, sn,
				// alarm_type, lng, lat, cell, pos_type));

			}
		});

		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				stage = 0;
				String snTxt = imei.get(0);

				String pt = ptype.get(0);

				final String devType = snTxt.substring(16, snTxt.length());
				final String sn = snTxt.substring(0, 15);
				final String cell = "";

				final int nDaysBefore = 0;

				if ("GPS".equals(pt)) {

					if (button2.getText().equals("自动模拟")) {
						button2.setText("停止模拟");
						if (timer != null) {
							timer.cancel();
							timer.purge();
						}
						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask() {

							@Override
							public void run() {

								if (stage == 0) {
									lat -= 0.00050;
									lng += 0.00100;
									stage++;
								} else if (stage == 1) {
									lat -= 0.00050;
									lng -= 0.00100;
									stage = 0;
								}

								// if (stage == 0) {
								// lat -= 0.005000;
								// lng += 0.010000;
								// stage++;
								// } else if (stage == 1) {
								// lat -= 0.005000;
								// lng -= 0.010000;
								// stage = 0;
								// }

								// if (stage == 0) {
								// lat += 0.005;
								//
								// if (Math.abs(lat - endY) < 0.000005) {
								// stage++;
								// lat = endY;
								// }
								// } else if (stage == 1) {
								// lng += 0.005;
								//
								// if (Math.abs(lng - endX) < 0.000005) {
								// stage++;
								// lng = endX;
								// }
								// } else if (stage == 2) {
								// lat -= 0.005;
								// if (Math.abs(lng - endX) < 0.000005) {
								// stage++;
								// lat = startY;
								// }
								// } else if (stage == 3) {
								// lng += 0.005;
								// if (Math.abs(lng - startX) < 0.000005) {
								// stage = 0;
								// lng = startX;
								// }
								// }
								//
								// stage ++;
								System.out.println(MessageUtils.getMessage(
										devType, sn, "AUT", lng, lat, cell, 1,
										nDaysBefore));
								sendMessage(MessageUtils.getMessage(devType,
										sn, "AUT", lng, lat, cell, 1,
										nDaysBefore));
							}
						}, 0, 5000);
					} else {
						if (timer != null) {
							timer.cancel();
							timer.purge();
						}
						button2.setText("自动模拟");
					}
				} else if ("Indoor".equals(pt)) {

					if (button2.getText().equals("自动模拟")) {
						button2.setText("停止模拟");
						if (timer != null) {
							timer.cancel();
							timer.purge();
						}
						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask() {

							@Override
							public void run() {

								// Random r = new Random();
								// int a = r.nextInt(100) + 10;
								// int b = r.nextInt(100) + 10;
								// int c = r.nextInt(100) + 10;
								//
								// if (stage == 0) {
								// lat -= 0.005000;
								// lng += 0.010000;
								// stage++;
								//
								// } else if (stage == 1) {
								// lat -= 0.005000;
								// lng -= 0.010000;
								// stage = 0;
								// }

								String wifi = "@";

								List<String> list = new ArrayList<String>();
								// list.add("|F0E00F0CA021|30");
								// list.add("|F0E00F0CA02F|28");
								// list.add("|F0E00F0CA035|20");
								// list.add("|F0E00F0CA03D|30");

								File ipfile = new File("beacon.txt");
								FileReader fread;
								BufferedReader br2 = null;
								try {
									fread = new FileReader(ipfile);
									br2 = new BufferedReader(fread);
									String s;
									while ((s = br2.readLine()) != null) {
										list.add(s);
									}
								} catch (Exception e2) {
									System.out.println("beacon.txt不存在。。。。。");
								} finally {
									try {
										br2.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}

								// list.add("|F0E00F03A014|28");
								// list.add("|F0E00F03A01C|20");

								Collections.shuffle(list);

								int count = 0;
								for (String s : list) {
									System.out.println(s);
									wifi += s + ",";

									count++;
									if (count == 3) {
										break;
									}
								}

								wifi = wifi.substring(0, wifi.length() - 1);

								// String wifi = "@|F0E00F03A030|" + a +
								// ",|F0E00F03A028|" + b
								// + ",|F0E00F03A020|" + c + "";

								// System.out.println(MessageUtils.getMessage(
								// devType, sn, "AUT", lng, lat, cell+wifi, 1));
								sendMessage(MessageUtils.getMessage(devType,
										sn, "AUT", lng, lat, cell + wifi, 1,
										nDaysBefore));
							}
						}, 0, 10000);
					} else {
						if (timer != null) {
							timer.cancel();
							timer.purge();
						}
						button2.setText("自动模拟");
					}

				}
			}
		});

		cbox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent ce) {
				if (cbox.isSelected()) {
					button3.setEnabled(true);
				} else {
					button3.setEnabled(false);
				}

			}
		});

		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = null;
				String str = text3.getText();

				// #359074050079138##00#0100#AUT#1###000000#000000.000##

				// if (str.startsWith("$$"))
				// msg = str.substring(0, str.length() - 1) + "\r\n";
				msg = str;
				if (msg != null && !msg.equals(""))
					sendMessage(msg);

			}
		});

		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				String snTxt = imei.get(0);
				String alarm_type = alarm.get(0);
				String pos_type = pos.get(0);
				String pt = ptype.get(0);

				String inpoint_txt = inpoint.get(0);
				String uptime_txt = uptime.get(0);

				String lngStr = "0";
				if (!text1.getText().equals(""))
					lngStr = text1.getText();
				String latStr = "0";
				String cell = "";
				if (!text2.getText().equals(""))
					latStr = text2.getText();
				// if (!text0.getText().equals(""))
				// snTxt = text0.getText();
				if (!text5.getText().equals(""))
					cell = text5.getText();
				if (!text6.getText().equals(""))
					cell += "," + text6.getText();
				if (!text7.getText().equals(""))
					cell += "," + text7.getText();
				if (!text8.getText().equals(""))
					cell += "," + text8.getText();
				if (!text9.getText().equals(""))
					cell += "," + text9.getText();
				if (!text10.getText().equals(""))
					cell += "," + text10.getText();
				double lng = 0;
				double lat = 0;
				try {
					lng = Double.parseDouble(lngStr);
					lat = Double.parseDouble(latStr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				String devType = snTxt.substring(16, snTxt.length());
				String sn = snTxt.substring(0, 15);

				String wifi = "@";
				// if (!text11.getText().equals(""))
				// wifi += text11.getText();
				// if (!text12.getText().equals(""))
				// wifi += "," + text12.getText();
				// if (!text13.getText().equals(""))
				// wifi += "," + text13.getText();

				String[] spoint = inpoint_txt.split(",");

				wifi += "|" + spoint[0] + "|10";

				if ("Indoor".equals(pt)) {
					cell += wifi;
				}

				int nDaysBefore = Integer.parseInt(uptime_txt);

				if ("OPENLOCK".equals(alarm_type)) {
					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 16, nDaysBefore));
				} else if ("OPENBOX".equals(alarm_type)) {
					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 1, nDaysBefore));
				} else if ("LEAVEOUT".equals(alarm_type)) {
					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 4, nDaysBefore));
				} else if ("VIB_T2".equals(alarm_type)) {

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 0, nDaysBefore));

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					sendMessage(MessageUtils.getMessage(devType, sn, "AUT",
							lng, lat, cell, pos_type, 1, nDaysBefore));

				} else {
					sendMessage(MessageUtils.getMessage(devType, sn,
							alarm_type, lng, lat, cell, pos_type, 1,
							nDaysBefore));
				}

			}
		});

		button5.addActionListener(new ActionListener() {

			int index = 0;

			@Override
			public void actionPerformed(ActionEvent ae) {
				stage = 0;
				String snTxt = imei.get(0);

				String pt = ptype.get(0);

				final String devType = snTxt.substring(16, snTxt.length());
				final String sn = snTxt.substring(0, 15);
				final String cell = "";

				final int nDaysBefore = 0;

				final List<String> list = new ArrayList<String>();
				File ipfile = new File("good.txt");
				FileReader fread;
				BufferedReader br2 = null;
				try {
					fread = new FileReader(ipfile);
					br2 = new BufferedReader(fread);
					String s;
					while ((s = br2.readLine()) != null) {
						list.add(s);
					}
				} catch (Exception e2) {
					System.out.println("good.txt不存在。。。。。");
				} finally {
					try {
						br2.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				// if ("GPS".equals(pt)) {
				//
				// if (button2.getText().equals("自动模拟")) {
				// button2.setText("停止模拟");
				// if (timer != null) {
				// timer.cancel();
				// timer.purge();
				// }
				// timer = new Timer();
				// timer.scheduleAtFixedRate(new TimerTask() {
				//
				// @Override
				// public void run() {
				//
				// if (stage == 0) {
				// lat -= 0.00050;
				// lng += 0.00100;
				// stage++;
				// } else if (stage == 1) {
				// lat -= 0.00050;
				// lng -= 0.00100;
				// stage = 0;
				// }
				//
				// System.out.println(MessageUtils.getMessage(
				// devType, sn, "AUT", lng, lat, cell, 1,
				// nDaysBefore));
				// sendMessage(MessageUtils.getMessage(devType,
				// sn, "AUT", lng, lat, cell, 1,
				// nDaysBefore));
				// }
				// }, 0, 5000);
				// } else {
				// if (timer != null) {
				// timer.cancel();
				// timer.purge();
				// }
				// button2.setText("自动模拟");
				// }
				// } else if ("Indoor".equals(pt)) {

				if (button5.getText().equals("模拟室内轨迹1层")) {
					button5.setText("停止模拟室内轨迹1层");
					if (timer != null) {
						timer.cancel();
						timer.purge();
					}
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {

						@Override
						public void run() {

							String wifi = "@";

							if (index < list.size()) {
								String[] spoint = list.get(index).split(",");
								index += 1;
								wifi += "|" + spoint[0] + "|10";

								// wifi = wifi.substring(0, wifi.length() - 1);

								sendMessage(MessageUtils.getMessage(devType,
										sn, "AUT", lng, lat, cell + wifi, 1,
										nDaysBefore));
							}
						}
					}, 0, 15000);
				} else {
					if (timer != null) {
						timer.cancel();
						timer.purge();
					}
					button5.setText("模拟室内轨迹1层");
				}

			}
			// }
		});

		button6.addActionListener(new ActionListener() {

			int index = 0;

			@Override
			public void actionPerformed(ActionEvent ae) {
				String snTxt = imei.get(0);
				String pt = ptype.get(0);
				final String devType = snTxt.substring(16, snTxt.length());
				final String sn = snTxt.substring(0, 15);
				final String cell = "";
				final int nDaysBefore = 0;
				final List<String> list = new ArrayList<String>();
				File ipfile = new File("good2.txt");
				FileReader fread;
				BufferedReader br2 = null;
				try {
					fread = new FileReader(ipfile);
					br2 = new BufferedReader(fread);
					String s;
					while ((s = br2.readLine()) != null) {
						list.add(s);
					}
				} catch (Exception e2) {
					System.out.println("good2.txt不存在。。。。。");
				} finally {
					try {
						br2.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				// if ("GPS".equals(pt)) {
				//
				// if (button2.getText().equals("自动模拟")) {
				// button2.setText("停止模拟");
				// if (timer != null) {
				// timer.cancel();
				// timer.purge();
				// }
				// timer = new Timer();
				// timer.scheduleAtFixedRate(new TimerTask() {
				//
				// @Override
				// public void run() {
				//
				// if (stage == 0) {
				// lat -= 0.00050;
				// lng += 0.00100;
				// stage++;
				// } else if (stage == 1) {
				// lat -= 0.00050;
				// lng -= 0.00100;
				// stage = 0;
				// }
				//
				// System.out.println(MessageUtils.getMessage(
				// devType, sn, "AUT", lng, lat, cell, 1,
				// nDaysBefore));
				// sendMessage(MessageUtils.getMessage(devType,
				// sn, "AUT", lng, lat, cell, 1,
				// nDaysBefore));
				// }
				// }, 0, 5000);
				// } else {
				// if (timer != null) {
				// timer.cancel();
				// timer.purge();
				// }
				// button2.setText("自动模拟");
				// }
				// } else if ("Indoor".equals(pt)) {

				if (button6.getText().equals("模拟室内轨迹2层")) {
					button6.setText("停止模拟室内轨迹2层");
					if (timer != null) {
						timer.cancel();
						timer.purge();
					}
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {

						@Override
						public void run() {

							String wifi = "@";

							if (index < list.size()) {
								String[] spoint = list.get(index).split(",");
								index += 1;
								wifi += "|" + spoint[0] + "|10";

								// wifi = wifi.substring(0, wifi.length() - 1);

								sendMessage(MessageUtils.getMessage(devType,
										sn, "AUT", lng, lat, cell + wifi, 1,
										nDaysBefore));
							}
						}
					}, 0, 15000);
				} else {
					if (timer != null) {
						timer.cancel();
						timer.purge();
					}
					button6.setText("模拟室内轨迹2层");
				}

			}
			// }
		});
		
		
		
		button7.addActionListener(new ActionListener() {

			int index = 0;

			@Override
			public void actionPerformed(ActionEvent ae) {
				String snTxt = imei.get(0);
				String pt = ptype.get(0);
				final String devType = snTxt.substring(16, snTxt.length());
				final String sn = snTxt.substring(0, 15);
				final String cell = "";
				final int nDaysBefore = 0;
				final List<String> list = new ArrayList<String>();
				File ipfile = new File("gps.txt");
				FileReader fread;
				BufferedReader br2 = null;
				try {
					fread = new FileReader(ipfile);
					br2 = new BufferedReader(fread);
					String s;
					while ((s = br2.readLine()) != null) {
						list.add(s);
					}
				} catch (Exception e2) {
					System.out.println("gps.txt不存在。。。。。");
				} finally {
					try {
						br2.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				// if ("GPS".equals(pt)) {
				//
				// if (button2.getText().equals("自动模拟")) {
				// button2.setText("停止模拟");
				// if (timer != null) {
				// timer.cancel();
				// timer.purge();
				// }
				// timer = new Timer();
				// timer.scheduleAtFixedRate(new TimerTask() {
				//
				// @Override
				// public void run() {
				//
				// if (stage == 0) {
				// lat -= 0.00050;
				// lng += 0.00100;
				// stage++;
				// } else if (stage == 1) {
				// lat -= 0.00050;
				// lng -= 0.00100;
				// stage = 0;
				// }
				//
				// System.out.println(MessageUtils.getMessage(
				// devType, sn, "AUT", lng, lat, cell, 1,
				// nDaysBefore));
				// sendMessage(MessageUtils.getMessage(devType,
				// sn, "AUT", lng, lat, cell, 1,
				// nDaysBefore));
				// }
				// }, 0, 5000);
				// } else {
				// if (timer != null) {
				// timer.cancel();
				// timer.purge();
				// }
				// button2.setText("自动模拟");
				// }
				// } else if ("Indoor".equals(pt)) {

				if (button7.getText().equals("模拟gps轨迹")) {
					button7.setText("停止模拟gps轨迹");
					if (timer != null) {
						timer.cancel();
						timer.purge();
					}
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {

						@Override
						public void run() {

							String wifi = "@";

							if (index < list.size()) {
								String[] spoint = list.get(index).split(",");
								index += 1;

								// wifi = wifi.substring(0, wifi.length() - 1);

								sendMessage(MessageUtils.getMessage(devType,
										sn, "AUT", Double.valueOf(spoint[1]),Double.valueOf(spoint[0]), cell + wifi, 1,
										nDaysBefore));
							}
						}
					}, 0, 15000);
				} else {
					if (timer != null) {
						timer.cancel();
						timer.purge();
					}
					button7.setText("模拟gps轨迹");
				}

			}
			// }
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new java.awt.FlowLayout(FlowLayout.LEADING));
		frame.getContentPane().add(label1);
		frame.getContentPane().add(combo1);
		// frame.getContentPane().add(text0);
		frame.getContentPane().add(label2);
		frame.getContentPane().add(combo2);
		frame.getContentPane().add(label3);
		frame.getContentPane().add(text1);
		frame.getContentPane().add(label4);
		frame.getContentPane().add(text2);
		frame.getContentPane().add(button1);
		frame.getContentPane().add(button2);

		frame.getContentPane().add(text5);
		frame.getContentPane().add(text6);
		frame.getContentPane().add(text7);
		frame.getContentPane().add(text8);
		frame.getContentPane().add(text9);
		frame.getContentPane().add(text10);

		frame.getContentPane().add(label11);
		frame.getContentPane().add(text11);
		frame.getContentPane().add(text12);
		frame.getContentPane().add(text13);

		frame.getContentPane().add(label6);
		frame.getContentPane().add(text3);
		frame.getContentPane().add(button3);
		frame.getContentPane().add(cbox);

		frame.getContentPane().add(label5);
		frame.getContentPane().add(text4);

		frame.getContentPane().add(label7);
		frame.getContentPane().add(combo7);

		// 定位
		frame.getContentPane().add(label8);
		frame.getContentPane().add(combo8);

		// 定位类型
		frame.getContentPane().add(label9);
		frame.getContentPane().add(combo9);

		frame.getContentPane().add(label15);
		frame.getContentPane().add(combo15);

		frame.getContentPane().add(label16);
		frame.getContentPane().add(combo16);

		frame.getContentPane().add(button4);
		frame.getContentPane().add(button5);

		frame.getContentPane().add(button6);
		frame.getContentPane().add(button7);

		frame.setSize(600, 450);
		frame.setVisible(true);

	}
}
