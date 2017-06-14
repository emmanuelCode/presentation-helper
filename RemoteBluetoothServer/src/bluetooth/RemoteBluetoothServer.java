package bluetooth;


public class RemoteBluetoothServer{


	public static void start() {
		Thread waitThread = new Thread(new WaitThread());
		waitThread.start();
	}


}
