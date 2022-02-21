package crypto.respawned.gweichaser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import crypto.forestfish.enums.EVMChain;
import crypto.forestfish.objects.evm.EVMBlockChain;
import crypto.forestfish.utils.EVMUtils;
import crypto.forestfish.utils.NotificationUtils;
import crypto.forestfish.utils.SystemUtils;
import net.pushover.client.MessagePriority;

public class Start {

    private static final Logger LOGGER = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        LOGGER.info("init()");

        GweiSettings settings = parseCliArgs(args);
        settings.sanityCheck();
        
        EVMBlockChain blockChain = EVMUtils.createBlockchain(settings.getChain(), settings.getProviderURL());
        Web3j ethWeb3j = Web3j.build(new HttpService(blockChain.getNodeURL()));

        int repeatCount = 0;
        while (true) {
            Integer gasPrice = EVMUtils.getCurrentStandardGasPriceInGWEI(ethWeb3j, blockChain);
            LOGGER.info(settings.getChain() + " current gasprice: " + gasPrice + " GWEI [below threshold counter:" + repeatCount + "]");
            
            if (gasPrice <= settings.getGweiThreshold()) {
                repeatCount++;
            } else {
                repeatCount = 0;
            }
            
            if (repeatCount >= settings.getRepeatbelowThreshold()) {
                NotificationUtils.pushover(settings.getApiTokenUser(), settings.getApiTokenApp(), settings.getChain() + " Low GWEI!", "time to hustle on ETH, gas is " + gasPrice  + " GWEI", MessagePriority.HIGH, "http://www.etherscan.io/gastracker", "Etherscan GWEI price", "cashregister");
                LOGGER.info("exiting.");
                SystemUtils.halt();
            }
            SystemUtils.sleepInSeconds(settings.getPollIntervalinSeconds());
        }
    }

    private static GweiSettings parseCliArgs(String[] args) {

        GweiSettings settings = new GweiSettings();
        Options options = new Options();

        // EVM chain shortname
        Option chain = new Option("c", "chain", true, "ETHEREUM,POLYGON,..");
        chain.setRequired(true);
        options.addOption(chain);
        
        // provider URL
        Option providerURL = new Option("p", "providerurl", true, "ETH Provider URL (infura etc)");
        providerURL.setRequired(true);
        options.addOption(providerURL);

        // GWEI threshold
        Option gweiThreshold = new Option("t", "gweithreshold", true, "GWEI threshold (default 20)");
        options.addOption(gweiThreshold);
        
        // API token app ID
        Option apiTokenApp = new Option("a", "apitokenappid", true, "API token app ID");
        options.addOption(apiTokenApp);
        
        // API token user ID
        Option apiTokenUser = new Option("u", "apitokenuserid", true, "API token user ID");
        options.addOption(apiTokenUser);
        
        // API token user ID
        Option pollIntervalinSeconds = new Option("s", "pollintervalinseconds", true, "poll interval in seconds (default 60)");
        options.addOption(pollIntervalinSeconds);
        
        // Number of times to remain below threshold before alert
        Option repeatbelowthreshold = new Option("r", "repeatbelowthreshold", true, "repeat below threshold (default 2)");
        options.addOption(repeatbelowthreshold);

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("c")) {
            	if (cmd.getOptionValue("chain").toUpperCase().equals("ETHEREUM")) settings.setChain(EVMChain.ETHEREUM);
            	if (cmd.getOptionValue("chain").toUpperCase().equals("POLYGON")) settings.setChain(EVMChain.POLYGON);
            }
            if (cmd.hasOption("p")) settings.setProviderURL(cmd.getOptionValue("providerurl"));
            if (cmd.hasOption("t")) settings.setGweiThreshold(Integer.parseInt(cmd.getOptionValue("gweithreshold")));
            if (cmd.hasOption("a")) settings.setApiTokenApp(cmd.getOptionValue("apitokenappid"));
            if (cmd.hasOption("u")) settings.setApiTokenUser(cmd.getOptionValue("apitokenuserid"));
            if (cmd.hasOption("s")) settings.setPollIntervalinSeconds(Integer.parseInt(cmd.getOptionValue("pollintervalinseconds")));
            if (cmd.hasOption("r")) settings.setRepeatbelowThreshold(Integer.parseInt(cmd.getOptionValue("repeatbelowthreshold")));
            
            settings.print();

        } catch (ParseException e) {
            LOGGER.error("ParseException: " + e.getMessage());
            formatter.printHelp(" ", options);
            SystemUtils.halt();
        }

        return settings;
    }
    
}
