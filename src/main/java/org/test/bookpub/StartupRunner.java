package org.test.bookpub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;

/**
 * Created by Noel on 12/17/16.
 */
public class StartupRunner implements CommandLineRunner {

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void run(String... args) throws Exception {
        logger.info("Hello");
    }
}
