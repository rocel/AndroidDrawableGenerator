package com.rocel.adg;

import javax.swing.SwingUtilities;

import com.rocel.adg.controlers.DropControler;
import com.rocel.adg.controlers.IDropControler;
import com.rocel.adg.ui.DropFrame;

public class Main {

	static IDropControler dropControler;
	static DropFrame dropFrame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dropControler = new DropControler();
				dropFrame = new DropFrame(dropControler);
				dropFrame.display();
			}
		});
	}

}
