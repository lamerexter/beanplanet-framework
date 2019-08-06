/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core;

import org.beanplanet.core.logging.BeanpanetLoggerFactory;
import org.beanplanet.core.logging.Logger;

import java.awt.*;
import java.io.File;

/**
 * Utility class containing useful Operating System functionality.
 */
public class OsUtil {
    private static boolean isWindows = false;
    private static boolean isUnix = false;
    private static boolean isLinux = false;
    private static boolean isMac = false;

    private static final Logger log = BeanpanetLoggerFactory.getSystemLoggerFor(OsUtil.class);

    static
    {
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = os.contains("win");
        isLinux = os.contains("nux");
        isMac = os.contains("mac");
        isUnix = isLinux || isMac || os.contains("nix");
    }

    public static boolean isWindows() { return isWindows; }
    public static boolean isUnix() { return isUnix; }
    public static boolean isLinux() { return isLinux; }
    public static boolean isMac() { return isMac; }

    public static boolean open(File file)
    {
        try
        {
            if ( isWindows() )
            {
                Runtime.getRuntime().exec(new String[]
                                                  {"rundll32", "url.dll,FileProtocolHandler",
                                                   file.getAbsolutePath()
                                                  }
                                                  );
                return true;
            } else if ( (isLinux() || isUnix() || isMac()) && new File("/usr/bin/open").canExecute())
            {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                                                       file.getAbsolutePath()});
                return true;
            } else
            {
                // Unknown OS, try with desktop
                if (Desktop.isDesktopSupported())
                {
                    Desktop.getDesktop().open(file);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } catch (Exception e)
        {
            log.error(e, "Unable to open file [{{0}}", file);
            return false;
        }
    }
}
