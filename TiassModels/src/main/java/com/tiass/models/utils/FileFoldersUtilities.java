package com.tiass.models.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class FileFoldersUtilities {

	public static boolean createFolder(String path, String FolderName) throws Exception {
		 
		boolean created = false;
		File folder = new File(path, FolderName);
		while (!created) {
			if (folder.exists() && folder.isDirectory()) {		 
				created = true; 
			} else {
				created = folder.mkdir(); 
			}
		}
		 
		return created;
	}

	public static boolean deleteFolder(String path, String FolderName) throws Exception {
		boolean created = false;
		File folder = new File(path, FolderName);
		while (folder.exists() && folder.isDirectory()) {
			folder.delete();
		}
		return created;
	}

	public static String wichMediaMimeType(File f) {
		
		String mimetypeimg = "";
		String mimetypevideo = "";
		try {// byte[] contentsfile =
				// IOUtils.toByteArray(item.getInputStream());
			mimetypeimg = Magic.getMagicMatch(f, false).getMimeType();
		} catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
			e.printStackTrace(System.out);
		}
		try {
 
			Path path = Paths.get(f.getAbsolutePath());
			mimetypevideo = Files.probeContentType(path);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		
		if (mimetypeimg != null && mimetypeimg.startsWith("image") && (mimetypeimg.endsWith("png") || mimetypeimg.endsWith("jpeg") || mimetypeimg.endsWith("jpg") || mimetypeimg.endsWith("gif"))) {
			return mimetypeimg;
		}
		if (mimetypevideo != null && mimetypevideo.startsWith("video") && (mimetypevideo.endsWith("mpeg") || mimetypevideo.endsWith("mpeg4") || mimetypevideo.endsWith("mp4") || mimetypevideo.endsWith("quicktime") || mimetypevideo.endsWith("x-ms-wmv") || mimetypevideo.endsWith("x-flv") || mimetypevideo.endsWith("x-msvideo") || mimetypevideo.endsWith("webm") || mimetypevideo.endsWith("avi"))) {
			return mimetypevideo;
		}
		return "";
	}
	@SuppressWarnings("resource")
	public static boolean renameFile(File folder, File srcFile, String fileName) {
		boolean renamed = false;
		try {
			File fnew = new File(folder, fileName);
			renamed = srcFile.renameTo(fnew);
			/*
			 * if (!renamed) { try { byte[] ba =
			 * ImageResizerUtility.resize(srcFile, 50, 50); IOUtils.write(ba,
			 * new FileOutputStream(fileName)); renamed = true; } catch
			 * (IOException ee) { renamed = false;
			 * ee.printStackTrace(System.out); } }
			 */
			if (!renamed) {
				FileChannel sourceChannel = null;
				FileChannel destChannel = null;
				try {
					sourceChannel = new FileInputStream(srcFile).getChannel();
					destChannel = new FileOutputStream(fnew).getChannel(); 
					long transfersaved = destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
					if (transfersaved > 0) {
						renamed = true;
					}
					sourceChannel.close();
					destChannel.close();
				} catch (IOException ee) {
					ee.printStackTrace(System.out);
				} finally {
					if(sourceChannel !=null && sourceChannel.isOpen() ) sourceChannel.close();
					if(destChannel !=null && destChannel.isOpen() ) destChannel.close();
				
				}
			}
			
		} catch (Exception ee) {
			renamed = false;
			ee.printStackTrace(System.out);
		}
		return renamed;
	}
}
