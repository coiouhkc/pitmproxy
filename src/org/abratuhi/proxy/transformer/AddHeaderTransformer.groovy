package org.abratuhi.proxy.transformer

import java.io.InputStream
import java.io.OutputStream;

import org.abratuhi.proxy.core.Proxy;

class AddHeaderTransformer implements IHTTPTransformer {
	
	Proxy proxy

	@Override
	public void setProxy(Proxy proxy) {
		this.proxy = proxy
	}

	@Override
	public void process(InputStream is, OutputStream os) {
		println "${this.class.name} processing..."
		
		byte[] buf = new byte[4096]
		def length
		def request = new String()
		while ((length = is.read(buf)) != -1){
			request += new String(buf)
			if(!is.available()) break;
		}
		
		int count = 0
		request.eachLine{ line ->
			if(count == 1){	// add header right after the VERB URL HTTP/VERSION clause
				os << "MyHeader: MyHeaderValue \r\n"
				println "MyHeader: MyHeaderValue"
			}
			
			os << line
			os << '\r\n'
			
			println line
			
			count ++
		}
		
		os << '\r\n\r\n'
		
		println "${this.class.name} processing done"
	}

}
