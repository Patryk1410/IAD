package cli;

import util.CommandLineUtil;

/**
 * Created by patry on 10/05/17.
 */
public interface InterfaceModule {

    CommandLineUtil commandLineUtil = CommandLineUtil.getInstance();

    void run();
}
