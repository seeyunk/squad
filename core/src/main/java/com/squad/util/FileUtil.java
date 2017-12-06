package com.squad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger( FileUtil.class );
	private static final String EXT_ZIP = ".zip";
	
	public static Properties loadProperties( String filePath ) throws Exception {
		Properties prop = new Properties();
		InputStream is = new FileInputStream( filePath );
		prop.load( is );
		is.close();
		return prop;
	}
	
	public static void delete( File path ) {
		if ( path.isDirectory() ) {
			for ( File file : path.listFiles() ) {
				delete( file );
			}
		}
		
		path.delete();
	}
	
	public static File zip( File target ) throws Exception {
		String zipPath = target.getPath() + EXT_ZIP;
		ZipFile zipFile = new ZipFile( zipPath );
		zipFile.addFolder( target, new ZipParameters() );
		return zipFile.getFile();
	}
}
