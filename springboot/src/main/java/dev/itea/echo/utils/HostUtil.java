package dev.itea.echo.utils;

import lombok.extern.slf4j.Slf4j;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Host util
 *
 * @author: isixe
 * @create: 2024-02-02 14:15
 **/
@Slf4j
public class HostUtil {

    public String getLocalHostExactAddress() {
        Optional<InetAddress> operation = getHostExactAddress();
        return operation.map(InetAddress::getHostAddress).orElse(null);
    }

    public Optional<InetAddress> getHostExactAddress() {
        try (Stream<NetworkInterface> networkInterfaces = NetworkInterface.networkInterfaces()) {
            return networkInterfaces
                    .flatMap(NetworkInterface::inetAddresses)
                    .filter(inetAddr -> !inetAddr.isLoopbackAddress())
                    .filter(InetAddress::isSiteLocalAddress)
                    .findFirst();
        } catch (SocketException e) {
            log.error(e.toString());
        }
        return Optional.empty();
    }
}