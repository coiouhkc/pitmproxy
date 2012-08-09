package org.abratuhi.proxy.transformer;

import java.io.InputStream;
import java.io.OutputStream;

import org.abratuhi.proxy.core.Proxy;

public interface ITransformer {
	
	public void setProxy(Proxy proxy);
	public void process(InputStream is, OutputStream os);

}
