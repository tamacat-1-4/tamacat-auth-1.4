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

	@Override
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	
	@Override
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	public String generate(String secret, Date date) {
		String time = DateUtils.getTime(date, timeFormat);
		String pw = EncryptionUtils.getMac(time, secret, algorithm);
		//LOG.debug(time+"="+pw);
		return pw;
	}
	
	@Override
	public String generate(String secret) {
		String time = DateUtils.getTimestamp(timeFormat);
		String pw = EncryptionUtils.getMac(time, secret, algorithm);
		//LOG.debug(time+"="+pw);
		return pw;
	}
	
	@Override
	public boolean check(String secret, String id) {
		Set<String> hash = new HashSet<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int period = oneTimePasswordPeriod;
		cal.add(Calendar.MINUTE, -period);
		for (int i=0; i<=period*2; i++) {
			String time = DateUtils.getTime(cal.getTime(),timeFormat);
			String pw = EncryptionUtils.getMac(time, secret, algorithm);
			//LOG.debug(time+" ["+id+"]=["+pw +"] result="+pw.equals(id));
			hash.add(pw);
			cal.add(Calendar.MINUTE, 1);
		}
		return hash.contains(id);
	}
	
	/**
	 * Minutes of time period.
	 * @param oneTimePasswordPeriod
	 */
	public void setOneTimePasswordPeriod(int oneTimePasswordPeriod) {
		this.oneTimePasswordPeriod = oneTimePasswordPeriod;
	}
}
