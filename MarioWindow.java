/*
	MarioWindow2.java is a nice program that allows you to create
	graphical threaded applications without having to learn specifics
	about java awt and java swing. I made this so you guys
	can concentrate on working on threads rather than graphics.

	To use simply make GameObjects and add() them to MarioWindow.
	See the (commented out) main method.

	v.1.1 - Just a basic hack
	v.1.2 - Learned double buffering! http://www.gamedev.net/page/resources/_/reference/programming/languages/java/java-games-active-rendering-r2418
	v.1.3 - Mouse clicks (thanks Jet Magbanua and Mark Enriquez!) and GIF (thanks Justin Asuncion!)
*/
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import javax.imageio.*;
import java.io.*;

/*
* CHANGES:
* Added keyboardfocusmanager to prevent unresponsiveness of keypresses after mouse click inside the window
* removed interface keylistener
* wired keyboard focus manager to functions from the keylistener interface
 */
public class MarioWindow extends JFrame implements MouseListener, MouseMotionListener {
	Vector<GameObject> gameobjects = new Vector<GameObject>();
	public static int WIDTH = 900;
	public static int HEIGHT = 600;
	public static final int REFRESH_RATE = 20;

	boolean fpsFlag = true;

	Canvas canvas;
	BufferStrategy buffer;
	GraphicsEnvironment ge;
	GraphicsDevice gd;
	GraphicsConfiguration gc;

	BufferedImage bi;

    public MarioWindow() {
		this.setTitle("MarioWindow");        
		this.setIgnoreRepaint(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//this.addKeyListener(this);

		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setSize(WIDTH,HEIGHT);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);

		this.add(canvas);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		// using back buffering
		canvas.createBufferStrategy(2);
		buffer = canvas.getBufferStrategy();

		// getting the graphics configuration
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();

		// creating the back buffer
		bi = gc.createCompatibleImage(WIDTH,HEIGHT);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					keyPressed(e);
				} else if (e.getID() == KeyEvent.KEY_RELEASED) {
					keyReleased(e);
				}
				return false;
			}
		});
    }

	public void showFPS(boolean fpsFlag) {
		this.fpsFlag = fpsFlag;
	}

	public void draw() {
		Random rand = new Random();
		Graphics graphics = null;
		Graphics2D g2d = null;
		Color background = Color.BLACK;

		// Variables for counting frames per seconds
		int fps = 0;
		int frames = 0;
		long totalTime = 0;
		long curTime = System.currentTimeMillis();
		long lastTime = curTime;

		while(true) {
			try {

				// count Frames per second...
				lastTime = curTime;
				curTime = System.currentTimeMillis();
				totalTime += curTime - lastTime;
				if( totalTime > 1000 ) {
					totalTime -= 1000;
					fps = frames;
					frames = 0;
				}
				++frames;

				// clear back buffer
				g2d = bi.createGraphics();
				g2d.setColor(background);
				g2d.fillRect(0,0,1216,704);

				// display frames per second...
				if (fpsFlag) {
					g2d.setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
					g2d.setColor( Color.GREEN );
					g2d.drawString( String.format( "FPS: %s", fps ), 20, 20 );
				}

				// draw stuff
				for (GameObject go : gameobjects) {
					go.paint(g2d);
				}

				// placing the back buffer in front
				graphics = buffer.getDrawGraphics();
				graphics.drawImage(bi,0,0,null);
				if (!buffer.contentsLost()) {
					buffer.show();
				}
				Thread.yield();
			} finally {
				if (graphics != null) {
					graphics.dispose();
				}
				if (g2d != null) {
					g2d.dispose();
				}

			}
		}
	}

    public synchronized void add(GameObject go) {
        gameobjects.add(go);
    }


    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        for (GameObject go : gameobjects) {
            go.keyPressed(key);
        }
    }

    public void keyReleased(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        for (GameObject go : gameobjects) {
            go.keyReleased(key);
        }
    }

	// MouseListener methods (all have to be implemented)
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int b = e.getButton();
		for (GameObject go : gameobjects) {
			go.mouseClicked(x, y, b);
		}
	}
	
	public void mousePressed(MouseEvent e) { 
		int x = e.getX();
		int y = e.getY();
		int b = e.getButton();
		for (GameObject go : gameobjects) {
			go.mousePressed(x, y, b);
		}	
	} 
	
	public void mouseReleased(MouseEvent e) { 
		int x = e.getX();
		int y = e.getY();
		int b = e.getButton();
		for (GameObject go : gameobjects) {
			go.mouseReleased(x, y, b);
		}	
	}	
	
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int b = e.getButton();
		for (GameObject go : gameobjects) {
			go.mouseDragged(x, y, b);
		}			
	}
	
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int b = e.getButton();
		for (GameObject go : gameobjects) {
			go.mouseMoved(x, y, b);
		}			
	}
	
	// unfortunately I don't know how to implement this since
	// these two methods are called when the mouse enters MarioWindow itself
	// and not the gameobjects
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	
	

    public void startGame() {
        System.out.println("MarioWindow: Starting all game objects...");
        for (GameObject go : gameobjects) {
			Thread t = new Thread(go);
            t.start();
        }
        System.out.println("MarioWindow: Starting game...");
        this.draw();
    }

	public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) { }
    }

	public static BufferedImage getImage(String filename) {
		try {
			File fp = new File(filename);
			BufferedImage img = ImageIO.read(fp);
			return img;
		} catch (Exception e) {
			System.out.println("Unable to read file: " + filename);
			return null;
		}
	}
	
	public static ImageIcon getGif(String filename) {
		try {
			ImageIcon icon = new ImageIcon(filename);
			return icon;
		} catch (Exception e) {
			System.out.println("Unable to read file: " + filename);
			return null;
		}
	}

//	public static void main(String args[]) {
//		MarioWindow2 w1 = new MarioWindow2();
//		for (int i = 0; i < 50; i++) {
//			MyGameObject2 g2 = new MyGameObject2();
//			w1.add(g2);
//		}
//		w1.startGame();
//
//	}
}
