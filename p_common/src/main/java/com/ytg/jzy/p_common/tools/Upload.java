package com.ytg.jzy.p_common.tools;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Upload {
	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 *            访问的服务器URL
	 * @param params
	 *            普通参数
	 * @param files
	 *            文件参数
	 * @return
	 * @throws IOException
	 */
	public static String postFiles(String actionUrl,
			Map<String, Object> params, Map<String, File> files,
			String filename, String servertype)
			throws IOException {

		String BOUNDARY = "--------------et567z";
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		StringBuilder sb2 = null;// 存放上传之后的返回结果
try {
	URL uri = new URL(actionUrl);
	HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
	conn.setReadTimeout(60000); // 缓存的最长时间
	conn.setDoInput(true);// 允许输入
	conn.setDoOutput(true);// 允许输出
	conn.setUseCaches(false); // 不允许使用缓存
	conn.setRequestMethod("POST");
	conn.setRequestProperty("connection", "keep-alive");
	conn.setRequestProperty("Charsert", "UTF-8");
	conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
			+ ";boundary=" + BOUNDARY);


		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;

		// 发送文件数据
		int res = 0;
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// name是post中传参的键 filename是文件的名称
				sb1.append("Content-Disposition: form-data; name=\""
						+ servertype + "\"; filename=\"" + filename + "\""
						+ LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				long totalsize = file.getValue().getTotalSpace();
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			res = conn.getResponseCode();
			if (res == 200) {

				try {
					in = conn.getInputStream();
				} catch (Exception e) {
				}

				int ch;
				sb2 = new StringBuilder();
				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
				return sb2.toString();
			}
			outStream.close();
			conn.disconnect();
		}
		
		return res + "";
		
	} catch (Exception e) {
		e.printStackTrace();
	}
return null;
	}

	
}
