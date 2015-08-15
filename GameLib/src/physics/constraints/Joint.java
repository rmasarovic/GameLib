package physics.constraints;

import java.awt.Color;
import java.awt.Graphics2D;

import physics.PhysicsEntity;

public abstract class Joint extends Constraint {

	private final PhysicsEntity a;
	private final PhysicsEntity b;

	public Joint(final PhysicsEntity a, final PhysicsEntity b) {
		this.a = a;
		this.b = b;
	}

	public PhysicsEntity getA() {
		return a;
	}

	public PhysicsEntity getB() {
		return b;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(Color.RED);
		final float x1 = getA().center().x;
		final float y1 = getA().center().y;
		final float x2 = getB().center().x;
		final float y2 = getB().center().y;
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
}
