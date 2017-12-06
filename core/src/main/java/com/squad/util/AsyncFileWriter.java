package com.squad.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncFileWriter {
	private static final Logger logger = LoggerFactory.getLogger( AsyncFileWriter.class );
	private RandomAccessFile raf;
	private FileChannel fileChannel;
	private String fullPath;
	
	public AsyncFileWriter( String dir, String fileName ) {
		try {
			this.fullPath = AsyncFileWriter.getFullPath( dir, fileName );
			File file = new File( this.fullPath );
			this.raf = new RandomAccessFile( file, "rw" );
			this.fileChannel = this.raf.getChannel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void write( ByteBuffer buffer ) {
		if ( this.fileChannel == null ) {
			throw new NullPointerException( "File channel must not be null" );
		}
		
		try {
			this.fileChannel.write( buffer );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		try {
			if ( this.fileChannel != null ) {
				this.fileChannel.close();
			}
			
			if ( this.raf != null ) {
				this.raf.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFullPath() {
		return this.fullPath;
	}
	
	public static String getFullPath( String dir, String fileName ) {
		if ( !dir.endsWith( "/" ) ) {
			StringBuilder sb = new StringBuilder();
			sb.append( dir );
			sb.append( "/" );
			dir = sb.toString();
		}
		return dir + fileName;
	}
}
