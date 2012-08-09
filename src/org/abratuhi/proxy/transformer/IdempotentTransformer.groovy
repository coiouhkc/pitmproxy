package org.abratuhi.proxy.transformer

import java.io.InputStream
import java.io.OutputStream

import org.abratuhi.proxy.core.Proxy


class IdempotentTransformer implements ITransformer{
	
	Proxy proxy
	
	public void setProxy(Proxy proxy){
		this.proxy = proxy
	}

	public void process(InputStream is, OutputStream os){
		byte[] buf = new byte[4096]
		def length
		while ((length = is.read(buf)) != -1){
			os.write(buf, 0, length)
			if(!is.available()) break;
		}
	}

}
