package com.volley.libirary.http.request;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
/**
 *
 * @author 于堂刚
 */
public class MyMultipartEntity extends MultipartEntity {

	private OnProgressListener mOnProgressListener;

	public MyMultipartEntity(OnProgressListener listener) {
		super();
		mOnProgressListener = listener;
	}

	public MyMultipartEntity(HttpMultipartMode mode, String boundary,
			Charset charset, OnProgressListener listener) {
		super(mode, boundary, charset);
		mOnProgressListener = listener;
	}

	public MyMultipartEntity(HttpMultipartMode mode, OnProgressListener listener) {
		super(mode);
		mOnProgressListener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, mOnProgressListener));

	}

	public static class CountingOutputStream extends FilterOutputStream {

		private OnProgressListener mOnProgressListener;
		private long mTransferred;

		public CountingOutputStream(OutputStream out,
				OnProgressListener listener) {
			super(out);
			mOnProgressListener = listener;
			mTransferred = 0;
		}

		@Override
		public void write(byte[] buffer, int offset, int length)
				throws IOException {
			out.write(buffer, offset, length);

			mTransferred += length;
			mOnProgressListener.transferred(mTransferred);
		}

		@Override
		public void write(int oneByte) throws IOException {
			out.write(oneByte);
			mTransferred++;
			mOnProgressListener.transferred(mTransferred);
		}

	}

	public interface OnProgressListener {

		public abstract void transferred(long size);
	}

}
