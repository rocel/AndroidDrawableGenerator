package com.rocel.adg;

import java.util.HashMap;
import java.util.Map;

public final class Globals {
	
	//FILES & FOLDERS PATHS
	public static final String PATH_IMG = "res\\DropImg.png";
	public static final String TMP_FOLDER = "com.rocel.adg";
	

	//FOLDERS NAMES
	public static final String FOLDER_LDPI = "drawable-ldpi";
	public static final String FOLDER_MDPI = "drawable-mdpi";
	public static final String FOLDER_HDPI = "drawable-hdpi";
	public static final String FOLDER_XHDPI = "drawable-xhdpi";
	
	//QUOTIENT
//	//NORMAL QUOTIENTS : MDPI AS PATTERN
//	public static final float QUOTIENT_LDPI = 0.75f;
//	public static final float QUOTIENT_MDPI = 1f;
//	public static final float QUOTIENT_HDPI = 1.5f;
//	public static final float QUOTIENT_XHDPI = 2f;

//	//XHDPI AS PATTERN
	public static final float QUOTIENT_LDPI = 8/3;
	public static final float QUOTIENT_MDPI = 2f;
	public static final float QUOTIENT_HDPI = 1.33f;
	public static final float QUOTIENT_XHDPI = 1f;
	
	//MAP OF DEFINITIONS
	public static Map<String,Float> MAP_DEFINITIONS = new HashMap<String,Float>();
	static{
		MAP_DEFINITIONS.put(FOLDER_LDPI, QUOTIENT_LDPI);
		MAP_DEFINITIONS.put(FOLDER_MDPI, QUOTIENT_MDPI);
		MAP_DEFINITIONS.put(FOLDER_HDPI, QUOTIENT_HDPI);
		MAP_DEFINITIONS.put(FOLDER_XHDPI, QUOTIENT_XHDPI);
	}
	
}
