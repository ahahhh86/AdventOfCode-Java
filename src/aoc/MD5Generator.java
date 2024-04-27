package aoc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;



/**
 * Class to create an MD5 hash from a salt and an index
 */
public class MD5Generator {
	private final String salt;

	/**
	 * constructor
	 * 
	 * @param salt
	 *          salt used
	 */
	public MD5Generator(String salt) {
		this.salt = salt;
	}

	protected static byte[] createHash(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("could not get MD5 hash");
		}
	}

	/**
	 * creates the MD5 hash of salt + index
	 * 
	 * @param index
	 *          index
	 * @return MD5 hash as array of bytes
	 */
	public byte[] createHashByte(int index) {
		return createHash(salt + index);
	}

	/**
	 * creates the MD5 hash of salt + index
	 * 
	 * @param index
	 *          index
	 * @return MD5 hash as hex string
	 */
	public String createHashHex(int index) {
		var hash = createHashByte(index);
		return HexFormat.of().formatHex(hash);
	}
}
