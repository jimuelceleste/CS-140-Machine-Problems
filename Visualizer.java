import java.awt.*;
import java.awt.image.*;
import java.util.List;

public class Visualizer extends GameObject {
	public final int WIDTH = MarioWindow.WIDTH;
	public final int HEIGHT = MarioWindow.HEIGHT;
	public final int MARGIN = 40;
	public final int WIDTH4 = MarioWindow.WIDTH/4;
	public final int HEIGHT4 = MarioWindow.HEIGHT/4;
	public final int POS_RIGHT_PANE = MarioWindow.WIDTH/2 + MARGIN + 30;
	public final Color RED = new Color(246, 46, 27);
	public final Color COLOR_BG_1 = new Color(38, 38, 38);
	public final Color COLOR_BG_2 = new Color(13, 13, 13);
	public final Color COLOR_LINES = new Color(89, 89, 89);
	public final Color COLOR_LABELS = new Color(217, 217, 217);
	public final Font FONT_RIGHT_LABELS = new Font("Tahoma", Font.PLAIN, 12);
	public final Font FONT_LEFT_LABELS = new Font("Tahoma", Font.PLAIN, 20);
	public final Font FONT_INFO_DAY = new Font("Tahoma", Font.BOLD, 80);
	public final Font FONT_INFO_SCHEDULER = new Font("Tahoma", Font.BOLD, 30);
	public final Font FONT_INFO_SCHEDULER_SMALL = new Font("Tahoma", Font.BOLD, 20);
	public final Font FONT_INFO_RUNNING = new Font("Tahoma", Font.BOLD, 45);
	public final Font FONT_INFO_WAITING = new Font("Tahoma", Font.BOLD, 40);
	public final Font FONT_INFO_READY = new Font("Tahoma", Font.PLAIN, 20);
	public final String[] labels = {"EMPEROR | ", "SUB-COMMANDERS", "READY QUEUE", "REMARKS"};
	public final BufferedImage messageIcon = MarioWindow.getImage("icons//messageIcon.png");
	public final BufferedImage locationIcon = MarioWindow.getImage("icons//locationIcon.png");
	public List<City> ready_list = null;
	public List<City> waiting_list = null;
	public String ready = "";
	public String waiting = "";
	public String day = ""; 
	public String running = "";
	public String scheduler = "";
	public String remarks = "";
	public SharedVars sv;
	
	Visualizer(SharedVars sv) {
		this.sv = sv;
		char role = sv.role.toUpperCase().charAt(0);
		switch (role) {
			case 'A': labels[0] += "Attack"; break;
			case 'S': labels[0] += "Siege"; break;
			case 'D': labels[0] += "Defend"; break;
			case 'P': labels[0] += "Parley"; break;
		}
	}

	private void paintBackground(Graphics2D g) {
		g.setColor(COLOR_BG_1);
	    g.fillRect(WIDTH4, 0, WIDTH, HEIGHT);
		g.setColor(COLOR_BG_2);
		g.fillRect(0, 0, WIDTH4, HEIGHT);
	}
	
	private void paintLines(Graphics2D g) {
		g.setColor(COLOR_LINES);
		for (int line = 0; line < 4; line++) {
			g.drawLine((WIDTH4+MARGIN), (HEIGHT4*line + MARGIN), (WIDTH - MARGIN), (HEIGHT4*line + MARGIN));
		}
		g.drawLine(WIDTH/2 + MARGIN, (HEIGHT4 + MARGIN + 20), WIDTH/2 + MARGIN, HEIGHT4*2 + 10);
		g.drawLine(WIDTH/2 + MARGIN, (HEIGHT4*2 + MARGIN + 20), WIDTH/2 + MARGIN, HEIGHT4*3 + 10);
	}
	
	private void paintLabels(Graphics2D g) {
		g.setColor(COLOR_LABELS);
		g.setFont(FONT_RIGHT_LABELS);
		for (int label = 0; label < 4; label++) {
			g.drawString(labels[label], (WIDTH4 + MARGIN), (HEIGHT4*label + MARGIN - 5));
		}
		g.setFont(FONT_LEFT_LABELS);
		g.drawString("Day", MARGIN, HEIGHT/5 + 30);
		g.drawString("Scheduler", MARGIN, HEIGHT/5 + 3*MARGIN);
	}
	
	private void paintIcons(Graphics2D g) {
		g.drawImage(locationIcon, WIDTH4 + MARGIN + 20, MARGIN + 30, null);
		g.drawImage(messageIcon, WIDTH4 + MARGIN + 20, HEIGHT4*3 + MARGIN + 30, null);
	}
	
	private void paintInfo(Graphics2D g) {
		// Day
		g.setColor(RED);
		g.setFont(FONT_INFO_DAY);
		g.drawString(day, MARGIN, HEIGHT/5);		
		
		// Scheduler Used
		g.setColor(Color.WHITE);
		if (scheduler.equals("Round Robin")) {
			g.setFont(FONT_INFO_SCHEDULER_SMALL);
		} else {
			g.setFont(FONT_INFO_SCHEDULER);
		}
		g.drawString(scheduler, MARGIN, HEIGHT/5 + 3*MARGIN - 30);		
		
		// Running
		g.setFont(FONT_INFO_RUNNING);
		g.drawString(running, WIDTH4 + 2*MARGIN + 30, MARGIN + 65);		
		
		// Waiting and Ready counts
		g.setFont(FONT_INFO_WAITING);
		g.drawString(waiting, WIDTH4 + 2*MARGIN, HEIGHT4 + 3*MARGIN);		
		g.drawString(ready, WIDTH4 + 2*MARGIN, HEIGHT4*2 + 3*MARGIN);	

		// Waiting and Ready Cities
		g.setFont(FONT_INFO_READY);
		if (waiting_list != null) {
			for (int i = 0; i < waiting_list.size(); i++) {
				// Time
				g.setColor(RED);
				g.drawString(String.valueOf(waiting_list.get(i).task.duration), POS_RIGHT_PANE, HEIGHT4 + 2*MARGIN + i*20);
				// Name
				g.setColor(Color.WHITE);
				g.drawString(waiting_list.get(i).toString(), POS_RIGHT_PANE + 40, HEIGHT4 + 2*MARGIN + i*20);
			}
		}

		if (ready_list != null) {
			for (int i = 0; i < ready_list.size(); i++) {
				// Time
				g.setColor(RED);
				g.drawString(String.valueOf(ready_list.get(i).task.duration), POS_RIGHT_PANE, HEIGHT4*2 + 2*MARGIN + i*20);
				// Name
				g.setColor(Color.WHITE);
				g.drawString(ready_list.get(i).toString(), POS_RIGHT_PANE + 40, HEIGHT4*2 + 2*MARGIN + i*20);
			}
		}

		// Remarks
		g.drawString(remarks, WIDTH4 + MARGIN + 60, HEIGHT4*3 + MARGIN + 47);	
	}

	@Override
	public void paint(Graphics2D g) {
		paintBackground(g);
		paintLines(g);
		paintLabels(g);
		paintIcons(g);
		paintInfo(g);
	}

	@Override
	public void run() {
		while (true) {
			MarioWindow.delay(20);
			synchronized (sv) {
				day = sv.day;
				running = sv.running;
				remarks = sv.remarks;
				scheduler = sv.schedulerName;
				waiting = sv.waiting;
				ready = sv.ready;
				waiting_list = sv.waiting_list;
				ready_list = sv.ready_list;
			}
		}
	}
}