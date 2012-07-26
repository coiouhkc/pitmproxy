package org.abratuhi.proxy.core

class IdempotentTransformer {

	def process(def is, def os){
		byte[] buf = new byte[4096]
		def length
		while ((length = is.read(buf)) != -1){
			os.write(buf, 0, length)
			
			if(! is.available()) break;
		}
	}

}
