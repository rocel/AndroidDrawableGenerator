package com.rocel.adg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.iharder.dnd.FileDrop;

import com.rocel.adg.Globals;
import com.rocel.adg.controlers.IDropControler;

public class DropFrame extends JFrame {
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 600;
	private static final int MIN_WIDTH = 210;
	private static final int MIN_HEIGHT = 300;
	private static final String TITLE = "Android Drawable Generator";

	private static final long serialVersionUID = 1L;
	private IDropControler controler;
	private JPanel contentPanel;
	private JLabel dropFileLabel;
	
//	private JPanel downloaderPanel;
//	private JLabel downloadFileLabel;
//	private static FileSystemView view = FileSystemView.getFileSystemView();      

	public DropFrame(IDropControler controler) throws HeadlessException {
		super();
		this.controler = controler;
		createUI();
	}

	private void createUI() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(getCenterContent());
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		this.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
	}

	private Component getCenterContent() {
		contentPanel = new JPanel();
		new FileDrop(contentPanel, new FileDrop.Listener() {
			public void filesDropped(File[] files) {
				ResizableThread thread = new ResizableThread(files);
				thread.start();
			}
		});
		contentPanel.setBackground(new Color(238,237,237));
//		dropFileLabel = new JLabel(new ImageIcon(Globals.PATH_IMG));
		URL imageurl = getClass().getResource(Globals.PATH_IMG);//assuming your package name is images 
        Image img = Toolkit.getDefaultToolkit().getImage(imageurl);
		dropFileLabel = new JLabel(new ImageIcon(img));
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(dropFileLabel, BorderLayout.CENTER);
//		downloaderPanel = new JPanel();
//		if(view != null){
//			File file;
//			try {
//				file = File.createTempFile("icon", ".zip");
//				sun.awt.shell.ShellFolder shellFolder = sun.awt.shell.ShellFolder.getShellFolder(file);      
//				downloadFileLabel = new JLabel(new ImageIcon(shellFolder.getIcon(true)));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}  
//		} else {
//			downloadFileLabel = new JLabel(new ImageIcon(Globals.PATH_IMG_DOWNLOADER));
//		}
//		downloaderPanel.add(downloadFileLabel);
//		downloadFileLabel.setVisible(false);
//		contentPanel.add(downloaderPanel, BorderLayout.SOUTH);
		return contentPanel;
	}

	protected void saveFileDownloader() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setSelectedFile(new File(Globals.EXPORT_ZIP_NAME));
	    FileFilter filter = new FileNameExtensionFilter(Globals.EXPORT_ZIP_FILTER, Globals.EXPORT_ZIP_EXTENSION);
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showSaveDialog(null);
		if (fileChooser.getSelectedFile() != null) {
			File fileToSave = fileChooser.getSelectedFile();
			controler.saveFile(fileToSave.getAbsolutePath());
		}
//		downloadFileLabel.setVisible(true);
	}

	public void display() {
		this.setVisible(true);
	}

	class ResizableThread extends Thread{
		File[] files;
		
		public ResizableThread(File[] files){
			this.files = files;
		}
		
		@Override
		public void run() {
			if (controler.generateDrawables(files)){
				saveFileDownloader();
			}
		}
	}
}
