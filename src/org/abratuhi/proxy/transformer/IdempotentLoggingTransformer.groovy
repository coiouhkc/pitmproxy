package org.abratuhi.proxy.transformer

import java.io.InputStream
import java.io.OutputStream

import org.abratuhi.proxy.core.Proxy


class IdempotentLoggingTransformer implements ITransformer{
	
	Proxy proxy
	
	public void setProxy(Proxy proxy){
		this.proxy = proxy
	}

	public void process(InputStream is, OutputStream os){
		byte[] buf = new byte[4096]
		def length
		def request = new String()
		while ((length = is.read(buf)) != -1){
			request += new String(buf)
			if(!is.available()) break;
		}
		
		println request
		
		os << request
		
		println "${this.class} processing done"
	}

}
