/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api;

import lombok.Getter;

public class Point
{
	@Getter
	protected final int x;

	@Getter
	protected final int y;

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString()
	{
		return "Point{" + "x=" + x + ", y=" + y + '}';
	}

	/**
	 * Find the distance from this point to another point
	 *
	 * @param other
	 * @return
	 */
	public double euclideanDistance(Point other)
	{
		return Math.hypot(getX() - other.getX(), getY() - other.getY());
	}

	public int chebyshevDistance(Point other)
	{
		return Math.max(Math.abs(this.x - other.x), Math.abs(this.y - other.y));
	}

	/**
	 * Moves the Point to the nearest edge if it is outside the area.
	 *
	 * @param minX The west border of the area
	 * @param maxX The east border of the area
	 * @param minY The south border of the area
	 * @param maxY The north border of the area
	 * @return Returns the moved WorldPoint
	 */
	public Point clamp(int minX, int maxX, int minY, int maxY)
	{
		if (x >= minX && x <= maxX && y >= minY && y <= maxY)
		{
			return this;
		}
		return new Point(
			Math.max(Math.min(maxX, x), minX),
			Math.max(Math.min(maxY, y), minY));
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 23 * hash + this.x;
		hash = 23 * hash + this.y;
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Point other = (Point) obj;
		if (this.x != other.x)
		{
			return false;
		}
		if (this.y != other.y)
		{
			return false;
		}
		return true;
	}
}
