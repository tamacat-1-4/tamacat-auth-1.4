package org.tamacat.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;
import org.tamacat.util.DateUtils;
import org.tamacat.util.EncryptionUtils;

public class TimeBasedOneTimePassword implements OneTimePassword {
	static Log LOG = LogFactory.getLog(TimeBasedOneTimePassword.class);
	
	protected String algorithm = "HmacSHA256";
	protected String timeFormat = "yyyyMMddHHmm";
	protected int oneTimePasswordPeriod = 15; //min
	protected int timeUnit = Calendar.MINUTE;
	protected boolean caseSensitive = true;
	
	@Override
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	
	@Override
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	@Override
	/**
	 * Calendar.MINUTE | Calendar.HOUR | Calendar.DATE;
	 * Default: Calendar.MINUTE
	 */
	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public String generate(String secret, Date date) {
		String time = DateUtils.getTime(date, timeFormat);
		String pw = EncryptionUtils.getMac(time, secret, algorithm);
		//LOG.debug(time+"="+pw);
		if (caseSensitive) {
			return pw;
		} else {
			return pw.toLowerCase();
		}
	}
	
	@Override
	public String generate(String secret) {
		String time = DateUtils.getTimestamp(timeFormat);
		String pw = EncryptionUtils.getMac(time, secret, algorithm);
		//LOG.debug(time+"="+pw);
		if (caseSensitive) {
			return pw;
		} else {
			return pw.toLowerCase();
		}
	}
	
	@Override
	public boolean check(String secret, String id) {
		Set<String> hash = new HashSet<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int period = oneTimePasswordPeriod;
		cal.add(timeUnit, -period);
		for (int i=0; i<=period*2; i++) {
			String time = DateUtils.getTime(cal.getTime(),timeFormat);
			String pw = EncryptionUtils.getMac(time, secret, algorithm);
			//LOG.debug(time+" ["+id+"]=["+pw +"] result="+pw.equals(id));
			if (caseSensitive) {
				hash.add(pw);
			} else {
				hash.add(pw.toLowerCase());
			}
			cal.add(timeUnit, 1);
		}
		if (caseSensitive) {
			return hash.contains(id);
		} else {
			return hash.contains(id.toLowerCase());
		}
	}
	
	/**
	 * Minutes of time period.
	 * @param oneTimePasswordPeriod
	 */
	public void setOneTimePasswordPeriod(int oneTimePasswordPeriod) {
		this.oneTimePasswordPeriod = oneTimePasswordPeriod;
	}

	@Override
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
}
