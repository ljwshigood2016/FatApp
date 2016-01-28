package com.publicnumber.satellite.util;


/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.ArrayList;
import java.util.HashMap;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

import com.publicnumber.satellite.service.BluetoothLeService;

/**
 * This class includes a small subset of standard GATT attributes for
 * demonstration purposes.
 */
public class GattAttributes {

	public static final int MSG_START_SHOCK = 5000;
	
	public static final int MSG_STOP_SHOCK = 5001;
	
	public static final int MSG_CONNECTED = 2 << 1;

	public static final int SEND_MSG = 10000;
	
	public static final int MSG_DISCORY = 30000;
	
	public static final int MSG_NOTIFY_TAB = 40000;
	
	public static ArrayList<BluetoothDevice> mLeDevices;
	// 储存需发送的UUID所在的Characteristic
	public static BluetoothGattCharacteristic mobileToEquipmentCharacteristic;

	// 储存需发送的UUID所在的Characteristic
	public static BluetoothGattCharacteristic equipmentToMobileCharacteristic;
	// 储存需发送的UUID所在的mBluetoothLeService
	public static BluetoothLeService mBluetoothLeService;

	private static HashMap<String, String> attributes = new HashMap();
	public static String HEART_RATE_MEASUREMENT = "0000C004-0000-1000-8000-00805f9b34fb";
	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

	static {
		// Services.
		attributes.put("0000fff00000-1000-8000-00805f9b34fb",
				"Heart Rate Service");
		attributes.put("0000180a-0000-1000-8000-00805f9b34fb",
				"Device Information Service");
		// Characteristics.
		attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
		attributes.put("00002a29-0000-1000-8000-00805f9b34fb",
				"Manufacturer Name String");
	}

	public static String lookup(String uuid, String defaultName) {
		String name = attributes.get(uuid);
		return name == null ? defaultName : name;
	}

	// 测距公式
	public static float getRSSIMeter(int rssi) {
		float d = (float) Math.pow(3, Math.abs((float) (43 - Math.abs(rssi)) / 25));
		return d;
	}
}
