package edu.albany.cs.baojian;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class TestInetSocketAddress {
	public static void main(String args[]){
		try {
			String ipAddress = "169.226.2.78";
			String hostName = "brucine.cs.albany.edu" ;
			int port = 1 ;
			InetSocketAddress byAddress1 = new InetSocketAddress(ipAddress, port);
			InetSocketAddress byAddress2 = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
			InetSocketAddress byName1 = new InetSocketAddress(hostName, port);
			InetSocketAddress byName2 = new InetSocketAddress(InetAddress.getByName(hostName), port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
