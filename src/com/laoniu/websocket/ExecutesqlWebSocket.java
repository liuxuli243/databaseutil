package com.laoniu.websocket;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value = "/executesql",configurator = HttpSessionConfigurator.class)
public class ExecutesqlWebSocket {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private static Map<String, Session> sessions = new ConcurrentHashMap<>();
	@OnOpen
	public void onopen(Session session,EndpointConfig config) {
		HttpSession httpsession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
		
		sessions.put(httpsession.getId(), session);
	}
	@OnClose
	public void onClose(Session session) {
		HttpSession httpsession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
		sessions.remove(httpsession.getId());
	}
	@OnError
	public void OnError(Throwable error,Session session) {
		logger.info("错误");
		error.printStackTrace();
	}
	/**
	 * 群发消息
	*Title: sendMessage
	*author:liuxuli
	*Description: 
	　 * @param text
	 */
	public static void sendMessage(String text) {
		Collection<Session> values = sessions.values();
		for (Session session : values) {
			if (text != null && text.length() > 0) {
				session.getAsyncRemote().sendText(text);
			}
		}
	}
	/**
	 * 定向发送消息
	*Title: sendMessage
	*author:liuxuli
	*Description: 
	　 * @param httpsession
	　 * @param text
	 */
	public static void sendMessage(HttpSession httpsession,String text) {
		Session session = sessions.get(httpsession.getId());
		if (text != null && text.length() > 0) {
			session.getAsyncRemote().sendText(text);
		}
		
	}
}
