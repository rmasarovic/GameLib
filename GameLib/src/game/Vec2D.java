package game;

import java.awt.geom.Point2D;

/**
 * An extension to the relatively impotent java.awt.geom.Point2D.Double, Vector2D allows mathematical manipulation of
 * 2-component vectors.
 *
 * @author Jadrian Miles
 * @version 20031122
 */
public class Vec2D {

	public static final Vec2D ZERO = new Vec2D();
	public final float x;
	public final float y;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.geom.Point2D.Double#Point2D.Double()
	 */
	public Vec2D(final float x, final float y) {
		assert !Double.isNaN(x) && !Double.isNaN(y);
		assert Double.isFinite(x) && Double.isFinite(y);

		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.geom.Point2D.Float#Point2D.Float()
	 */
	public Vec2D() {
		this(0, 0);
	}

	/**
	 * Copy constructor
	 */
	public Vec2D(final Vec2D v) {
		this(v.x, v.y);
	}

	/**
	 * @return the angle (argument) of the vector in polar coordinates in the range [-pi, pi]
	 */
	public float getTheta() {
		return (float) Math.atan2(y, x);
	}

	/** The sum of the vector and rhs */
	public Vec2D plus(final Vec2D rhs) {
		return new Vec2D(x + rhs.x, y + rhs.y);
	}

	/** The difference of the vector and rhs: this - rhs */
	public Vec2D minus(final Vec2D rhs) {
		return new Vec2D(x - rhs.x, y - rhs.y);
	}

	/** Product of the vector and scalar */
	public Vec2D multiply(final float scalar) {
		return new Vec2D(scalar * x, scalar * y);
	}

	/** Product of the vector and scalar */
	public Vec2D divide(final float scalar) {
		return new Vec2D(x / scalar, y / scalar);
	}

	/** Dot product of the vector and rhs */
	public float dotProduct(final Vec2D rhs) {
		return x * rhs.x + y * rhs.y;
	}

	/**
	 * Since Vector2D works only in the x-y plane, (u x v) points directly along the z axis. This function returns the
	 * value on the z axis that (u x v) reaches.
	 *
	 * @return signed magnitude of (this x rhs)
	 */
	public float crossProduct(final Vec2D rhs) {
		return x * rhs.y - y * rhs.x;
	}

	/** Product of components of the vector: compenentProduct( <x y>) = x*y. */
	public float componentProduct() {
		return x * y;
	}

	/** Componentwise product: <this.x*rhs.x, this.y*rhs.y> */
	public Vec2D componentwiseProduct(final Vec2D rhs) {
		return new Vec2D(x * rhs.x, y * rhs.y);
	}

	/**
	 * An alias for getR()
	 *
	 * @return the length of this
	 */
	public float length() {
		return 1 / invSqrt(x * x + y * y);
	}

	private static float invSqrt(float x) {
		final float xhalf = 0.5f * x;
		int i = java.lang.Float.floatToIntBits(x);
		i = 0x5f3759df - (i >> 1);
		x = java.lang.Float.intBitsToFloat(i);
		x = x * (1.5f - xhalf * x * x);
		return x;
	}

	/**
	 * Returns a new vector with the same direction as the vector but with length 1, except in the case of zero vectors,
	 * which return a copy of themselves.
	 */
	public Vec2D unitVector() {
		final float length = length();
		if (length != 0) {
			return new Vec2D(x / length, y / length);
		}
		return new Vec2D(0, 0);
	}

	/**
	 * @return Standard string representation of a vector: "<x, y>"
	 */
	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Vec2D other = (Vec2D) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		return true;
	}

	public Point2D toPoint() {
		return new Point2D.Float(x, y);
	}
}