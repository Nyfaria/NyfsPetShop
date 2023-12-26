package com.nyfaria.nyfspetshop.network;

import com.nyfaria.nyfspetshop.network.packetsc2s.SelectCosmeticPacket;
import commonnetwork.api.Network;

public class PacketInit {
    public static void loadClass() {
        Network.registerPacket(SelectCosmeticPacket.LOCATION, SelectCosmeticPacket.class,SelectCosmeticPacket::encode, SelectCosmeticPacket::decode, SelectCosmeticPacket::handle);
    }

}
