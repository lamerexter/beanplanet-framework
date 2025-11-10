/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
