package org.abratuhi.proxy.core

class Proxy {

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

		def i = options.i as int
		def o = options.o as int
		def h = options.h
		def a = options.a
		def b = options.b

		def ss = new ServerSocket(i);
		while(true){
			ss.accept(true){socket ->
				socket.withStreams{input, output ->
					def s = new Socket(h, o);

					s.withStreams {is, os ->

						if(a){
							def transformer = Class.forName(a, true, Thread.currentThread().contextClassLoader).newInstance()
							transformer.process(input, os)
						} else {
							new IdempotentTransformer().process(input, os)
						}

						if(b) {
							def transformer = Class.forName(b, true, Thread.currentThread().contextClassLoader).newInstance()
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
