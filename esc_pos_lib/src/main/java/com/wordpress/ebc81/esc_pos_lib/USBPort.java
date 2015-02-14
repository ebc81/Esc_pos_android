package com.wordpress.ebc81.esc_pos_lib; /**
 * Created by ebc on 05.01.2015.
 */
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class USBPort {
    private  UsbManager mUsbManager;
    private  UsbInterface mUsb_intf;
    private  UsbEndpoint mUsb_epinput;
    private  UsbEndpoint mUsb_epoutput;
    private  UsbDeviceConnection mUsbDeviceConnection;
    private  UsbDevice   mUsbConnectedDevice;
    private  Thread requestHandler;
    private  boolean isconnected;

    //private BlockingQueue queue;
    private BlockingQueue<byte[]> queue =
            new ArrayBlockingQueue<byte[]>(100);


    //private static com.wordpress.ebc81.esc_pos_lib.USBPort rq;

    private static final String TAG = "USBPORT";

    public USBPort(UsbManager usbManager)
    {
        this.mUsbManager = usbManager;
        this.mUsb_intf = null;
        this.mUsb_epinput = null;
        this.mUsb_epoutput = null;
        this.mUsbDeviceConnection = null;
        this.mUsbConnectedDevice = null;
        this.requestHandler = null;
        this.isconnected = false;
    }

    private void open()
    {
        this.requestHandler = new Thread(new SenderThread());
        this.requestHandler.start();
        Log.i(TAG,"open()");
    }

    public void close()
            throws InterruptedException
    {
        int count = 0;
        while ((!this.queue.isEmpty()) && (count < 3)) {
            Thread.sleep(100L);
            count++;
        }
        this.queue.clear();
        if (mUsbDeviceConnection != null ) {
            this.mUsbDeviceConnection.releaseInterface(this.mUsb_intf);
            this.mUsbDeviceConnection.close();
        }
        if ((this.requestHandler != null) && (this.requestHandler.isAlive())) {
            this.requestHandler.interrupt();
        }
        this.mUsbConnectedDevice = null;
        Log.i(TAG,"close()");
    }

    public int GetUSBVendorID()
    {
        if ( this.isconnected && mUsbConnectedDevice != null)
            return mUsbConnectedDevice.getVendorId();
        return 0;
    }
    public int GetUSBProductID()
    {
        if ( this.isconnected && mUsbConnectedDevice != null)
            return mUsbConnectedDevice.getProductId();
        return 0;
    }

    public String GetVendorNamer()
    {
        if ( this.isconnected && mUsbConnectedDevice != null)
            return this.translateVendorID( mUsbConnectedDevice.getVendorId());
        return "";
    }


    private String translateDeviceClass(int deviceClass) {
        switch (deviceClass) {
            case UsbConstants.USB_CLASS_APP_SPEC:
                return "Application specific USB class";
            case UsbConstants.USB_CLASS_AUDIO:
                return "USB class for audio devices";
            case UsbConstants.USB_CLASS_CDC_DATA:
                return "USB class for CDC devices (communications device class)";
            case UsbConstants.USB_CLASS_COMM:
                return "USB class for communication devices";
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                return "USB class for content security devices";
            case UsbConstants.USB_CLASS_CSCID:
                return "USB class for content smart card devices";
            case UsbConstants.USB_CLASS_HID:
                return "USB class for human interface devices (for example, mice and keyboards)";
            case UsbConstants.USB_CLASS_HUB:
                return "USB class for USB hubs";
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                return "USB class for mass storage devices";
            case UsbConstants.USB_CLASS_MISC:
                return "USB class for wireless miscellaneous devices";
            case UsbConstants.USB_CLASS_PER_INTERFACE:
                return "USB class indicating that the class is determined on a per-interface basis";
            case UsbConstants.USB_CLASS_PHYSICA:
                return "USB class for physical devices";
            case UsbConstants.USB_CLASS_PRINTER:
                return "USB class for printers";
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                return "USB class for still image devices (digital cameras)";
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                return "Vendor specific USB class";
            case UsbConstants.USB_CLASS_VIDEO:
                return "USB class for video devices";
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                return "USB class for wireless controller devices";
            default:
                return "Unknown USB class!";
        }
    }
    private String translateVendorID(int vendorID) {
        switch (vendorID) {
            case 1208:
                return "Epson";
            case 1305:
                return "StarMicro";
            default:
                return "";
        }
    }
    /** get_usb_devices_list
      * Gets a list of all usb devices connected to the system
      *
    */
    public ArrayList<String> get_usb_devices_list()
    {
        ArrayList<String> usbdescrlist = new ArrayList<String>();

        try {
            HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            String tempstr = "";
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                tempstr = "VendorID: " + device.getVendorId() + "\t" +
                        translateVendorID(device.getVendorId()) + "\t" +
                        " ProductID: " + device.getProductId() + "\t" +
                        " DeviceID: " + device.getDeviceId() + "\t" +
                        //"DeviceName: " + device.getDeviceName() + "\t" +
                        " DeviceClass: " + device.getDeviceClass() + " - "
                        + translateDeviceClass(device.getDeviceClass()) + "\t" +
                        " DeviceSubClass: " + device.getDeviceSubclass() + "\t";
                usbdescrlist.add(tempstr);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return usbdescrlist;
    }

    public UsbDevice search_device(int vendorId, int productId)
    {
        HashMap<String, UsbDevice> usblist = this.mUsbManager.getDeviceList();
        Iterator<String> iterator = usblist.keySet().iterator();
        UsbDevice usbDev = null;

        while (iterator.hasNext())
        {
            usbDev = (UsbDevice)usblist.get(iterator.next());
            if ((usbDev.getVendorId() == vendorId) &&
                    (usbDev.getProductId() == productId))
            {
                Log.i(TAG, "USB Connected. VID " + Integer.toHexString(usbDev.getVendorId()) + ", PID " + Integer.toHexString(usbDev.getProductId()));
                break;
            }
            usbDev = null;
        }
        return usbDev;
    }

    public Boolean connect_device(UsbDevice usbDev)
            throws Exception
    {
        this.close();

        //this.mUsbManager.requestPermission(usbDev, mPermissionIntent);

        mUsb_intf = null;
        mUsb_epinput = null;
        mUsb_epoutput = null;

        if (usbDev == null) return false;
        UsbInterface intf = null;
        int interfaceCount = 0;
        int endPointCount = 0;
        UsbEndpoint epin = null;
        UsbEndpoint epout = null;
        interfaceCount = usbDev.getInterfaceCount();
        Log.i(TAG, "Interface count " + interfaceCount);
        if (interfaceCount <= 0) {
            return false;
        }
        for (int i = 0; i < interfaceCount; i++)
        {
            intf = usbDev.getInterface(i);
            endPointCount = intf.getEndpointCount();
            Log.i(TAG, "Endpoint count " + endPointCount);
            if (endPointCount <= 0) {
                return null;
            }
            for (int j = 0; j < endPointCount; j++)
            {
                UsbEndpoint usbEndPoint = intf.getEndpoint(j);
                if (usbEndPoint.getDirection() == 128) {
                    epin = usbEndPoint;
                } else if (usbEndPoint.getDirection() == 0) {
                    epout = usbEndPoint;
                }
            }
            UsbDeviceConnection connection = this.mUsbManager.openDevice(usbDev);
            if ((connection != null) && (connection.claimInterface(intf, true)))
            {
                mUsb_intf = intf;
                mUsb_epinput = epin;
                mUsb_epoutput = epout;
                mUsbDeviceConnection = connection;
                this.mUsbConnectedDevice = usbDev;
                this.open();
                this.isconnected = true;
                return true;
            }
            throw new Exception("");
        }
        return false;
    }

    public void SetUSBConnectionFlag(boolean connected)
    {
        this.isconnected = connected;
        if ( this.isconnected == false)
        {
            try {
                this.close();
            }
            catch (Exception e ) {

            }

        }
    }
    public boolean GetUSBConnectionFlag() {return this.isconnected;}

    public void AddData2Printer(byte[] data)
    {
        if ( this.isconnected )
        {
            Log.i(TAG, "AddData2Printer " + data);
            this.queue.add(data);
        }
    }

    class SenderThread
            implements Runnable
    {
        SenderThread() {}

        public void run()
        {
            try
            {
                while (!Thread.currentThread().isInterrupted())
                {
                    try {
                        byte[] data = (byte[])USBPort.this.queue.poll(100, TimeUnit.MILLISECONDS);
                        if ( data != null) {
                            //process queueElement
                            int datatransfered = USBPort.this.mUsbDeviceConnection.bulkTransfer(USBPort.this.mUsb_epoutput, data, data.length, 2000);

                        }
                    } catch (InterruptedException e) {
                        if (queue.isEmpty() )
                            Thread.sleep(10L);
                    }
                    Thread.sleep(10L);

                }
            }
            catch (Exception e)
            {
                USBPort.this.queue.clear();
                USBPort.this.isconnected = false;
            }
        }
    }



}
