/**
 * Copyright (c) 2013-2014 Robert Maupin
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 *    1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 
 *    2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 
 *    3. This notice may not be removed or altered from any source
 *    distribution.
 */
package org.csdgn.maru;

import java.nio.ByteOrder;

/**
 * I am tired of rewriting all these methods when I need them.
 * 
 * Why use a complicated ByteBuffer when you can get exactly what you want with
 * this ByteConverter?
 * 
 * All those who work with binary files may appreciate this class.
 * 
 * @author Robert Maupin
 * 
 */
public class ByteConverter {	
	public static final ByteConverter LITTLE_ENDIAN = new ByteConverter(ByteOrder.LITTLE_ENDIAN);
	public static final ByteConverter BIG_ENDIAN = new ByteConverter(ByteOrder.BIG_ENDIAN);

	/**
	 * For reference:<br/>
	 * Big Endian = Most Significant Bit First<br />
	 * Little Endian = Least Significant Bit First
	 */
	private ByteOrder order;

	public ByteConverter() {
		order = ByteOrder.nativeOrder();
	}

	public ByteConverter(ByteOrder order) {
		this.order = order;
	}

	public byte[] fromDouble(double value) {
		return fromLong(Double.doubleToRawLongBits(value));
	}

	public byte[] fromDoubleArray(double[] value) {
		return fromDoubleArray(value, 0, value.length);
	}

	public byte[] fromDoubleArray(double[] value, int start) {
		return fromDoubleArray(value, start, value.length - start);
	}

	public byte[] fromDoubleArray(double[] value, int start, int length) {
		int end = length + start;
		byte[] output = new byte[length << 3];
		for (int i = start; i < end; ++i) {
			int n = i - start << 3;
			byte[] buf = fromDouble(value[i]);
			output[n] = buf[0];
			output[n + 1] = buf[1];
			output[n + 2] = buf[2];
			output[n + 3] = buf[3];
			output[n + 4] = buf[4];
			output[n + 5] = buf[5];
			output[n + 6] = buf[6];
			output[n + 7] = buf[7];
		}
		return output;
	}

	public byte[] fromFloat(float value) {
		return fromInt(Float.floatToRawIntBits(value));
	}

	public byte[] fromFloatArray(float[] value) {
		return fromFloatArray(value, 0, value.length);
	}

	public byte[] fromFloatArray(float[] value, int start) {
		return fromFloatArray(value, start, value.length - start);
	}

	public byte[] fromFloatArray(float[] value, int start, int length) {
		int end = length + start;
		byte[] output = new byte[length << 2];
		for (int i = start; i < end; ++i) {
			int n = i - start << 2;
			byte[] buf = fromFloat(value[i]);
			output[n] = buf[0];
			output[n + 1] = buf[1];
			output[n + 2] = buf[2];
			output[n + 3] = buf[3];
		}
		return output;
	}

