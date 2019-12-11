package com.lzq.selfdiscipline.business.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.lzq.selfdiscipline.business.SystemProperties;

import java.net.ConnectException;

/**
 * openoffice 连接转换类：单例模式
 */
public class OpenOfficeConSingleton {
    private OpenOfficeConSingleton() {
    }

    private static class OpenOfficeConn {
        private static DocumentConverter converter;
        private static final OpenOfficeConnection connection =
                new SocketOpenOfficeConnection(SystemProperties.openofficeHost, SystemProperties.openofficePort);

        static {
            try {
                connection.connect();
                converter = new StreamOpenOfficeDocumentConverter(connection);
            } catch (ConnectException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    public static DocumentConverter getConverter() {
        return OpenOfficeConn.converter;
    }
}
