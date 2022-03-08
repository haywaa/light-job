package com.chf.lightjob.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.StringUtils;

import com.chf.lightjob.exception.BizException;

/**
 * 密码加密实现提供者
 */
public class PasswordeUtil {

	private static final String SUFFIX = "`lightjob";

	/**
	 * 加密
	 * 
	 * @param password
	 *            Md5密码
	 * @return
	 */
	public static String encrypt(String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new BizException("密码不能为空");
		}

		if (password.trim().length() != password.length()) {
			throw new BizException("密码不能包含空格");
		}

		return md5(new StringBuilder(password).append(SUFFIX).toString());
	}

	private static String md5(String str) {
		String password = null;
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			password = new BigInteger(1, md.digest()).toString(16);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return password;
	}

	public static boolean checkPasswordMatch(String plain, String encrypted) {
		if (StringUtils.isEmpty(plain) || StringUtils.isEmpty(encrypted)) {
			return false;
		}
		return encrypt(plain).equals(encrypted);
	}

	public static void main(String[] args) {
		System.err.println("加密后:" + encrypt("admin"));
	}
}