package org.abratuhi.proxy.transformer

import java.io.InputStream
import java.io.OutputStream;

import org.abratuhi.proxy.core.Proxy;

class AdjustLocationTransformer implements IHTTPTransformer {
	
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
		
		request.eachLine{ line ->
			if(line.startsWith("Location:")){	// change Location: to the requestor's location
				os << "Location: localhost:${proxy.i} \r\n"
				println "Location: localhost:${proxy.i}"
			} else {
				os << line
				println line
			}
		}
		
		println "${this.class.name} processing done"
	}

}
