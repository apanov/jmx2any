package org.vafer.jmx.util;

import java.io.FileInputStream;
import java.io.IOException;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NativeUtil {
    private static final CStdLib STD_LIB = Native.loadLibrary("c", CStdLib.class);
    public static final int ROOT_NETWORK_FD = getSelfNetworkFD();

    public interface CStdLib extends Library {
        int syscall(int number, Object... args);
    }

    public static int setns(int fd) throws IOException {
        if (fd < 0) {
            throw new IllegalArgumentException("Can't open docker /ns/net for given process");
        }

        // 308 == setns, 0x4 -- network namespace
        return STD_LIB.syscall(308, fd, 0x40000000);
    }

    public static int getNetworkFD(int pid) throws IOException {
        FileInputStream file = new FileInputStream("/proc/" + pid + "/ns/net");
        return sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess().get(file.getFD());
    }

    private static int getSelfNetworkFD() {
        try {
            FileInputStream file = new FileInputStream("/proc/self/ns/net");
            return sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess().get(file.getFD());
        } catch (IOException e) {
            return -1;
        }
    }

}
