package com.nyfaria.petshop.network;

import com.nyfaria.petshop.network.packetsc2s.SelectCosmeticPacket;
import commonnetwork.api.Network;

public class PacketInit {
    public static void loadClass() {
        Network.registerPacket(SelectCosmeticPacket.LOCATION, SelectCosmeticPacket.class, SelectCosmeticPacket::encode, SelectCosmeticPacket::decode, SelectCosmeticPacket::handle);
    }

}
