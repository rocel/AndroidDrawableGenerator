package com.rocel.adg.controlers;

import java.io.File;

public interface IDropControler {

	boolean generateDrawables(File[] files);
	void saveFile(String filePath);

}
