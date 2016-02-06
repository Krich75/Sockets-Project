package builder;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import exception.SSLContextInitializationException;

/**
 * Allow to build a SSL context based on manually loaded trust store and/or key store.
 * Whereas default Java mechanisms impose to have these kind of file aside an application,
 * this class allow for example to load them from a jar. 
 * @author greg
 *
 */
public class CustomSSLSocketFactoryBuilder {
	
	private static CustomSSLSocketFactoryBuilder instance;
	
	/**
	 * Get or build an instance of CustomSSLSocketFactoryBuilder based on the specified parameters.
	 * All given input streams are closed after file loading.
	 * @param keyStoreStream An InputStream on a key store file or null.
	 * @param keyStorePassword The password associated with the previous key store or null.
	 * @param trustStoreStream An InputStream on a trust store file or null.
	 * @param trustStorePassword The password associated with the previous key store or null.
	 * @return A new instance of CustomSSLSocketFactoryBuilder
	 * @throws SSLContextInitializationException If an error occur due to the current implementation of Java
	 * or to the specified parameters.
	 */
	public static CustomSSLSocketFactoryBuilder getInstance(InputStream keyStoreStream, String keyStorePassword, InputStream trustStoreStream, String trustStorePassword)
			throws SSLContextInitializationException {
			if (instance == null){
				instance = new CustomSSLSocketFactoryBuilder(keyStoreStream, keyStorePassword, trustStoreStream, trustStorePassword);
				
				try{
					if (keyStoreStream != null)
						keyStoreStream.close();
					
					if (trustStoreStream != null)
						trustStoreStream.close();
				}catch (IOException e){}
				
			}
			
			return instance;
		}
	
	private SSLServerSocketFactory sssf = null;
	private SSLSocketFactory ssf = null;
	
	/**
	 * Plain constructor.
	 * @param keyStoreStream
	 * @param keyStorePassword
	 * @param trustStoreStream
	 * @param trustStorePassword
	 * @throws SSLContextInitializationException
	 */
	private CustomSSLSocketFactoryBuilder(InputStream keyStoreStream, String keyStorePassword, InputStream trustStoreStream, String trustStorePassword)
		throws SSLContextInitializationException {
		KeyManager[] keyManagers = null;
		TrustManager[] trustManagers = null;
		
		try{
		
		if (keyStoreStream != null || keyStorePassword != null){
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			keystore.load(keyStoreStream, keyStorePassword.toCharArray());
	        keyManagerFactory.init(keystore, keyStorePassword.toCharArray());
	        
	        keyManagers = keyManagerFactory.getKeyManagers();
		}
		
		if (trustStoreStream != null || trustStorePassword != null) {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			trustStore.load(trustStoreStream, trustStorePassword.toCharArray());
			trustManagerFactory.init(trustStore);
			
			trustManagers = trustManagerFactory.getTrustManagers();
		}
		
		SSLContext ctx = SSLContext.getInstance("TLS"); // was SSL
        ctx.init(keyManagers, trustManagers, new SecureRandom());
        
        sssf = ctx.getServerSocketFactory();
        ssf = ctx.getSocketFactory();
        
		}catch (Exception e){
			throw new SSLContextInitializationException("An error occured while initilizing SSL context", e);
		}
	}
	
	/**
	 * Gives an SSLServerSocketFactory using the specified key store or/and trust store.  
	 * @return An SSLServerSocketFactory.
	 */
	public SSLServerSocketFactory getSSLServerSocketFactory(){
		return sssf;
	}
	
	/**
	 * Gives an SSLSocketFactory using the specified key store or/and trust store.  
	 * @return An SSLSocketFactory.
	 */
	public SSLSocketFactory getSSLSocketFactory(){
		return ssf;
	}
}
