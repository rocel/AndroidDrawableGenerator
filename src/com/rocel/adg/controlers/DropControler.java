package com.rocel.adg.controlers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.rocel.adg.Globals;

public class DropControler implements IDropControler{

	public DropControler(){

	}

	@Override
	public boolean generateDrawables(File[] files) {
		List<File> filesList = getDropedFiles(files);
		File tmpFolder ;
		if (System.getProperty("java.io.tmpdir") == null || System.getProperty("java.io.tmpdir").isEmpty()){
			tmpFolder = new File(Globals.TMP_FOLDER);
		} else {
			tmpFolder = new File(System.getProperty("java.io.tmpdir")+ ""+ Globals.TMP_FOLDER);
		}

		try {
			for (Iterator<String> i = Globals.MAP_DEFINITIONS.keySet().iterator() ; i.hasNext() ; ) {
				String key = i.next();
				forceMkdir(new File(tmpFolder + "\\" + key));
				for(File file : filesList){
					BufferedImage resizedImage = resizeImage(file, Globals.MAP_DEFINITIONS.get(key));
					ImageIO.write(resizedImage, getFileType(file), new File(tmpFolder + "\\" + key + "\\" + file.getName()));
					System.out.println("Created the file :" + tmpFolder + "\\" + "\\" + key + "\\" + file.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
//		zipFile(new File(tmpFolder);
		
		return true;
	}

	private void zipFile(File file) {
		
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
}
