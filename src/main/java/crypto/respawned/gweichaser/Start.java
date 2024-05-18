package crypto.respawned.gweichaser;

import java.math.BigDecimal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crypto.forestfish.enums.evm.EVMChain;
import crypto.forestfish.objects.evm.connector.EVMBlockChainConnector;
import crypto.forestfish.objects.evm.model.chain.EVMChainInfo;
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

		EVMBlockChainConnector connector;
		if (settings.isNodeOptimize()) {
			connector = EVMUtils.getEVMChainConnector(settings.getChain(), true, settings.isHaltOnRPCNodeSelectionFail());
		} else if (settings.getProviderURL().length() > 3) {
			connector = EVMUtils.getEVMChainConnector(settings.getChain(), settings.getProviderURL(), settings.isHaltOnRPCNodeSelectionFail());
		} else {
			connector = EVMUtils.getEVMChainConnector(settings.getChain(), false, settings.isHaltOnRPCNodeSelectionFail());
		}
		EVMUtils.sanityCheckWithEarlyExit(connector);

		int repeatCount = 0;
		boolean notificationSent = false;
		while (true) {
			BigDecimal gasPriceInGwei = EVMUtils.getCurrentNetworkGasPriceInGWEI(connector);
			System.out.println(settings.getChain() + " current gasprice: " + gasPriceInGwei + " gwei [below threshold counter:" + repeatCount + "] threshold: " + settings.getGweiThreshold() + " gwei");

			if (gasPriceInGwei.doubleValue() <= settings.getGweiThreshold()) {
				repeatCount++;
			} else {
				repeatCount = 0;
			}

			if (repeatCount >= settings.getRepeatbelowThreshold()) {
				LOGGER.info("We reached below the gwei threshold (" + settings.getGweiThreshold() + ") " + repeatCount + " times. Current gasprice is " + gasPriceInGwei + " gwei for " + settings.getChain());

				EVMChainInfo chainInfo = EVMUtils.getEVMChainInfo(settings.getChain());
				String gastrackerURL = "http://www.etherscan.io/gastracker";
				if (!chainInfo.getBlockexplorerURLs().isEmpty()) {
					gastrackerURL = chainInfo.getBlockexplorerURLs().get(0) + "/gastracker";
				}
				
				if (!"xxxxxxxxx".equals(settings.getApiTokenApp()) && !notificationSent) {
					LOGGER.info("Shipping notification to the pushover service ..");
					NotificationUtils.pushover(settings.getApiTokenUser(), settings.getApiTokenApp(), settings.getChain() + " Low GWEI!", "time to hustle on " + settings.getChain() + ", gas is " + gasPriceInGwei  + " GWEI", MessagePriority.HIGH, gastrackerURL, settings.getChain() + " GWEI price", "cashregister");
					notificationSent = true;
				}

				if (!settings.isContinousMode()) {
					LOGGER.info("exiting since we are not in continous mode..");
					SystemUtils.halt();
				}

			}
			SystemUtils.sleepInSeconds(settings.getPollIntervalinSeconds());
		}
	}

	private static GweiSettings parseCliArgs(String[] args) {

		GweiSettings settings = new GweiSettings();
		Options options = new Options();

		// EVM chain shortname
		Option chainopt = new Option("c", "chain", true, "ETHEREUM, POLYGON, .. see forestfish docs for supported chains");
		chainopt.setRequired(true);
		options.addOption(chainopt);

		// provider URL
		Option providerURL = new Option("p", "providerurl", true, "ETH Provider URL (infura etc)");
		options.addOption(providerURL);

		// GWEI threshold
		Option gweiThreshold = new Option("t", "gweithreshold", true, "GWEI threshold");
		gweiThreshold.setRequired(true);
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

		// Node optimize
		Option nodeopt = new Option("n", "nodeopt", false, "set to true for provideURL/node selection optimization");
		options.addOption(nodeopt);

		// Number of times to remain below threshold before alert
		Option repeatbelowthreshold = new Option("r", "repeatbelowthreshold", true, "repeat below threshold (default 2)");
		options.addOption(repeatbelowthreshold);
		
		// Continous mode
		Option continousOpt = new Option("k", "continous", false, "set to true to keep running after threshold criteria met");
		options.addOption(continousOpt);

		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("c")) {
				EVMChain chain = EVMChain.ETH;
				try {
					chain = EVMChain.valueOf(cmd.getOptionValue("chain"));
				} catch (Exception e) {
					LOGGER.error("Unknown chain " + cmd.getOptionValue("chain") + " specified, see forestfish docs for supported chains");
					SystemUtils.halt();
				}
				settings.setChain(chain);
			}
			if (cmd.hasOption("p")) settings.setProviderURL(cmd.getOptionValue("providerurl"));
			if (cmd.hasOption("t")) settings.setGweiThreshold(Double.parseDouble(cmd.getOptionValue("gweithreshold")));
			if (cmd.hasOption("a")) settings.setApiTokenApp(cmd.getOptionValue("apitokenappid"));
			if (cmd.hasOption("u")) settings.setApiTokenUser(cmd.getOptionValue("apitokenuserid"));
			if (cmd.hasOption("s")) settings.setPollIntervalinSeconds(Integer.parseInt(cmd.getOptionValue("pollintervalinseconds")));
			if (cmd.hasOption("r")) settings.setRepeatbelowThreshold(Integer.parseInt(cmd.getOptionValue("repeatbelowthreshold")));
			if (cmd.hasOption("n")) settings.setNodeOptimize(true);
			if (cmd.hasOption("k")) settings.setContinousMode(true);

			settings.print();

		} catch (ParseException e) {
			LOGGER.error("ParseException: " + e.getMessage());
			formatter.printHelp(" ", options);
			SystemUtils.halt();
		}

		return settings;
	}

}
