package cn.ac.iie.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtils {

	/**
	 * 将文件下载到本地
	 * 
	 * @param remoteFilePath
	 * @param f
	 * @return
	 */
	public static boolean downloadFile(String remoteFilePath, File f) {
		URL urlfile = null;
		HttpURLConnection httpUrl = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			urlfile = new URL(remoteFilePath);
			httpUrl = (HttpURLConnection) urlfile.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(f));
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			bos.flush();
			bis.close();
			httpUrl.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 将字符串写入文件中，每次写入都会覆盖之前的内容
	 * 
	 * @param filePath
	 *            文件路径
	 * @param content
	 *            写入内容
	 */
	public static void saveFile(String filePath, String content) {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.error("创建" + filePath + "出错:" + e.getMessage());
			}
		}

		// 写入文件
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (IOException e) {
			log.error("写入" + filePath + "文件出错：" + e.getMessage());
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}

			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * 删除文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		boolean flag = false;
		File file = new File(path);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	
	/**
	 * 删除文件夹
	 * @param path
	 * @return
	 */
	public static boolean deleteDir(String path) {
		File dirFile = new File(path);

		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}

		boolean flag = true;
		//删除文件夹下的所有文件(包括子目录)  
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				flag = deleteDir(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}

		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 设置excel文件响应（解决火狐中文名问题）
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param filename 文件名
	 * @throws Exception
	 */
	public static void setResponse(HttpServletRequest request, HttpServletResponse response, String filename) throws Exception {
		String userAgent = request.getHeader("User-Agent");
		if(userAgent.toLowerCase().contains("firefox")){
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		}else{
			filename = URLEncoder.encode(filename, "UTF-8");
		}
		//response.setContentType(request.getServletContext().getMimeType(filename)); 		
		response.setContentType("application/x-msdownload"); 		
		response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", filename));
	}

	public static List<String> readTxt(String path) {
		ArrayList<String> arrayList = new ArrayList<>();
		try {
			File file = new File(path);
			InputStreamReader input = new InputStreamReader(new FileInputStream(file));
			BufferedReader bf = new BufferedReader(input);
			// 按行读取字符串
			String str;
			while ((str = bf.readLine()) != null) {
				arrayList.add(str);
			}
			bf.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayList;
		
	}

	public static String readFile(String path){
		StringBuilder result = new StringBuilder();
		try{
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				result.append(System.lineSeparator()+s);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public static void addToTxt(String filename, String content){
		FileWriter fw = null;
		try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f=new File(filename);
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(content);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	 
}
