package com.rocel.adg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.iharder.dnd.FileDrop;

import com.rocel.adg.Globals;
import com.rocel.adg.controlers.IDropControler;

public class DropFrame extends JFrame {
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 600;
	private static final int MIN_WIDTH = 210;
	private static final int MIN_HEIGHT = 260;
	private static final String TITLE = "Android Drawable Generator";

	private static final long serialVersionUID = 1L;
	private IDropControler controler;
	private JPanel contentPanel;
	private JLabel dropFileLabel;
	
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
				controler.generateDrawables(files);
			}
		});
		contentPanel.setBackground(new Color(238,237,237));
		dropFileLabel = new JLabel(new ImageIcon(Globals.PATH_IMG));
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(dropFileLabel, BorderLayout.CENTER);
		return contentPanel;
	}

	public void display() {
		this.setVisible(true);
	}

}
