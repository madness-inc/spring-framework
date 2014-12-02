package org.springframework.context.support;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

/**
 * A {@link MessageSource} that wraps multiple {@link MessageSource} and processes them in sequence. The message is
 * always taken from the first {@link MessageSource} that returns a non-{@code null} value (if any).
 * 
 * @author Matthias Mueller
 * 
 */
public class MessageSourceChain implements MessageSource {

	private MessageSource[] messageSources;

	public MessageSourceChain() {

	}

	public MessageSourceChain(MessageSource... messageSources) {
		this.messageSources = messageSources;
	}

	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		checkSources();
		String message = null;
		for (int i = 0; i < messageSources.length && message == null; i++) {
			message = messageSources[i].getMessage(code, args, defaultMessage, locale);
		}
		return message;
	}

	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		checkSources();
		String message = null;
		NoSuchMessageException nsme = null;
		for (int i = 0; i < messageSources.length && message == null; i++) {
			try {
				message = messageSources[i].getMessage(code, args, locale);
			} catch (NoSuchMessageException ex) {
				nsme = ex;
			}
		}
		if (null == message) {
			throw nsme;
		}
		return message;
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		checkSources();
		String message = null;
		NoSuchMessageException nsme = null;
		for (int i = 0; i < messageSources.length && message == null; i++) {
			try {
				message = messageSources[i].getMessage(resolvable, locale);
			} catch (NoSuchMessageException ex) {
				nsme = ex;
			}
		}
		if (null == message) {
			throw nsme;
		}
		return message;
	}

	private void checkSources() {
		if (messageSources == null || messageSources.length == 0) {
			throw new UnsupportedOperationException("MessageSourceChain must contain at least one MessageSource");
		}
	}

	public MessageSource[] getMessageSources() {
		return messageSources;
	}

	public void setMessageSources(MessageSource[] messageSources) {
		this.messageSources = messageSources;
	}

}
