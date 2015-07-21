package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import physics.GameEntity;
import physics.Material;
import physics.collision.CircleShape;
import physics.collision.Collisions;
import physics.collision.RectShape;
import draw.DrawingPanel;

public class Test extends DrawingPanel {

	private static JPanel panel;
	private final List<GameEntity> objects = new ArrayList<>();

	public Test(final JPanel panel) {
		super(60, 120, panel, Color.WHITE);

		final GameEntity ob = new GameEntity();

		ob.setMaterial(Material.STEEL);
		ob.shape = new RectShape(new Vec2D(0, 750), new Vec2D(760, 800));
		ob.setMass(GameEntity.INFINITE_MASS);
		ob.velocity = new Vec2D();
		objects.add(ob);

		for (int i = 0; i < 5; i++) {
			for (int j = i; j < 5; j++) {
				final GameEntity o = new GameEntity();
				o.setMaterial(Material.STEEL);
				final int x = i * 40 - j * 20 + 200;
				final int y = j * 40 + 100;
				o.shape = new RectShape(new Vec2D(x, y), new Vec2D(x + 40, y + 40));
				o.setMass(((RectShape) o.shape).area());
				o.velocity = new Vec2D();
				objects.add(o);
			}
			final GameEntity o = new GameEntity();
			o.setMaterial(Material.STEEL);
			final int radius = i * 3;
			o.shape = new CircleShape(new Vec2D(225, i * radius), radius);
			o.setMass(radius * radius);
			o.velocity = new Vec2D(41, 1);
			objects.add(o);
		}
	}

	public static void main(final String[] args) throws HeadlessException, InvocationTargetException, InterruptedException {
		panel = new JPanel();

		SwingUtilities.invokeAndWait(() -> {
			final JFrame frame = new JFrame();
			panel.setPreferredSize(new Dimension(1000, 1000));
			frame.add(panel);

			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
		new Test(panel).run();

	}

	@Override
	public void draw(final Graphics g) {
		for (final GameEntity object : objects) {
			if (object.shape instanceof RectShape) {
				final int x = (int) ((RectShape) object.shape).min.x;
				final int y = (int) ((RectShape) object.shape).min.y;
				final int width = (int) (((RectShape) object.shape).max.x - ((RectShape) object.shape).min.x);
				final int height = (int) (((RectShape) object.shape).max.y - ((RectShape) object.shape).min.y);
				if (!object.sleeping) {
					g.setColor(new Color(50, 100, 200));
				} else {
					g.setColor(new Color(200, 200, 200));
				}
				g.fillRect(x, y, width, height);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, width, height);
			} else if (object.shape instanceof CircleShape) {
				final int radius = (int) ((CircleShape) object.shape).radius;
				final int x = (int) ((CircleShape) object.shape).center.x - radius;
				final int y = (int) ((CircleShape) object.shape).center.y - radius;
				if (!object.sleeping) {
					g.setColor(new Color(200, 100, 50));
				} else {
					g.setColor(new Color(200, 200, 200));
				}
				g.fillOval(x, y, radius * 2, radius * 2);
				g.setColor(Color.BLACK);
				g.drawOval(x, y, radius * 2, radius * 2);
			}
		}
		g.setColor(Color.RED);
		g.drawString("Entities: " + objects.size(), 10, 15);
		g.drawString("FPS: " + getCurrentFPS(), 10, 30);
		g.drawString("UPS: " + getCurrentUPS(), 10, 45);
	}

	@Override
	public void processInput(final Queue<KeyEvent> keyEvents, final Queue<EventPair<MouseEvent, MouseEventType>> mouseEvents,
			final Queue<MouseWheelEvent> mouseWheelEvents2) {
		final Random rand = new Random();
		for (final EventPair<MouseEvent, MouseEventType> e : mouseEvents) {
			if (e.type != MouseEventType.CLICK) {
				continue;
			}

			final GameEntity o = new GameEntity();
			o.setMaterial(Material.STEEL);
			final int radius = rand.nextInt(20) + 5;
			o.shape = new CircleShape(new Vec2D(e.event.getX(), e.event.getY()), radius);
			o.setMass(radius * radius);
			o.velocity = new Vec2D();
			objects.add(o);
		}
	}

	@Override
	public void update(final float dt) {
		int count = 0;
		final Vec2D gravity = new Vec2D(0, .98f);
		for (int i = 0; i < objects.size(); i++) {
			final GameEntity a = objects.get(i);

			if (!a.sleeping) {
				// remove if out of map
				if (a.center().y > panel.getHeight()) {
					objects.remove(i);
					i--;
					continue;
				}
				// apply gravity
				if (a.getMass() != GameEntity.INFINITE_MASS) {
					a.applyForce(gravity.divide(a.getInvMass()));
				}
				a.update(dt);
			}
			// check collisions
			for (int j = i + 1; j < objects.size(); j++) {
				final GameEntity b = objects.get(j);
				if (b.sleeping && a.sleeping) {
					continue;
				}
				count++;
				if (Collisions.isColliding(a, b)) {
					Collisions.fixCollision(a, b);
				}
			}
			if (GameEntity.SLEEPING_ENABLED) {
				checkSleep(a);
			}
		}
		System.out.println(count);
	}

	private static void checkSleep(final GameEntity a) {
		if (a.velocity.x < GameEntity.SLEEP_THRESHOLD && a.velocity.x > -GameEntity.SLEEP_THRESHOLD //
				&& a.velocity.y < GameEntity.SLEEP_THRESHOLD && a.velocity.y > -GameEntity.SLEEP_THRESHOLD) {
			if (a.framesStill > GameEntity.FRAMES_STILL_TO_SLEEP) {
				a.sleeping = true;
			} else {
				a.framesStill++;
			}
		} else {
			a.sleeping = false;
			a.framesStill = 0;
		}
	}
}