	public byte[] fromInt(int value) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return new byte[] { ubyte(value, 24), ubyte(value, 16), ubyte(value, 8), ubyte(value, 0) };
		}
		return new byte[] { ubyte(value, 0), ubyte(value, 8), ubyte(value, 16), ubyte(value, 24) };
	}

	public byte[] fromIntArray(int[] value) {
		return fromIntArray(value, 0, value.length);
	}

	public byte[] fromIntArray(int[] value, int start) {
		return fromIntArray(value, start, value.length - start);
	}

	public byte[] fromIntArray(int[] value, int start, int length) {
		int end = length + start;
		byte[] output = new byte[length << 2];
		for (int i = start; i < end; ++i) {
			int n = i - start << 2;
			byte[] buf = fromInt(value[i]);
			output[n] = buf[0];
			output[n + 1] = buf[1];
			output[n + 2] = buf[2];
			output[n + 3] = buf[3];
		}
		return output;
	}

	public byte[] fromLong(long value) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return new byte[] { ubyte(value, 56), ubyte(value, 48), ubyte(value, 40), ubyte(value, 32), ubyte(value, 24), ubyte(value, 16),
					ubyte(value, 8), ubyte(value, 0) };
		}
		return new byte[] { ubyte(value, 0), ubyte(value, 8), ubyte(value, 16), ubyte(value, 24), ubyte(value, 32), ubyte(value, 40),
				ubyte(value, 48), ubyte(value, 56) };
	}

	public byte[] fromLongArray(long[] value) {
		return fromLongArray(value, 0, value.length);
	}

	public byte[] fromLongArray(long[] value, int start) {
		return fromLongArray(value, start, value.length - start);
	}

	public byte[] fromLongArray(long[] value, int start, int length) {
		int end = length + start;
		byte[] output = new byte[length << 3];
		for (int i = start; i < end; ++i) {
			int n = i - start << 3;
			byte[] buf = fromLong(value[i]);
			output[n] = buf[0];
			output[n + 1] = buf[1];
			output[n + 2] = buf[2];
			output[n + 3] = buf[3];
			output[n + 4] = buf[4];
			output[n + 5] = buf[5];
			output[n + 6] = buf[6];
			output[n + 7] = buf[7];
		}
		return output;
	}

	public byte[] fromShort(short value) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return new byte[] { ubyte(value, 8), ubyte(value, 0) };
		}
		return new byte[] { ubyte(value, 0), ubyte(value, 8) };
	}

	public byte[] fromShortArray(short[] value) {
		return fromShortArray(value, 0, value.length);
	}

	public byte[] fromShortArray(short[] value, int start) {
		return fromShortArray(value, start, value.length - start);
	}

	public byte[] fromShortArray(short[] value, int start, int length) {
		int end = length + start;
		byte[] output = new byte[length << 1];
		for (int i = start; i < end; ++i) {
			int n = i - start << 1;
			byte[] buf = fromShort(value[i]);
			output[n] = buf[0];
			output[n + 1] = buf[1];
		}
		return output;
	}

	public void setByteOrder(ByteOrder order) {
		this.order = order;
	}

	public double toDouble(byte[] array) {
		return Double.longBitsToDouble(toLong(array));
	}

	public double toDouble(byte[] array, int start) {
		return Double.longBitsToDouble(toLong(array, start));
	}

	public double[] toDoubleArray(byte[] array) {
		return toDoubleArray(array, 0, array.length);
	}

	public double[] toDoubleArray(byte[] array, int start) {
		return toDoubleArray(array, start, array.length - start);
	}

	public double[] toDoubleArray(byte[] array, int start, int length) {
		double[] output = new double[length - start >> 3];
		for (int i = 0; i < output.length; ++i) {
			output[i] = toDouble(array, start + (i << 3));
		}
		return output;
	}

	public float toFloat(byte[] array) {
		return Float.intBitsToFloat(toInt(array));
	}

	public float toFloat(byte[] array, int start) {
		return Float.intBitsToFloat(toInt(array, start));
	}

	public float[] toFloatArray(byte[] array) {
		return toFloatArray(array, 0, array.length);
	}

	public float[] toFloatArray(byte[] array, int start) {
		return toFloatArray(array, start, array.length - start);
	}

	public float[] toFloatArray(byte[] array, int start, int length) {
		float[] output = new float[length - start >> 2];
		for (int i = 0; i < output.length; ++i) {
			output[i] = toFloat(array, start + (i << 2));
		}
		return output;
	}

	public int toInt(byte[] array) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return uint(array[0], 24) | uint(array[1], 16) | uint(array[2], 8) | uint(array[3], 0);
		}
		return uint(array[3], 24) | uint(array[2], 16) | uint(array[1], 8) | uint(array[0], 0);
	}

	public int toInt(byte[] array, int start) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return uint(array[start], 24) | uint(array[start + 1], 16) | uint(array[start + 2], 8) | uint(array[start + 3], 0);
		}
		return uint(array[start + 3], 24) | uint(array[start + 2], 16) | uint(array[start + 1], 8) | uint(array[start], 0);
	}

	public int[] toIntArray(byte[] array) {
		return toIntArray(array, 0, array.length);
	}

	public int[] toIntArray(byte[] array, int start) {
		return toIntArray(array, start, array.length - start);
	}

	public int[] toIntArray(byte[] array, int start, int length) {
		int[] output = new int[length - start >> 2];
		for (int i = 0; i < output.length; ++i) {
			output[i] = toInt(array, start + (i << 2));
		}
		return output;
	}

	public long toLong(byte[] array) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return ulong(array[0], 56) | ulong(array[1], 48) | ulong(array[2], 40) | ulong(array[3], 32) | ulong(array[4], 24)
					| ulong(array[5], 16) | ulong(array[6], 8) | ulong(array[7], 0);
		}
		return ulong(array[7], 56) | ulong(array[6], 48) | ulong(array[5], 40) | ulong(array[4], 32) | ulong(array[3], 24)
				| ulong(array[2], 16) | ulong(array[1], 8) | ulong(array[0], 0);
	}

	public long toLong(byte[] array, int start) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return ulong(array[start], 56) | ulong(array[start + 1], 48) | ulong(array[start + 2], 40) | ulong(array[start + 3], 32)
					| ulong(array[start + 4], 24) | ulong(array[start + 5], 16) | ulong(array[start + 6], 8) | ulong(array[start + 7], 0);
		}
		return ulong(array[start + 7], 56) | ulong(array[start + 6], 48) | ulong(array[start + 5], 40) | ulong(array[start + 4], 32)
				| ulong(array[start + 3], 24) | ulong(array[start + 2], 16) | ulong(array[start + 1], 8) | ulong(array[start], 0);
	}

	public long[] toLongArray(byte[] array) {
		return toLongArray(array, 0, array.length);
	}

	public long[] toLongArray(byte[] array, int start) {
		return toLongArray(array, start, array.length - start);
	}

	public long[] toLongArray(byte[] array, int start, int length) {
		long[] output = new long[length - start >> 3];
		for (int i = 0; i < output.length; ++i) {
			output[i] = toLong(array, start + (i << 3));
		}
		return output;
	}

	public short toShort(byte[] array) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return (short) (uint(array[0], 8) | uint(array[1], 0));
		}
		return (short) (uint(array[1], 8) | uint(array[0], 0));
	}

	public short toShort(byte[] array, int start) {
		if (order == ByteOrder.BIG_ENDIAN) {
			return (short) (uint(array[start], 8) | uint(array[start + 1], 0));
		}
		return (short) (uint(array[start + 1], 8) | uint(array[start], 0));
	}

	public short[] toShortArray(byte[] array) {
		return toShortArray(array, 0, array.length);
	}

	public short[] toShortArray(byte[] array, int start) {
		return toShortArray(array, start, array.length - start);
	}

	public short[] toShortArray(byte[] array, int start, int length) {
		short[] output = new short[length - start >> 1];
		for (int i = 0; i < output.length; ++i) {
			output[i] = toShort(array, start + (i << 1));
		}
		return output;
	}

	private byte ubyte(long b, int rshf) {
		return (byte) (b >>> rshf);
	}

	private int uint(byte b, int lsft) {
		return (b & 0xFF) << lsft;
	}

	private long ulong(byte b, long lsft) {
		return (b & 0xFFL) << lsft;
	}
}