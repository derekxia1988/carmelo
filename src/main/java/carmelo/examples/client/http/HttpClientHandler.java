package carmelo.examples.client.http;

import carmelo.json.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
/*		ByteBuf bb = null;
		try {
			bb = Unpooled.wrappedBuffer(new String("id=1").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		String url = "http://127.0.0.1:8034/command=user!login?name=1&password=123";
		HttpRequest request = new DefaultFullHttpRequest(
	            HttpVersion.HTTP_1_1, HttpMethod.GET, url);
	    request.headers().set(HttpHeaders.Names.HOST, 8043);
	    request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
	    request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
	    
	    System.out.println("request: " + url);
	    
	    ctx.writeAndFlush(request);
	}
	
	
    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
    	
/*        if (msg instanceof HttpResponse) {
        	System.out.println("HttpResponse#######################");
            HttpResponse response = (HttpResponse) msg;

            System.out.println("STATUS: " + response.getStatus());
            System.out.println("VERSION: " + response.getProtocolVersion());
            System.out.println();

            if (!response.headers().isEmpty()) {
                for (String name: response.headers().names()) {
                    for (String value: response.headers().getAll(name)) {
                        System.out.println("HEADER: " + name + " = " + value);
                    }
                }
                System.out.println();
            }

            if (HttpHeaders.isTransferEncodingChunked(response)) {
                System.out.println("CHUNKED CONTENT {");
            } else {
                System.out.println("CONTENT {");
            }
        }*/
        if (msg instanceof HttpContent) {
        	//System.out.println("HttpContent#######################");
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String decContent = JsonUtil.decompress(bytes);

            System.out.print("response: " + decContent);
            //System.out.flush();

/*            if (content instanceof LastHttpContent) {
                System.out.println("} END OF CONTENT");
            }*/
        }
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
