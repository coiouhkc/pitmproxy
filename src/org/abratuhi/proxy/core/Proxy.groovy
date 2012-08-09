package org.abratuhi.proxy.core

import org.abratuhi.proxy.transformer.ITransformer
import org.abratuhi.proxy.transformer.IdempotentTransformer;

class Proxy {
	
	def i
	def o
	def h
	def a
	def b

	public static void main(String[] args) {
		def cli = new CliBuilder();
		cli.with{
			i longOpt: 'i', args: 1, argName: 'in', 'Incoming port'
			o longOpt: 'o', args: 1, argName: 'out', 'Outgoing port'
			h longOpt: 'h', args: 1, argName: 'host', 'Outgoing host'
			a longOpt: 'a', args: 1, argName: 'at', 'A-stream-transformer'
			b longOpt: 'b', args: 1, argName: 'bt', 'B-stream-transformer'
		}

		def options = cli.parse(args);
		
		def proxy = new Proxy()

		proxy.i = options.i as int
		proxy.o = options.o as int
		proxy.h = options.h
		proxy.a = options.a
		proxy.b = options.b

		def ss = new ServerSocket(proxy.i);
		while(true){
			ss.accept(true){socket ->
				socket.withStreams{input, output ->
					def s = new Socket(proxy.h, proxy.o);

					s.withStreams {is, os ->

						if(proxy.a){
							ITransformer transformer = (ITransformer) Class.forName(proxy.a, true, Thread.currentThread().contextClassLoader).newInstance()
							transformer.setProxy(proxy)
							transformer.process(input, os)
						} else {
							new IdempotentTransformer().process(input, os)
						}

						if(proxy.b) {
							ITransformer transformer = (ITransformer) Class.forName(proxy.b, true, Thread.currentThread().contextClassLoader).newInstance()
							transformer.setProxy(proxy)
							transformer.process(is, output);
						} else {
							new IdempotentTransformer().process(is, output);
						}

					}

					s.close()
				}
				socket.close()
			}
		}
	}

}
