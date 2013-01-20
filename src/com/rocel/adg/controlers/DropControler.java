package com.rocel.adg.controlers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import com.rocel.adg.Globals;

public class DropControler implements IDropControler{

	public DropControler(){ }

	@Override
	public boolean generateDrawables(File[] files) {
		List<File> filesList = getDropedFiles(files);
		File tmpFolder ;
		if (System.getProperty("java.io.tmpdir") == null || System.getProperty("java.io.tmpdir").isEmpty()){
			tmpFolder = new File(Globals.TMP_FOLDER + Globals.TMP_FOLDER_IMAGES);
			Globals.PATH_TO_TMP_FOLDER = Globals.TMP_FOLDER;
		} else {
			tmpFolder = new File(System.getProperty("java.io.tmpdir")+ ""+ Globals.TMP_FOLDER + Globals.TMP_FOLDER_IMAGES);
			Globals.PATH_TO_TMP_FOLDER = System.getProperty("java.io.tmpdir")+ ""+ Globals.TMP_FOLDER;
		}

		//clear the folder
		cleanFolder(tmpFolder);

		try {
			for (Iterator<String> i = Globals.MAP_DEFINITIONS.keySet().iterator() ; i.hasNext() ; ) {
				String key = i.next();
				forceMkdir(new File(tmpFolder + "\\" + key));
				for(File file : filesList){
					//					BufferedImage resizedImage = resizeImage(file, Globals.MAP_DEFINITIONS.get(key));
					BufferedImage originalImage = ImageIO.read(file);
					BufferedImage resizedImage = Scalr.resize(originalImage,
							Scalr.Method.SPEED,
							Scalr.Mode.FIT_TO_WIDTH,
							(int) (originalImage.getWidth() * Globals.MAP_DEFINITIONS.get(key)),
							(int) (originalImage.getHeight() * Globals.MAP_DEFINITIONS.get(key)), Scalr.OP_ANTIALIAS);
					ImageIO.write(resizedImage, getFileType(file), new File(tmpFolder + "\\" + key + "\\" + file.getName()));
					System.out.println("Created the file :" + tmpFolder + "\\" + key + "\\" + file.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 

		try {
			zip(tmpFolder,  new File(tmpFolder.getParent() + "\\" + Globals.EXPORT_ZIP_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static void cleanFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					cleanFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	public static void zip(File directory, File zipfile) throws IOException {
		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<File>();
		queue.push(directory);
		OutputStream out = new FileOutputStream(zipfile);
		Closeable res = out;
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));
					} else {
						zout.putNextEntry(new ZipEntry(name));
						copy(kid, zout);
						zout.closeEntry();
					}
				}
			}
		} finally {
			res.close();
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

	private static void copy(File file, OutputStream out) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			copy(in, out);
		} finally {
			in.close();
		}
	}

	public static void forceMkdir(File directory) throws IOException {
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				String message =
						"File "
								+ directory
								+ " exists and is "
								+ "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		} else {
			if (!directory.mkdirs()) {
				// Double-check that some other thread or process hasn't made
				// the directory in the background
				if (!directory.isDirectory())
				{
					String message =
							"Unable to create directory " + directory;
					throw new IOException(message);
				}
			}
		}
	}

	private String getFileType(File file) {
		String extension = "";
		int i = file.getName().lastIndexOf('.');
		if (i > 0) {
			extension = file.getName().substring(i+1);
		}
		return extension;
	}

	private BufferedImage resizeImage(File file, Float ratio) {
		System.out.println("Resizing file : " + file.getName() + " - with ratio:"+ ratio);
		BufferedImage originalImage;
		BufferedImage resizedImage = null;
		try {
			originalImage = ImageIO.read(file);
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			resizedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, (int)(originalImage.getWidth()*ratio), (int)(originalImage.getHeight()*ratio), null);
			g.dispose();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizedImage;
	}

	private List<File> getDropedFiles(File[] files) {
		List<File> filesList = new ArrayList<File>();
		for(File file : files){
			if(file.isFile()){
				filesList.add(file);
			} else {
				filesList.addAll(getFiles(file));
			}
		}
		return filesList;
	}


	private List<File> getFiles( File folder ) {
		File[] list = folder.listFiles();
		List<File> files = new ArrayList<File>();
		for ( File f : list ) {
			if ( f.isDirectory() ) {
				files.addAll(getFiles(f));
			} else {
				files.add(f);
			}
		}
		return files;
	}

	@Override
	public void saveFile(String filePath) {
		if(!filePath.endsWith("." + Globals.EXPORT_ZIP_EXTENSION)){
			filePath += "." + Globals.EXPORT_ZIP_EXTENSION;
		}
		System.out.println("Going to save the file to : " + filePath);
		System.out.println("From the file : " + Globals.PATH_TO_TMP_FOLDER + "\\" + Globals.EXPORT_ZIP_NAME);
		String sCurrentLine = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(Globals.PATH_TO_TMP_FOLDER + "\\" + Globals.EXPORT_ZIP_NAME));
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			while ((sCurrentLine = br.readLine()) != null) {
				bw.write(sCurrentLine);
				bw.newLine();
			}
			br.close();
			bw.close();
//			File org = new File(Globals.PATH_TO_TMP_FOLDER + "\\" + Globals.EXPORT_ZIP_NAME);
//			org.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
