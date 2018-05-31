package com.framework.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AppendableSerilization extends ObjectOutputStream {
	
	public static ObjectOutputStream getObjectOutputStream(File storageFile) throws IOException {
        
            // this is a workaround so that we can append objects to an existing file
        	FileOutputStream fos=new FileOutputStream(storageFile,true);
        	BufferedOutputStream bos=new BufferedOutputStream(fos);
        	long pos = fos.getChannel().position();
        	if(pos == 0) {
        		return new ObjectOutputStream(bos);
        	} else {
        		return new AppendableSerilization(bos);
        	}
    }
	
	public AppendableSerilization(OutputStream out) throws IOException {
		super(out);
		}
	
	@Override
	protected void writeStreamHeader() throws IOException{
		reset();
		}
}
