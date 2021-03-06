/*
 * Copyright 2010 Andreas Veithen
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
package com.googlecode.wascommons.loghub;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.joran.JoranConfigurator;

public class LogHub {
    public static void main(String[] args) throws Exception {
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.doConfigure(new File(args[0]), LogManager.getLoggerRepository());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutting down...");
                LogManager.getLoggerRepository().shutdown();
            }
        });
        synchronized (LogHub.class) {
            LogHub.class.wait();
        }
    }
}
